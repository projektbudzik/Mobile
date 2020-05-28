package m.example.wakeapp2.Alarm.Model;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import java.text.DateFormat;
import java.util.Calendar;

import m.example.wakeapp2.R;


public class AddAlarm extends Fragment implements DatePickerDialog.OnDateSetListener{


    private OnFragmentInteractionListener mListener;

    Button btn_cofnij, Datepick1;
    String selectedDate;
    TimePicker timePicker;
    EditText startAlarm;

    public AddAlarm() {
        // Required empty public constructor
    }


    public static AddAlarm newInstance() {
        AddAlarm fragment = new AddAlarm();
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

        View view = inflater.inflate(R.layout.fragment_add_alarm, container, false);

        timePicker = view.findViewById(R.id.time_picker1);
        timePicker.setIs24HourView(true);
        btn_cofnij = view.findViewById(R.id.btn_cofnij);
        startAlarm = view.findViewById(R.id.textView3);
        startAlarm.setText(   DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime()));
        Switch sw = view.findViewById(R.id.switch1);
        final LinearLayout linearLayout1 = view.findViewById(R.id.LinearLayout1);
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        startAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialogFragment newFragment = new m.example.wakeapp2.Alarm.Model.DatePicker();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(AddAlarm.this, 11);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });




        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayout1.setVisibility(View.VISIBLE);
                } else {
                    linearLayout1.setVisibility(View.GONE);
                }
            }
        });
        btn_cofnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBack();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            // get date from string
             selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            startAlarm.setText(selectedDate);
        }
    }

    public void sendBack() {
        if (mListener != null) {
            mListener.onFragmentInteraction("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(String sendBackText);
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        EditText editText = view.findViewById(R.id.textView3);
        editText.setText(currentDateString);
    }
}



