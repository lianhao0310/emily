package com.alice.emily.jackson.msgpack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.reactivestreams.Publisher;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.HttpMessageDecoder;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class MessagePackDecoder extends MessagePackCodecSupport implements HttpMessageDecoder<Object> {

    private final MessagePackObjectDecoder fluxDecoder = new MessagePackObjectDecoder(true);

    private final MessagePackObjectDecoder monoDecoder = new MessagePackObjectDecoder(false);


    public MessagePackDecoder(MessagePackMapper mapper) {
        this(mapper, supportedMimeTypesArray());
    }

    public MessagePackDecoder(MessagePackMapper mapper, MimeType... mimeTypes) {
        super(mapper, mimeTypes);
    }


    @Override
    public boolean canDecode(@Nonnull ResolvableType elementType, MimeType mimeType) {
        JavaType javaType = objectMapper().getTypeFactory().constructType(elementType.getType());
        // Skip String: CharSequenceDecoder + "*/*" comes after
        return (!CharSequence.class.isAssignableFrom(elementType.resolve(Object.class)) &&
                objectMapper().canDeserialize(javaType) && supportsMimeType(mimeType));
    }

    @Override
    public List<MimeType> getDecodableMimeTypes() {
        return MESSAGE_PACK_MIME_TYPES;
    }

    @Override
    public Flux<Object> decode(@Nonnull Publisher<DataBuffer> input, @Nonnull ResolvableType elementType,
                               MimeType mimeType, Map<String, Object> hints) {

        return decodeInternal(fluxDecoder, input, elementType, mimeType, hints);
    }

    @Override
    public Mono<Object> decodeToMono(@Nonnull Publisher<DataBuffer> input, @Nonnull ResolvableType elementType,
                                     MimeType mimeType, Map<String, Object> hints) {

        return decodeInternal(monoDecoder, input, elementType, mimeType, hints).singleOrEmpty();
    }

    private Flux<Object> decodeInternal(MessagePackObjectDecoder objectDecoder, Publisher<DataBuffer> inputStream,
                                        ResolvableType elementType, MimeType mimeType,
                                        Map<String, Object> hints) {

        Assert.notNull(inputStream, "'inputStream' must not be null");
        Assert.notNull(elementType, "'elementType' must not be null");

        Class<?> contextClass = Optional.ofNullable(getParameter(elementType))
                .map(MethodParameter::getContainingClass).orElse(null);
        JavaType javaType = getJavaType(elementType.getType(), contextClass);
        Class<?> jsonView = (Class<?>) hints.get(Jackson2CodecSupport.JSON_VIEW_HINT);

        ObjectReader reader = (jsonView != null ?
                objectMapper().readerWithView(jsonView).forType(javaType) :
                objectMapper().readerFor(javaType));

        return objectDecoder.decode(inputStream, elementType, mimeType, hints)
                .map(dataBuffer -> {
                    try {
                        Object value = reader.readValue(dataBuffer.asInputStream());
                        DataBufferUtils.release(dataBuffer);
                        return value;
                    } catch (InvalidDefinitionException ex) {
                        throw new CodecException("Type definition error: " + ex.getType(), ex);
                    } catch (JsonProcessingException ex) {
                        throw new DecodingException("MessagePack decoding error: " + ex.getOriginalMessage(), ex);
                    } catch (IOException ex) {
                        throw new DecodingException("I/O error while parsing input stream", ex);
                    }
                });
    }


    // HttpMessageDecoder...

    @Override
    public Map<String, Object> getDecodeHints(@Nonnull ResolvableType actualType, @Nonnull ResolvableType elementType,
                                              @Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response) {

        return getHints(actualType);
    }

    @Override
    protected <A extends Annotation> A getAnnotation(@Nonnull MethodParameter parameter, @Nonnull Class<A> annotType) {
        return parameter.getParameterAnnotation(annotType);
    }

}
