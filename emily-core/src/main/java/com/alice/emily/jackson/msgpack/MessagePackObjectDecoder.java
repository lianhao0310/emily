package com.alice.emily.jackson.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.msgpack.core.ExtensionTypeHeader;
import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessageInsufficientBufferException;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.core.buffer.MessageBuffer;
import org.msgpack.core.buffer.MessageBufferInput;
import org.msgpack.value.ValueType;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.AbstractDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

class MessagePackObjectDecoder extends AbstractDecoder<DataBuffer> {

    private static final int ST_CORRUPTED = -1;

    private static final int ST_INIT = 0;

    private static final int ST_DECODING_NORMAL = 1;

    private static final int ST_DECODING_ARRAY_STREAM = 2;

    private final int maxObjectLength;

    private final boolean streamArrayElements;

    MessagePackObjectDecoder() {
        // 1 MB
        this(1024 * 1024);
    }

    MessagePackObjectDecoder(int maxObjectLength) {
        this(maxObjectLength, true);
    }

    MessagePackObjectDecoder(boolean streamArrayElements) {
        this(1024 * 1024, streamArrayElements);
    }


    /**
     * @param maxObjectLength     maximum number of bytes a JSON object/array may
     *                            use (including braces and all). Objects exceeding this length are dropped
     *                            and an {@link IllegalStateException} is thrown.
     * @param streamArrayElements if set to true and the "top level" JSON object
     *                            is an array, each of its entries is passed through the pipeline individually
     *                            and immediately after it was fully received, allowing for arrays with
     */
    MessagePackObjectDecoder(int maxObjectLength,
                             boolean streamArrayElements) {
        super(MessagePackCodecSupport.supportedMimeTypesArray());
        if (maxObjectLength < 1) {
            throw new IllegalArgumentException("maxObjectLength must be a positive int");
        }
        this.maxObjectLength = maxObjectLength;
        this.streamArrayElements = streamArrayElements;
    }

