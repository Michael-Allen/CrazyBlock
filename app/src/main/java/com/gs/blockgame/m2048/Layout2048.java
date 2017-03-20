package com.gs.blockgame.m2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class Layout2048 extends RelativeLayout
{

	/**
	 * ����Item������n*n��Ĭ��Ϊ4
	 */
	private int mColumn = 4;
	/**
	 * ������е�Item
	 */
	private Item2048[] mItem2048;

	/**
	 * Item����������ı߾�
	 */
	private int mMargin = 10;
	/**
	 * ����padding
	 */
	private int mPadding;
	/**
	 * ����û�����������
	 */
	private GestureDetector mGestureDetector;

	// ����ȷ���Ƿ���Ҫ����һ���µ�ֵ
	private boolean isMergeHappen = true;
	private boolean isMoveHappen = true;

	/**
	 * ��¼����
	 */
	private int mScore;

	/**
	 * ����ÿһ����Ϊ�ĵ�ջ
	 */
	private Stack<int[]> m2048Stack;
	
	/**
	 * ��������ĵ�ջ
	 */
	private Stack<Integer> mScoreStack;
	
	public interface OnGame2048Listener
	{
		void onScoreChange(int score);

		void onGameOver();
	}

	private OnGame2048Listener mGame2048Listener;

	public void setOnGame2048Listener(OnGame2048Listener mGame2048Listener)
	{
		this.mGame2048Listener = mGame2048Listener;
	}

	/**
	 * �˶������ö��
	 * 
	 * @author zhy
	 * 
	 */
	private enum ACTION
	{
		LEFT, RIGHT, UP, DOWM
	}

	@SuppressLint("NewApi")
	public Layout2048(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				mMargin, getResources().getDisplayMetrics());
		// ����Layout���ڱ߾࣬�ı�һ�£�����Ϊ���ڱ߾��е���Сֵ
		mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
				getPaddingBottom());

		mGestureDetector = new GestureDetector(context , new MyGestureDetector());

	}

	/**
	 * �����û��˶�����������ƶ��ϲ�ֵ��
	 */
	private void action(ACTION action)
	{
		//long a = System.currentTimeMillis();
		//Log.e("gaoshang","a = " + a);
		save();
		//long b = System.currentTimeMillis();
		//Log.e("gaoshang","save time  = " + (b-a));
		// ��|��
		for (int i = 0; i < mColumn; i++)
		{
			List<Item2048> row = new ArrayList<Item2048>();
			// ��|��
			//��¼��Ϊ0������
			for (int j = 0; j < mColumn; j++)
			{
				// �õ��±�
				int index = getIndexByAction(action, i, j);

				Item2048 item = mItem2048[index];
				// ��¼��Ϊ0������
				if (item.getNumber() != 0)
				{
					row.add(item);
				}
			}
			
			//�ж��Ƿ����ƶ�
			for (int j = 0; j < mColumn && j < row.size(); j++)
			{
				int index = getIndexByAction(action, i, j);
				Item2048 item = mItem2048[index];

				if (item.getNumber() != row.get(j).getNumber())
				{
					isMoveHappen = true;
				}
			}
			
			// �ϲ���ͬ��
			mergeItem(row);
			
			// ���úϲ����ֵ
			for (int j = 0; j < mColumn; j++)
			{
				int index = getIndexByAction(action, i, j);
				if (row.size() > j)
				{
					mItem2048[index].setNumber(row.get(j).getNumber());
				} else
				{
					mItem2048[index].setNumber(0);
				}
			}

		}
		//��������
		generateNum();
	}

	/**
	 * ���浱ǰ�Ľ�����ջ
	 */
	private void save()
	{
		int[] Item2048 = new int[mColumn * mColumn];
		for (int i=0 ; i < mColumn * mColumn ; i++){
				Item2048[i] = mItem2048[i].getNumber();
			}
		m2048Stack.push(Item2048);
		mScoreStack.push(mScore);
		//Log.e("gaoshang","Stacksize = " + m2048Stack.size());
		/*int[] a = m2048Stack.peek();
		for (int i=0;i<mColumn * mColumn;i++){
			Log.e("gaoshang","number = " + a[i]);
		}*/
	}
	
	/**
	 * �˻�
	 */
	public void back()
	{
		if ( !m2048Stack.empty() ) {
			int[] a = m2048Stack.pop();
			int Score = mScoreStack.pop();
			mScore = Score;
			
		if (mGame2048Listener != null) {
			mGame2048Listener.onScoreChange(mScore);
			}
		
		for (int i=0; i < mColumn * mColumn; i++) {
			Log.e("gaoshang","number = " + a[i]);
		}
		
		for (int i=0;i<mColumn * mColumn;i++)
			{
				if (a[i] != 0)
				{
						mItem2048[i].setNumber(a[i]);
				} else
				{
					mItem2048[i].setNumber(0);
				}
			}
		}
	}
	
	/**
	 * ����Action��i,j�õ��±�
	 * 
	 * @param action
	 * @param i
	 * @param j
	 * @return
	 */
	private int getIndexByAction(ACTION action, int i, int j)
	{
		int index = -1;
		switch (action)
		{
		case LEFT:
			index = i * mColumn + j;
			break;
		case RIGHT:
			index = i * mColumn + mColumn - j - 1;
			break;
		case UP:
			index = i + j * mColumn;
			break;
		case DOWM:
			index = i + (mColumn - 1 - j) * mColumn;
			break;
		}
		return index;
	}

	/**
	 * �ϲ���ͬ��Item
	 * 
	 * @param row
	 */
	private void mergeItem(List<Item2048> row)
	{
		if (row.size() < 2)
			return;

		for (int j = 0; j < row.size() - 1; j++)
		{
			Item2048 item1 = row.get(j);
			Item2048 item2 = row.get(j + 1);

			if (item1.getNumber() == item2.getNumber())
			{
				isMergeHappen = true;

				int val = item1.getNumber() + item2.getNumber();
				item1.setNumber(val);

				// �ӷ�
				mScore += val;
				if (mGame2048Listener != null)
				{
					mGame2048Listener.onScoreChange(mScore);
				}

				// ��ǰ�ƶ�
				for (int k = j + 1; k < row.size() - 1; k++)
				{
					row.get(k).setNumber(row.get(k + 1).getNumber());
				}
				
				row.get(row.size() - 1).setNumber(0);
				return;
			}

		}

	}

	/**
	 * �õ���ֵ�е���Сֵ
	 * 
	 * @param params
	 * @return
	 */
	private int min(int... params)
	{
		int min = params[0];
		for (int param : params)
		{
			if (min > param)
			{
				min = param;
			}
		}
		return min;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mGestureDetector.onTouchEvent(event);
		return true;
	}
	@SuppressLint("ClickableViewAccessibility")
	public Layout2048(Context context)
	{
		this(context, null);
	}

	public Layout2048(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	private boolean once;

	/**
	 * ����Layout�Ŀ�͸ߣ��Լ�����Item�Ŀ�͸ߣ��������wrap_content �Կ���֮�е���Сֵ����������
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ��������εı߳�
		int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
		// ���Item�Ŀ��
		int childWidth = (length - mPadding * 2 - mMargin * (mColumn - 1))
				/ mColumn;

		if (!once)
		{
			if (mItem2048 == null)
			{
				mItem2048 = new Item2048[mColumn * mColumn];
			}
			if (m2048Stack == null)
			{
				m2048Stack = new Stack<int[]>();
			}
			if (mScoreStack == null)
			{
				mScoreStack = new Stack<Integer>();
			}
			// ����Item
			for (int i = 0; i < mItem2048.length; i++)
			{
				Item2048 item = new Item2048(getContext());

				mItem2048[i] = item;
				item.setId(i + 1);
				RelativeLayout.LayoutParams lp = new LayoutParams(childWidth,
						childWidth);
				// ���ú���߾�,�������һ��
				if ((i + 1) % mColumn != 0)
				{
					lp.rightMargin = mMargin;
				}
				// ������ǵ�һ��
				if (i % mColumn != 0)
				{
					lp.addRule(RelativeLayout.RIGHT_OF,//
							mItem2048[i - 1].getId());
				}
				// ������ǵ�һ�У�//��������߾࣬�����һ��
				if ((i + 1) > mColumn)
				{
					lp.topMargin = mMargin;
					lp.addRule(RelativeLayout.BELOW,//
							mItem2048[i - mColumn].getId());
				}
				addView(item, lp);
			}
			generateNum();
		}
		once = true;

		setMeasuredDimension(length, length);
	}

	/**
	 * �Ƿ���������
	 * 
	 * @return
	 */
	private boolean isFull()
	{
		// ����Ƿ�����λ�ö�������
		for (int i = 0; i < mItem2048.length; i++)
		{
			if (mItem2048[i].getNumber() == 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * ��⵱ǰ���е�λ�ö������֣������ڵ�û����ͬ������
	 * 
	 * @return
	 */
	private boolean checkOver()
	{
		// ����Ƿ�����λ�ö�������
		if (!isFull())
		{
			return false;
		}

		for (int i = 0; i < mColumn; i++)
		{
			for (int j = 0; j < mColumn; j++)
			{

				int index = i * mColumn + j;

				// ��ǰ��Item
				Item2048 item = mItem2048[index];
				// �ұ�
				if ((index + 1) % mColumn != 0)
				{
					Log.e("TAG", "RIGHT");
					// �ұߵ�Item
					Item2048 itemRight = mItem2048[index + 1];
					if (item.getNumber() == itemRight.getNumber())
						return false;
				}
				// �±�
				if ((index + mColumn) < mColumn * mColumn)
				{
					Log.e("TAG", "DOWN");
					Item2048 itemBottom = mItem2048[index + mColumn];
					if (item.getNumber() == itemBottom.getNumber())
						return false;
				}
				// ���
				if (index % mColumn != 0)
				{
					Log.e("TAG", "LEFT");
					Item2048 itemLeft = mItem2048[index - 1];
					if (itemLeft.getNumber() == item.getNumber())
						return false;
				}
				// �ϱ�
				if (index + 1 > mColumn)
				{
					Log.e("TAG", "UP");
					Item2048 itemTop = mItem2048[index - mColumn];
					if (item.getNumber() == itemTop.getNumber())
						return false;
				}

			}

		}

		return true;

	}

	/**
	 * ����һ������
	 */
	public void generateNum()
	{

		if (checkOver())
		{
			Log.e("TAG", "GAME OVER");
			if (mGame2048Listener != null)
			{
				mGame2048Listener.onGameOver();
			}
			return;
		}

		if (!isFull())
		{
			if (isMoveHappen || isMergeHappen)
			{
				Random random = new Random();
				int next = random.nextInt(16);
				Item2048 item = mItem2048[next];

				while (item.getNumber() != 0)
				{
					next = random.nextInt(16);
					item = mItem2048[next];
				}

				item.setNumber(Math.random() > 0.75 ? 4 : 2);

				isMergeHappen = isMoveHappen = false;
			}

		}
	}

	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void restart()
	{
		for (Item2048 item : mItem2048)
		{
			item.setNumber(0);
		}
		mScore = 0;
		if (mGame2048Listener != null)
		{
			mGame2048Listener.onScoreChange(mScore);
		}
		isMoveHappen = isMergeHappen = true;
		generateNum();
		m2048Stack.clear();
		mScoreStack.clear();
	}

	class MyGestureDetector extends GestureDetector.SimpleOnGestureListener
	{

		final int FLING_MIN_DISTANCE = 50;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY)
		{
			float x = e2.getX() - e1.getX();
			float y = e2.getY() - e1.getY();

			if (x > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > Math.abs(velocityY))
			{
				action(ACTION.RIGHT);
				// Toast.makeText(getContext(), "toRight",
				// Toast.LENGTH_SHORT).show();

			} else if (x < -FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > Math.abs(velocityY))
			{
				action(ACTION.LEFT);
				// Toast.makeText(getContext(), "toLeft",
				// Toast.LENGTH_SHORT).show();

			} else if (y > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) < Math.abs(velocityY))
			{
				action(ACTION.DOWM);
				// Toast.makeText(getContext(), "toDown",
				// Toast.LENGTH_SHORT).show();

			} else if (y < -FLING_MIN_DISTANCE
					&& Math.abs(velocityX) < Math.abs(velocityY))
			{
				action(ACTION.UP);
				// Toast.makeText(getContext(), "toUp",
				// Toast.LENGTH_SHORT).show();
			}
			return true;

		}

	}

}
