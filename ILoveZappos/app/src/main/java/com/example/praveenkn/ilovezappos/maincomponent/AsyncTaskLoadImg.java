package com.example.praveenkn.ilovezappos.maincomponent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.praveenkn.ilovezappos.interfaces.ServiceCallback;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Praveen Kn on 01-25-2017.
 */

/**
 * Class Name: {@link AsyncTaskLoadImg}
 * Handles: Asynchronous task management to fetch the image for the given URL
 */
public class AsyncTaskLoadImg extends AsyncTask<String, Void, Bitmap> {

    private ServiceCallback callback;
    private final WeakReference<ImageView> imageViewRef;

    public AsyncTaskLoadImg(ImageView imageView) {
        this.imageViewRef = new WeakReference<ImageView>(imageView);
    }

    /**
     * Method Name: doInBackground
     * Functionality: Performs background task based on the given parameters,
     * here strings[0] contains the URL of the individual product image
     * @param strings
     * @return imgBitmap, which can be set to any imageView as drawable
     */
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap imgBitmap = null;
        imgBitmap = getImgFromUrl(strings[0]);
        return imgBitmap;

    }

    /**
     * Method Name: onPostExecute
     * Functionality: Performs the UI operation for the result sent from doInBackground,
     * here it is setting the bitmap for the imageView
     * @param bitmap
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewRef != null) {
            ImageView imageView = imageViewRef.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    /**
     *Method Name: getImgFromUrl
     * Functionality: Gets the streams from the object productImgurl. Which is later decoded to {@link Bitmap}
     * @param url
     * @return imgBitmap
     */
    private Bitmap getImgFromUrl(String url) {
        Bitmap imgBitmap = null;
        try {
            URL productImgurl = new URL(url);
            imgBitmap = BitmapFactory.decodeStream(productImgurl.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return imgBitmap;
    }
}

