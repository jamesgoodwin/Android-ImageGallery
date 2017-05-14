package com.imagegallery;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

class JsonpInterceptor implements Interceptor {

    private static final String JSON_P_START_TEXT = "jsonFlickrFeed({";
    private static final String JSON_P_END_TEXT = ")}";

    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Request request = chain.request();

        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();

        String body = new String(responseBody.bytes());

        if (body.startsWith(JSON_P_START_TEXT) && body.endsWith(JSON_P_END_TEXT)) {
            body = body.replace(JSON_P_START_TEXT, "{")
                    .replace(JSON_P_END_TEXT, "}");

            ResponseBody responseBody2 = ResponseBody.create(MediaType.parse("text/json"), body);

            return response.newBuilder()
                    .body(responseBody2)
                    .build();
        } else {
            return response;
        }
    }

}