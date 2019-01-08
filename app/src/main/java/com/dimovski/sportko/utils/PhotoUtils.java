package com.dimovski.sportko.utils;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;

/**
 * Utility class for photo resources*/
public class PhotoUtils {

    /**
     * Creates Uri for provided photoResourceId
     * @param resources - reference to app resources
     * @param photoResourceId - ID of the resource
     * @return Uri to provided resource*/
    public static Uri getUriForId(Resources resources, int photoResourceId) {

        return  new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(photoResourceId))
                .appendPath(resources.getResourceTypeName(photoResourceId))
                .appendPath(resources.getResourceEntryName(photoResourceId))
                .build();
    }
}
