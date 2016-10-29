package com.example.activitytest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.cy.R;
import com.example.util.BitmapUtil;

public class Activity_D extends Activity {

    ImageView img_ora;
    ImageView img_new;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);

        // 测试两种获取缩略图的方法
        img_ora = (ImageView) findViewById(R.id.img_ora);
        img_new = (ImageView) findViewById(R.id.img_new);
        
        mHandler.sendEmptyMessageDelayed(9527, 200);
        // 测试两种获取缩略图的方法 end
        
    }
    
    public void onAttachedToWindow() {
        
    };
    
    Handler mHandler = new Handler() {
      public void handleMessage(android.os.Message msg) {
          Resources resource = getResources();
          BitmapFactory.Options opts = new BitmapFactory.Options();
          opts.inPreferredConfig = Bitmap.Config.RGB_565;
          Bitmap bitmap = BitmapFactory.decodeResource(resource, R.drawable.root_bg, opts);

          img_ora.setImageBitmap(resizeBitmap(bitmap, img_ora));
          img_new.setImageBitmap(getDiskCacheBitmap(img_new));
      };  
    };
    
    private Bitmap getDiskCacheBitmap(View targetView) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.root_bg, opts);
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = computeSampleSize(opts, targetView.getWidth(), targetView.getHeight());
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.root_bg, opts);
        
        return bitmap;
    }
    
    public static int computeSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > targetHeight || width > targetWidth) {
            final int heightRatio = Math.round((float) height / targetHeight);
            final int widthRatio = Math.round((float) width / targetWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;
    }
    
    protected Bitmap resizeBitmap(Bitmap bitmap, View view) {
        if (view == null || bitmap == null) {
            return bitmap;
        }

        int desWidth = view.getWidth();
        int desHeight = view.getHeight();
        return extractThumbnailIfNeed(bitmap, desWidth, desHeight);
    }
    
    public static Bitmap extractThumbnailIfNeed(Bitmap source, int desWidth, int desHeight) {
        if (needExtract(source, desWidth, desHeight)) {
            return extractThumbnail(source, desWidth, desHeight);
        } else {
            return source;
        }
    }
    
    private static final int MAX_DIFFER_PX = 100;

    private static boolean needExtract(Bitmap source, int desWidth, int desHeight) {
        if (desWidth <= 0 || desHeight <= 0 || source == null) {
            return false;
        }
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        return sourceWidth > desWidth + MAX_DIFFER_PX || sourceHeight > desHeight + MAX_DIFFER_PX;
    }

    public static Bitmap extractThumbnail(Bitmap source, int desWidth, int desHeight) {
        if (desWidth <= 0 || desHeight <= 0) {
            return source;
        }

        return cropBitmap(source, desWidth, desHeight);
    }
    
    public static final Paint HIGH_PAINT = new Paint(Paint.FILTER_BITMAP_FLAG);

    private static Bitmap cropBitmap(Bitmap source, int aimWidth, int aimHeight) {
        if (source == null) {
            return null;
        }
        float sourceWidth = source.getWidth();
        float sourceHeight = source.getHeight();
        if (sourceWidth == aimWidth && sourceHeight == aimHeight) {
            return source;
        }
        float offsetX = (aimWidth - sourceWidth) / 2f;
        float offsetY = (aimHeight - sourceHeight) / 2f;
        float scaleWidth = aimWidth / sourceWidth;
        float scaleHeight = aimHeight / sourceHeight;
        float scale = Math.max(scaleWidth, scaleHeight);

        Bitmap retBitmap = BitmapUtil.createBitmap(aimWidth, aimHeight, Bitmap.Config.ARGB_8888);
        Matrix matrix = new Matrix();
        Canvas canvas = new Canvas(retBitmap);
        matrix.postTranslate(offsetX, offsetY);
        matrix.postScale(scale, scale, aimWidth / 2f, aimHeight / 2f);
        canvas.drawBitmap(source, matrix, HIGH_PAINT);
        BitmapUtil.recycleBitmap(source);
        return retBitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