    @Override
    public Flux<DataBuffer> decode(@Nonnull Publisher<DataBuffer> inputStream, @Nonnull ResolvableType elementType,
                                   MimeType mimeType, Map<String, Object> hints) {

        return Flux.from(inputStream)
                .flatMap(new Function<DataBuffer, Publisher<? extends DataBuffer>>() {

                    private ByteBuf input;
                    private int index;
                    private Integer writerIndex;
                    private int state;
                    private Stack<Integer> stack = new Stack<>();

                    @Override
                    public Publisher<? extends DataBuffer> apply(DataBuffer buffer) {
                        if (this.input == null) {
                            this.input = Unpooled.copiedBuffer(buffer.asByteBuffer());
                            DataBufferUtils.release(buffer);
                            this.writerIndex = this.input.writerIndex();
                        } else {
                            this.index = this.index - this.input.readerIndex();
                            this.input = Unpooled.copiedBuffer(this.input, Unpooled.copiedBuffer(buffer.asByteBuffer()));
                            DataBufferUtils.release(buffer);
                            this.writerIndex = this.input.writerIndex();
                        }

                        if (this.state == ST_CORRUPTED) {
                            this.input.skipBytes(this.input.readableBytes());
                            return Flux.error(new IllegalStateException("Corrupted stream"));
                        }

                        if (this.writerIndex > maxObjectLength) {
                            // buffer size exceeded maxObjectLength; discarding the complete buffer.
                            this.input.skipBytes(this.input.readableBytes());
                            reset();
                            return Flux.error(new IllegalStateException("object length exceeds " +
                                    maxObjectLength + ": " + this.writerIndex + " bytes discarded"));
                        }

                        List<DataBuffer> chunks = extractChunks(buffer);
                        return Flux.fromIterable(chunks);

                    }

                    private List<DataBuffer> extractChunks(DataBuffer dataBuffer) {
                        List<DataBuffer> chunks = new ArrayList<>();
                        DataBufferFactory dataBufferFactory = dataBuffer.factory();

                        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(new MessageBufferInput() {

                            private boolean read = false;  // make sure only read once

                            @Override
                            public MessageBuffer next() throws IOException {
                                if (!read) {
                                    read = true;
                                    if (writerIndex > index) {
                                        ByteBuffer byteBuffer = input.nioBuffer(index, writerIndex - index);
                                        return MessageBuffer.wrap(byteBuffer);
                                    }
                                }
                                return null;
                            }

                            @Override
                            public void close() throws IOException {

                            }
                        });

                        int base = this.index;
                        try {
                            iterateMessageFormats(chunks, dataBufferFactory, unpacker, base);
                        } catch (MessageInsufficientBufferException e) {
                            // buffer is insufficient, wait for next DataBuffer
                        } catch (IOException e) {
                            // something is going wrong
                            this.state = ST_CORRUPTED;
                        }

                        return chunks;
                    }

                    private void iterateMessageFormats(List<DataBuffer> chunks,
                                                       DataBufferFactory dataBufferFactory,
                                                       MessageUnpacker unpacker,
                                                       int base) throws IOException {
                        while (unpacker.hasNext()) {
                            this.index = base + (int) unpacker.getTotalReadBytes();
                            MessageFormat format = unpacker.getNextFormat();
                            ValueType type = format.getValueType();
                            ExtensionTypeHeader extension;
                            int length;
                            boolean canExtract = true;
                            switch (type) {
                                case NIL:
                                    unpacker.unpackNil();
                                    break;
                                case BOOLEAN:
                                    unpacker.unpackBoolean();
                                    break;
                                case INTEGER:
                                    switch (format) {
                                        case UINT64:
                                            unpacker.unpackBigInteger();
                                            break;
                                        case INT64:
                                        case UINT32:
                                            unpacker.unpackLong();
                                            break;
                                        default:
                                            unpacker.unpackInt();
                                            break;
                                    }
                                    break;
                                case FLOAT:
                                    unpacker.unpackDouble();
                                    break;
                                case STRING:
                                    unpacker.unpackString();
                                    break;
                                case BINARY:
                                    length = unpacker.unpackBinaryHeader();
                                    unpacker.readPayload(new byte[length]);
                                    break;
                                case ARRAY:
                                    length = unpacker.unpackArrayHeader();
                                    initState(length, streamArrayElements ? ST_DECODING_ARRAY_STREAM : ST_DECODING_NORMAL);
                                    canExtract = false;
                                    break;
                                case MAP:
                                    length = unpacker.unpackMapHeader();
                                    initState(length * 2, ST_DECODING_NORMAL);
                                    canExtract = false;
                                    break;
                                case EXTENSION:
                                    extension = unpacker.unpackExtensionTypeHeader();
                                    unpacker.readPayload(new byte[extension.getLength()]);
                                    break;
                                default:
                                    break;
                            }

                            if (canExtract) {
                                extractChunk(unpacker, dataBufferFactory, chunks, base);
                            }
                        }
                    }

                    private void extractChunk(MessageUnpacker unpacker,
                                              DataBufferFactory dataBufferFactory,
                                              List<DataBuffer> chunks,
                                              int base) {

                        int unpackIndex = base + (int) unpacker.getTotalReadBytes();

                        // discard scalar values
                        if (stack.isEmpty()) {
                            input.readerIndex(unpackIndex);
                            return;
                        }

                        Integer length = stack.pop();
                        length -= 1;

                        while (length == 0 && state != ST_INIT) {
                            switch (state) {
                                case ST_DECODING_ARRAY_STREAM:
                                    if (stack.size() == 0) {
                                        input.readerIndex(unpackIndex);
                                        reset();
                                    } else {
                                        if (stack.size() == 1) {
                                            ByteBuf msgPack = extractObject(unpackIndex);
                                            chunks.add(dataBufferFactory.wrap(msgPack.nioBuffer()));
                                        }
                                        length = stack.pop();
                                        length -= 1;
                                    }
                                    break;
                                case ST_DECODING_NORMAL:
                                    if (stack.size() == 0) {
                                        ByteBuf msgPack = extractObject(unpackIndex);
                                        chunks.add(dataBufferFactory.wrap(msgPack.nioBuffer()));
                                        reset();
                                    } else {
                                        length = stack.pop();
                                        length -= 1;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }

                        if (length > 0) {
                            stack.push(length);
                        }
                    }

                    private ByteBuf extractObject(int unpackIndex) {
                        int readerIndex = input.readerIndex();
                        ByteBuf msgPack = input.slice(readerIndex, unpackIndex - readerIndex).retain();
                        input.readerIndex(unpackIndex);
                        return msgPack;
                    }

                    private void initState(int length, int state) {
                        this.stack.push(length);
                        if (this.state == ST_INIT) {
                            this.state = state;
                        }
                    }

                    private void reset() {
                        this.state = ST_INIT;
                        this.stack.clear();
                    }
                });
    }

}
