package ca.connect.dal.dalconnect.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import ca.connect.dal.dalconnect.R;

/**
 * Created by gaoyounan on 2018/3/9.
 */

public class FileStorageUtils
{
    private static FileStorageUtils instance;
    private static FirebaseStorage storage = null;
    private static final String STORAGE_URL = "gs://dalconnect-c74c7.appspot.com/chat_portrait";
    private static File cache_directory = null;
    private static List<String> filename_list = null;

    private FileStorageUtils() {}

    public static FileStorageUtils getInstance(File fileDirectory)
    {
        if(instance==null)
        {
            instance = new FileStorageUtils();
            storage = FirebaseStorage.getInstance();
            cache_directory = fileDirectory;
            filename_list = new ArrayList<String>();
        }
        return instance;
    }

    public void clearCacheFile()
    {
        for(String filename: filename_list)
        {
            File file = new File(cache_directory, filename);
            if(file.exists())
            {
                file.delete();
            }
        }

    }

    private File createTempFile(String fileName)
    {
        File localfile = new File(cache_directory, fileName);

        File parentFile = localfile.getParentFile();
        if (!parentFile.exists()){
            parentFile.mkdirs();
        }

        filename_list.add(fileName);
        return localfile;
    }

    public void loadImage(final String portraitName, final Map<String, Bitmap> bitMapMemoryCache, final Handler mHandler, final Resources resources)
    {
        StorageReference storageRef = storage.getReference().child("Portraits/"+ portraitName);

        final File localFile = createTempFile(portraitName);
        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                Bitmap bitmap = getBitmap(localFile);
                bitMapMemoryCache.put(portraitName, bitmap);
                mHandler.obtainMessage(bitMapMemoryCache.size()).sendToTarget();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

                System.out.println("Handle any errors");
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon);;
                bitMapMemoryCache.put(portraitName, bitmap);
                setPortraitbyName(portraitName, bitmap);
                mHandler.obtainMessage(bitMapMemoryCache.size()).sendToTarget();
            }
        });



    }

    public void loadImage(final String portraitName, final ImageView iv, final Resources resources)
    {

        StorageReference storageRef = storage.getReference().child("Portraits/"+ portraitName);

        final File localFile = createTempFile(portraitName);
        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                if (iv != null ) {
                    Bitmap bitmap = getBitmap(localFile);
                    iv.setImageBitmap(bitmap);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

                System.out.println("Handle any errors");
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon);
                setPortraitbyName(portraitName, bitmap);
                if (iv != null ) {
                    iv.setImageBitmap(bitmap);
                }
            }
        });


    }



    public void loadImage(final String portraitName, final String userName, final ViewGroup viewGroup, final Resources resources)
    {

        StorageReference storageRef = storage.getReference().child("Portraits/"+ portraitName);
        final File localFile = createTempFile(portraitName);

        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                ImageView iv = (ImageView) viewGroup.findViewWithTag(userName);
                if (iv != null ) {
                    Bitmap bitmap = getBitmap(localFile);
                    iv.setImageBitmap(bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("Handle any errors");
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon);
                setPortraitbyName(portraitName, bitmap);
                ImageView iv = (ImageView) viewGroup.findViewWithTag(userName);
                if (iv != null ) {
                    iv.setImageBitmap(bitmap);
                }

            }
        });


    }

    public Bitmap getPortraitByName(String portrait_name)
    {
        File file=new File(cache_directory ,portrait_name);
        if(file.exists()){

            return getBitmap(file) ;
        }
        else{

            return null;
        }
    }

    public void setPortraitbyName(String portrait_name, Bitmap bitmap)
    {
        try {

            File file = this.createTempFile(portrait_name);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
            filename_list.add(portrait_name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmap(File file) {
        try {

            Bitmap bitmap = null;
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);

                bitmap = BitmapFactory.decodeStream(fileInputStream);
                if(bitmap != null)
                {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                }
            }
            return bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public Bitmap loadImageSync(final String portraitName){

        try
        {
            final File  localFile = File.createTempFile(portraitName, "jpg");
            final List<Bitmap> bitmapList = new ArrayList<Bitmap>();
            final StorageReference storageRef = storage.getReference().child("Portraits/"+ portraitName);
            final CountDownLatch latch = new CountDownLatch(1);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created
                            bitmapList.add(getBitmap(localFile));
                            latch.countDown();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors

                            System.out.println("Handle any errors");
                        }
                    });
                }
            }).start();

            try
            {
                latch.await();
            }
            catch (InterruptedException e)
            {
            }

            return bitmapList.size()>0 ? bitmapList.get(1) : null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public Bitmap loadImageSync2(final String portraitName)
    {
        StorageReference storageRef = storage.getReference().child("Portraits/"+ portraitName);

        try
        {
            final File localFile = File.createTempFile(portraitName, "jpg");
            final List<Bitmap> bitmapList = new ArrayList<Bitmap>();

            OnSuccessListener successListener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    //synchronized(this) {
                    bitmapList.add(getBitmap(localFile));
                    this.notify();
                    //}
                }
            };

            synchronized (this) {

                storageRef.getFile(localFile).addOnSuccessListener(successListener);
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return bitmapList.size()>0 ? bitmapList.get(1) : null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }*/
}
