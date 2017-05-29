package com.gs.blockgame.Klotski;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.content.Context;

import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.gs.blockgame.Klotski.utils.Utils;
import com.gs.blockgame.Klotski.KlotskiRole.ACTION;

public class KlotskiLayout extends RelativeLayout {


    public static final int HIGHT = 5;
    public static final int WIDTH = 4;
    /**
     * Role Map
     */
    final ArrayMap<String, KlotskiRole> mRoles =
            new ArrayMap<String, KlotskiRole>();
    /**
     * 位置表
     */
    private String[][] mRolesTable = new String[WIDTH][HIGHT];

    /**
     * 2个空格的位置和排列情况
     */
    private int[] mBlank1 = {0, 0};
    private int[] mBlank2 = {0, 0};

    private BLANKTYPE mBlankType = BLANKTYPE.NONE;

    public enum BLANKTYPE
    {
        NONE, INDEPENDENT, HORIZONTAL, VERTICAL
    }

    private int childWidth;
    private int childHeight;
    /**
     * Current Step
     */
    private int mStep;

    /**
     * Step Stack
     */
    private Stack<String[][]> mKlotskiStack;

    /**
     * Score Stack
     */
    private Stack<Integer> mScoreStack;
    private OnGameKlotskiListener mGameKlotskiListener;
    private boolean once;

