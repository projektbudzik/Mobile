package m.example.wakeapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import m.example.wakeapp2.user_log_reg.Login2Activity;

public class Settings extends AppCompatActivity {
    public static final String Name = "nameKey";
    public static final String GroupName = "groupNameKey";
    public static final String Email = "emailKey";
    public static final String phoneNumber = "numberKey";
    Intent intent;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView tv_sets_name = findViewById(R.id.tv_sets_name);
        TextView tv_sets_email = findViewById(R.id.tv_sets_email);
        TextView tv_sets_phone = findViewById(R.id.tv_sets_phone);
        TextView tv_sets_group = findViewById(R.id.tv_sets_group);
        Button btn_cofnij = findViewById(R.id.btn_back);
        Button btn_logout = findViewById(R.id.btn_logout);
        intent = new Intent(this, AlarmService.class);

        sharedPreferences = getSharedPreferences("MyPref", 0);
        if (sharedPreferences.contains(Name)){
            String tName = sharedPreferences.getString(Name,"");
            tv_sets_name.setText(tName);
        }
        if (sharedPreferences.contains(Email)){
            String tName = sharedPreferences.getString(Email,"");
            tv_sets_email.setText(tName);
        }
        if (sharedPreferences.contains(phoneNumber)){
            String tName = sharedPreferences.getString(phoneNumber,"");
            tv_sets_phone.setText(tName);
        }
        if (sharedPreferences.contains(GroupName)){
            String tName = sharedPreferences.getString(GroupName,"");
            tv_sets_group.setText(tName);
        }

        btn_cofnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                stopService(intent);
                finish();
                Intent intent = new Intent(v.getContext(), Login2Activity.class);
                startActivity(intent);
            }
        });
    }
}
