package com.example.praveenkn.ilovezappos.maincomponent;

import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.praveenkn.ilovezappos.R;
import com.example.praveenkn.ilovezappos.custom.CustomDatabase;
import com.example.praveenkn.ilovezappos.custom.CustomSimpleCursorAdapter;
import com.example.praveenkn.ilovezappos.custom.ProductViewAdapter;
import com.example.praveenkn.ilovezappos.databinding.ActivityProductPageBinding;
import com.example.praveenkn.ilovezappos.interfaces.ProductAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * {@link ProductPageActivity} is the main activity of this application
 * which implements {@link android.support.v7.widget.SearchView.OnSuggestionListener} to populate previously searched elements.
 */
public class ProductPageActivity extends AppCompatActivity implements SearchView.OnSuggestionListener {
    ProductAPI productAPI;
    ImageView productHeadText;
    ActivityProductPageBinding binding;
    List<Product> products;
    SearchView searchView;
    TextView originalPrice;
    TextView priceCutoff, productCountTxt;
    RecyclerView content;
    RelativeLayout noContentLayout;
    Call<ProductResponse> productResponse;
    ObjectAnimator objectAnimator;
    Animation animShake;
    FloatingActionButton fab;
    float x;
    float y;
    int count;
    boolean hideTxt = false;
    public CustomDatabase database;
    CoordinatorLayout coordinatorLayout;
    ProductViewAdapter productViewAdapter;

