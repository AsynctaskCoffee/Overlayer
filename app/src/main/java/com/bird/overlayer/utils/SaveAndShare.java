package com.bird.overlayer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import com.bird.overlayer.R;

import java.io.File;
import java.io.FileOutputStream;


public class SaveAndShare {

    public static void save(Activity activity, Bitmap bmImg, String imageName,
                            String shareTitle, String shareMessage) {

        if (checkPermissionForExternalStorage(activity)) {
            File filename;
            try {
                String path1 = android.os.Environment.getExternalStorageDirectory()
                        .toString();
                //Log.i("in save()", "after mkdir");
                File file = new File(path1 + "/" + "Overlayer");
                if (!file.exists())
                    file.mkdirs();

                String DEFAULT_IMAGE_NAME = imageName;
                if (imageName == null) {
                    DEFAULT_IMAGE_NAME = String.valueOf(System.currentTimeMillis()) + "_" + "Overlayer";
                }

                filename = new File(file.getAbsolutePath() + "/" + DEFAULT_IMAGE_NAME
                        + ".jpg");
                //Log.i("in save()", "after file");
                FileOutputStream out = new FileOutputStream(filename);
                //Log.i("in save()", "after outputstream");
                bmImg.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

                ContentValues image = getImageContent(filename, DEFAULT_IMAGE_NAME, activity);
                Uri result = activity.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);

                String SHARE_TITLE = shareTitle;
                String SHARE_MESSAGE = shareMessage;
                if (shareMessage == null) {
                    SHARE_MESSAGE = "Your photo is saved under your local folder, now you can share it via Overlayer!";
                }
                if (shareTitle == null) {
                    SHARE_TITLE = "Perfect!";
                }
                openShareOptions(SHARE_TITLE, SHARE_MESSAGE, result, activity);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            requestPermissionForExternalStorage(activity);
        }
    }

    public static ContentValues getImageContent(File parent, String imageName, Activity activity) {
        ContentValues image = new ContentValues();
        image.put(MediaStore.Images.Media.TITLE, activity.getString(R.string.app_name));
        image.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        image.put(MediaStore.Images.Media.DESCRIPTION, "This image is created by Overlayer, download app at https://egemenozogul.com");
        image.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        image.put(MediaStore.Images.Media.ORIENTATION, 0);
        image.put(MediaStore.Images.ImageColumns.BUCKET_ID, parent.toString()
                .toLowerCase().hashCode());
        image.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, parent.getName()
                .toLowerCase());
        image.put(MediaStore.Images.Media.SIZE, parent.length());
        image.put(MediaStore.Images.Media.DATA, parent.getAbsolutePath());
        return image;
    }

    public static void openShareOptions(String title, String description,
                                        final Uri imageUrl, final Activity activity) {
        new BottomDialog.Builder(activity)
                .setTitle(title)
                .setContent(description)
                .setPositiveText("SHARE")
                .setPositiveBackgroundColorResource(R.color.colorPrimary)
                .setPositiveTextColorResource(android.R.color.white)
                .setNegativeText("OPEN")
                .setNegativeTextColorResource(R.color.colorPrimaryDark)
                .onPositive(dialog -> {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    share.putExtra(Intent.EXTRA_TEXT, "This awesome image is created by Overlayer, download app at https://play.google.com/store/apps/details?id=com.bird.overlayer");
                    share.putExtra(Intent.EXTRA_STREAM, imageUrl);
                    activity.startActivity(Intent.createChooser(share, "Choose app to share"));
                    dialog.dismiss();
                    activity.finish();
                }).onNegative(dialog -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(imageUrl, "image/*");
            activity.startActivity(intent);
            dialog.dismiss();
            activity.finish();
        }).show();
    }

    public static void requestPermissionForExternalStorage(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity,
                    "External Storage permission needed. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2000);
        }
    }

    public static boolean checkPermissionForExternalStorage(Activity activity) {
        return ActivityCompat.checkSelfPermission((Activity) activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}