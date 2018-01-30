package com.zhy.blogcodes.largeImage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TimeUtils;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhy on 15/5/16.
 */
public class LargeImageView extends View
{
    private boolean isShouldLayout=false;
    private BitmapRegionDecoder mDecoder;
    /**
     * 图片的宽度和高度
     */
    public int mImageWidth, mImageHeight;
    /**
     * 绘制的区域
     */
    private volatile Rect mRect = new Rect();

    private MoveGestureDetector mDetector;


    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    static
    {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public void setInputStream(InputStream is)
    {
        try
        {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // Grab the bounds for the scene dimensions
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;
            Log.i("TAG", "mImageWidth and mImageHeight=" + mImageWidth + ":" + mImageHeight);

            requestLayout();
            invalidate();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {

            try
            {
                if (is != null) is.close();
            } catch (Exception e)
            {
            }
        }
    }


    public void init()
    {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector()
        {
            @Override
            public boolean onMove(MoveGestureDetector detector)
            {
                int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();
                isShouldLayout= true;

                if (mImageWidth > getWidth())
                {
                    mRect.offset(-moveX, 0);
                    Log.i("TAG", "LargeImag=" + mRect.left);
                    checkWidth();
                    invalidate();


                }
                if (mImageHeight > getHeight())
                {
                    mRect.offset(0, -moveY);
                    checkHeight();
                    invalidate();


                }

                return true;
            }
        });
    }


    private void checkWidth()
    {


        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.right > imageWidth)
        {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();

        }

        if (rect.left < 0)
        {
            rect.left = 0;
            rect.right = getWidth();

        }
    }


    private void checkHeight()
    {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;


            if (rect.bottom > imageHeight)
            {
                rect.bottom = imageHeight;

                rect.top = imageHeight - getHeight();
        }

        if (rect.top < 0)
        {
            rect.top = 0;
            rect.bottom = getHeight();

        }
    }


    public LargeImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mDetector.onToucEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Log.i("TAG","on Draw()");
        Log.i("TAG", "LargeImag1=" + mRect.left);
        Bitmap bm = mDecoder.decodeRegion(mRect, options);
        canvas.drawBitmap(bm, 0, 0, null);
//        if(isShouldLayout) {
            requestLayout();
//            isShouldLayout=false;
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight/2-height/2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
        Log.i("TAG", "left top right bottom=" + mRect.left+" "+mRect.top+" "+mRect.right+" "+mRect.bottom);
    }
    public int getRectTop(){
        return mRect.top;
    }
    public int getRectBottom(){
        return mRect.bottom;
    }
    public int getRectLeft(){
        return mRect.left;
    }
    public int getRectRight(){
        return mRect.right;
    }



}
