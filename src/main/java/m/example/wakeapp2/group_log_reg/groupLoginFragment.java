package m.example.wakeapp2.group_log_reg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.JSONreader;
import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.info_log_reg.Login3Activity;
import m.example.wakeapp2.user_log_reg.Login2Activity;
import m.example.wakeapp2.R;


public class groupLoginFragment extends Fragment {
    EditText txtLogin, txtPassword;
    public static final String Group = "groupKey";
    SharedPreferences sharedpreferences;

    public groupLoginFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_login, container, false);
        txtLogin = view.findViewById(R.id.et_login);
        txtPassword = view.findViewById(R.id.et_password);
        Button btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewGroup();
            }
        });


        txtLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });
        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });
        return view;
    }


    public void openNewGroup(){
           String email = ((LoginActivity)getActivity()).getEmail();
            String username = txtLogin.getText().toString();
            String password = txtPassword.getText().toString();
            String callbackMsg = "";
            String type="grouplog";
            BackgroundTask backgroundTask = new BackgroundTask(getActivity().getApplicationContext());

            try {
                callbackMsg = backgroundTask.execute(type, username, password, email).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            JSONreader jsoNreader = new JSONreader();
            String getStatus = jsoNreader.readJSONdata(callbackMsg, "status");
            String getGroupId = jsoNreader.readJSONdata(callbackMsg, "GroupId");

            if (getStatus.equals("OK")){

                sharedpreferences = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Group, getGroupId);
                editor.commit();

                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


