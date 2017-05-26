package com.android.move_box_game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.*;

/**
 * 用canvas实现画布的设计，UI美化
 * Created by quXian on 2017/5/3.
 */
public class GameView extends View {

    private Bitmap box;  //箱子的位图
    private Bitmap people; //小人的位图
    private Bitmap flag;  //旗子的位图
    private Bitmap wall;  //墙壁的位图

    boolean success = false;  //成功的标志

    private static int MAX_SPACE = 12;  //设置最大的空格数
    private int spaceTotalLength;  //设置正方形区域的边长
    private float eachSpaceLength; //设置每个小正方格的边长

    private ArrayList<int[][]> footMap = new ArrayList<int[][]>(); //记录足迹

  //  int[][]map = new int[MAX_SPACE][MAX_SPACE];
  //设置地图

    int[][]map ={ //0是空格，1是墙壁，2是旗子(7,3)(8,3)(3,4)(3,5)，3是箱子，4是人(6,5)
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,1,1,1,1,1,1,1,0,0,0},
            {0,0,1,0,2,2,3,0,1,0,0,0},
            {0,0,1,0,1,0,3,0,1,0,0,0},
            {0,0,1,0,1,0,1,0,1,0,0,0},
            {0,0,1,0,3,4,1,0,1,0,0,0},
            {0,0,1,2,3,0,0,0,1,0,0,0},
            {0,0,1,2,1,1,1,1,1,0,0,0},
            {0,0,1,1,1,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
    };

    int[][]addMap = new int[MAX_SPACE][MAX_SPACE];

    //当前人的坐标
    private int curX = 6;
    private int curY = 5;
    private java.util.List<Point> flagPosition =  new ArrayList<>();//当前旗子的位置

    //第一关：2是旗子(7,3)(8,3)(3,4)(3,5)
    Point f1 = new Point(7,3);
    Point f2 = new Point(8,3);
    Point f3 = new Point(3,4);
    Point f4 = new Point(3,5);


    private java.util.List<Point> touchPointArray= new ArrayList<>();

    private Paint linePaint = new Paint();//划线的样式
/*******************************************************************************/
    //自定义一个passListener
    private PassListener mPassListener;
    public void setPassListener(PassListener mPassListener){
        this.mPassListener  = mPassListener;
    }

    public interface PassListener{
       public void exit();
        public void next();
    }
/*********************************************************************************/


    //类似于view的构造函数,3种方式
    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        InitLinePaint();
        InitBitmap();
        InitFootMap();
        InitFlag();
    }
    public GameView(Context context) {
        super(context);
        InitFootMap();
        InitLinePaint();
        InitBitmap();
        InitFlag();
    }
    public GameView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet,defStyle);
        InitFootMap();
        InitLinePaint();
        InitBitmap();
        InitFlag();
    }
    /***********************************************************/
    private void InitFootMap(){
        footMap.add(map);
    }

    public void InitFlag(){
        if(flagPosition.size()==0){
            flagPosition.add(f1);
            flagPosition.add(f2);
            flagPosition.add(f3);
            flagPosition.add(f4);
        }
        //Log.e("tag",""+flagPosition.size());
    }

    //每次走一步增加一份地图
    private void addFootMap(){
        for(int i =0;i<MAX_SPACE;i++){
            for(int j=0;j<MAX_SPACE;j++){
                addMap[i][j] = map[i][j];
            }
        }
        footMap.add(addMap);
       // Log.e("666",""+footMap.size());
    }

    //删去最后一份地图并且返回前一地图
    public void getBeforeMap(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(footMap.size()>0){
                    map = footMap.get(footMap.size()-1);
                    Log.e("111","执行");
                    postInvalidate();
                    footMap.remove(footMap.size()-1);
                }
            }
        });
      thread.start();
        Log.e("666",""+footMap.size());
    }

    private void InitBitmap(){
        //设置位图
        flag = BitmapFactory.decodeResource(getResources(),R.drawable.flag);
        wall = BitmapFactory.decodeResource(getResources(),R.drawable.wall);
        box = BitmapFactory.decodeResource(getResources(),R.drawable.box);
       people = BitmapFactory.decodeResource(getResources(),R.drawable.people);

    }


    //初始化画布
    private void InitLinePaint(){
        //初始化wallPaint
        linePaint.setColor(Color.BLACK);
        linePaint.setAlpha(100);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAntiAlias(true);//Antialias 适应该效果后,普通显示效果,消除锯齿
        linePaint.setDither(true);
        linePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    //传入两个参数，设计画布为正方形
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        //super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize  = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode  = MeasureSpec.getMode(heightMeasureSpec);


        int width  = Math.min(widthSize,heightSize);

        //如果有其中一个数为0时
        if(heightMode == MeasureSpec.UNSPECIFIED){
            width  = widthSize;
        }else if(widthMode == MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }
        Log.e("l","正方形的边长是"+width);
        setMeasuredDimension(width,width);
    }


    //设置长度
    @Override
    protected  void onSizeChanged(int w,int h,int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        spaceTotalLength = w;             //总宽度
        eachSpaceLength = spaceTotalLength*1.0f/MAX_SPACE; //每个的宽度
    }


