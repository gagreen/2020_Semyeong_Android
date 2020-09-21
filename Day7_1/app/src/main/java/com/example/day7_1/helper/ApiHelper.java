package com.example.day7_1.helper;

import com.example.day7_1.db.SearchItem;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class ApiHelper {
    public interface IApi {
        @GET("/v1/search/shop.json")
        @Headers({"X-Naver-Client-Id: umn7F7a_VxPu85hTYCOB", "X-Naver-Client-Secret: v0dXROex9F"})
        Call<Res> search(@Query("query") String keyword);
    }

    public static IApi getService() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        Retrofit r = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")
                .client(new OkHttpClient.Builder().addInterceptor(logger).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return r.create(IApi.class);
    }

    public class Res {
        String lastBuildRate;
        int total;
        int start;
        int display;
        public ArrayList<SearchItem> items;
    }


}
