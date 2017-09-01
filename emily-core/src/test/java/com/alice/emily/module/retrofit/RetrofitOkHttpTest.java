package com.alice.emily.module.retrofit;

import com.alice.emily.module.okhttp.OkHttpEndpoint;
import com.alice.emily.utils.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lianhao on 2017/3/31.
 */
@SpringBootTest
public class RetrofitOkHttpTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private OkHttpClient client;

    @Autowired
    private OkHttpEndpoint endpoint;

    @RetrofitService("test")
    private HttpBinService binService;

    @Test
    public void testHttpClient() throws IOException {
        String url = "http://ip.mvnsearch.org";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        System.out.println(JSONUtils.toJson(endpoint.getMetrics()));
    }

    @Test
    public void testRetrofit() throws IOException {
        Call<HttpBinResponse> call = binService.postWithJson(new LoginData("username", "secret"));
        printResponse(call.execute());

        call = binService.postWithFormParams("emily");
        printResponse(call.execute());

        call = binService.get();
        printResponse(call.execute());

        call = binService.getWithArg("retrofit");
        printResponse(call.execute());
    }

    private void printResponse(retrofit2.Response<HttpBinResponse> response) {
        // http response status code + headers
        System.out.println("Response status code: " + response.code());

        // isSuccess is true if response code => 200 and <= 300
        if (!response.isSuccessful()) {
            // print response body if unsuccessful
            try {
                System.out.println(response.errorBody().string());
            } catch (IOException e) {
                // do nothing
            }
            return;
        }

        // if parsing the JSON body failed, `response.body()` returns null
        HttpBinResponse decodedResponse = response.body();
        if (decodedResponse == null) return;

        // at this point the JSON body has been successfully parsed
        System.out.println("Response (contains request infos):");
        System.out.println("- url:         " + decodedResponse.url);
        System.out.println("- ip:          " + decodedResponse.origin);
        System.out.println("- headers:     " + decodedResponse.headers);
        System.out.println("- args:        " + decodedResponse.args);
        System.out.println("- form params: " + decodedResponse.form);
        System.out.println("- json params: " + decodedResponse.json);
    }

    /**
     * Generic HttpBin.org Response Container
     */
    @Data
    static class HttpBinResponse {
        private String url;           // the request url
        private String origin;        // the requester ip
        private Map headers;          // all headers that have been sent
        private Map args;             // url arguments
        private Map form;             // post form parameters
        private Map json;             // post body json
    }

    /**
     * Exemplary login data sent as JSON
     */
    @Data
    @AllArgsConstructor
    static class LoginData {
        private String username;
        private String password;
    }

    /**
     * HttpBin.org service definition
     */
    public interface HttpBinService {
        @GET("/get")
        Call<HttpBinResponse> get();

        // request /get?testArg=...
        @GET("/get")
        Call<HttpBinResponse> getWithArg(
                @Query("testArg") String arg
        );

        // POST form encoded with form field params
        @FormUrlEncoded
        @POST("/post")
        Call<HttpBinResponse> postWithFormParams(
                @Field("field1") String field1
        );

        // POST form encoded with form field params
        @POST("/post")
        Call<HttpBinResponse> postWithJson(
                @Body LoginData loginData
        );
    }
}