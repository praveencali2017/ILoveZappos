package com.example.praveenkn.ilovezappos.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Praveen Kn on 01-25-2017.
 */

/**
 * Interface Name: {@link ServiceCallback}
 * Contains: getBitmap which can be overridden by any implemented classes
 */
public interface ServiceCallback {
   void getBitmap(Bitmap bitmap);
}
