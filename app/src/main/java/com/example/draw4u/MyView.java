package com.example.draw4u;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    private Paint paint = new Paint();


    //여러가지의 그리기 명령을 모았다가 한번에 출력해주는
    //버퍼역할을 담당한다..
    private Path path = new Path();

    private int x,y;

    public MyView(Context context){

        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setColor(Color.BLACK);

        //STROKE속성을 이용하여 테두리...선...
        paint.setStyle(Paint.Style.STROKE);

        //두께
        paint.setStrokeWidth(3);


        //path객체가 가지고 있는 경로를 화면에 그린다...
        canvas.drawPath(path,paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = (int)event.getX();
        y = (int)event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int)event.getX();
                y = (int)event.getY();

                path.lineTo(x,y);
                break;
        }

        //View의 onDraw()를 호출하는 메소드...
        invalidate();

        return true;
    }
}