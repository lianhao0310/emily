package com.alice.emily.module.okhttp;

import lombok.Data;
import lombok.Setter;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.logging.log4j.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static lombok.AccessLevel.NONE;

@Data
@ConfigurationProperties(prefix = "emily.okhttp")
public class OkHttpProperties {

    private long connectionTimeout = 10000L;
    private long readTimeout = 10000L;
    private long writeTimeout = 10000L;
    /**
     * The interval between web socket pings initiated by this client (The default value of 0 disables client-initiated pings).
     */
    private long pingInterval = 0L;

    @Setter(NONE)
    private Cache cache = new Cache();

    /**
     * Whether to follow redirects from HTTPS to HTTP and from HTTP to HTTPS.
     */
    private boolean followSslRedirects = true;
    private boolean followRedirects = true;
    private boolean retryOnConnectionFailure = true;

    private Logging logging = new Logging();


    @Data
    public static class Cache {
        /**
         * The maximum number of bytes this cache should use to store.
         */
        private long size = 10485760;

        /**
         * The path of the directory where the cache should be stored.
         */
        private String directory;

        private Mode mode = Mode.NONE;

        /**
         * @author Lars Grefer
         */
        public enum Mode {
            /**
             * No caching.
             */
            NONE,
            /**
             * Caching in a temporary directory.
             */
            TEMPORARY,
            /**
             * Caching in a persistent directory.
             */
            PERSISTENT
        }
    }

    @Data
    public static class Logging {
        /**
         * The level at which the HttpLoggingInterceptor logs.
         */
        private HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.NONE;

        private String loggerName = "com.alice.emily.module.okhttp";

        private Level loggerLevel = Level.DEBUG;
    }
}
