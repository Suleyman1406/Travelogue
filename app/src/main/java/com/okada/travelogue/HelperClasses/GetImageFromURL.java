package com.okada.travelogue.HelperClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Bitmap bitmap;
    GifImageView gifImageView;
    Map<Integer,Bitmap> map;
    Integer position;

    public GetImageFromURL(ImageView imageView, GifImageView gifImageView,Map<Integer,Bitmap> map,Integer position) {
        this.imageView = imageView;
        this.gifImageView=gifImageView;
        this.map=map;
        this.position=position;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlDisplay = strings[0];
        bitmap = null;
        try {
            InputStream stream = new java.net.URL(urlDisplay).openStream();
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        gifImageView.setVisibility(View.INVISIBLE);
        map.put(position,bitmap);
    }
}