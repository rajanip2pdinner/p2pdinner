package com.p2pdinner.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.entities.UserProfile;
import com.p2pdinner.restclient.ImageDownloadTask;
import com.p2pdinner.restclient.UserProfileManager;

import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.CATEGORY_OPENABLE;

public class FoodAndSafetyActivity extends BaseAppCompatActivity {

    private ImageView[] imageViews = new ImageView[4];
    private Button mBtnGoSell;
    private ImageView selectedImageView;
    private ProgressBar mProgressBar;
    private String selectedImagePath;

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE = 100;
    private static final String TAG = FoodAndSafetyActivity.class.getName();

    private Handler handler;


    @Inject
    UserProfileManager userProfileManager;


    private DinnerMenuItem dinnerMenuItem;

    @Inject
    Tracker mTracker;

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("SellerHome.Photo");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_and_safety);
        imageViews[0] = (ImageView) findViewById(R.id.photo1);
        imageViews[1] = (ImageView) findViewById(R.id.photo2);
        imageViews[2] = (ImageView) findViewById(R.id.photo3);
        imageViews[3] = (ImageView) findViewById(R.id.photo4);
        mProgressBar = (ProgressBar) findViewById(R.id.uploadProgress);
        for (int imageIdx = 0; imageIdx < imageViews.length; imageIdx++) {
            imageViews[imageIdx].setOnClickListener(new ImageViewClickListener());
        }
        String certificates = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE).getString(Constants.CERTIFICATES, "");
        if (StringUtils.hasText(certificates)) {
            String[] images =certificates.split(",");
            if (images != null && images.length != 0) {
                for (int idx = 0; idx < images.length; idx++) {
                    String uri = StringUtils.replace(images[idx], "\"", "");
                    imageViews[idx].setTag(images[idx]);
                    ImageDownloadTask imageDownloadTask = new ImageDownloadTask(imageViews[idx]);
                    imageDownloadTask.execute(uri);
                }
            }
        }

        mBtnGoSell = (Button) findViewById(R.id.btnGoSell);
        mBtnGoSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
                List<String> imageUris = new ArrayList<>();
                for (int imageIdx = 0; imageIdx < imageViews.length; imageIdx++) {
                    if (imageViews[imageIdx].getTag() != null) {
                        imageUris.add(imageViews[imageIdx].getTag().toString());
                    }
                }
                Long profileId = sharedPreferences.getLong(Constants.PROFILE_ID, -1);
                String certificates = StringUtils.collectionToCommaDelimitedString(imageUris);

                if (StringUtils.hasText(certificates)) {
                    userProfileManager.updateCertificates(profileId, certificates)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<UserProfile>() {
                                @Override
                                public void onCompleted() {
                                    Log.i(TAG, "Certificates updated successfully");
                                    Intent intent = new Intent(getApplicationContext(), CreateDinnerActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, e.getMessage(), e);
                                }

                                @Override
                                public void onNext(UserProfile userProfile) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Constants.CERTIFICATES, userProfile.getCertificates());
                                    editor.apply();
                                    editor.commit();
                                }
                            });
                }

            }
        });

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.Message.FILE_UPLOAD_SUCCESS:
                        Log.i(TAG, "File uploaded successfully");
                        String newImageUri = (String) msg.obj;
                        StringBuffer imageUri = new StringBuffer();
                        if (StringUtils.hasText(dinnerMenuItem.getImageUri())) {
                            imageUri.append(dinnerMenuItem.getImageUri()).append(",").append(newImageUri);
                        } else {
                            imageUri.append(newImageUri);
                        }
                        dinnerMenuItem.setImageUri(imageUri.toString());
                        break;
                    case Constants.Message.FILE_UPLOAD_FAILURE:
                        Log.i(TAG, "Failed to upload the file");
                        String m = (String) msg.obj;
                        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.Message.SAVE_MENU_ITEM_SUCCESS:
                        Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
    }

    public class ImageViewClickListener implements ImageView.OnClickListener {

        @Override
        public void onClick(final View v) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(FoodAndSafetyActivity.this, R.style.AppTheme))
                    .setItems(R.array.choose_photo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, REQUEST_IMAGE);
                                    selectedImageView = (ImageView) v;
                                    break;
                                case 1:
                                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    galleryIntent.setType("image/*");
                                    galleryIntent.addCategory(CATEGORY_OPENABLE);
                                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), SELECT_PICTURE);
                                    selectedImageView = (ImageView) v;
                            }
                        }
                    });

            alertDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap image = null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_PICTURE:
                    final Uri selectedImageUri = data.getData();
                    selectedImagePath = getAbsolutePath(selectedImageUri);
                    File selectedImage = new File(selectedImagePath);
                    if (selectedImage != null && selectedImage.length() > 2048) {
                        Toast.makeText(getApplicationContext(), "Image too large to upload. Max size cannot be more than 2 MB", Toast.LENGTH_LONG).show();
                        return;
                    }
                    selectedImageView.setImageURI(selectedImageUri);
                    image = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
                    break;
                case REQUEST_IMAGE:
                    image = (Bitmap) data.getExtras().get("data");
                    selectedImageView.setImageBitmap(image);
                    break;
            }
            if (image == null) return;
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            userProfileManager.uploadBitMap("upload.JPEG", image)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        private String url;

                        @Override
                        public void onCompleted() {
                            selectedImageView.setTag(url);
                            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            mBtnGoSell.setEnabled(true);
                        }

                        @Override
                        public void onNext(String url) {
                            this.url = url;
                        }
                    });
        }
    }

    private String getAbsolutePath(Uri uri) {
        String selectedImagePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(columnIndex);
        } else {
            selectedImagePath = null;
        }
        if (!StringUtils.hasText(selectedImagePath)) {
            selectedImagePath = uri.getPath();
        }
        return selectedImagePath;
    }

    private void setImageView(View v, String selectedImage) {
        if (!StringUtils.hasText(selectedImage)) {
            return;
        }
        Bitmap thumbnailBmp = null;
        try {

            final int THUMBNAIL_SIZE = 512;

            FileInputStream fis = new FileInputStream(selectedImage);
            thumbnailBmp = BitmapFactory.decodeStream(fis);

            thumbnailBmp = Bitmap.createScaledBitmap(thumbnailBmp,
                    THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnailBmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);


        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        if (thumbnailBmp != null) {
            ImageView imageView = (ImageView) v;
            imageView.setImageBitmap(thumbnailBmp);
        }
    }

}
