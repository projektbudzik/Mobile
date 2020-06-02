package m.example.wakeapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.Alarm.Model.AlarmActivity;
import m.example.wakeapp2.Device.Model.DeviceActivity;
import m.example.wakeapp2.User.Model.UserActivity;
import m.example.wakeapp2.user_log_reg.Login2Activity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    public static final String Name = "nameKey";
    public static final String GroupName = "groupNameKey";
    public static final String phoneNumber = "numberKey";
    private String  callbackMsg1;
    TextView tv_title, tv_subtitle;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences("MyPref", 0);
        Button btn_konto = findViewById(R.id.btn_konto);
        Button btn_alarm_mgmt = findViewById(R.id.btn_alarm_mgmt);
        Button btn_close = findViewById(R.id.btn_close);
        Button btn_divice_mgmt = findViewById(R.id.btn_divice_mgmt);
        Button btn_user_mgmt = findViewById(R.id.btn_user_mgmt);
        Button btn_logout = findViewById(R.id.btn_logout);
        tv_subtitle = findViewById(R.id.tv_subtitle);
        tv_title = findViewById(R.id.tv_title);
        intent = new Intent(this, AlarmService.class);

        if (sharedpreferences.contains(Name)) {
            String Title = "Witaj, " + sharedpreferences.getString(Name, "");
            tv_title.setText(Title);
        }

        btn_konto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreferencesActivity();
            }
        });

        btn_user_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserAct();
                stopService(intent);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        btn_alarm_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlarm();
            }
        });

        btn_divice_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDevice();
            }
        });

//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.clear();
//                editor.apply();
//                stopServices(intent);
//                finish();
//                Intent intent = new Intent(v.getContext(), Login2Activity.class);
//                startActivity(intent);
//            }
//        });

    }

    private void ServiceCaller(Intent intent,String aDate){

        stopServices(intent);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(c);

        if (aDate.equals(today)){
        startService(intent);
        }
    }

    private void stopServices(Intent intent){
        stopService(intent);
    }

    public  void getAlarm(Intent intent){
        stopServices(intent);
        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
        JSONreader jsoNreader = new JSONreader();
        String phoneN = sharedpreferences.getString(phoneNumber, "");

        try {
            callbackMsg1 = backgroundTask.execute("getAlarm", phoneN ).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        String alarmDate = jsoNreader.readJSONdata(callbackMsg1, "Closed_date");
        String alarmTime = jsoNreader.readJSONdata(callbackMsg1, "Time");

        ServiceCaller(intent, alarmDate);
        String currentAlarm = "Alarm: " + alarmDate +", " + alarmTime;
        tv_subtitle.setText(currentAlarm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAlarm(intent);

    }

    public void openAlarm(){
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    public void openDevice(){
        Intent intent = new Intent(this, DeviceActivity.class);
        startActivity(intent);
    }

    public void openUserAct(){
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
    private void startPreferencesActivity() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}