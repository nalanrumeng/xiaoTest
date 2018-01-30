package com.zhy.blogcodes.largeImage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zhy.blogcodes.R;
import com.zhy.blogcodes.largeImage.CircleBitmapDisplayer;

/**
 * Created by $LiZhuHui on 2018/1/23 20:13
 */
public class LargeLikeMapFrameLayout extends FrameLayout {
    private Context context;
    //半径
    private int mRadius;

    private int Size;
    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 6f;
    /**
     * 菜单的中心child的默认尺寸
     */
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量(内边距，即item和viewgroup的边框距离）
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;

    /**
     * 菜单项的文本
     */
    private String[] mItemTexts;
    /**
     * 菜单项的图标
     */
    private int[] mItemImgs;

    private String[] mImagesUrl;

    private Float[] radios;
    private int updateCount = 0;
    private int mMenuItemLayoutId = R.layout.circle_menu_item;


    /**
     * 菜单的个数
     */
    private int mMenuItemCount;

    private LargeImageView largeImageView;

    public LargeLikeMapFrameLayout(Context context) {
        this(context,null);
    }

    public LargeLikeMapFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
    }
    public void setRatio(Float[] ratio) {
        radios = ratio;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;

        /**
         * 根据传入的参数，分别获取测量模式和测量值
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 如果宽或者高的测量模式非精确值
         */
        if (widthMode != MeasureSpec.EXACTLY
                || heightMode != MeasureSpec.EXACTLY) {
            // 主要设置为背景图的高度
            resWidth = getSuggestedMinimumWidth();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

            resHeight = getSuggestedMinimumHeight();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {
            // 如果都设置为精确值，则直接取小值；
            if (Size == 0) {
                Size = ((Math.max(width, height)));
            }
            resWidth = resHeight = Size;
            Log.i("TAG", "resWidth=" + resWidth);
        }

        setMeasuredDimension(resWidth, resHeight);

        // 获得直径
        mRadius = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = (int) (mRadius);

        // menu item数量
        final int count = getChildCount();
        // menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        // menu item测量模式(我们设置精确值，希望子View让我们设置的尺寸来显示大小
        int childMode = MeasureSpec.EXACTLY;

        // 迭代测量
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            // 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
            int makeMeasureSpec = -1;
            //测量中心view的大小
            if (child.getId() == R.id.id_largetImageview) {
                if(largeImageView==null) {
                    largeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
                    makeMeasureSpec = MeasureSpec.makeMeasureSpec(
                            height,
                            childMode);
                }else{
                    continue;
                }
            } else {
                if(largeImageView!=null) {
                    int rectLeft=largeImageView.getRectLeft();
                    int rectRight=largeImageView.getRectRight();
                    int rectTop=largeImageView.getRectTop();
                    int rectBottom=largeImageView.getRectBottom();
                    int ImageWidth=largeImageView.mImageWidth;
                    int ImageHeight=largeImageView.mImageHeight;
                    int childX= (int) (radios[i]*ImageWidth);
                    int childY=(int) (radios[i]*ImageHeight);
                    if(childX>=rectLeft&&childX<=rectRight&&childY>=rectTop&&childY<=rectBottom) {
                        makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
                                childMode);
                    }else{
                        continue;
                    }
                }
            }
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }

    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int layoutRadius = mRadius;
        // Laying out the child views
        final int childCount = getChildCount();
        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        int l,t;

        Log.i("TAG", "getChildCount=" + getChildCount());

