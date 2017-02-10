package com.example.praveenkn.ilovezappos.interfaces;

import com.example.praveenkn.ilovezappos.maincomponent.ProductResponse;
import com.example.praveenkn.ilovezappos.maincomponent.Utility;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Praveen Kn on 01-24-2017.
 */

/**
 * Interface Name: {@link ProductAPI}
 * Contains: getProductResponse a method which can be overridden by the implemented classes
 * Retrofit: @{@link GET} annotation to query search based request for the given string path
 */
public interface ProductAPI {
    @GET("Search?term=&key="+Utility.zapposKey)
    Call<ProductResponse> getProductResponse(@Query("term") String searchStr);
}
