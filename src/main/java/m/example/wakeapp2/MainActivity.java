package m.example.wakeapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

import m.example.wakeapp2.Alarm.Model.AlarmActivity;
import m.example.wakeapp2.Device.Model.DeviceActivity;
import m.example.wakeapp2.User.Model.UserActivity;
import m.example.wakeapp2.user_log_reg.Login2Activity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    public static final String Name = "nameKey";
    public static final String phoneNumber = "numberKey";

    TextView tv_title, tv_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_alarm_mgmt = findViewById(R.id.btn_alarm_mgmt);
        Button btn_close = findViewById(R.id.btn_close);
        Button btn_divice_mgmt = findViewById(R.id.btn_divice_mgmt);
        Button btn_user_mgmt = findViewById(R.id.btn_user_mgmt);
        Button btn_logout = findViewById(R.id.btn_logout);
        tv_subtitle = findViewById(R.id.tv_subtitle);
        tv_title = findViewById(R.id.tv_title);

        tv_subtitle.setText(
                DateFormat.getDateInstance(DateFormat.MEDIUM).format(Calendar.getInstance().getTime()));
        sharedpreferences = getSharedPreferences("MyPref", 0);

        if (sharedpreferences.contains(Name)) {
            tv_title.setText("Witaj, " + sharedpreferences.getString(Name, ""));
        }

        btn_user_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserAct();
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

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent intent = new Intent(v.getContext(), Login2Activity.class);
                startActivity(intent);
            }
        });

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
}