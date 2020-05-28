package m.example.wakeapp2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmService extends Service {

    private Integer alarmHour;
    private Integer alarmMinute;
    private Ringtone ringtone;
    private Timer t = new Timer();
    int notiStatus;
    static final String CHANNEL_ID = "Budzik_alarm";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        alarmHour = intent.getIntExtra("alarmHour", 0);
        alarmMinute = intent.getIntExtra("alarmMinute", 0);

        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));



        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Calendar.getInstance().getTime().getHours() == alarmHour &&
                        Calendar.getInstance().getTime().getMinutes() == alarmMinute){
                    ringtone.play();
                    if (notiStatus == 0) {
                        makeNotification();
                        notiStatus = 1;
                    }
                }
                else {
                    ringtone.stop();
                }

            }
        }, 0, 2000);

        return super.onStartCommand(intent, flags, startId);
    }

    public void makeNotification(){
        try {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Notyfikacja High", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notyfikacje podczas alarmu");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

            Intent notificationIntent = new Intent(this, AlarmON.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID )
                    .setContentTitle("POBUDKA")
                    .setContentText("Czas wstawać. Jest godzina " + alarmHour.toString() + " : " + alarmMinute.toString())
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_access_alarm_white_24dp, "Wyłącz", pendingIntent)
                    .setSmallIcon(R.drawable.ic_access_alarm_white_24dp)
                    .build();

            startForeground(111333, notification);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        notiStatus = 0;
        ringtone.stop();
        t.cancel();
        super.onDestroy();
    }
}
