package com.android.move_box_game;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBar extends RelativeLayout {

    private Button leftButton;
    private Button rightButton;
    private TextView title;

    private Drawable titleBackground;
    private Drawable leftBackground;
    private Drawable rightBackground;

    private String titleText;
    private String leftText;
    private String rightText;

    private int  leftColor;
    private int rightColor;
    private int  titleColor;

    private float titleTextSize;
    private float leftSize;
    private float rightSize;

    private LayoutParams leftParams,rightParams,titleParams;

    private  TopBarOnClickListener mListener;
    public interface TopBarOnClickListener{
        public void leftClick();
        public void rightCilck();
    }

    public void setOnTopBarListener(TopBarOnClickListener listener){
        this.mListener = listener;
    }

    public TopBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta  = context.obtainStyledAttributes(attrs,R.styleable.TopBar);
        leftColor = ta.getColor(R.styleable.TopBar_leftTextColor,0);
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
        leftText = ta.getString(R.styleable.TopBar_leftText);
        leftSize  =ta.getDimension(R.styleable.TopBar_leftTextSize,0);

       rightColor = ta.getColor(R.styleable.TopBar_rightTextColor,0);
        rightBackground = ta.getDrawable(R.styleable.TopBar_RightBackground);
        rightText = ta.getString(R.styleable.TopBar_rightText);
        rightSize  =ta.getDimension(R.styleable.TopBar_rightTextSize,0);

       titleColor = ta.getColor(R.styleable.TopBar_titleTextColor,0);
        titleBackground  =ta.getDrawable(R.styleable.TopBar_titleBackground);
        titleText = ta.getString(R.styleable.TopBar_title);
        titleTextSize  =ta.getDimension(R.styleable.TopBar_titleTextSize,0);

        ta.recycle();

        leftButton= new Button(context);
        rightButton  =new Button(context);
        title = new Button(context);

        leftButton.setText(leftText);
        leftButton.setTextColor(leftColor);
        leftButton.setBackground(leftBackground);
        leftButton.setTextSize(leftSize);

        rightButton.setText(rightText);
        rightButton.setTextColor(rightColor);
        rightButton.setBackground(rightBackground);
        rightButton.setTextSize(rightSize);

        title.setBackground(titleBackground);
        title.setText(titleText);
        title.setTextColor(titleColor);
        title.setTextSize(titleTextSize);
        title.setGravity(Gravity.CENTER);

        setBackgroundColor(0xfff0ffff);

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButton,leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightButton,rightParams);

       titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(title,titleParams);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.leftClick();
            }
        });


        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.rightCilck();
            }
        });

    }
    public void setTitle(String string){
        title.setText(string);
    }

}