        Log.i("TAG", "left top right bottom=" + left + " " + top + " " + right + " " + bottom);
        if(largeImageView==null) {
            largeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
            largeImageView.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
        }
        // 遍历去设置menuitem的位置
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }
            if(child.getId()==R.id.id_largetImageview){
                continue;
            }
            if (largeImageView != null) {
//                Log.i("TAG", "left=" + largeImageView.getRectLeft());
//                int rectLeft = largeImageView.getRectLeft();
//                int rectRight = largeImageView.getRectRight();
//                int rectTop = largeImageView.getRectTop();
//                int rectBottom = largeImageView.getRectBottom();
//                int ImageWidth = largeImageView.mImageWidth;
//                int ImageHeight = largeImageView.mImageHeight;
//                int childX = (int) (radios[i] * ImageWidth);
//                int childY = (int) (radios[i-1] * ImageHeight);
//                if(childX<0){
//                    childX=0;
//                }
//                if(childY>ImageHeight){
//                    childY=ImageHeight;
//                }
//                if (childX >= rectLeft && childX <= rectRight && childY >= rectTop && childY <= rectBottom) {
//                    Log.i("TAG", "childX chidY=" + childX + "  " + childY+" "+i);
//                    l=childX-rectLeft;
//                    t=childY-rectTop;
////                   if(i==50) {
//                       Log.i("TAG", "l t=" + l + "  " + t + " " + i);
////                   }
//                    child.layout(l, t, l + cWidth, t + cWidth);
//                } else {
//                    continue;
//                }

            }
        }
    }
    /**
     * 设置菜单条目的图标和文本
     *
     * @param
     */
    public void setMenuItemIconsAndTexts(String[] texts, String[] imagesUrl) {

        mItemTexts = texts;
        mImagesUrl = imagesUrl;

        // 参数检查
        if (imagesUrl == null && texts == null) {
            throw new IllegalArgumentException("菜单项文本和图片至少设置其一");
        }

        // 初始化mMenuCount
        mMenuItemCount = imagesUrl == null ? texts.length : imagesUrl.length;

        if (imagesUrl != null && texts != null) {
            mMenuItemCount = Math.min(imagesUrl.length, texts.length);
        }

        addMenuItems();

    }
    /**
     * 添加菜单项
     */
    private void addMenuItems() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());

        /**
         * 根据用户设置的参数，初始化view
         */
        Log.i("TAG", "mMenuItemCount=" + mMenuItemCount);
        for (int i = 0; i < mMenuItemCount; i++) {
            Log.i("TAG","mImagesUrl[i]="+i  +mImagesUrl[i]);
            final int j = i;
            View view = mInflater.inflate(mMenuItemLayoutId, this, false);
            ImageView iv = (ImageView) view
                    .findViewById(R.id.id_circle_menu_item_image);
            TextView tv = (TextView) view
                    .findViewById(R.id.id_circle_menu_item_text);

            if (iv != null) {
                iv.setVisibility(View.VISIBLE);
//				iv.setImageResource(mItemImgs[i]);
                if (!mImagesUrl[i].isEmpty() && mImagesUrl[i].startsWith("http")) {
                    ImageLoader.getInstance().displayImage(mImagesUrl[i], iv, circleoptions);
                }
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(v, j);
                            Log.i("TAG", "j=" + j);
                        }
                    }
                });
            }
            if (tv != null) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(mItemTexts[i]);
            }

            // 添加view到容器中
            addView(view);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.i("TAG", "dispatchDraw");
        super.dispatchDraw(canvas);
    }

    /**
     * 获得默认该layout的尺寸
     *
     * @return
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }
    public  DisplayImageOptions circleoptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_useravatar)
            .showImageForEmptyUri(R.drawable.default_useravatar)
            .showImageOnFail(R.drawable.default_useravatar)
            .showImageOnLoading(R.color.white)
            .showImageForEmptyUri(R.drawable.default_useravatar)
            .showImageOnFail(R.color.white)
            .resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new CircleBitmapDisplayer())
            .build();
    /**
     * MenuItem的点击事件接口
     *
     * @author zhy
     */
    public interface OnMenuItemClickListener {
        void itemClick(View view, int pos);

        void itemCenterClick(View view);
    }
    /**
     * MenuItem的点击事件接口
     */
    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     * 设置MenuItem的点击事件接口
     *
     * @param mOnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(
            OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
}
