package m.example.wakeapp2.user_log_reg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.ArrayList;

import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.R;
import m.example.wakeapp2.group_log_reg.LoginActivity;
import m.example.wakeapp2.group_log_reg.groupWelcomeFragment;

public class Login2Activity extends AppCompatActivity {

    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    SharedPreferences sharedPreferences;
    public static final String Group = "groupKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ViewPager viewPager = findViewById(R.id.viewPager2);

        AuthenticationPagerAdapter pagerAdapterUser =
                new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapterUser.addFragmet(new groupWelcomeFragment());
        pagerAdapterUser.addFragmet(new userStartFragment());
        pagerAdapterUser.addFragmet(new userLoginFragment());
        pagerAdapterUser.addFragmet(new userRegisterFragment());
        viewPager.setAdapter(pagerAdapterUser);
        sharedPreferences = getSharedPreferences("MyPref", 0);


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
    }

    class AuthenticationPagerAdapter  extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        AuthenticationPagerAdapter(FragmentManager fm) {
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
