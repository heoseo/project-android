package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button_1 = findViewById(R.id.button_1);
        final Button button_2 = findViewById(R.id.button_2);
        final Button button_3 = findViewById(R.id.button_3);

        final Button btnDelete_B = findViewById(R.id.btnDelete_1);
        final Button btnDelete_L = findViewById(R.id.btnDelete_2);
        final Button btnDelete_D = findViewById(R.id.btnDelete_3);

        final TimePicker picker_1=findViewById(R.id.timePicker_1);
        final TimePicker picker_2=findViewById(R.id.timePicker_2);
        final TimePicker picker_3=findViewById(R.id.timePicker_3);



        final Switch switch1 = findViewById(R.id.switch1);


        final SharedPreferences preferences_switch = getSharedPreferences("switch", MODE_PRIVATE);

        final boolean value = preferences_switch.getBoolean("KEY", false);

        switch1.setChecked(value);

        if(switch1.isChecked())
        {
            switch1.setChecked(false);
            switch1.setChecked(true);

            startAlarmApp(picker_1, picker_2, picker_3, button_1, button_2, button_3, btnDelete_B, btnDelete_L, btnDelete_D);

        }
        else{
            setEnabled(picker_1, picker_2, picker_3, button_1, button_2, button_3, btnDelete_B, btnDelete_L, btnDelete_D, false);
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                SharedPreferences.Editor editor = preferences_switch.edit();
                editor.putBoolean("KEY", switch1.isChecked());
                editor.commit();


                if (switch1.isChecked()){
//                    switch1.setChecked(true);

                    switch1.setText("알림 설정");
                    Toast.makeText(getApplicationContext(), "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show();

                    // 다 활성화
                    setEnabled(picker_1, picker_2, picker_3, button_1, button_2, button_3, btnDelete_B, btnDelete_L, btnDelete_D, true);

                }
                else{
//                    switch1.setChecked(false);

                    switch1.setText("알림 해제");
                    Toast.makeText(getApplicationContext(), "알림이 해제되었습니다.", Toast.LENGTH_SHORT).show();

                    cancelAlarm(1);
                    cancelAlarm(2);
                    cancelAlarm(3);




                    // 다 비활성화
                    setEnabled(picker_1, picker_2, picker_3, button_1, button_2, button_3, btnDelete_B, btnDelete_L, btnDelete_D, false);
                }
            }
        });

    }

    void setEnabled(final TimePicker picker_1, final TimePicker picker_2, final TimePicker picker_3, Button button_1, Button button_2, Button button_3, Button btnDelete_B, Button btnDelete_L, Button btnDelete_D, Boolean flag){

        if(!flag){
            picker_1.setHour(9);
            picker_1.setMinute(0);
            picker_2.setHour(12);
            picker_2.setMinute(0);
            picker_3.setHour(18);
            picker_3.setMinute(0);
        }


        button_1.setEnabled(flag);
        button_2.setEnabled(flag);
        button_3.setEnabled(flag);
        btnDelete_B.setEnabled(flag);
        btnDelete_L.setEnabled(flag);
        btnDelete_D.setEnabled(flag);
        picker_1.setEnabled(flag);
        picker_2.setEnabled(flag);
        picker_3.setEnabled(flag);
    }



    //처음 앱 켰을때 바로 동작하도록. 스위치 on해야만 동작해서 함수로 사용.
    void startAlarmApp(final TimePicker picker_1, final TimePicker picker_2, final TimePicker picker_3, Button button_1, Button button_2, Button button_3, Button btnDelete_B, Button btnDelete_L, Button btnDelete_D){

        //Breakfast "B"     Lunch "L"       Dinner "D"
        SharedPreferences sharedPreferences_B = getSharedPreferences("daily_alarm_B", MODE_PRIVATE);
        SharedPreferences sharedPreferences_L = getSharedPreferences("daily_alarm_L", MODE_PRIVATE);
        SharedPreferences sharedPreferences_D = getSharedPreferences("daily_alarm_D", MODE_PRIVATE);

        long millis_B = sharedPreferences_B.getLong("nextNotifyTime_B", Calendar.getInstance().getTimeInMillis());
        long millis_L = sharedPreferences_L.getLong("nextNotifyTime_L", Calendar.getInstance().getTimeInMillis());
        long millis_D = sharedPreferences_D.getLong("nextNotifyTime_D", Calendar.getInstance().getTimeInMillis());

        Calendar nextNotifyTime_B = new GregorianCalendar();
        Calendar nextNotifyTime_L = new GregorianCalendar();
        Calendar nextNotifyTime_D = new GregorianCalendar();

        nextNotifyTime_B.setTimeInMillis(millis_B);
        nextNotifyTime_L.setTimeInMillis(millis_L);
        nextNotifyTime_D.setTimeInMillis(millis_D);

        // 이전 설정값으로 TimePicker 초기화
        Date nextDate_B = nextNotifyTime_B.getTime();
        Date nextDate_L = nextNotifyTime_L.getTime();
        Date nextDate_D = nextNotifyTime_D.getTime();



        Date currentTime_B = nextNotifyTime_B.getTime();
        Date currentTime_L = nextNotifyTime_L.getTime();
        Date currentTime_D = nextNotifyTime_D.getTime();

        SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());

        int pre_hour1 = Integer.parseInt(HourFormat.format(currentTime_B));
        int pre_minute1 = Integer.parseInt(MinuteFormat.format(currentTime_B));
        int pre_hour2 = Integer.parseInt(HourFormat.format(currentTime_L));
        int pre_minute2 = Integer.parseInt(MinuteFormat.format(currentTime_L));
        int pre_hour3 = Integer.parseInt(HourFormat.format(currentTime_D));
        int pre_minute3 = Integer.parseInt(MinuteFormat.format(currentTime_D));


        if (Build.VERSION.SDK_INT >= 23 ){
            picker_1.setHour(pre_hour1);
            picker_1.setMinute(pre_minute1);
            picker_2.setHour(pre_hour2);
            picker_2.setMinute(pre_minute2);
            picker_3.setHour(pre_hour3);
            picker_3.setMinute(pre_minute3);
        }
        else{
            picker_1.setCurrentHour(pre_hour1);
            picker_1.setCurrentMinute(pre_minute1);
            picker_2.setCurrentHour(pre_hour2);
            picker_2.setCurrentMinute(pre_minute2);
            picker_3.setCurrentHour(pre_hour3);
            picker_3.setCurrentMinute(pre_minute3);
        }


        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setAlarm(picker_1, "daily_alarm_B", "nextNotifyTime_B", "1", 1);
            }
        });


        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setAlarm(picker_2, "daily_alarm_L", "nextNotifyTime_L", "2", 2);
            }

        });

        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setAlarm(picker_3, "daily_alarm_D", "nextNotifyTime_D", "3", 3);
            }

        });

        btnDelete_B.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0){
                boolean isAlarmEmpty = cancelAlarm(1);      // 설정된 알람이 없으면 true 있으면 false

                if(isAlarmEmpty == false)
                    Toast.makeText(getApplicationContext(), "아침 알림이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "등록된 아침 알림이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete_L.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0){
                boolean isAlarmEmpty = cancelAlarm(2);      // 설정된 알람이 없으면 true 있으면 false

                if(isAlarmEmpty == false)
                    Toast.makeText(getApplicationContext(), "점심 알림이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "등록된 점심 알림이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        btnDelete_D.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0){
                boolean isAlarmEmpty = cancelAlarm(3);      // 설정된 알람이 없으면 true 있으면 false

                if(isAlarmEmpty == false)
                    Toast.makeText(getApplicationContext(), "저녁 알림이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "등록된 저녁 알림이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void setAlarm(TimePicker picker, String Daily_alarm_BLD, String NextNotifyTime_BLD, String BLD_123, int BLD){    //BLD_123 : bld를 123으로 표시.
        TimePicker timePicker  = picker;
        String daily_alarm_bld = Daily_alarm_BLD;
        String nextNotifytTime_bld = NextNotifyTime_BLD;
        String bld_123 = BLD_123;
        int bld = BLD;

        int hour, hour_24, minute;
        String am_pm;

        String name_bld = null;
        switch(bld){
            case 1:
                name_bld = "아침";
                break;
            case 2:
                name_bld = "점심";
                break;
            case 3:
                name_bld = "저녁";
                break;
        }

        if (Build.VERSION.SDK_INT >= 23 ){
            hour_24 = picker.getHour();
            minute = picker.getMinute();
        }
        else{
            hour_24 = picker.getCurrentHour();
            minute = picker.getCurrentMinute();
        }
        if(hour_24 > 12) {
            am_pm = "PM";
            hour = hour_24 - 12;
        }
        else
        {
            hour = hour_24;
            am_pm="AM";
        }

        // 현재 지정된 시간으로 알람 시간 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour_24);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Date currentDateTime = calendar.getTime();
        String date_text = new SimpleDateFormat("a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);


        Toast.makeText(getApplicationContext(),date_text + "으로 " + name_bld + " 알람이 설정되었습니다", Toast.LENGTH_SHORT).show();

        //  Preference에 설정한 값 저장
        SharedPreferences.Editor editor = getSharedPreferences(daily_alarm_bld, MODE_PRIVATE).edit();
        editor.putLong(nextNotifytTime_bld, (long)calendar.getTimeInMillis());
        editor.apply();


        diaryNotification(calendar, bld_123);


    }

    boolean cancelAlarm(int bld){
        int BLD = bld;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, BLD, intent, PendingIntent.FLAG_UPDATE_CURRENT);



        PendingIntent sender = PendingIntent.getBroadcast(this, BLD, intent, PendingIntent.FLAG_NO_CREATE);

        if(sender==null)        // 설정된 알람이 없는경우
            return true;
        else{                   // 설정된 알람이 있으면 false 반환하고 알람 취소.
            alarmManager.cancel(pendingIntent);
            return false;
        }
    }


    void diaryNotification(Calendar calendar, String bld)
    {
        Boolean dailyNotify = true; // 무조건 알람을 사용
        String BLD = bld;

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("state","alarm on");
        alarmIntent.putExtra("BLD", BLD);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(BLD), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {


            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }
    }
}
