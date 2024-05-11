package com.example.hcitest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PoemApi {
    @GET("author,title/{author};{title}")
    Call<List<Poem>> getPoems(@Path("author") String author, @Path("title") String title);
}