    /**
     * Method Name: onCreate
     * Functionality: Creates the Activity Components
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildAllComponents();
    }

    /**
     * Method Name: buildAllComponents
     * Functionality: Builds all the Basic UI Components Which are Necessary for the Current Content View
     */
    private void buildAllComponents() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate_mainlayout);
        setContentView(R.layout.activity_product_page);
        productCountTxt = (TextView) findViewById(R.id.productCountTxt);
        database = new CustomDatabase(this);
        //binding = DataBindingUtil.setContentView(ProductPageActivity.this, R.layout.activity_product_page);
        productHeadText = (ImageView) findViewById(R.id.productsHeadText);
        content = (RecyclerView) findViewById(R.id.includedContent);
        content.setLayoutManager(new LinearLayoutManager(this));
        // content.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        searchView = (SearchView) findViewById(R.id.productSearch);
        searchView.onActionViewCollapsed();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                final View cardView = (View) dragEvent.getLocalState();
                final String productName = (String) cardView.getTag();
                switch (dragEvent.getAction()) {

                    case DragEvent.ACTION_DROP:
                        cardView.animate().alpha(1f).start();
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_view);
                        fab.startAnimation(animation);
                        count++;
                        String val = basketCountStr();
                        productCountTxt.setText(val);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Snackbar snackbar = Snackbar.make(cardView.getRootView(), productName + " has been added to your cart", Snackbar.LENGTH_LONG);
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                snackbar.show();

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:

                        //Toast.makeText(getApplicationContext(),"ACTION_DRAG_ENDED",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }

        });
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake_view);

        content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                    productCountTxt.clearAnimation();
                    productCountTxt.setVisibility(View.GONE);
                    hideTxt = false;

                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.setFillAfter(true);
                animationSet.setDuration(Utility.basicAnimationDuration);
                if (hideTxt) {
                    animateHideForCount(animationSet);
                } else {
                    if (count >= 1) {
                        animateShowForCount(animationSet);
                    }
                }


                // fab.setVisibility(View.GONE);

            }
        });


        noContentLayout = (RelativeLayout) findViewById(R.id.noContentLayout);
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(Utility.zapposBaseurl).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
        productAPI = retrofit.create(ProductAPI.class);
        //getSupportActionBar().setTitle(null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView.getQuery().length() > 0) {

                    productResponse = productAPI.getProductResponse(query);
                    productResponse.enqueue(new Callback<ProductResponse>() {
                        @Override
                        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                            int statusCode = response.code();
                            if (statusCode == 200) {
                                ProductResponse productResponse1 = (ProductResponse) response.body();
                                products = productResponse1.getProducts();
                                if (products.size() > 0) {
                                    content.setVisibility(View.VISIBLE);
                                    fab.setVisibility(View.VISIBLE);
                                    noContentLayout.setVisibility(View.GONE);
                                    productHeadText.setVisibility(View.INVISIBLE);
                                    productViewAdapter = new ProductViewAdapter(ProductPageActivity.this, (List<Product>) products);
                                    content.setAdapter(productViewAdapter);

                                    searchView.clearFocus();
                                    createHint();
                                    // loadProductDetails(products.get(7));
                                } else {
                                    content.setVisibility(View.INVISIBLE);
                                    fab.setVisibility(View.INVISIBLE);
                                    noContentLayout.setVisibility(View.VISIBLE);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ProductResponse> call, Throwable t) {
                            String val = t.getMessage();
                        }
                    });
                } else {
                    content.setVisibility(View.INVISIBLE);
                    // fab.setVisibility(View.INVISIBLE);
                    noContentLayout.setVisibility(View.VISIBLE);
                }
                long result = database.insertValues(query);
                return result != -1;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.matches("[A-Za-z]*")) {
                    Cursor cursor = database.getValues(newText);
                    String[] columns = new String[]{CustomDatabase.SUGGESTION_VAL};
                    int[] columnTextId = new int[]{R.id.suggestionTxt};

                    final CustomSimpleCursorAdapter simple = new CustomSimpleCursorAdapter(getBaseContext(),
                            R.layout.content_suggestion, cursor,
                            columns, columnTextId
                            , 0);
                    searchView.setSuggestionsAdapter(simple);
                    if (cursor.getCount() != 0) {
                        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                            @Override
                            public boolean onSuggestionSelect(int position) {
                                // searchView.search
                                return false;
                            }

                            @Override
                            public boolean onSuggestionClick(int position) {
                                Cursor searchCursor = simple.getCursor();
                                if (searchCursor.moveToPosition(position)) {
                                    String selectedItem = searchCursor.getString(1);
                                    searchView.setQuery(selectedItem, true);
                                }
                                return false;
                            }
                        });
                        return true;
                    }
                } else {
                    if (newText.length() >= 1)
                        searchView.setQuery(newText.substring(0, newText.length() - 1), false);
                }
                return true;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View searchPlate = searchView.findViewById(R.id.search_plate);
                if (searchPlate != null) {
                    searchPlate.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.searchTint));
                    TextView searchText = (TextView) searchPlate.findViewById(R.id.search_src_text);
                    searchPlate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bottom_border));
                    if (searchText != null) {
                        searchText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_light_grey));
                        searchText.setHint(getResources().getString(R.string.needProducts));
                        searchText.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    }
                }
                animateWhileSearch();

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.setFillAfter(true);
                animationSet.setDuration(Utility.basicAnimationDuration);
                animateHideForCount(animationSet);
                return animateWhileSearchClose();
            }
        });
    }

    private void animateShowForCount(AnimationSet animationSet) {
        productCountTxt.setVisibility(View.VISIBLE);
        ScaleAnimation scale = new ScaleAnimation(0f, 1f, 0f, 1f, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        animationSet.addAnimation(scale);
        TranslateAnimation translate = new TranslateAnimation(fab.getPivotX(), productCountTxt.getTranslationX(), fab.getPivotY(), productCountTxt.getTranslationY());
        animationSet.addAnimation(translate);
        productCountTxt.startAnimation(animationSet);
        hideTxt = true;
    }

    private void animateHideForCount(AnimationSet animationSet) {
        ScaleAnimation scale = new ScaleAnimation(1f, 0f, 1f, 0f, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        animationSet.addAnimation(scale);
        TranslateAnimation translate = new TranslateAnimation(productCountTxt.getTranslationX(), fab.getPivotX(), productCountTxt.getTranslationY(), fab.getPivotY());
        animationSet.addAnimation(translate);
        productCountTxt.startAnimation(animationSet);
        productCountTxt.setVisibility(View.GONE);
        hideTxt = false;
    }

    @NonNull
    private String basketCountStr() {
        String val = "";
        if (count > 1) {
            val = Utility.basketItemBuyStr + count + Utility.basketItemsStr;
        } else {
            val = Utility.basketItemBuyStr + count + Utility.basketItemStr;
        }
        return val;
    }

    /**
     * Method Name: animateWhileSearchClose
     * Functionality: Sets the INVISIBLE/HIDE attributes to the views with animation for an easy translation during {@link SearchView} close
     *
     * @return
     */
    private boolean animateWhileSearchClose() {

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in);
        productHeadText.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // searchView.setVisibility(View.INVISIBLE);
                content.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                productHeadText.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                searchView.setQueryHint("");
                noContentLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // productHeadText.setVisibility(View.VISIBLE);
        return false;
    }

    /**
     * Method Name: animateWhileSearch
     * Functionality: Sets the INVISIBLE/HIDE attributes to the views with animation for an easy translation during {@link SearchView} click
     *
     * @return
     */
    private void animateWhileSearch() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_out);
        productHeadText.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                productHeadText.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Method Name: onSaveInstanceState
     * Functionality: Handles the Save states required before destroying the activity
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isSearchViewIconified", searchView.isIconified());
        outState.putString("searchQuery", searchView.getQuery().toString());
        outState.putInt("basketCount", count);

    }

    /**
     * Method Name: onRestoreInstanceState
     * Functionality: Which provides the bundle object containing all the data that were saved in onSaveInstanceState
     * so that we can restore the previous state of the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean isIconified = savedInstanceState.getBoolean("isSearchViewIconified");
        int basketCount = savedInstanceState.getInt("basketCount");
        count = basketCount;
        String val = basketCountStr();
        productCountTxt.setText(val);
        if (!searchView.isIconified()) {
            String query = savedInstanceState.getString("searchQuery");
            if (query != null && !query.isEmpty()) {
                searchView.setQuery(query, true);
            }
        }
    }

    /**
     * Method Name: createHint
     * Functionality: Creates a snackbar just to hint the user on how to add products to cart
     */
    public void createHint() {
        Snackbar snackbar = Snackbar.make(searchView, "Cart is ready!!! Drag and drop your products.", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        snackbar.show();
    }

    /**
     * Method Name: onSuggestionSelect
     * Functionality: Unused method which is added due to the implemented {@link android.support.v7.widget.SearchView.OnSuggestionListener}
     */
    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    /**
     * Method Name: onSuggestionClick
     * Functionality: Based on position of the clicked item in {@link SearchView} suggestions, corresponding word is searched in the SQlite
     * to add it to the searchQuery
     *
     * @param position
     * @return
     */
    @Override
    public boolean onSuggestionClick(int position) {
        SQLiteCursor cursor = (SQLiteCursor) searchView.getSuggestionsAdapter().getItem(position);
        int indexColumnSuggestion = cursor.getColumnIndex(CustomDatabase.SEARCH_ID);
        searchView.setQuery(cursor.getString(indexColumnSuggestion), false);

        return true;
    }
}
