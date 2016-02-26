package com.cfun.proxy;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import com.cfun.proxy.Base.BaseApplication;
import com.cfun.proxy.util.DensityUtil;

/**
 * 实现监听左右滑动的事件，哪个view需要的时候直接setOnTouchListener就可以用了
 * @author LinZhiquan
 *
 */
public abstract class GestureListener extends SimpleOnGestureListener implements OnTouchListener {
	/** 左右滑动的最短距离 dp*/
	private int distance = 100;
	/** 左右滑动的最小速度 dp*/
	private int velocity = 300;

	private GestureDetector gestureDetector;

	public GestureListener() {
		super();
		gestureDetector = new GestureDetector(BaseApplication.getInstance(), this);
	}

	/**
	 * 向左滑的时候调用的方法，子类应该重写
	 * @return
	 */
	public abstract boolean left();

	/**
	 * 向右滑的时候调用的方法，子类应该重写
	 * @return
	 */
	public abstract boolean right();

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	                       float velocityY) {
		// TODO Auto-generated method stub
		// e1：第1个ACTION_DOWN MotionEvent
		// e2：最后一个ACTION_MOVE MotionEvent
		// velocityX：X轴上的移动速度（像素/秒）
		// velocityY：Y轴上的移动速度（像素/秒）

		if(e1 == null || e2 == null)
			return false;
		// 向左滑
		if (DensityUtil.px2dip(e1.getX() - e2.getX())> distance
				&& DensityUtil.px2dip(Math.abs(velocityX)) > velocity) {
			left();
		}
		// 向右滑
		if (DensityUtil.px2dip(e2.getX() - e1.getX()) > distance
				&& DensityUtil.px2dip(Math.abs(velocityX)) > velocity) {
			right();
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		gestureDetector.onTouchEvent(event);
		return false;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
}

