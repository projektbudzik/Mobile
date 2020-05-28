package m.example.wakeapp2.Device.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.R;
import m.example.wakeapp2.User.Model.ListUser;
import m.example.wakeapp2.User.Model.ListUserAdapter;
import m.example.wakeapp2.User.Model.UserActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddDiviceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddDiviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDiviceFragment extends Fragment  implements DeviceScanerMAC.OnFragmentInteractionListener {


    private EditText et_deviceName, et_deviceMAC, et_deviceUser;
    private TextView deviceMAC, deviceUser;
    private RadioGroup radioDevice;
    private RadioButton radioTelefon;
    private String defaultValue;
    private Button btn_cofnij, btn_dodaj_device;
    ListView listUser;
    List<String> userList;
    private String GroupId;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    public static final String Name = "nameKey";
    public static final String Group_sp = "groupKey";

    public static final String adrMAC = "numberAdrMAC";

    private OnFragmentInteractionListener mListener;

    public AddDiviceFragment() {
        // Required empty public constructor
    }


    public static AddDiviceFragment newInstance() {
        AddDiviceFragment fragment = new AddDiviceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_divice, container, false);
        et_deviceName = view.findViewById(R.id.et_deviceName);
        radioDevice = view.findViewById(R.id.radioDevice);
        et_deviceMAC = view.findViewById(R.id.et_deviceMAC);
        deviceMAC = view.findViewById(R.id.deviceMAC);
        et_deviceUser = view.findViewById(R.id.et_deviceUser);
        deviceUser= view.findViewById(R.id.deviceUser);
        radioTelefon = view.findViewById(R.id.radioTelefon);
        btn_cofnij = view.findViewById(R.id.btn_cofnij);
        listUser =view.findViewById(R.id.listUser);
        userList = new ArrayList<>();
        btn_dodaj_device = view.findViewById(R.id.btn_dodaj_device);
        BackgroundTask backgroundTask = new BackgroundTask(getContext());
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPref", 0);
        String type="getUsers";
        if (sharedpreferences.contains(Name)) {

           GroupId = sharedpreferences.getString(Group_sp, "");
        }

        try {
            String s = backgroundTask.execute(type, GroupId).get();
            JSONObject obj = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
            JSONArray array = obj.getJSONArray("users");
            for (int i=0; i <array.length(); i++){

                JSONObject alarm = array.getJSONObject(i);
                userList.add(alarm.getString("Name"));

            }


            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_activated_1,userList);
            listUser.setAdapter(arrayAdapter);


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e)  {

        }



        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (prefs.contains(adrMAC)) {
                    defaultValue = prefs.getString(adrMAC, "");
                    et_deviceMAC.setText(defaultValue);
                    prefs.edit().remove(adrMAC).commit();}
            }
        };

        sharedpreferences.registerOnSharedPreferenceChangeListener(listener);



        radioDevice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb= getView().findViewById(checkedId);

                if (!radioTelefon.isChecked()){
                    deviceMAC.setText("Adres MAC");
                    et_deviceMAC.setHint("Kliknij aby zeskanować");

                } else {
                    deviceMAC.setText("Numet telefonu");
                    et_deviceMAC.setHint("Numet telefonu");


                }
            }
        });



        et_deviceMAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioTelefon.isChecked()){
                    DeviceScanerMAC fragment = DeviceScanerMAC.newInstance();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.exit_from_right, R.anim.exit_to_right, R.anim.exit_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.add(R.id.fragment_container_deviceMAC, fragment, "BLANK_FRAGMENT").commit();

                }
            }
        });

        btn_cofnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(getActivity().getIntent());


            }
        });

        btn_dodaj_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask backgroundTask = new BackgroundTask(getContext());
                String dType;

                if (radioTelefon.isChecked()) {
                     dType = "telefon";
                }else{
                    dType = "arduino";
                }

                String dName = et_deviceName.getText().toString();
                String dMac = et_deviceMAC.getText().toString();
                String dUser =  userList.get(listUser.getSelectedItemPosition()+1);
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPref", 0);
                String dGroupId = sharedpreferences.getString(Group_sp, "");
                Log.e("dddd", dName + dType +  dMac + dUser + dGroupId);
                backgroundTask.execute("addDevice", dName,dType, dMac, dUser, dGroupId );

            }
        });

        return  view;
    }

    public void sendBack() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPref", 0);

        if (sharedpreferences.contains(adrMAC)) {
            defaultValue = sharedpreferences.getString(adrMAC, "");
            et_deviceMAC.setText(defaultValue);
            sharedpreferences.edit().remove(adrMAC).commit();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