    @SuppressLint("NewApi")
    public KlotskiLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        //mMargin, getResources().getDisplayMetrics());
        // ����Layout���ڱ߾࣬�ı�һ�£�����Ϊ���ڱ߾��е���Сֵ
        //mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
        //		getPaddingBottom());

    }

    @SuppressLint("ClickableViewAccessibility")
    public KlotskiLayout(Context context) {
        this(context, null);
    }

    public KlotskiLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setOnGame2048Listener(OnGameKlotskiListener mGameKlotskiListener) {
        this.mGameKlotskiListener = mGameKlotskiListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void setLevel(int level) {
        Context mContext = getContext();
        mRoles.put(Utils.CC, new KlotskiRole(mContext, Utils.CC, this));
        mRoles.put(Utils.GY, new KlotskiRole(mContext, Utils.GY, this));
        mRoles.put(Utils.HZ, new KlotskiRole(mContext, Utils.HZ, this));
        mRoles.put(Utils.ZY, new KlotskiRole(mContext, Utils.ZY, this));
        mRoles.put(Utils.ZF, new KlotskiRole(mContext, Utils.ZF, this));
        mRoles.put(Utils.MC, new KlotskiRole(mContext, Utils.MC, this));
        mRoles.put(Utils.SB1, new KlotskiRole(mContext, Utils.SB1, this));
        mRoles.put(Utils.SB2, new KlotskiRole(mContext, Utils.SB2, this));
        mRoles.put(Utils.SB3, new KlotskiRole(mContext, Utils.SB3, this));
        mRoles.put(Utils.SB4, new KlotskiRole(mContext, Utils.SB4, this));
        if (mScoreStack == null) {
            mScoreStack = new Stack<Integer>();
        }
        Levle levle = new Levle(0);
        mRolesTable = levle.getLeaveTable();
    }

    /**
     * ����Layout�Ŀ�͸ߣ��Լ�����Item�Ŀ�͸ߣ��������wrap_content �Կ���֮�е���Сֵ����������
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // ��������εı߳�
        int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
        //int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
        //int height = Math.min(getMeasuredHeight(), getMeasuredWidth());
        childWidth = getMeasuredWidth() / 4;
        childHeight = getMeasuredHeight() / 5;

        if (!once) {
				/*
                if ((i + 1) % mColumn != 0)
				{
					lp.rightMargin = mMargin;
				}
				if (i % mColumn != 0)
				{
					lp.addRule(RelativeLayout.RIGHT_OF,//
							mKlotski_Role[i - 1].getId());
				}
				if ((i + 1) > mColumn)
				{
					lp.topMargin = mMargin;
					lp.addRule(RelativeLayout.BELOW,//
							mKlotski_Role[i - mColumn].getId());
				}*/
            initRolesView(childHeight, childWidth);
            once = true;

            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    private void initRolesView(int childHeight, int childWidth) {
        for (int i = 0; i < HIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                String rolesName = mRolesTable[i][j];
                if (rolesName != null) {
                    KlotskiRole role = mRoles.get(rolesName);
                    if (role.hasBeenAdd) continue;
                    RelativeLayout.LayoutParams lp = null;
                    KlotskiRole.TYPE type = KlotskiRole.TYPE.SMALL_SQUARE;
                    if (rolesName.equals(Utils.ZY) || rolesName.equals(Utils.MC) || rolesName.equals(Utils.GY) || rolesName.equals(Utils.ZF) || rolesName.equals(Utils.HZ)) {
                        if (i < HIGHT - 1) {
                            String rolesNameDown = mRolesTable[i + 1][j];
                            if (rolesName.equals(rolesNameDown) && lp == null) {
                                lp = new LayoutParams(childWidth, childHeight * 2);
                                type = KlotskiRole.TYPE.VERTICAL_RECTANGLE;
                            }
                        }
                        if (j < WIDTH - 1) {
                            String rolesNameRight = mRolesTable[i][j + 1];
                            if (rolesName.equals(rolesNameRight) && lp == null) {
                                lp = new LayoutParams(childWidth * 2, childHeight);
                                type = KlotskiRole.TYPE.HORIZONTAL_RECTANGLE;
                            }
                        }
                    } else if (rolesName.equals(Utils.CC)) {
                        lp = new LayoutParams(childWidth * 2, childHeight * 2);
                        type = KlotskiRole.TYPE.LARGE_SQUARE;

                    } else if (rolesName.equals(Utils.SB1) || rolesName.equals(Utils.SB2) || rolesName.equals(Utils.SB3) || rolesName.equals(Utils.SB4)) {
                        lp = new LayoutParams(childWidth, childHeight);
                        type = KlotskiRole.TYPE.SMALL_SQUARE;
                    }
                    lp.topMargin = childHeight * i;
                    lp.leftMargin = childWidth * j;
                    addView(role, lp);
                    role.init(type, childWidth, childHeight);
                }
            }
        }
        syncRolesMvoeflags();
    }

    private void syncRolesMvoeflags() {
        clearRolesMvoeflags();

        for (int i = 0; i < HIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                String rolesName = mRolesTable[i][j];
                if (rolesName == null) {
                    if (mBlankType == BLANKTYPE.NONE) {
                        mBlank1[0] = i;
                        mBlank1[1] = j;
                        if (i < HIGHT - 1 && mRolesTable[i+1][j] == null) {
                            mBlankType = BLANKTYPE.VERTICAL;
                        } else if (j < WIDTH -1 && mRolesTable[i][j+1] == null) {
                            mBlankType = BLANKTYPE.HORIZONTAL;
                        } else {
                            mBlankType = BLANKTYPE.INDEPENDENT;
                        }
                    } else {
                        mBlank2[0] = i;
                        mBlank2[1] = j;
                    }
                }
            }
        }

        switch (mBlankType) {
            case INDEPENDENT:
                int i = mBlank1[0];
                int j = mBlank1[1];
                if (i - 1 >= 0) {
                    KlotskiRole role = mRoles.get(mRolesTable[i-1][j]);
                    if (role.getRoleWidth() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_DOWN;
                    }
                }
                if (i < HIGHT - 1) {
                    KlotskiRole role = mRoles.get(mRolesTable[i+1][j]);
                    if (role.getRoleWidth() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_UP;
                    }
                }
                if (j - 1 >= 0) {
                    KlotskiRole role = mRoles.get(mRolesTable[i][j-1]);
                    if (role.getRoleHeight() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_RIGHT;
                    }
                }
                if (j < WIDTH - 1) {
                    KlotskiRole role = mRoles.get(mRolesTable[i][j+1]);
                    if (role.getRoleHeight() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_LEFT;
                    }
                }

                i = mBlank2[0];
                j = mBlank2[1];
                if (i - 1 >= 0) {
                    KlotskiRole role = mRoles.get(mRolesTable[i-1][j]);
                    if (role.getRoleWidth() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_DOWN;
                    }
                }
                if (i < HIGHT - 1) {
                    KlotskiRole role = mRoles.get(mRolesTable[i+1][j]);
                    if (role.getRoleWidth() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_UP;
                    }
                }
                if (j - 1 >= 0) {
                    KlotskiRole role = mRoles.get(mRolesTable[i][j-1]);
                    if (role.getRoleHeight() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_RIGHT;
                    }
                }
                if (j < WIDTH - 1) {
                    KlotskiRole role = mRoles.get(mRolesTable[i][j+1]);
                    if (role.getRoleHeight() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_LEFT;
                    }
                }
                break;
            case VERTICAL:
                i = mBlank1[0];
                j = mBlank1[1];
                if (i - 1 >= 0) {
                    KlotskiRole role = mRoles.get(mRolesTable[i-1][j]);
                    if (role.getRoleWidth() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_DOWN;
                    }
                }
                if (i < HIGHT - 2) {
                    KlotskiRole role = mRoles.get(mRolesTable[i+2][j]);
                    if (role.getRoleWidth() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_UP;
                    }
                }
                if (j - 1 >= 0) {
                    KlotskiRole role1 = mRoles.get(mRolesTable[i][j-1]);
                    KlotskiRole role2 = mRoles.get(mRolesTable[i+1][j-1]);
                    if (role1.equals(role2)) {
                        role1.mMoveFlag = role1.mMoveFlag | KlotskiRole.MOVE_FLAG_RIGHT;
                    } else {
                        if (role1.getRoleHeight() == 1) {
                            role1.mMoveFlag = role1.mMoveFlag | KlotskiRole.MOVE_FLAG_RIGHT;
                        }
                        if (role2.getRoleHeight() == 1) {
                            role2.mMoveFlag = role2.mMoveFlag | KlotskiRole.MOVE_FLAG_RIGHT;
                        }
                    }
                }
                if (j < WIDTH - 1) {
                    KlotskiRole role1 = mRoles.get(mRolesTable[i][j+1]);
                    KlotskiRole role2 = mRoles.get(mRolesTable[i+1][j+1]);
                    if (role1.equals(role2)) {
                        role1.mMoveFlag = role1.mMoveFlag | KlotskiRole.MOVE_FLAG_LEFT;
                    } else {
                        if (role1.getRoleHeight() == 1) {
                            role1.mMoveFlag = role1.mMoveFlag | KlotskiRole.MOVE_FLAG_LEFT;
                        }
                        if (role2.getRoleHeight() == 1) {
                            role2.mMoveFlag = role2.mMoveFlag | KlotskiRole.MOVE_FLAG_LEFT;
                        }
                    }
                }
                break;
            case HORIZONTAL:
                i = mBlank1[0];
                j = mBlank1[1];
                if (j - 1 >= 0) {
                    KlotskiRole role = mRoles.get(mRolesTable[i][j-1]);
                    if (role.getRoleHeight() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_RIGHT;
                    }
                }
                if (j < WIDTH - 2) {
                    KlotskiRole role = mRoles.get(mRolesTable[i][j+2]);
                    if (role.getRoleHeight() == 1) {
                        role.mMoveFlag = role.mMoveFlag | KlotskiRole.MOVE_FLAG_LEFT;
                    }
                }
                if (i - 1 >= 0) {
                    KlotskiRole role1 = mRoles.get(mRolesTable[i-1][j]);
                    KlotskiRole role2 = mRoles.get(mRolesTable[i-1][j+1]);
                    if (role1.equals(role2)) {
                        role1.mMoveFlag = role1.mMoveFlag | KlotskiRole.MOVE_FLAG_DOWN;
                    } else {
                        if (role1.getRoleWidth() == 1) {
                            role1.mMoveFlag = role1.mMoveFlag | KlotskiRole.MOVE_FLAG_DOWN;
                        }
                        if (role2.getRoleWidth() == 1) {
                            role2.mMoveFlag = role2.mMoveFlag | KlotskiRole.MOVE_FLAG_DOWN;
                        }
                    }
                }
                if (i < HIGHT - 1) {
                    KlotskiRole role1 = mRoles.get(mRolesTable[i+1][j]);
                    KlotskiRole role2 = mRoles.get(mRolesTable[i+1][j+1]);
                    if (role1.equals(role2)) {
                        role1.mMoveFlag = role1.mMoveFlag | KlotskiRole.MOVE_FLAG_UP;
                    } else {
                        if (role1.getRoleWidth() == 1) {
                            role1.mMoveFlag = role1.mMoveFlag | KlotskiRole.MOVE_FLAG_UP;
                        }
                        if (role2.getRoleWidth() == 1) {
                            role2.mMoveFlag = role2.mMoveFlag | KlotskiRole.MOVE_FLAG_UP;
                        }
                    }
                }
                break;
        }
    }

    private void clearRolesMvoeflags() {
        mRoles.get(Utils.CC).mMoveFlag = 0;
        mRoles.get(Utils.GY).mMoveFlag = 0;
        mRoles.get(Utils.HZ).mMoveFlag = 0;
        mRoles.get(Utils.MC).mMoveFlag = 0;
        mRoles.get(Utils.ZF).mMoveFlag = 0;
        mRoles.get(Utils.ZY).mMoveFlag = 0;
        mRoles.get(Utils.SB1).mMoveFlag = 0;
        mRoles.get(Utils.SB2).mMoveFlag = 0;
        mRoles.get(Utils.SB3).mMoveFlag = 0;
        mRoles.get(Utils.SB4).mMoveFlag = 0;
        mBlankType = BLANKTYPE.NONE;
    }

    public void rolesMoveTo(ACTION action, KlotskiRole role) {
        int[] roleIndex = roleToIndex(role);
        switch (action) {
            case RIGHT:
                for (int i = roleIndex[0]; i < roleIndex[0] + role.getRoleHeight(); i++) {
                    for (int j = roleIndex[1]; j < roleIndex[1] + role.getRoleWidth(); j++) {
                        mRolesTable[i][j+1] = role.getName();
                    }
                }
                for (int i = 0; i < role.getRoleHeight(); i++) {
                    mRolesTable[roleIndex[0] + i][roleIndex[1]] = null;
                }
                break;
            case LEFT:
                for (int i = roleIndex[0]; i < roleIndex[0] + role.getRoleHeight(); i++) {
                    for (int j = roleIndex[1]; j < roleIndex[1] + role.getRoleWidth(); j++) {
                        mRolesTable[i][j-1] = role.getName();
                    }
                }
                for (int i = 0; i < role.getRoleHeight(); i++) {
                    mRolesTable[roleIndex[0] + i][roleIndex[1] + (role.getRoleWidth() - 1)] = null;
                }
                break;
            case DOWM:
                for (int i = roleIndex[0]; i < roleIndex[0] + role.getRoleHeight(); i++) {
                    for (int j = roleIndex[1]; j < roleIndex[1] + role.getRoleWidth(); j++) {
                        mRolesTable[i+1][j] = role.getName();
                    }
                }
                for (int i = 0; i < role.getRoleWidth(); i++) {
                    mRolesTable[roleIndex[0]][roleIndex[1] + i] = null;
                }
                break;
            case UP:
                for (int i = roleIndex[0]; i < roleIndex[0] + role.getRoleHeight(); i++) {
                    for (int j = roleIndex[1]; j < roleIndex[1] + role.getRoleWidth(); j++) {
                        mRolesTable[i-1][j] = role.getName();
                    }
                }
                for (int i = 0; i < role.getRoleWidth(); i++) {
                    mRolesTable[roleIndex[0] + (role.getRoleHeight() -1)][roleIndex[1] + i] = null;
                }
                break;
        }
        syncRolesMvoeflags();
    }

    public int[] roleToIndex(KlotskiRole role) {
        String name = role.getName();
        int[] index = new int[2];
        for (int i = 0; i < HIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if(name.equals(mRolesTable[i][j])) {
                    index[0] = i; index[1] = j;
                    return index;
                }
            }
        }
        return index;
    }

    /**
     * ���ݵ�����
     */

    public interface OnGameKlotskiListener {
        void onScoreChange(int score);

        void onGameOver();
    }
}
