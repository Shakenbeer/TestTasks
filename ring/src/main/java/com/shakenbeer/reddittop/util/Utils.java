package com.shakenbeer.reddittop.util;


import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shakenbeer.reddittop.R;

import java.util.Date;

import static android.text.format.DateUtils.HOUR_IN_MILLIS;

@SuppressWarnings("unused")
public class Utils {

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Context context = view.getContext();

        RequestOptions requestOptions = new RequestOptions()
                .dontTransform()
                .dontAnimate()
                .placeholder(error)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context).load(url).apply(requestOptions).into(view);


    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Context context = view.getContext();

        RequestOptions requestOptions = new RequestOptions()
                .dontTransform()
                .dontAnimate()
                .placeholder(R.drawable.reddit)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context).load(url).apply(requestOptions).into(view);
    }

    public static String relativeTime(long seconds) {
        return (String) DateUtils.getRelativeTimeSpanString(seconds * 1000, new Date().getTime(), HOUR_IN_MILLIS);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
