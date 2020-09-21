package com.example.day3.helper;

import java.util.ArrayList;

import retrofit2.Call;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        public ArrayList<Item> items;

        public class Item {
            String title;
            String link;
            String image;
            int lprice;
            int hprice;
            String mallName;

            public String getTitle() {
                return title;
            }

            public String getLink() {
                return link;
            }

            public String getImage() {
                return image;
            }

            public int getLprice() {
                return lprice;
            }

            public int getHprice() {
                return hprice;
            }

            public String getMallName() {
                return mallName;
            }
        }
    }


}
