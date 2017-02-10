package com.example.praveenkn.ilovezappos.maincomponent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Praveen Kn on 01-24-2017.
 */

/**
 * Class Name: {@link Product}
 * Handles: Data binding for the model objects of type {@link Product}
 */
public class Product {
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("thumbnailImageUrl")
    @Expose
    private String thumbnailImageUrl;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("originalPrice")
    @Expose
    private String originalPrice;
    @SerializedName("styleId")
    @Expose
    private String styleId;
    @SerializedName("colorId")
    @Expose
    private String colorId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("percentOff")
    @Expose
    private String percentOff;
    @SerializedName("productUrl")
    @Expose
    private String productUrl;
    @SerializedName("productName")
    @Expose
    private String productName;

    public String getProductName() {
        return productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getPrice() {
        return price;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getPercentOff() {
        return percentOff;
    }
}
