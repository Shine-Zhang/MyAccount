package com.example.zs.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zs.utils.DensityUtil;

/**
 * Created by Administrator on 2016/9/6 0006.
 * @author  Shine-Zhang
 * 该自定义View的功能主要是实现球中的波纹动画效果
 */
public class DynamicWave extends View {

    // 波纹颜色
  private static  int WAVE_PAINT_COLOR = 0xff30C9F2;
    // y = Asin(wx+b)+h
    private static  float STRETCH_FACTOR_A = 20;
    private static  int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static  int TRANSLATE_X_SPEED_ONE = 7;
    // 第二条水波移动速度
    private static  int TRANSLATE_X_SPEED_TWO = 11;
    private int mTotalWidth;
    private int mWaveHight;
    private int mWaveWidth;
    private  int mTotalHeight;
    private  float mCurentRatio=-1;
    private boolean isUpHomisphere;
    //mCycleFactorW这个参数主要影响的是波浪数，这个可以不改
    private float mCycleFactorW;
    private float[] mYPositions;
    private float[] mYStart;
    private float[] mYEnd;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    Context context;
    private float mRadius;
    private int start;
    private int end;
    private float[] mWaveEnd;
    private float[] mSkeleton;
    private int upStart;
    private int upEnd;
    public boolean flag=true;

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = DensityUtil.dip2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = DensityUtil.dip2px(context, TRANSLATE_X_SPEED_TWO);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        //mWavePaint.setColor(Color.BLUE);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        if(!isUpHomisphere){
            for(int i=upStart,j=upStart,k=upStart;i<upEnd;i++) {
                if (i + mXOneOffset < upEnd-1) {
                    canvas.drawLine(i, mYPositions[mXOneOffset + i - upStart], i, mWaveEnd[i - upStart], mWavePaint);
                } else {
                    if(j<upEnd-1){
                        canvas.drawLine(i, mYPositions[j - upStart], i, mWaveEnd[j - upStart], mWavePaint);
                        j++;
                    }

                }
            }

        }else{

            float tmp=-1;
            // Log.i("haha","start---end: "+start+"--"+end+" upstart--upend:"+upStart+" "+upEnd+"mWaveWidth: "+mWaveWidth+" mYPositions: "+mYPositions.length);
            for(int i=start,j=start,k=start;i<end;i++) {

                if (i + mXOneOffset < end-1) {
                    Log.i("haha","第一条: "+"start--end"+start+"--"+end+"--"+(mXOneOffset + i - start)+"i="+i);

                    if( mYPositions[mXOneOffset + i - start]<=mSkeleton[i]){
                        //  Log.i("haha","第一条：mSkeleton[i]: "+mSkeleton[i]+"i= "+i);
                        //tmp = mSkeleton[i];
                        tmp =  mYPositions[mXOneOffset + i - start];
                    }else{

                        tmp =  mYPositions[mXOneOffset + i - start];
                    }

                    canvas.drawLine(i,tmp, i, OFFSET_Y+STRETCH_FACTOR_A, mWavePaint);

                } else {
                    if(j<end-1) {
                        if (mYPositions[j - start] < mSkeleton[i]) {
                            Log.i("haha","第二条01: "+"start--end"+start+"--"+end+"--"+"upstart--upend:"+upStart+upEnd+"--"+(j - start)+" j="+j+"i: "+i);
                            tmp = mYPositions[j - start];
                        } else {
                            Log.i("haha","第二条02: "+"start--end"+start+"--"+end+"--"+(j - start)+" j="+j+"i: "+i);
                            tmp = mYPositions[j - start];
                        }
                        canvas.drawLine(i, tmp, i, OFFSET_Y + STRETCH_FACTOR_A, mWavePaint);
                        j++;
                    }

                }
            }
        }
        mXOneOffset += mXOffsetSpeedOne;
        if(!isUpHomisphere){

            if (mXOneOffset >= upEnd) {
                mXOneOffset = 0;
            }
        }else{
            if (mXOneOffset >=end) {
                mXOneOffset = 0;
            }
        }

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        if(flag) {
            postInvalidate();
        }
    }

    private void doneInflate(Canvas canvas) {

       if(isUpHomisphere){

           for(int i=0;i<mTotalWidth;i++){
               canvas.drawLine(i,mYStart[i],i,mYEnd[i],mWavePaint);
           }

       }else{

           for(int i=start;i<=end;i++){
               canvas.drawLine(i,mYStart[i],i,mYEnd[i],mWavePaint);
           }

       }

    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高

        //四舍五入求得半径
        mRadius = (float) (h*1.0/2);
       // Log.i("haha","*&*&*&*&*mRadius:"+mRadius);
        mTotalWidth =w;
       // Log.i("haha","*&*&*&*&*:mTotalWidth"+mTotalWidth);
        mTotalHeight = h;
       // Log.i("haha","*&*&*&*&*:mTotalHeight"+mTotalHeight);
        if(mCurentRatio>=0) {
         //   Log.i("haha","**********************************");
            mWaveHight = getCorrespondingHeight(mCurentRatio);
            STRETCH_FACTOR_A = mWaveHight/15;
            if(mCurentRatio<0.5) {
                float yDistance = Math.abs(mRadius - mWaveHight);
                mWaveWidth = (int) Math.sqrt(mRadius * mRadius - yDistance * yDistance);
            }else{
                float yDistance = Math.abs(mRadius - mWaveHight)-STRETCH_FACTOR_A;
                mWaveWidth = (int) Math.sqrt(mRadius * mRadius - yDistance * yDistance);
            }
            Log.i("hhhh","mWaveWidth: "+mWaveWidth);
            OFFSET_Y = mTotalHeight-mWaveHight;
            Log.i("haha","*&*&*&*&*:mCurentRatio"+mCurentRatio);
            // 用于保存原始波纹的y值
            mYPositions = new float[2*mWaveWidth];
            // 用于保存波纹一的y值
            mResetOneYPositions = new float[2*mWaveWidth];
            // 用于保存波纹二的y值
            mResetTwoYPositions = new float[2*mWaveWidth];

            // 将周期定为view总宽度
            mCycleFactorW = (float) (2 * Math.PI / (2*mWaveWidth));
            mYStart = new float[2*mTotalWidth];
            mYEnd = new float[2*mTotalWidth];
            // 根据view总宽度得出所有对应的y值
            for (int i = 0; i < 2*mWaveWidth; i++) {
                mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
            }

            //由于不同于矩形，每一点的高度并非定值，因此也必须计算画出的每一条直线的高度
            initWave();
        }else{

            //如果没有接受到相应的参数，就打Toasst
            Toast.makeText(context,"没有接受到正确的Ratio参数",Toast.LENGTH_LONG).show();
        }
        //根据圆形的相关计算方式计算，mTotalWidth

    }



    private void initWave() {
        //下面就需要分情况讨论了
        //1.当mWaveHight<R
        float yDistance;
        float currentWaveWidth;

        if(OFFSET_Y>mRadius){
            Log.i("cacaca","***********下半************");
            isUpHomisphere = false;
            float downSide = OFFSET_Y+STRETCH_FACTOR_A;
            currentWaveWidth = (float) Math.sqrt(mRadius*mRadius-(downSide-mRadius)*(downSide-mRadius));
           /* yDistance = mRadius - (mWaveHight);*/
            /*float xDistance = (float) Math.sqrt(mRadius*mRadius-yDistance*yDistance);*/
            float xDistance = mWaveWidth;
            mWaveEnd = new float[mWaveWidth*2];
            start = (int) (mRadius-currentWaveWidth);
            end = (int) (mRadius+currentWaveWidth);
            upStart = (int) (mRadius-mWaveWidth);
           // Log.i("hhhh","upStart: "+upStart);

           // Log.i("hhhh","mTotalWidth: "+mTotalWidth);
            upEnd = (int) (mRadius+mWaveWidth);
           // Log.i("hhhh","upStart: "+upStart+" upEnd: "+upEnd+"  "+"mRadius: "+mRadius+" start: "+start);
            for(int i = upStart; i<=mRadius; i++){
                if(i<start){
                    mWaveEnd[i-upStart] = mRadius+(float) Math.sqrt(mRadius*mRadius-((mRadius-i)*(mRadius-i)));
                  //  Log.i("hhh",""+(i-upStart)+": "+mWaveEnd[i-upStart]);
                    mWaveEnd[upEnd-i-1] = mWaveEnd[i-upStart];
                   // Log.i("hhh",""+(upEnd-i-1)+": "+mWaveEnd[upEnd-i-1]);
                }else{
                    mWaveEnd[i-upStart]=OFFSET_Y+STRETCH_FACTOR_A;
                   // Log.i("hhh",""+(i-upStart)+": "+mWaveEnd[i-upStart]);
                    mWaveEnd[upEnd-i-1]=mWaveEnd[i-upStart];
                   // Log.i("hhh",""+(upEnd-i-1)+": "+mWaveEnd[upEnd-i-1]);
                }
            }
            int i;
            for(i=start; i<= mRadius; i++){
                float tmp = (float) Math.sqrt(mRadius*mRadius-((mRadius-i)*(mRadius-i)));
                mYStart[i] = OFFSET_Y+STRETCH_FACTOR_A;
                mYStart[end-i+start]=mYStart[i];
                mYEnd[i] =tmp+mRadius;
                mYEnd[end-i+start]=mYEnd[i];
            }

        }else {
            Log.i("cacaca","***********上半************");
            isUpHomisphere = true;
            mSkeleton = new float[mTotalWidth];
            //计算上半圆的轮廓
            for(int i=0;i<mRadius;i++){
                Log.i("haha","***********************");
                mSkeleton[i] =mRadius- (float) Math.sqrt(mRadius*mRadius-((mRadius-i)*(mRadius-i)));
                mSkeleton[mTotalWidth-i-1] = mSkeleton[i];
            }
/*            yDistance = mWaveHight-mRadius;
            float xDistance = (float) Math.sqrt(mRadius*mRadius-yDistance*yDistance);*/
            float downSide = OFFSET_Y+STRETCH_FACTOR_A;
            currentWaveWidth = (float) Math.sqrt(mRadius*mRadius-(mRadius-downSide)*(mRadius-downSide));
            float tmps = (float) Math.sqrt(mRadius*mRadius-(mRadius-OFFSET_Y)*(mRadius-OFFSET_Y));
            upStart = (int) (mRadius-tmps);
            upEnd = (int) (mRadius+tmps);
            start = (int) (mRadius-currentWaveWidth);
             end = (int) (mRadius+currentWaveWidth);
            Log.i("hghghg","upStart: "+upStart+" start: "+start);


            int i;
            for( i=0;i<start;i++){
                /*float tmp = (float) Math.sqrt(mRadius*mRadius-((mRadius-i)*(mRadius-i)));*/
                float tmp = mSkeleton[i];
                mYStart[i] = tmp;
                mYStart[mTotalWidth-1-i]=mYStart[i];
                mYEnd[i] = mTotalHeight-tmp;
                mYEnd[mTotalWidth-1-i]=mYEnd[i];
            }

            for(;i<=mRadius;i++){
             /*   float tmp = (float) Math.sqrt(mRadius*mRadius-((mRadius-i)*(mRadius-i)));*/
                float tmp = mSkeleton[i];
                mYStart[i] =  OFFSET_Y+STRETCH_FACTOR_A;
                mYStart[mTotalWidth-1-i]=mYStart[i];
                mYEnd[i] = mTotalHeight-tmp;
                mYEnd[mTotalWidth-1-i]=mYEnd[i];
            }
        }
    }

    public static int getWavePaintColor() {
        return WAVE_PAINT_COLOR;
    }

    public static void setWavePaintColor(int wavePaintColor) {
        WAVE_PAINT_COLOR = wavePaintColor;
    }

    public static float getStretchFactorA() {
        return STRETCH_FACTOR_A;
    }

    public static void setStretchFactorA(float stretchFactorA) {
        STRETCH_FACTOR_A = stretchFactorA;
    }

    public static int getOffsetY() {
        return OFFSET_Y;
    }

    public static void setOffsetY(int offsetY) {
        OFFSET_Y = offsetY;
    }public static int getTranslateXSpeedOne() {
        return TRANSLATE_X_SPEED_ONE;
    }

    public static void setTranslateXSpeedOne(int translateXSpeedOne) {
        TRANSLATE_X_SPEED_ONE = translateXSpeedOne;
    }

    public static int getTranslateXSpeedTwo() {
        return TRANSLATE_X_SPEED_TWO;
    }

    public static void setTranslateXSpeedTwo(int translateXSpeedTwo) {
        TRANSLATE_X_SPEED_TWO = translateXSpeedTwo;
    }

    public void setmCurentRatio(float mCurentRatio) {
        this.mCurentRatio = mCurentRatio;
    }

    //根据明细页面传递过来的ratio,计算其对应的高度(通过四舍五入的方式计算出整数形式的高度)
    private int getCorrespondingHeight(float mCurentRatio){

        return (int)(mCurentRatio*mTotalHeight+0.5);
    }


}
