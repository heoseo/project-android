package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            // on device boot complete, reset the alarm
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//

            SharedPreferences sharedPreferences_1 = context.getSharedPreferences("daily alarm_B", MODE_PRIVATE);
            SharedPreferences sharedPreferences_2 = context.getSharedPreferences("daily alarm_L", MODE_PRIVATE);
            SharedPreferences sharedPreferences_3 = context.getSharedPreferences("daily alarm_D", MODE_PRIVATE);
            long millis_1 = sharedPreferences_1.getLong("nextNotifyTime_B", Calendar.getInstance().getTimeInMillis());
            long millis_2 = sharedPreferences_2.getLong("nextNotifyTime_L", Calendar.getInstance().getTimeInMillis());
            long millis_3 = sharedPreferences_3.getLong("nextNotifyTime_D", Calendar.getInstance().getTimeInMillis());


            Calendar current_calendar = Calendar.getInstance();
            Calendar nextNotifyTime = new GregorianCalendar();
            nextNotifyTime.setTimeInMillis(sharedPreferences_1.getLong("nextNotifyTime_B", millis_1));
            nextNotifyTime.setTimeInMillis(sharedPreferences_2.getLong("nextNotifyTime_L", millis_2));
            nextNotifyTime.setTimeInMillis(sharedPreferences_3.getLong("nextNotifyTime_D", millis_3));

            if (current_calendar.after(nextNotifyTime)) {
                nextNotifyTime.add(Calendar.DATE, 1);
            }

            Date currentDateTime = nextNotifyTime.getTime();
            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
            Toast.makeText(context.getApplicationContext(),"[재부팅후] 다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();


            if (manager != null) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }
}
