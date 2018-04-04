package ca.connect.dal.dalconnect.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by gaoyounan on 2018/3/9.
 */

public class FileStorageUtils
{
    private static FileStorageUtils instance;
    private static FirebaseStorage storage = null;
    private static final String STORAGE_URL = "gs://dalconnect-c74c7.appspot.com/chat_portrait";

    private FileStorageUtils() {}

    public static FileStorageUtils getInstance()
    {
        if(instance==null)
        {
            instance = new FileStorageUtils();
            storage = FirebaseStorage.getInstance();
        }
        return instance;
    }

    public void loadImage(final String portraitName, final ImageView iv)
    {

        StorageReference storageRef = storage.getReference().child("Portraits/"+ portraitName);

        try
        {
            final File  localFile = File.createTempFile(portraitName, "jpg");

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created

                    if (iv != null ) {
                        Bitmap bitmap = getBitmap(localFile);
                        iv.setImageBitmap(bitmap);
                        PortraitUtils.getInstance().setPortraitbyName(portraitName, new BitmapDrawable(bitmap));
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    System.out.println("Handle any errors");
                }
            });


        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {

        }

    }



    public void loadImage(final String portraitName, final String userName, final ViewGroup viewGroup)
    {

        StorageReference storageRef = storage.getReference().child("Portraits/"+ portraitName);

        try
        {
            final File  localFile = File.createTempFile(portraitName, "jpg");

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created

                    ImageView iv = (ImageView) viewGroup.findViewWithTag(userName);
                    if (iv != null ) {
                        Bitmap bitmap = getBitmap(localFile);
                        iv.setImageBitmap(bitmap);
                        PortraitUtils.getInstance().setPortraitbyName(portraitName, new BitmapDrawable(bitmap));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    System.out.println("Handle any errors");
                }
            });


        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmap(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
