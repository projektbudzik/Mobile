package m.example.wakeapp2.info_log_reg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.R;
import m.example.wakeapp2.WifiDeviceActivity;

public class infoContextFragment extends Fragment {

    private EditText et_mobile_num, et_mobile_name;
    private static final String phoneNumber = "numberKey";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Group = "groupKey";
    private SharedPreferences sharedpreferences;
    private Button btn_skip, btn_add_phone;

    public infoContextFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_context, container, false);
        et_mobile_num = view.findViewById(R.id.et_mobile_num);
        et_mobile_name = view.findViewById(R.id.et_mobile_name);
        sharedpreferences = getActivity().getSharedPreferences("MyPref", 0);
        btn_skip = view.findViewById(R.id.btn_skip);
        btn_add_phone = view.findViewById(R.id.btn_add_phone);
        String tel = sharedpreferences.getString(phoneNumber, "");
        if (sharedpreferences.contains(phoneNumber)) {
            et_mobile_num.setText(tel.substring(3));
        }

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
        btn_add_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask backgroundTask = new BackgroundTask(getContext());
                String dType = "telefon";
                String dName = et_mobile_name.getText().toString();
                String dMac = et_mobile_num.getText().toString();
                String dUser =  sharedpreferences.getString(Name,"");
                String dGroupId = sharedpreferences.getString(Group, "");
                backgroundTask.execute("addDevice", dName,dType, dMac, dUser, dGroupId );
                openMain();
            }
        });

        et_mobile_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        et_mobile_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

    return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void openMain(){
        getActivity().finish();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}

