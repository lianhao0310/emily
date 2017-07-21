package com.alice.emily.module.resteasy.provider;

import org.jboss.resteasy.plugins.delegates.EntityTagDelegate;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.springframework.stereotype.Component;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Provider
@ConstrainedTo(RuntimeType.SERVER)
public class HttpCacheInterceptor implements WriterInterceptor {

    private static final String PSEUDO[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    private static EntityTagDelegate entityTagDelegate = new EntityTagDelegate();

    @Context
    protected HttpRequest request;

    @Context
    protected Request validation;

    public static String byteArrayToHexString(byte[] bytes) {

        byte ch = 0x00;

        StringBuffer out = new StringBuffer(bytes.length * 2);

        for (byte b : bytes) {

            ch = (byte) (b & 0xF0);
            ch = (byte) (ch >>> 4);
            ch = (byte) (ch & 0x0F);
            out.append(PSEUDO[(int) ch]);
            ch = (byte) (b & 0x0F);
            out.append(PSEUDO[(int) ch]);

        }

        return new String(out);

    }

    protected String createHash(byte[] entity) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            byte abyte0[] = messagedigest.digest(entity);
            return byteArrayToHexString(abyte0);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        if (!request.getHttpMethod().equalsIgnoreCase("GET")) {
            context.proceed();
            return;
        }

        Object occ = context.getHeaders().getFirst(HttpHeaders.CACHE_CONTROL);
        if (occ == null) {
            context.proceed();
            return;
        }
        CacheControl cc;

        if (occ instanceof CacheControl) {
            cc = (CacheControl) occ;
        } else {
            cc = CacheControl.valueOf(occ.toString());
        }

        if (cc.isNoCache()) {
            context.proceed();
            return;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        OutputStream old = context.getOutputStream();
        try {
            context.setOutputStream(buffer);
            context.proceed();
            byte[] entity = buffer.toByteArray();
            Object etagObject = context.getHeaders().getFirst(HttpHeaders.ETAG);
            EntityTag etag;
            if (etagObject == null) {
                String hash = createHash(entity);
                etag = new EntityTag(hash, true);
                context.getHeaders().putSingle(HttpHeaders.ETAG, entityTagDelegate.toString(etag));
            } else {
                etag = entityTagDelegate.fromString(etagObject.toString());
            }

            // check to see if ETags are the same.  If they are, we don't need to send a response back.
            Response.ResponseBuilder validatedResponse = validation.evaluatePreconditions(etag);
            if (validatedResponse != null) {
                throw new NoLogWebApplicationException(validatedResponse.status(Response.Status.NOT_MODIFIED).cacheControl(cc).build());
            }

            old.write(entity);
        } finally {
            context.setOutputStream(old);
        }

    }
}
