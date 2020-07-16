package com.example.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);

        String get_yout_string = intent.getExtras().getString("state");
        String str = intent.getExtras().getString(("BLD"));

//        int BLD = intent.getExtras().getInt("BLD");
        int BLD = Integer.parseInt(str);

        String name_bld = null;
        switch(BLD){
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


        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingI = PendingIntent.getActivity(context, BLD,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");


        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.mipmap.ic_launcher_1_round); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남


            String channelName ="매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        }else builder.setSmallIcon(R.mipmap.ic_launcher_1_round); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남


        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())

                .setTicker("{Time to watch some cool stuff!}")
                .setContentTitle("알림")
                .setContentText(name_bld + " 약 먹을 시간입니다.")
                .setContentInfo("INFO")
                .setContentIntent(pendingI);

        if (notificationManager != null) {

            // 노티피케이션 동작시킴
            notificationManager.notify(BLD, builder.build());


            Calendar nextNotifyTime = Calendar.getInstance();

            // 내일 같은 시간으로 알람시간 결정
            nextNotifyTime.add(Calendar.DATE, 1);

            //  Preference에 설정한 값 저장
            switch(BLD){
                case 1:
                    SharedPreferences.Editor editor_1 = context.getSharedPreferences("daily alarm_1", MODE_PRIVATE).edit();
                    editor_1.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
                    editor_1.apply();
                    break;
                case 2:
                    SharedPreferences.Editor editor_2 = context.getSharedPreferences("daily alarm_2", MODE_PRIVATE).edit();
                    editor_2.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
                    editor_2.apply();
                    break;
                case 3:
                    SharedPreferences.Editor editor_3 = context.getSharedPreferences("daily alarm_3", MODE_PRIVATE).edit();
                    editor_3.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
                    editor_3.apply();
                    break;
            }


            Date currentDateTime = nextNotifyTime.getTime();
            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
            Toast.makeText(context.getApplicationContext(),"다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
        }
    }
}
