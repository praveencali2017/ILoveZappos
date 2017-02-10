package com.example.praveenkn.ilovezappos.maincomponent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Praveen Kn on 01-24-2017.
 */

/**
 * Class Name: {@link ProductResponse}
 * Handles: Databinding for the Response objects, which are used as a single item in {@link android.support.v7.widget.RecyclerView}
 */
public class ProductResponse {
    @SerializedName("originalTerm")
    @Expose
    private String originalTerm;
    @SerializedName("currentResultCount")
    @Expose
    private String currentResultCount;
    @SerializedName("totalResultCount")
    @Expose
    private String totalResultCount;
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("results")
    @Expose
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }
}
