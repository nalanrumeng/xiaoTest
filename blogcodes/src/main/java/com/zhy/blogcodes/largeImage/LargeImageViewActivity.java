package com.zhy.blogcodes.largeImage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zhy.blogcodes.R;
import com.zhy.blogcodes.largeImage.view.LargeImageView;
import com.zhy.blogcodes.largeImage.view.LargeLikeMapFrameLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class LargeImageViewActivity extends AppCompatActivity {
    private LargeImageView mLargeImageView;
    private LargeLikeMapFrameLayout largeLikeMapFrameLayout;
    private String[] Texts = new String[50];
    private String[] Images = new String[50];
    private Float[] randoms = new Float[51];
    int ImageWidth = 0;
    int ImageHeight = 0;
    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image_view);
        largeLikeMapFrameLayout = (LargeLikeMapFrameLayout) findViewById(R.id.large_likeMapFrameLayout);
        for (int i = 0; i < 50; i++) {
            Texts[i] = "测试" + i;
            Images[i] = "http://say1.julaibao.com//uploads/images/20180123/1516711533202169.jpg";
            randoms[i] = random.nextFloat();
            Log.i("TAG","radoms[i]="+randoms[i]);
        }
        randoms[50] = random.nextFloat();
        Log.i("TAG", "Texts[48]=" + Texts[48]);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                largeLikeMapFrameLayout.setMenuItemIconsAndTexts(Texts, Images);
                largeLikeMapFrameLayout.setRatio(randoms);
            }
        },2000);
        largeLikeMapFrameLayout.setOnMenuItemClickListener(new LargeLikeMapFrameLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                Log.i("TAG", "pos=" + pos);
            }

            @Override
            public void itemCenterClick(View view) {

            }
        });



        mLargeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
        try {
            InputStream inputStream = getAssets().open("ditu.jpg");
            mLargeImageView.setInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
