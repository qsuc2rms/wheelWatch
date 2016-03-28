package ops.dcg.lenovo.wheelwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * Created by lilh8 on 2016/3/25.
 */
public class WatchView extends View {

    private int width;
    private int height;
    private int circleRadius;
    private Paint mPaintLine;
    private Paint mPaintCircle;
    private Paint mPaintRound;
    private Paint mPaintRoundHour;
    private Paint mPaintRoundMinute;
    private Paint mPaintHour;
    private Paint mPaintMinute;
    private Paint mPaintSec;
    private Paint mPaintText;
    private Calendar mCalendar;
    public static final int NEED_INVALIDATE = 0X23;

    public WatchView(Context context) {
        super(context);
    }

    //每隔一秒，在handler中调用一次重新绘制方法
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case NEED_INVALIDATE:
                    mCalendar = Calendar.getInstance();
                    invalidate();//告诉UI主线程重新绘制
                    handler.sendEmptyMessageDelayed(NEED_INVALIDATE, 1000);
                    break;
                default:
                    break;
            }
        }
    };

    public WatchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCalendar = Calendar.getInstance();

        mPaintLine = new Paint();
        mPaintLine.setColor(Color.BLUE);
        mPaintLine.setStrokeWidth(10);

        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.DKGRAY);//设置颜色
        mPaintCircle.setStrokeWidth(10);//设置线宽
        mPaintCircle.setAntiAlias(true);//设置是否抗锯齿
        mPaintCircle.setStyle(Paint.Style.STROKE);//设置绘制风格

        //指针的固定螺丝
        mPaintRound = new Paint();
        mPaintRound.setColor(Color.BLACK);//设置颜色
        mPaintRound.setStrokeWidth(3);//设置线宽
        mPaintRound.setAntiAlias(true);//设置是否抗锯齿
        mPaintRound.setStyle(Paint.Style.FILL);//设置绘制风格

        //时针所在的圆盘，默认透明
        mPaintRoundHour = new Paint();
        mPaintRoundHour.setColor(Color.TRANSPARENT);//设置颜色
        mPaintRoundHour.setStrokeWidth(1);//设置线宽
        mPaintRoundHour.setAntiAlias(true);//设置是否抗锯齿
        mPaintRoundHour.setStyle(Paint.Style.FILL);//设置绘制风格

        //分针所在的圆盘，默认透明
        mPaintRoundMinute = new Paint();
        mPaintRoundMinute.setColor(Color.TRANSPARENT);//设置颜色
        mPaintRoundMinute.setStrokeWidth(1);//设置线宽
        mPaintRoundMinute.setAntiAlias(true);//设置是否抗锯齿
        mPaintRoundMinute.setStyle(Paint.Style.FILL);//设置绘制风格

        mPaintText = new Paint();
        mPaintText.setColor(Color.DKGRAY);
        mPaintText.setStrokeWidth(10);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(40);

        mPaintHour = new Paint();
        mPaintHour.setStrokeWidth(15);
        mPaintCircle.setAntiAlias(true);//设置是否抗锯齿
        mPaintHour.setColor(Color.BLACK);

        mPaintMinute = new Paint();
        mPaintMinute.setStrokeWidth(12);
        mPaintCircle.setAntiAlias(true);//设置是否抗锯齿
        mPaintMinute.setColor(Color.DKGRAY);

        mPaintSec = new Paint();
        mPaintSec.setStrokeWidth(10);
        mPaintCircle.setAntiAlias(true);//设置是否抗锯齿
        mPaintSec.setColor(Color.GRAY);

        handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        height = width = Math.min(width, height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        circleRadius = Math.min(width, height) / 2 - 20;//200;
        //画出大圆
        canvas.drawCircle(width / 2, height / 2, circleRadius, mPaintCircle);

        //依次旋转画布，画出每个刻度和对应数字
        for (int i = 1; i <= 12; i++) {
            canvas.save();//保存当前画布
            canvas.rotate(360 / 12 * i, width / 2, height / 2);
            //左起：起始位置x坐标，起始位置y坐标，终止位置x坐标，终止位置y坐标，画笔(一个Paint对象)
            canvas.drawLine(width / 2, height / 2 - circleRadius, width / 2, height / 2 - circleRadius + 30, mPaintCircle);
            //左起：文本内容，起始位置x坐标，起始位置y坐标，画笔
            canvas.drawText("" + i, width / 2, height / 2 - circleRadius + 70, mPaintText);
            canvas.restore();//
        }

        int minute = mCalendar.get(Calendar.MINUTE);//得到当前分钟数
        int hour = mCalendar.get(Calendar.HOUR);//得到当前小时数
        int sec = mCalendar.get(Calendar.SECOND);//得到当前秒数

        float secDegree = sec / 60f * 360;//得到秒针旋转的角度
        canvas.save();
        canvas.rotate(secDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius + 30, width / 2, height / 2 + 40, mPaintSec);
        canvas.restore();

        //画出分针圆
        canvas.drawCircle(width / 2, height / 2, circleRadius * 2 / 3, mPaintRoundMinute);

        float minuteDegree = minute / 60f * 360;//得到分针旋转的角度
        canvas.save();
        canvas.rotate(minuteDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius * 2 / 3, width / 2, height / 2 + 35, mPaintMinute);
        canvas.restore();

        //画出时针圆
        canvas.drawCircle(width / 2, height / 2, circleRadius * 1 / 2, mPaintRoundHour);

        float hourDegree = (hour * 60 + minute) / 12f / 60 * 360;//得到时钟旋转的角度
        canvas.save();
        canvas.rotate(hourDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius * 1 / 2, width / 2, height / 2 + 30, mPaintHour);
        canvas.restore();

        //画出圆中心
        canvas.drawCircle(width / 2, height / 2, 15, mPaintRound);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //判断选中的是哪个指针
//                Log.e("test", "" + ev.getX() + ":" + ev.getY());
//                Log.e("test", "" + width / 2 + ":" + height / 2);

                double distance = Math.sqrt((ev.getX() - width / 2) * (ev.getX() - width / 2) + (ev.getY() - height / 2) * (ev.getY() - height / 2));
                if (distance > circleRadius) {
                    return super.onTouchEvent(ev);
                } else if (distance > circleRadius * 1 / 2) {
                    mPaintMinute.setStrokeWidth(20);
                    mPaintRoundMinute.setColor(Color.LTGRAY);
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                } else {
                    mPaintHour.setStrokeWidth(20);
                    mPaintRoundHour.setColor(Color.GRAY);
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                }

                return true;
            case MotionEvent.ACTION_MOVE:

                return true;
            case MotionEvent.ACTION_UP:
                mPaintHour.setStrokeWidth(15);
                mPaintRoundHour.setColor(Color.TRANSPARENT);
                mPaintMinute.setStrokeWidth(12);
                mPaintRoundMinute.setColor(Color.TRANSPARENT);
                handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
