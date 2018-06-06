package com.example.cpttm.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class AndroidImageAdapter extends PagerAdapter {
    Context mContext;

    private ArrayList<Bitmap> sliderImagesBitmap;

    AndroidImageAdapter(Context context, ArrayList<Bitmap> bitmapArray) {
        this.mContext = context;
        sliderImagesBitmap = bitmapArray;
    }

    @Override
    public int getCount() {
        Log.d("ImagesBitmapCount>>>", String.valueOf(sliderImagesBitmap.size()));
        return sliderImagesBitmap.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ((ImageView) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageBitmap(sliderImagesBitmap.get(i));
        //mImageView.setImageResource(sliderImagesId[i]);
        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ((ViewPager) container).removeView((ImageView) obj);
    }
}