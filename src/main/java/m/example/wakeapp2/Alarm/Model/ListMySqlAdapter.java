package m.example.wakeapp2.Alarm.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import m.example.wakeapp2.Alarm.Model.ListMySQl;
import m.example.wakeapp2.R;

public class ListMySqlAdapter extends ArrayAdapter<ListMySQl> {

    private List<ListMySQl> SqlList;
    private Context mCtx;

    public ListMySqlAdapter(List<ListMySQl> P, Context C){
        super (C, R.layout.listalarm, P);
        this.SqlList = P;
        this.mCtx = C;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listalarm, null, true);
        TextView id = view.findViewById(R.id.tvID);
        TextView alarmDateStart = view.findViewById(R.id.tvDateStart);
        TextView alarmDtaeEnd = view.findViewById(R.id.tvDateEnd);
        TextView alarmTime = view.findViewById(R.id.tvTime);
        TextView alarmSekwencja = view.findViewById(R.id.tvSekwencja);
        TextView alarmUser = view.findViewById(R.id.tvUser);
        TextView alarmDevice = view.findViewById(R.id.tvDevice);

        ListMySQl listMySQl = SqlList.get(position);

        alarmDateStart.setText(listMySQl.getDateStart());

        if (listMySQl.getDateEnd() == "null") {
            alarmDtaeEnd.setText(" ");
        } else{
         alarmDtaeEnd.setText(listMySQl.getDateEnd());}

        alarmTime.setText(listMySQl.getTime());

        if (listMySQl.getSequence() == "null") {
            alarmSekwencja.setText("Tylko raz");
        } else{
            alarmSekwencja.setText(listMySQl.getSequence());}

        id.setText(listMySQl.getAlarmId());
        alarmDevice.setText(listMySQl.getDeviceId());

        // TODO: zmienic strukture sql dla alarmow
        alarmUser.setText("Damian");


        return view;
    }
}
