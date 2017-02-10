package com.example.praveenkn.ilovezappos.custom;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.praveenkn.ilovezappos.maincomponent.AsyncTaskLoadImg;
import com.example.praveenkn.ilovezappos.maincomponent.Product;
import com.example.praveenkn.ilovezappos.R;
import com.example.praveenkn.ilovezappos.databinding.ContentProductPageBinding;

import java.util.List;

/**
 * Created by Praveen Kn on 02-01-2017.
 */

/**
 * Class Name: {@link ProductViewAdapter}
 * Handles: Creation of Adapter for the given recycler view
 */
public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewHolder> implements View.OnLongClickListener {
    private List<Product> products;
    private Context context;
    ObjectAnimator objectAnimator;
    LayoutInflater layoutInflater;

    public ProductViewAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
    }

    /**
     *Method Name: onCreateViewHolder
     * Functionality: inflates the contentview based on the given binding {@link ContentProductPageBinding} and returns the holder
     * @param parent
     * @param viewType
     * @return ProductViewHolder
     */
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {

            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        ContentProductPageBinding contentProductPageBinding = ContentProductPageBinding.inflate(layoutInflater, parent, false);
        return new ProductViewHolder(contentProductPageBinding);

    }

    /**
     * Method Name: onBindViewHolder
     * Functionality: Sets the values for the views on binding of the {@link android.support.v7.widget.RecyclerView.ViewHolder}
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);

        View view = holder.itemView;
        final FloatingActionButton fab, fabAdd;
        final Animation animShake;
        TextView priceCutoff = (TextView) view.findViewById(R.id.afterCutOff);
        TextView originalPrice = (TextView) view.findViewById(R.id.originalPriceTxt);
        ImageView img = (ImageView) view.findViewById(R.id.productImg);
        TableRow.LayoutParams params = null;
        if (product.getPercentOff().equals("0%")) {
            priceCutoff.setVisibility(View.GONE);
            originalPrice.setVisibility(View.VISIBLE);
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            originalPrice.setLayoutParams(params);
            originalPrice.setPaintFlags(0);
        } else {
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            originalPrice.setLayoutParams(params);
            priceCutoff.setLayoutParams(params);
            priceCutoff.setVisibility(View.VISIBLE);
        }
        new AsyncTaskLoadImg((ImageView) holder.itemView.findViewById(R.id.productImg)).execute(product.getThumbnailImageUrl());
        holder.getBinding().setVariable(position, products.get(position));
        holder.getBinding().executePendingBindings();
        img.setTag(product.getProductName());
        View cardView=view.findViewById(R.id.card_view);
        cardView.setTag(product.getProductName());
        cardView.setOnLongClickListener(this);
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_in);
        cardView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    /**
     * Method Name: onLongClick
     * Functionality: On view long press corresponding image's shadow is built to start serious of drag events
     * @param view
     * @return boolean
     */
    @Override
    public boolean onLongClick(View view) {
        final ImageView img = (ImageView) view.findViewById(R.id.productImg);
        ClipData.Item item = new ClipData.Item((CharSequence) img.getTag());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(img.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(img);
        view.startDrag(data, shadowBuilder, img, 0);
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_EXITED:
                        view.findViewWithTag(img.getTag()).animate().alpha(0.50f).start();
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        view.findViewWithTag(img.getTag()).animate().alpha(1f).start();
                        break;

                }

                return true;
            }
        });
        return true;
    }

}
