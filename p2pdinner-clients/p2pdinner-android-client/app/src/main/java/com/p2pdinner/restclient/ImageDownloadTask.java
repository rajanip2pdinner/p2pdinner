package com.p2pdinner.restclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by rajaniy on 12/6/15.
 */
public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = ImageDownloadTask.class.getName();
    private ImageView imageView;

    public ImageDownloadTask(ImageView view) {
        this.imageView = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
       imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        super.onCancelled(bitmap);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String imageURL = params[0];
        Bitmap bitmap = downloadImage(imageURL);
        return bitmap;

    }

    private Bitmap downloadImage(String imageURL) {
        Bitmap bitmap = null;
        final int THUMBNAIL_SIZE = 512;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(input);
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        } catch (Exception e) {
           Log.e(TAG, e.getMessage(), e);
        }
        return bitmap;
    }

    private int calculateBitmapSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int sampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while ((halfHeight / sampleSize) > reqHeight && (halfWidth / sampleSize) > reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }
}
