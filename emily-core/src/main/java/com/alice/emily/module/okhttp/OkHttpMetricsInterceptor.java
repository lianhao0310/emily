package com.alice.emily.module.okhttp;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * okhttp3 metrics interceptor
 *
 */
public class OkHttpMetricsInterceptor implements Interceptor {

    private final MetricRegistry metrics;

    public OkHttpMetricsInterceptor(MetricRegistry metrics) {
        this.metrics = metrics;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = request.url().host();
        Response response;
        final Timer timer = metrics.timer(name(OkHttpClient.class, host, request.method()));
        final Timer.Context context = timer.time();
        try {
            response = chain.proceed(request);
        } finally {
            context.stop();
        }
        return response;
    }
}