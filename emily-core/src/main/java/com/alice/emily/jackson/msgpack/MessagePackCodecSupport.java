package com.alice.emily.jackson.msgpack;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.MimeType;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lianhao on 2017/6/10.
 */
public abstract class MessagePackCodecSupport extends Jackson2CodecSupport {

    public static final List<MimeType> MESSAGE_PACK_MIME_TYPES = Arrays.asList(
            new MimeType("application", "x-msgpack", StandardCharsets.UTF_8),
            new MimeType("application", "x-msgpack")
    );

    /**
     * Constructor with a Jackson {@link ObjectMapper} to use.
     *
     * @param objectMapper
     * @param mimeTypes
     */
    protected MessagePackCodecSupport(ObjectMapper objectMapper, MimeType... mimeTypes) {
        super(objectMapper, mimeTypes);
    }

    public static MimeType[] supportedMimeTypesArray() {
        return MESSAGE_PACK_MIME_TYPES.toArray(new MimeType[MESSAGE_PACK_MIME_TYPES.size()]);
    }

    public static void register(@Nonnull CodecConfigurer configurer, @Nonnull MessagePackMapper mapper) {
        MimeType[] mimeTypes = supportedMimeTypesArray();
        configurer.customCodecs().decoder(new MessagePackDecoder(mapper, mimeTypes));
        configurer.customCodecs().encoder(new MessagePackEncoder(mapper, mimeTypes));
    }

    public static class MessagePackEncoder extends Jackson2JsonEncoder {

        public MessagePackEncoder(MessagePackMapper mapper, MimeType... mimeTypes) {
            super(mapper, mimeTypes);
        }

        @Override
        public @Nonnull List<MimeType> getEncodableMimeTypes() {
            return MESSAGE_PACK_MIME_TYPES;
        }
    }
}