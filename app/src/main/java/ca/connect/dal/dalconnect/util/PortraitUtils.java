package ca.connect.dal.dalconnect.util;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by gaoyounan on 2018/3/19.
 */

public class PortraitUtils {

    private static PortraitUtils instance;
    private static LruCache<String, BitmapDrawable> mImageCache;

    private PortraitUtils() {}

    public static PortraitUtils getInstance()
    {
        if(instance==null)
        {
            instance = new PortraitUtils();

            int maxCache = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxCache / 8;
            mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
                @Override
                protected int sizeOf(String key, BitmapDrawable value) {
                    return value.getBitmap().getByteCount();
                }
            };
        }

        return instance;
    }

    public BitmapDrawable getPortraitByName(String portrait_name)
    {
        return mImageCache.get(portrait_name);
    }

    public void setPortraitbyName(String portrait_name, BitmapDrawable bitmapDrawable)
    {
        mImageCache.put(portrait_name, bitmapDrawable);
    }


}
