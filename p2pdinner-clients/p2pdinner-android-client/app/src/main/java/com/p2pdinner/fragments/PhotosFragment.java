package com.p2pdinner.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.p2pdinner.R;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.ImageDownloadTask;
import com.p2pdinner.restclient.MenuServiceManager;

import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.CATEGORY_OPENABLE;

/**
 * Created by rajaniy on 9/24/15.
 */
public class PhotosFragment extends BaseFragment {


    private ImageView[] imageViews = new ImageView[4];
    private Button mBtnNext;
    private ImageView selectedImageView;
    private ProgressBar mProgressBar;
    private String selectedImagePath;

    private static final int SELECT_PICTURE = 1;
    private static final String TAG = MenuServiceManager.class.getName();

    private Handler handler;
    @Inject
    MenuServiceManager menuServiceManager;
    private DinnerMenuItem dinnerMenuItem;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && getActivity() != null) {
            dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photos_layout, container, false);
        dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        imageViews[0] = (ImageView) view.findViewById(R.id.photo1);
        imageViews[1] = (ImageView) view.findViewById(R.id.photo2);
        imageViews[2] = (ImageView) view.findViewById(R.id.photo3);
        imageViews[3] = (ImageView) view.findViewById(R.id.photo4);
        mProgressBar = (ProgressBar) view.findViewById(R.id.uploadProgress);
        for (int imageIdx = 0; imageIdx < imageViews.length; imageIdx++) {
            imageViews[imageIdx].setOnClickListener(new ImageViewClickListener());
        }
        if (StringUtils.hasText(dinnerMenuItem.getImageUri())) {
            String[] images = dinnerMenuItem.getImageUri().split(",");
            if (images != null && images.length != 0) {
                for (int idx = 0; idx < images.length; idx++) {
                    String uri = StringUtils.replace(images[idx], "\"", "");
                    imageViews[idx].setTag(images[idx]);
                    ImageDownloadTask imageDownloadTask = new ImageDownloadTask(imageViews[idx]);
                    imageDownloadTask.execute(uri);
                }
            }
        }
        mBtnNext = (Button) view.findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.hasText(dinnerMenuItem.getTitle())) {
                    List<String> imageUris = new ArrayList<>();
                    for(int imageIdx = 0; imageIdx < imageViews.length; imageIdx++) {
                        if (imageViews[imageIdx].getTag() != null) {
                            imageUris.add(imageViews[imageIdx].getTag().toString());
                        }
                    }
                    dinnerMenuItem.setImageUri(StringUtils.collectionToCommaDelimitedString(imageUris));
                    menuServiceManager.saveMenuItem(dinnerMenuItem)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<DinnerMenuItem>() {
                                private DinnerMenuItem item;

                                @Override
                                public void onCompleted() {
                                    dinnerMenuItem.setId(item.getId());
                                    Toast.makeText(getActivity(), "Item saved successfully", Toast.LENGTH_SHORT).show();
                                    ListDinnerActivity listDinnerActivity = (ListDinnerActivity) getActivity();
                                    listDinnerActivity.moveToNextTab();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNext(DinnerMenuItem item) {
                                    this.item = item;
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
                        Toast.makeText(getActivity(), m, Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.Message.SAVE_MENU_ITEM_SUCCESS:
                        //Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                        ListDinnerActivity listDinnerActivity = (ListDinnerActivity) getActivity();
                        listDinnerActivity.moveToNextTab();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
        return view;
    }


    public class ImageViewClickListener implements ImageView.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE);
            selectedImageView = (ImageView) v;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                final Uri selectedImageUri = data.getData();
                selectedImageView.setImageURI(selectedImageUri);
                selectedImagePath = getAbsolutePath(selectedImageUri);
                Bitmap image = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                menuServiceManager.uploadBitMap("upload.JPEG", image)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            private String url;
                            @Override
                            public void onCompleted() {
                                selectedImageView.setTag(url);
                                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
//                                StringBuffer imageUri = new StringBuffer();
//                                if (StringUtils.hasText(dinnerMenuItem.getImageUri())) {
//                                    imageUri.append(dinnerMenuItem.getImageUri()).append(",").append(url);
//                                } else {
//                                    imageUri.append(url);
//                                }
//                                dinnerMenuItem.setImageUri(imageUri.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                mBtnNext.setEnabled(true);
                            }

                            @Override
                            public void onNext(String url) {
                                this.url = url;
                            }
                        });
            }
        }
    }

    private String getAbsolutePath(Uri uri) {
        String selectedImagePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
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
