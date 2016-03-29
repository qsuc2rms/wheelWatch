package ops.dcg.lenovo.wheelwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
    private float degreeMinuteTarget;
    private float degreeHourTarget;
    private Paint mPaintLine;
    private Paint mPaintCircle;
    private Paint mPaintRound;
    private Paint mPaintRoundHour;
    private Paint mPaintRoundMinute;
    private Paint mPaintHour;
    private Paint mPaintHourTarget;
    private Paint mPaintMinute;
    private Paint mPaintMinuteTarget;
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
        mPaintHour.setAntiAlias(true);//设置是否抗锯齿
        mPaintHour.setColor(Color.BLACK);

        mPaintHourTarget = new Paint();
        mPaintHourTarget.setStrokeWidth(3);
        mPaintHourTarget.setAntiAlias(true);//设置是否抗锯齿
        mPaintHourTarget.setColor(Color.TRANSPARENT);
        mPaintHourTarget.setStrokeWidth(3);//设置线宽
        mPaintHourTarget.setStyle(Paint.Style.FILL);//设置绘制风格

        mPaintMinute = new Paint();
        mPaintMinute.setStrokeWidth(12);
        mPaintMinute.setAntiAlias(true);//设置是否抗锯齿
        mPaintMinute.setColor(Color.DKGRAY);

        mPaintMinuteTarget = new Paint();
        mPaintMinuteTarget.setStrokeWidth(3);
        mPaintMinuteTarget.setAntiAlias(true);//设置是否抗锯齿
        mPaintMinuteTarget.setColor(Color.TRANSPARENT);
        mPaintMinuteTarget.setStrokeWidth(3);//设置线宽
        mPaintMinuteTarget.setStyle(Paint.Style.FILL);//设置绘制风格

        mPaintSec = new Paint();
        mPaintSec.setStrokeWidth(8);
        mPaintSec.setAntiAlias(true);//设置是否抗锯齿
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

        //画出分针圆
        canvas.drawCircle(width / 2, height / 2, circleRadius * 2 / 3, mPaintRoundMinute);

        float minuteDegree = minute / 60f * 360;//得到分针旋转的角度

        //画定时分针的圆弧
        RectF rectMinute = new RectF(width / 2 - circleRadius * 2 / 3, height / 2 - circleRadius * 2 / 3, width / 2
                + circleRadius * 2 / 3, height / 2 + circleRadius * 2 / 3);
        if (degreeMinuteTarget >= minuteDegree) {
            canvas.drawArc(rectMinute, minuteDegree - 90, degreeMinuteTarget - minuteDegree, true, mPaintMinuteTarget);
        } else {
            canvas.drawArc(rectMinute, minuteDegree - 90, 360 - minuteDegree, true, mPaintMinuteTarget);
            canvas.drawArc(rectMinute, 0 - 90, degreeMinuteTarget - 0, true, mPaintMinuteTarget);
        }

        canvas.save();
        canvas.rotate(minuteDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius * 2 / 3, width / 2, height / 2 + 35, mPaintMinute);
        canvas.restore();

        //画出定时分针
        canvas.save();
        canvas.rotate(degreeMinuteTarget, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius * 2 / 3, width / 2, height / 2 , mPaintMinuteTarget);
        canvas.restore();

        //画出时针圆
        canvas.drawCircle(width / 2, height / 2, circleRadius * 1 / 2, mPaintRoundHour);

        float hourDegree = (hour * 60 + minute) / 12f / 60 * 360;//得到时钟旋转的角度

        //画定时时针的圆弧
        RectF rectHour = new RectF(width / 2 - circleRadius * 1 / 2, height / 2 - circleRadius * 1 / 2, width / 2
                + circleRadius * 1 / 2, height / 2 + circleRadius * 1 / 2);
        if (degreeHourTarget >= hourDegree) {
            canvas.drawArc(rectHour, hourDegree - 90, degreeHourTarget - hourDegree, true, mPaintHourTarget);
        } else {
            canvas.drawArc(rectHour, hourDegree - 90, 360 - hourDegree, true, mPaintHourTarget);
            canvas.drawArc(rectHour, 0 - 90, degreeHourTarget - 0, true, mPaintHourTarget);
        }

        canvas.save();
        canvas.rotate(hourDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius * 1 / 2, width / 2, height / 2 + 30, mPaintHour);
        canvas.restore();

        //画出定时时针
        canvas.save();
        canvas.rotate(degreeHourTarget, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius * 1 / 2, width / 2, height / 2 , mPaintHourTarget);
        canvas.restore();

        //秒针在最上面
        float secDegree = sec / 60f * 360;//得到秒针旋转的角度
        canvas.save();
        canvas.rotate(secDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius + 30, width / 2, height / 2 + 40, mPaintSec);
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

                double distance = Math.sqrt((ev.getX() - width / 2) * (ev.getX() - width / 2) + (ev.getY() - height / 2) * (ev.getY() - height / 2));
                if (distance > circleRadius) {
                    return super.onTouchEvent(ev);
                } else if (distance > circleRadius * 1 / 2) {
                    mPaintMinute.setStrokeWidth(20);
                    mPaintRoundMinute.setColor(Color.LTGRAY);
                    degreeMinuteTarget = getDegree(ev.getX(), ev.getY());
                    mPaintMinuteTarget.setColor(Color.CYAN);
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                } else {
                    mPaintHour.setStrokeWidth(20);
                    mPaintRoundHour.setColor(Color.GRAY);
                    degreeHourTarget = getDegree(ev.getX(), ev.getY());
                    mPaintHourTarget.setColor(Color.RED);
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                }

                return true;
            case MotionEvent.ACTION_MOVE:

                if (mPaintHour.getStrokeWidth() >= 20) {
                    degreeHourTarget = getDegree(ev.getX(), ev.getY());
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                } else if (mPaintMinute.getStrokeWidth() >= 20) {
                    degreeMinuteTarget = getDegree(ev.getX(), ev.getY());
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                }

                return true;
            case MotionEvent.ACTION_UP:
                mPaintHour.setStrokeWidth(15);
                mPaintRoundHour.setColor(Color.TRANSPARENT);
                mPaintMinute.setStrokeWidth(12);
                mPaintRoundMinute.setColor(Color.TRANSPARENT);
                handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘

                //向调用者传递已选好的时间
                if (listener != null) {
                    listener.onTimerSetUp(getTimerTime());
                }
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private float getDegree(float x, float y) {
        float x0 = width / 2, y0 = height / 2;
        float disx = Math.abs(x0 - x);
        float disy = Math.abs(y0 - y);
        double z = Math.sqrt(disx * disx + disy * disy);

        float degree = Math.round(Math.asin(disx / z) / Math.PI * 180);
        if (y0 >= y && x0 <= x) {
            //default
        } else if (y0 <= y && x0 <= x) {
            degree = 180 - Math.round(Math.asin(disx / z) / Math.PI * 180);
        } else if (y0 <= y && x0 >= x) {
            degree = 180 + Math.round(Math.asin(disx / z) / Math.PI * 180);
        } else {
            degree = 360 - Math.round(Math.asin(disx / z) / Math.PI * 180);
        }

        return degree;
    }

    /*
    *获取定时的分钟
     */
    public int getTimerMinute() {
        int minute = (int) (degreeMinuteTarget * 60 / 360);
        return minute;
    }

    /*
    *获取定时的小时
     */
    public int getTimerHour() {
        int hour = (int) (degreeHourTarget * 12 / 360);
        return hour;
    }

    /*
    *获取定时的时间
     */
    public String getTimerTime() {

        String strH = "" + getTimerHour();
        if (strH.length() < 2) {
            strH = "0" + strH;
        }
        String strM = "" + getTimerMinute();
        if (strM.length() < 2) {
            strM = "0" + strM;
        }
        return strH + ":" + strM + ":" + "00";
    }

    public interface OnTimerSetUpListener {
        public void onTimerSetUp(String time);
    }

    private OnTimerSetUpListener listener;
    public  void setOnTimerSetUpListener(OnTimerSetUpListener outerlistener){
        listener=outerlistener;
    }
}
