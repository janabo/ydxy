package com.dk.mp.schedule.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.schedule.R;
import com.dk.mp.schedule.utils.CalendarUtil;

import java.util.Calendar;

/**
 * 月视图
 * 作者：janabo on 2016/12/29 10:03
 */
public class MonthView extends View {
    @SuppressWarnings("unused")
    private static final String TAG = "MonthView";

    public interface OnCellSelectListener {
        /**
         * 当单元格被选中
         * @param monthView    当前视图
         * @param calendar     选中的日期
         */
        void onCellSelect(MonthView monthView, Calendar calendar);
    }

    private OnCellSelectListener onCellSelectListener;

    public void setOnCellSelectListener(OnCellSelectListener onCellSelectListener) {
        this.onCellSelectListener = onCellSelectListener;
        if(cellHeight != 0){
            callListener(0);
        }
    }

    private int width;
    private int height;
    private boolean isFirstDraw = true;

    private float cellWidth;
    private float cellHeight;
    // 保留每个单元格的左上角坐标
    private PointF[] cellOrigin;
    private PointF[] dateTextOffsetPoint;
//    private PointF[] lunarTextOffsetPoint;
//    private String[] cellLunarString;
    int selectCell = -1;
    private RectF tmp1;

    private GestureDetector gestureDetector;

    // Paint
    private Paint dateTextPaint;
    private Paint holidayTextPaint;
//    private Paint lunarTextPaint;
    private Paint selectCellBGPaint;
    private MonthViewPaint monthViewPaint;

    int deltaDay = 0;

    private Calendar currentFirstDayCalendar;
    private MonthViewSwitcher viewSwitcher;

    float xOffset;

    private static int touchSlop;

    /**
     * 设置月份
     * @param calendar
     */
    public void setCalendar(Calendar calendar){
        if(calendar == null) return;
        currentFirstDayCalendar = CalendarUtil.getFirstDayCalendarOfMonth(calendar);
 //       currentFirstDayCalendar = CalendarUtil.getNowDay();
        deltaDay = getDeltaDay(CalendarUtil.getFirstDayOfMonth(calendar));
 //       deltaDay = getDeltaDay(CalendarUtil.getNowWeek());
        cellOrigin = new PointF[CalendarUtil.getDayCount(calendar)];
        dateTextOffsetPoint = new PointF[cellOrigin.length];
//        lunarTextOffsetPoint = new PointF[cellOrigin.length];
//        cellLunarString = new String[cellOrigin.length];
        calculatePosition();
        callListener(0);
        invalidate();
    }

    void callListener(int position) {
        selectCell = position;
        Calendar resultCalendar = (Calendar) currentFirstDayCalendar.clone();
        resultCalendar.set(Calendar.DAY_OF_MONTH, selectCell + 1);
        if(onCellSelectListener != null){
            onCellSelectListener.onCellSelect(MonthView.this, resultCalendar);
        }
    }
    void callListener(){
        callListener(0);
    }

    private int getDeltaDay(int firstDay){
        return firstDay - 1;
    }

    float getCellHeight() {
        return cellHeight;
    }

    /**
     * 获取当前MonthView代表的月历第一天的Calendar
     */
    public Calendar getFirstDayCalendar(){
        return currentFirstDayCalendar;
    }

    public MonthView(Context context) {
        super(context);
        initPaint();
        gestureDetector = new GestureDetector(context, simpleOnGestureListener);
        tmp1 = new RectF();
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setCalendar(Calendar.getInstance());
        if(touchSlop <= 0){
            touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            touchSlop = Math.max(touchSlop, 16);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewSwitcher = (MonthViewSwitcher) getParent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isFirstDraw){
            width = getWidth();
            height = 600;
            isFirstDraw = false;
            Log.v(TAG, "width and is " + width + " height is " + height);
            calculatePosition();
            callListener(selectCell);
        }
        canvas.save();
        canvas.translate(xOffset, 0);
        canvas.save();
        originalDraw(canvas);
        canvas.restore();
        if(viewSwitcher.isCurrentView(this) && xOffset != 0){
            canvas.save();
            if(xOffset > 0){
                canvas.translate(-width, 0);
            }else if(xOffset < 0){
                canvas.translate(width, 0);
            }
            viewSwitcher.getNextView().originalDraw(canvas);
            canvas.restore();
        }
        canvas.restore();
    }
    //  draw calendar, no position change
    private void originalDraw(Canvas canvas) {
        for(int i = 0; i < cellOrigin.length; i++){
            PointF cell = cellOrigin[i];
            PointF cellOffset = dateTextOffsetPoint[i];
//            PointF lunarOffset = lunarTextOffsetPoint[i];
            if(i == selectCell){
                tmp1.set(cell.x+DeviceUtil.dip2px(getContext(),12), cell.y+DeviceUtil.dip2px(getContext(),12), cell.x+ cellWidth/2+DeviceUtil.dip2px(getContext(),15), cell.y + cellWidth/2+DeviceUtil.dip2px(getContext(),15));
                canvas.drawArc(tmp1, 360, 360, false, selectCellBGPaint);
            }
            canvas.drawText(String.valueOf(i + 1), cell.x + cellOffset.x, cell.y + cellOffset.y, dateTextPaint);
//            canvas.drawText(cellLunarString[i], cell.x + lunarOffset.x, cell.y + lunarOffset.y, lunarTextPaint);
        }
    }

