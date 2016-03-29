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
    private Paint mPaintHour;
    private Paint mPaintHourTarget;
    private Paint mPaintHourArc;
    private Paint mPaintMinute;
    private Paint mPaintMinuteRing;
    private Paint mPaintMinuteTriangle;
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

        mPaintHourArc = new Paint();
        mPaintHourArc.setAntiAlias(true);//设置是否抗锯齿
        mPaintHourArc.setColor(Color.TRANSPARENT);
        mPaintHourArc.setStrokeWidth(3);//设置线宽
        mPaintHourArc.setStyle(Paint.Style.FILL);//设置绘制风格

        mPaintMinute = new Paint();
        mPaintMinute.setStrokeWidth(12);
        mPaintMinute.setAntiAlias(true);//设置是否抗锯齿
        mPaintMinute.setColor(Color.DKGRAY);

        mPaintMinuteRing = new Paint();
        mPaintMinuteRing.setAntiAlias(true);//设置是否抗锯齿
        mPaintMinuteRing.setColor(Color.TRANSPARENT);
        mPaintMinuteRing.setStrokeWidth(20);//设置线宽
        mPaintMinuteRing.setStyle(Paint.Style.STROKE);//设置绘制风格

        mPaintMinuteTriangle = new Paint();
        mPaintMinuteTriangle.setAntiAlias(true);//设置是否抗锯齿
        mPaintMinuteTriangle.setColor(Color.TRANSPARENT);
        mPaintMinuteTriangle.setStrokeWidth(3);//设置线宽
        mPaintMinuteTriangle.setStyle(Paint.Style.FILL);//设置绘制风格

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

        float minuteDegree = minute / 60f * 360;//得到分针旋转的角度
        float hourDegree = (hour * 60 + minute) / 12f / 60 * 360;//得到时钟旋转的角度

        float minuteNeedleLength=circleRadius * 2 / 3;
        float hourNeedleLength=circleRadius * 1 / 2;

        //画定时分针的圆环和三角
        mPaintMinuteRing.setStrokeWidth(minuteNeedleLength-hourNeedleLength);
        canvas.drawCircle(width / 2, height / 2, circleRadius * 1 / 2 + mPaintMinuteRing.getStrokeWidth() / 2+3, mPaintMinuteRing);

        canvas.save();//保存当前画布
        canvas.rotate(360 / 60 * getTimerMinute(), width / 2, height / 2);
        //左起：起始位置x坐标，起始位置y坐标，终止位置x坐标，终止位置y坐标，画笔(一个Paint对象)
        canvas.drawLine(width / 2, height/2 -hourNeedleLength-( mPaintMinuteRing.getStrokeWidth() / 2+3), width / 2, height/2-hourNeedleLength - (mPaintMinuteRing.getStrokeWidth()), mPaintMinuteTriangle);
        canvas.restore();

        //画定时时针的圆弧
        RectF rectHour = new RectF(width / 2 - hourNeedleLength, height / 2 - hourNeedleLength, width / 2
                + hourNeedleLength, height / 2 + hourNeedleLength);
        if (degreeHourTarget >= hourDegree) {
            canvas.drawArc(rectHour, hourDegree - 90, degreeHourTarget - hourDegree, true, mPaintHourArc);
        } else {
            canvas.drawArc(rectHour, hourDegree - 90, 360 - hourDegree, true, mPaintHourArc);
            canvas.drawArc(rectHour, 0 - 90, degreeHourTarget - 0, true, mPaintHourArc);
        }

        //画出分针
        canvas.save();
        canvas.rotate(minuteDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - minuteNeedleLength, width / 2, height / 2 + 35, mPaintMinute);
        canvas.restore();

        //画出时针
        canvas.save();
        canvas.rotate(hourDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - hourNeedleLength, width / 2, height / 2 + 30, mPaintHour);
        canvas.restore();

        //画出定时时针
        canvas.save();
        canvas.rotate(degreeHourTarget, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - hourNeedleLength, width / 2, height / 2, mPaintHourTarget);
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
                    degreeMinuteTarget = getDegree(ev.getX(), ev.getY());
                    mPaintMinuteRing.setColor(Color.RED);
                    mPaintMinuteTriangle.setColor(Color.BLACK);
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                } else {
                    mPaintHour.setStrokeWidth(20);
                    degreeHourTarget = getDegree(ev.getX(), ev.getY());
                    mPaintHourArc.setColor(Color.RED);
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                }

                return true;
            case MotionEvent.ACTION_MOVE:

                if (mPaintHour.getStrokeWidth() >= 20) {
                    degreeHourTarget = getDegree(ev.getX(), ev.getY());
                    mPaintHourTarget.setColor(Color.BLACK);
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                } else if (mPaintMinute.getStrokeWidth() >= 20) {
                    degreeMinuteTarget = getDegree(ev.getX(), ev.getY());
                    updateDegreeHourTarget(degreeMinuteTarget);
                    mPaintMinuteRing.setColor(Color.RED);
                    mPaintMinuteTriangle.setColor(Color.BLACK);
                    handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
                }

                //向调用者传递选好的时间
                if (listener != null) {
                    listener.onTimerSetUp(getTimerTime());
                }

                return true;
            case MotionEvent.ACTION_UP:
                mPaintHour.setStrokeWidth(15);
                mPaintMinute.setStrokeWidth(12);
//                mPaintMinuteRing.setColor(Color.TRANSPARENT);
//                mPaintMinuteTriangle.setColor(Color.TRANSPARENT);
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

    private void updateDegreeHourTarget(float minuteDegree){
        int fromHour = getTimerHour();
        int toMinute=(int)(minuteDegree *60/360);
        degreeHourTarget=(fromHour * 60 + toMinute) / 12f / 60 * 360;
    }

    /*
    *获取定时的分钟
     */
    public int getTimerMinute() {
        int fromHour = getTimerHour();
        float fromDegree = fromHour * 360 / 12;
        int minute = (int) ((degreeHourTarget - fromDegree) * 60 / (360 / 12));
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

    public void setOnTimerSetUpListener(OnTimerSetUpListener outerlistener) {
        listener = outerlistener;
    }
}
