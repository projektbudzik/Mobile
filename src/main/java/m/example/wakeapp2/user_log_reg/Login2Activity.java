package m.example.wakeapp2.user_log_reg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.R;
import m.example.wakeapp2.group_log_reg.LoginActivity;
import m.example.wakeapp2.group_log_reg.groupWelcomeFragment;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;


public class Login2Activity extends AppCompatActivity {

    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String phoneNumber = "numberKey";
    public static final String Group = "groupKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ViewPager viewPager = findViewById(R.id.viewPager2);

        AuthenticationPagerAdapter pagerAdapterUser = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapterUser.addFragmet(new groupWelcomeFragment());
        pagerAdapterUser.addFragmet(new userStartFragment());
        pagerAdapterUser.addFragmet(new userLoginFragment());
        pagerAdapterUser.addFragmet(new userRegisterFragment());
        viewPager.setAdapter(pagerAdapterUser);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0); // 0 - for private mode

        if (sharedPreferences.contains(Name) && sharedPreferences.contains(Group)  ) {

            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            if (sharedPreferences.contains(Name)){
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("EMAIL", sharedPreferences.getString(Email, ""));
            startActivity(intent);
         }
        }
        if (  ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) ==
                        PackageManager.PERMISSION_GRANTED ) {
            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(phoneNumber, mPhoneNumber);
            editor.commit();
            return;
        } else {
            requestPermission();
        }
    }



    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_PHONE_STATE}, 100);
            requestPermissions(new String[]{CAMERA}, 100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();

                SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(phoneNumber, mPhoneNumber);
                editor.commit();


                break;
        }
    }

    class AuthenticationPagerAdapter  extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }


}