public boolean onTouchEvent(MotionEvent event){
    int action = event.getAction();
    if(action == MotionEvent.ACTION_DOWN){
        int x = (int)event.getX();
        int y = (int)event.getY();
        Point touchPoint = new Point(x,y);
        int Y = (int)(x/eachSpaceLength);
        int X = (int)(y/eachSpaceLength);
       // Log.e("tag",""+X);//点击空格的横坐标
       // Log.e("tag",""+Y);//点击空格的纵坐标
        if(map[X][Y] == 0 || map[X][Y] == 2  || map[X][Y] == 3){
            if(X == curX && Y<curY){
                for(int sept = 0;sept<=curY-Y;sept++){
                    moveLeft();
                }
            }
            if(X == curX && Y >curY){
                for(int sept = 0;sept<= Y -curY;sept++){
                    moveRight();
                }
            }
            if(X > curX && Y ==curY){
                for(int sept = 0;sept<=X-curX;sept++){
                    moveDown();
                }
            }
            if(X<curX && Y==curY){
                for(int sept = 0;sept<=curX-X;sept++){
                    moveUp();
                }
            }
        }
        if(map[X][Y] == 1){
          Toast.makeText(getContext(),"无法移动",Toast.LENGTH_SHORT).show();
        }
        return true;
    }//对touch事件感兴趣
    return super.onTouchEvent(event);
}


    @Override
    protected  void onDraw(Canvas canvas) {
     super.onDraw(canvas);
        int s = spaceTotalLength;
        float e = eachSpaceLength;
        for(int i = 0 ;i<MAX_SPACE;i++){
            int startX = 0;
            int endX = s+1;
            int y =(int)(e*i+1);
            canvas.drawLine(startX, y , endX , y ,linePaint);//画出一条横线
        }

        for(int i = 0 ;i<MAX_SPACE;i++){
            int startY = 0;
            int endY = s+1;
            int x =(int)(e*i+1);
            canvas.drawLine(x,startY,x,endY,linePaint);//画出一条纵线
        }


        //绘制地图
        for(int i = 0;i<MAX_SPACE;i++){             //y轴
            for(int k = 0;k<MAX_SPACE;k++){        //x轴
                int x  =map[i][k];
                Rect rect = new Rect((int)((eachSpaceLength)*k),(int)((eachSpaceLength)*i),
                        (int)((eachSpaceLength)*(k+1)),(int)((eachSpaceLength)*(i+1)));
                switch(x){//0是空格，1是墙壁，2是旗子，3是箱子，4是人，42是人覆盖了红旗，32是箱子覆盖了红旗
                    //2可能会被3或者4覆盖，此时2将不显示，当为0的时候重新显示
                    case 0://空的位置不作任何处理,红旗的位置不会改变
                        for(int a=0;a<flagPosition.size();a++){
                            if(i==flagPosition.get(a).x && k==flagPosition.get(a).y){
                                canvas.drawBitmap(flag,null,rect,null);
                            }
                        }
                      break;
                    case 1:
                        canvas.drawBitmap(wall,null,rect,null);
                        break;
                    case 2:
                        canvas.drawBitmap(flag,null,rect,null);
                        break;
                    case 3:
                        canvas.drawBitmap(box,null,rect,null);
                        break;
                    case 4:
                        canvas.drawBitmap(people,null,rect,null);
                        break;
                    default:
                        break;
                }
            }

        }
    }

    //设置新的地图,为以后的关卡做准备
    public void setNewMap(int [][]newMap){
        flagPosition.clear();
        for(int i = 0;i<MAX_SPACE;i++){
            for(int j = 0;j<MAX_SPACE;j++){
                map[i][j]=newMap[i][j];
                if(map[i][j]==4){
                    curY=j;curX=i;
                }
                if(map[i][j]==2){
                    Point p = new Point(i,j);
                    flagPosition.add(p);
                }
            }
        }
        postInvalidate();
    }

    //0是空格，1是墙壁，2是旗子，3是箱子，4是人
 //向左边移动
    public void moveUp(){
        addFootMap();
      int x = map[curX][curY];
        switch (map[curX-1][curY]){
            case 0:
                map[curX-1][curY] = 4;
                map[curX][curY] = 0;
                break;
            case 1:
                Toast.makeText(getContext(),"上边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                map[curX-1][curY] = 4;
                map[curX][curY] = 0;
                break;

            case 3:
                switch (map[curX-2][curY]){
                    case 0:
                        map[curX-2][curY] = 3;
                        map[curX-1][curY] = 4;
                        map[curX][curY] = 0;
                        break;
                    case 1:
                        Toast.makeText(getContext(),"上边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        map[curX-2][curY] = 3;
                        map[curX-1][curY] = 4;
                        map[curX][curY] = 0;
                        break;
                    case 3:
                        Toast.makeText(getContext(),"上面是两个箱子，无法移动",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                break;
        }
        for(int i = 0;i<MAX_SPACE;i++){
            for(int k = 0;k<MAX_SPACE;k++ ){
                if(map[i][k]==4){
                    curY=k;
                    curX=i;
                }
            }
        }
        postInvalidate();
     //   addFootMap(map);
        ifPass();
       // Log.e("tag","当前小人的位置"+curX+curY);
    }
    //向右边移动
    public void moveDown(){
        addFootMap();
        int x = map[curX][curY];
        switch (map[curX+1][curY]){
            case 0:
                map[curX+1][curY] = 4;
                map[curX][curY] = 0;
                break;

            case 1:
                Toast.makeText(getContext(),"下边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                break;

            case 2:
                map[curX+1][curY] = 4;
                map[curX][curY] = 0;
                break;

            case 3:
                switch (map[curX+2][curY]){
                    case 0:
                        map[curX+2][curY] = 3;
                        map[curX+1][curY] = 4;
                        map[curX][curY] = 0;
                        break;
                    case 1:
                        Toast.makeText(getContext(),"下边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        map[curX+2][curY] = 3;
                        map[curX+1][curY] = 4;
                        map[curX][curY] = 0;
                        break;
                    case 3:
                        Toast.makeText(getContext(),"下边是两个箱子，无法移动",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                break;
        }
        for(int i = 0;i<MAX_SPACE;i++){
            for(int k = 0;k<MAX_SPACE;k++ ){
                if(map[i][k]==4){
                    curY=k;
                    curX=i;
                }
            }
        }
        postInvalidate();
       // addFootMap(map);
        ifPass();
       // Log.e("tag","当前小人的位置"+curX+curY);
    }
    //向左边移动
    //0是空格，1是墙壁，2是旗子，3是箱子，4是人
    public void moveLeft(){
        addFootMap();
        int x = map[curX][curY];
        switch (map[curX][curY-1]){
            case 0:
                map[curX][curY-1] = 4;
                map[curX][curY] = 0;
                break;

            case 1:
                Toast.makeText(getContext(),"左边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                break;

            case 2:
                map[curX][curY-1] = 4;//人覆盖了红旗
                map[curX][curY] = 0;
                break;

            case 3:
                switch (map[curX][curY-2]){
                    case 0:
                        map[curX][curY-2] = 3;
                        map[curX][curY-1] = 4;
                        map[curX][curY] = 0;
                        break;
                    case 1:
                        Toast.makeText(getContext(),"左边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        map[curX][curY-2] = 3;//箱子覆盖了红旗
                        map[curX][curY-1] = 4;
                        map[curX][curY] = 0;
                        break;
                    case 3:
                        Toast.makeText(getContext(),"左边是两个箱子，无法移动",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                break;
        }
        for(int i = 0;i<MAX_SPACE;i++){
            for(int k = 0;k<MAX_SPACE;k++ ){
                if(map[i][k]==4){
                    curY=k;
                    curX=i;
                }
            }
        }
        postInvalidate();
        //addFootMap(map);
        ifPass();
        //Log.e("tag","当前小人的位置"+curX+curY);
    }
    //向下边移动
    public void moveRight(){
        addFootMap();
        int x = map[curX][curY];
        switch (map[curX][curY+1]){
            case 0:
                map[curX][curY+1] = 4;
                map[curX][curY] = 0;
                break;

            case 1:
                Toast.makeText(getContext(),"右边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                break;

            case 2:
                map[curX][curY+1] = 4;
                map[curX][curY] = 2;
                break;

            case 3:
                switch (map[curX][curY+2]){
                    case 0:
                        map[curX][curY+2] = 3;
                        map[curX][curY+1] = 4;
                        map[curX][curY] = 0;
                        break;
                    case 1:
                        Toast.makeText(getContext(),"右边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        map[curX][curY+2] = 3;
                        map[curX][curY+1] = 4;
                        map[curX][curY] = 0;
                        break;
                    case 3:
                        Toast.makeText(getContext(),"右边是墙壁，无法移动",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                break;
        }
        for(int i = 0;i<MAX_SPACE;i++){
            for(int k = 0;k<MAX_SPACE;k++ ){
                if(map[i][k]==4){
                    curY=k;
                    curX=i;
                }
            }
        }
        postInvalidate();
       // addFootMap(map);
        ifPass();
      //  Log.e("tag","当前小人的位置"+curX+curY);
    }


    public void ifPass(){
        int successNumner = 0;
        for(int i = 0;i<flagPosition.size();i++){
            if(map[flagPosition.get(i).x][flagPosition.get(i).y] == 3){
                successNumner++;
            }
        }
        if(successNumner==flagPosition.size()){
            success = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(R.drawable.icon);
            builder.setTitle(R.string.pass);
            builder.setMessage(R.string.ifContinue);
            builder.setPositiveButton(R.string.again, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPassListener.next();
                }
            });

            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   mPassListener.exit();
                }
            });
            builder.show();
        }

    }



    public void reSet(int[][]oldMap){
        footMap.clear();
       curX = 6;
       curY = 5;
        map = oldMap;
        footMap.add(map);
        postInvalidate();
        ifPass();
    }

}





