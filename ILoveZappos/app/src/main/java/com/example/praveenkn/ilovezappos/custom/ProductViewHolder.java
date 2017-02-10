package com.example.praveenkn.ilovezappos.custom;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.example.praveenkn.ilovezappos.maincomponent.Product;
import com.example.praveenkn.ilovezappos.databinding.ContentProductPageBinding;

/**
 * Created by Praveen Kn on 02-01-2017.
 */

/**
 * Class Name: {@link ProductViewHolder}
 * Handles: Databinding for the {@link android.support.v7.widget.RecyclerView.ViewHolder}
 */
public class ProductViewHolder extends RecyclerView.ViewHolder  {
    private ContentProductPageBinding binding;
    public ProductViewHolder(ContentProductPageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }

    public void bind(Product product) {
        this.binding.setProduct(product);
    }
    public ViewDataBinding getBinding(){
        return binding;
    }


}
