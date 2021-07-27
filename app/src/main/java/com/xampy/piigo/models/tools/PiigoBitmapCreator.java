package com.xampy.piigo.models.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.xampy.piigo.R;

public class PiigoBitmapCreator {

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource,
                                            String name) {

        View marker = ((LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker_layout, null);

        ImageView iv = marker.findViewById(R.id.marker_image);
        iv.setImageResource(resource);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        marker.setLayoutParams(new ViewGroup.LayoutParams(
                32,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );

        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        marker.buildDrawingCache();


        Bitmap bitmap = Bitmap.createBitmap(
                marker.getMeasuredWidth(),
                marker.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);

        marker.draw(canvas);

        return bitmap;
    }
}
