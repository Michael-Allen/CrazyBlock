package com.gs.blockgame.Klotski;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.gs.blockgame.Klotski.utils.Utils;
import com.gs.blockgame.R;

public class KlotskiRole extends View implements Cloneable{

    public static final int MAX = 10;
    public static final int MOVE_FLAG_CANT = 0x0000;
    public static final int MOVE_FLAG_UP = 0x0001;
    public static final int MOVE_FLAG_DOWN = 0x0010;
    public static final int MOVE_FLAG_LEFT = 0x0100;
    public static final int MOVE_FLAG_RIGHT = 0x1000;

	private String mName;
    private KlotskiLayout mLayout;
    private Paint mPaint;
    private TYPE mType = TYPE.SMALL_SQUARE;
    private int mWidth = 1;
    private int mHeight = 1;
    private int mUnitWidth;
    private int mUnitHeight;
    public int mMoveFlag = MOVE_FLAG_CANT;

    public enum TYPE
    {
        SMALL_SQUARE, LARGE_SQUARE, HORIZONTAL_RECTANGLE, VERTICAL_RECTANGLE
    }

    public enum ACTION
    {
        LEFT, RIGHT, UP, DOWM
    }

    private GestureDetector mGestureDetector;
    public boolean hasBeenAdd;

	public KlotskiRole(Context context, String name, KlotskiLayout layout)
	{
		super(context, null, 0);
        mGestureDetector = new GestureDetector(context , new MyGestureDetector());
        mLayout = layout;
		mName = name;
        mPaint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//String mBgColor = "#CCC0B3";
        //mPaint.setColor(Color.parseColor(mBgColor));
        //mPaint.setStyle(Paint.Style.FILL);
        //canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        //float x = getWidth() / 2;
        //float y = getHeight()/ 2;
        //mPaint.setColor(Color.BLACK);
        //mPaint.setTextSize(10 * getResources().getDisplayMetrics().density);
        //mPaint.setTextAlign(Paint.Align.CENTER);
        //canvas.drawText(mName, x, y, mPaint);
        if (mName.equals(Utils.CC)) {
            setBackgroundResource(R.mipmap.caocao);
        } else if (mName.equals(Utils.GY)) {
            setBackgroundResource(R.mipmap.guanyu);
        } else if (mName.equals(Utils.HZ)) {
            setBackgroundResource(R.mipmap.huangzhong);
        } else if (mName.equals(Utils.MC)) {
            setBackgroundResource(R.mipmap.machao);
        } else if (mName.equals(Utils.SB1)) {
            setBackgroundResource(R.mipmap.shibing);
        } else if (mName.equals(Utils.SB2)) {
            setBackgroundResource(R.mipmap.shibing);
        } else if (mName.equals(Utils.SB3)) {
            setBackgroundResource(R.mipmap.shibing);
        } else if (mName.equals(Utils.SB4)) {
            setBackgroundResource(R.mipmap.shibing);
        } else if (mName.equals(Utils.ZF)) {
            setBackgroundResource(R.mipmap.zhangfei);
        } else if (mName.equals(Utils.ZY)) {
            setBackgroundResource(R.mipmap.zhaoyun);
        }
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    public String getName() {
        return mName;
    }

    public TYPE getType() {
        return mType;
    }

    public int getRoleWidth() {
        return mWidth;
    }

    public int getRoleHeight() {
        return mHeight;
    }

    public void init(TYPE type, int unitWidth, int unitHeight) {
        mType = type;
        mUnitWidth = unitWidth;
        mUnitHeight = unitHeight;
        switch (mType) {
            case SMALL_SQUARE:
                mWidth = 1;
                mHeight = 1;
                break;
            case LARGE_SQUARE:
                mWidth = 2;
                mHeight = 2;
                break;
            case HORIZONTAL_RECTANGLE:
                mWidth = 2;
                mHeight = 1;
                break;
            case VERTICAL_RECTANGLE:
                mWidth = 1;
                mHeight = 2;
                break;
        }
        hasBeenAdd = true;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        final int FLING_MIN_DISTANCE = 50;
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if (x > FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
                if ((mMoveFlag & MOVE_FLAG_RIGHT) != 0) {
                    setX(getX() + mUnitWidth);
                    mLayout.rolesMoveTo(ACTION.RIGHT, KlotskiRole.this);
                    Toast.makeText(getContext(), getName() + "toRight", Toast.LENGTH_SHORT).show();
                }
            } else if (x < -FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
                if ((mMoveFlag & MOVE_FLAG_LEFT) != 0) {
                    setX(getX() - mUnitWidth);
                    mLayout.rolesMoveTo(ACTION.LEFT, KlotskiRole.this);
                    Toast.makeText(getContext(), getName() + "toLeft", Toast.LENGTH_SHORT).show();
                }
            } else if (y > FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
                if ((mMoveFlag & MOVE_FLAG_DOWN) != 0) {
                    setY(getY() + mUnitHeight);
                    mLayout.rolesMoveTo(ACTION.DOWM, KlotskiRole.this);
                    Toast.makeText(getContext(), getName() + "toDown", Toast.LENGTH_SHORT).show();
                }
            } else if (y < -FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
                if ((mMoveFlag & MOVE_FLAG_UP) != 0) {
                    setY(getY() - mUnitHeight);
                    mLayout.rolesMoveTo(ACTION.UP, KlotskiRole.this);
                    Toast.makeText(getContext(), getName() + "toUp", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }

    }
}
