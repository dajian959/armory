package cn.armory.explore.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.armory.common.base.mvp.VpBaseActivity;
import cn.armory.explore.R;
import cn.armory.explore.mvp.MainPresenter;
import cn.armory.explore.mvp.MainView;

public class MainActivity extends VpBaseActivity<MainPresenter> implements CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, MainView {
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.click)
    Button button;
    @BindView(R.id.ccc)
    CalendarLayout calendarLayout;
    @BindView(R.id.date)
    TextView date;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initLocalData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meizu;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        // mPresenter.getList();
        date.setText(getString(R.string.date, mCalendarView.getSelectedCalendar().getYear(), mCalendarView.getSelectedCalendar().getMonth()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarLayout.isExpand()) {
                    boolean a = calendarLayout.shrink();
                } else calendarLayout.expand();
                Intent intent = new Intent(MainActivity.this, VmMainActivity.class);
                startActivity(intent);
            }
        });
        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month - 1, 30, 0xFFdf1356, "公积金查询").toString(),
                getSchemeCalendar(year, month - 1, 30, 0xFFdf1356, "公积金查询"));
        map.put(getSchemeCalendar(year, month, 3, 0xFFdf1356, "高考查询").toString(),
                getSchemeCalendar(year, month, 3, 0xFFdf1356, "高考查询"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFdf1356, "查询").toString(),
                getSchemeCalendar(year, month, 6, 0xFFdf1356, "查询"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
        mCalendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                date.setText(getString(R.string.date, year, month));

            }
        });
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "高考查询");
        return calendar;
    }

    @Override
    public void onTextSuccess() {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {

    }

    @Override
    public void onYearChange(int year) {

    }
}