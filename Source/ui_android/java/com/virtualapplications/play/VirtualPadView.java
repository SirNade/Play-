package com.virtualapplications.play;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import java.util.*;

public class VirtualPadView extends SurfaceView
{
	private ArrayList<VirtualPadButton> _buttons = new ArrayList<VirtualPadButton>();

	private Bitmap select = BitmapFactory.decodeResource(getResources(), R.drawable.select);
	private Bitmap start = BitmapFactory.decodeResource(getResources(), R.drawable.start);
	private Bitmap up = BitmapFactory.decodeResource(getResources(), R.drawable.up);
	private Bitmap down = BitmapFactory.decodeResource(getResources(), R.drawable.down);
	private Bitmap left = BitmapFactory.decodeResource(getResources(), R.drawable.left);
	private Bitmap right = BitmapFactory.decodeResource(getResources(), R.drawable.right);
	private Bitmap triangle = BitmapFactory.decodeResource(getResources(), R.drawable.triangle);
	private Bitmap cross = BitmapFactory.decodeResource(getResources(), R.drawable.cross);
	private Bitmap square = BitmapFactory.decodeResource(getResources(), R.drawable.square);
	private Bitmap circle = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
	
	public VirtualPadView(Context context, AttributeSet attribs)
	{
		super(context, attribs);

		setWillNotDraw(false);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		Log.w(Constants.TAG, String.format("onSizeChanged - %d, %d, %d, %d", w, h, oldw, oldh));

		float margin = 64;
		
		float dpadPosX = margin;
		float dpadPosY = h - 384 - margin;
		float actionPadPosX = w - 384 - margin;
		float actionPadPosY = h - 384 - margin;
		float startSelPadPosX = (w - 384) / 2;
		float startSelPadPosY = h - 64 - margin;
		
		_buttons.clear();

		_buttons.add(new VirtualPadButton("Select", VirtualPadConstants.BUTTON_SELECT,
				new RectF(startSelPadPosX + 0, startSelPadPosY + 0, startSelPadPosX + 128, startSelPadPosY + 64),
				select));
		_buttons.add(new VirtualPadButton("Start", VirtualPadConstants.BUTTON_START,
				new RectF(startSelPadPosX + 256, startSelPadPosY + 0, startSelPadPosX + 384, startSelPadPosY + 64),
				start));

		_buttons.add(new VirtualPadButton("Up", VirtualPadConstants.BUTTON_UP,
				new RectF(dpadPosX + 128, dpadPosY + 0, dpadPosX + 256, dpadPosY + 192),
				up));
		_buttons.add(new VirtualPadButton("Down", VirtualPadConstants.BUTTON_DOWN,
				new RectF(dpadPosX + 128, dpadPosY + 192, dpadPosX + 256, dpadPosY + 384),
				down));
		_buttons.add(new VirtualPadButton("Left", VirtualPadConstants.BUTTON_LEFT,
				new RectF(dpadPosX + 0, dpadPosY + 128, dpadPosX + 192, dpadPosY + 256),
				left));
		_buttons.add(new VirtualPadButton("Right", VirtualPadConstants.BUTTON_RIGHT,
				new RectF(dpadPosX + 192, dpadPosY + 128, dpadPosX + 384, dpadPosY + 256),
				right));

		_buttons.add(new VirtualPadButton("Triangle", VirtualPadConstants.BUTTON_TRIANGLE,
				new RectF(actionPadPosX + 128, actionPadPosY + 0, actionPadPosX + 256, actionPadPosY + 128),
				triangle));
		_buttons.add(new VirtualPadButton("Cross", VirtualPadConstants.BUTTON_CROSS,
				new RectF(actionPadPosX + 128, actionPadPosY + 256, actionPadPosX + 256, actionPadPosY + 384),
				cross));
		_buttons.add(new VirtualPadButton("Square", VirtualPadConstants.BUTTON_SQUARE,
				new RectF(actionPadPosX + 0, actionPadPosY + 128, actionPadPosX + 128, actionPadPosY + 256),
				square));
		_buttons.add(new VirtualPadButton("Circle", VirtualPadConstants.BUTTON_CIRCLE,
				new RectF(actionPadPosX + 256, actionPadPosY + 128, actionPadPosX + 384, actionPadPosY + 256),
				circle));

		postInvalidate();
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		for(VirtualPadButton button : _buttons)
		{
			button.draw(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		int action = event.getActionMasked();
		int pointerIndex = event.getActionIndex();
		int pointerId = event.getPointerId(pointerIndex);
		float x = event.getX(pointerIndex);
		float y = event.getY(pointerIndex);
		if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)
		{
			for(VirtualPadButton button : _buttons)
			{
				RectF bounds = button.getBounds();
				if(bounds.contains(x, y))
				{
					NativeInterop.reportInput(button.getValue(), true);
					button.setPressed(true);
					postInvalidate();
					button.setPointerId(pointerId);
					break;
				}
			}
		}
		else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
		{
			for(VirtualPadButton button : _buttons)
			{
				if(button.getPointerId() == pointerId && button.getPressed())
				{
					NativeInterop.reportInput(button.getValue(), false);
					button.setPressed(false);
					postInvalidate();
					break;
				}
			}
		}
		return true;
	}
};