    private float touchLastX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isBeingAnimation){
            return false;
        }
        int action = event.getAction();
        gestureDetector.onTouchEvent(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                touchLastX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                onMove(event);
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "onPoint up");
                if(Math.abs(xOffset) > touchSlop){
                    scrollLeft();
                    viewSwitcher.getNextView().scrollLeft();
                }
                return true;
        }
        return false;
    }
    private boolean isBeingAnimation = false;
    private void scrollLeft() {
        TranslateAnimation translateAnimation;
        if(viewSwitcher.isCurrentView(this)){
            if(xOffset > 0){
                viewSwitcher.getNextView().xOffset = xOffset - width;
                translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, xOffset, Animation.RELATIVE_TO_SELF, 1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            }else{
                viewSwitcher.getNextView().xOffset = width + xOffset;
                translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, xOffset, Animation.RELATIVE_TO_SELF, -1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            }
            translateAnimation.setDuration(300);
            outAnimationStart(translateAnimation);
        }else{
            if(xOffset > 0){
                translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, xOffset, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            }else{
                translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, xOffset, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            }
            translateAnimation.setDuration(300);
            inAnimationStart(translateAnimation);
        }

//        translateAnimation.setFillAfter(true);
    }

    void outAnimationStart(@NonNull Animation translateAnimation) {
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isBeingAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(TAG, "scrollLeft end");
                viewSwitcher.changeCurrentPosition();
                setVisibility(INVISIBLE);
                isBeingAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationStart(translateAnimation);
    }

    void inAnimationStart(@NonNull Animation translateAnimation) {
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(TAG, "onAnimationStart");
                setVisibility(VISIBLE);
                isBeingAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(TAG, "next monthView animation end");
                isBeingAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationStart(translateAnimation);
    }

    private void animationStart(@NonNull Animation translateAnimation) {
        clearAnimation();
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        startAnimation(translateAnimation);
        xOffset = 0;
    }

    // 滑动事件
    private void onMove(MotionEvent event) {
        float dx = event.getX() - touchLastX;
        xOffset += dx;
        touchLastX = event.getX();

        if(Math.abs(dx) > touchSlop){
            viewSwitcher.requestDisallowInterceptTouchEvent(true);
            if(xOffset > 0){
                viewSwitcher.lastMonth();
            }else {
                viewSwitcher.nextMonth();
            }
        }
        postInvalidate();
    }
    // 计算中的位置, 只计算一次
    private void calculatePosition(){
        if (width <= 0) return;
        cellWidth = width / 7f;
        cellHeight = height/CalendarUtil.getWeeksOfMonth(currentFirstDayCalendar);

        // 将单元格模仿成方形, 并默认cellWidth < cellHeight;
        float cellHeightMargin = (cellHeight - cellWidth)/2;
        Calendar calendar = (Calendar) currentFirstDayCalendar.clone();
        for(int i = 0; i < cellOrigin.length; i++){
//            LunarHelper helper = new LunarHelper(calendar);
//            String lunarDay = helper.getLunarDay();
//            if("初一".equals(lunarDay)){
//                lunarDay = helper.getLunarMonthString();
//            }
//            cellLunarString[i] =  lunarDay;
            calendar.roll(Calendar.DAY_OF_MONTH, 1);

            cellOrigin[i] = new PointF();
            dateTextOffsetPoint[i] = new PointF();
//            lunarTextOffsetPoint[i] = new PointF();
            // calculate the cell index
            int j = i + deltaDay;
            int columns = j % 7;
            int rows = j / 7;

            cellOrigin[i].set(columns * cellWidth, rows * cellHeight + cellHeightMargin);

            // 计算日期文字与农历文字的开始位置
            int dateTextHOffset = (int) ((cellWidth - dateTextPaint.measureText(String.valueOf(i + 1)))/2);
            int dateTextHeight = DeviceUtil.getTextHeight(dateTextPaint, "10");

//            int lunarTextHeight = DeviceUtil.getTextHeight(lunarTextPaint, cellLunarString[i]) + DeviceUtil.dip2px(MyApplication.getContext(),6);
//            int lunarTextHOffset = (int) ((cellWidth - lunarTextPaint.measureText(cellLunarString[i]))/2);

//            int topPadding = (int) ((cellWidth - dateTextHeight - lunarTextHeight)/2);
            int topPadding = (int) ((cellWidth - dateTextHeight)/2);
            int dateTextVOffset = topPadding + dateTextHeight;
//            int lunarTextVOffset = topPadding + dateTextHeight + lunarTextHeight;
            dateTextOffsetPoint[i].set(dateTextHOffset, dateTextVOffset);
//            lunarTextOffsetPoint[i].set(lunarTextHOffset, lunarTextVOffset);
        }
    }

    private void initPaint(){
        monthViewPaint = new MonthViewPaint();
        dateTextPaint = monthViewPaint.dateTextPaint;

        selectCellBGPaint = monthViewPaint.selectCellBGPaint;

//        lunarTextPaint = monthViewPaint.lunarTextPaint;
    }

    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.v(TAG, "onFling");
            float dx = e2.getX() - e1.getX();
            float dy = e2.getY() - e1.getY();
            if(Math.abs(dx) <= Math.abs(dy)) return false;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.v(TAG, "onSingleTapConfirmed()");

            float pointX = e.getX();
            float pointY = e.getY();
            int inColumns = (int) (pointX / cellWidth);
            int inRows = (int) (pointY/cellHeight);
            int index = inRows * 7 + inColumns - deltaDay;
            if(index < cellOrigin.length && index >= 0){
                callListener(index);
            }
            invalidate();
            return true;
        }
    };
}
