package m.example.wakeapp2.info_log_reg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.R;
import m.example.wakeapp2.WifiDeviceActivity;

public class infoContextFragment extends Fragment {

    SurfaceView surfaceView;
    TextView camSerialNum;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Button btn_accept, btn_skip;


    public infoContextFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_info_context, container, false);
    btn_accept = view.findViewById(R.id.btn_start_info);
    btn_skip = view.findViewById(R.id.btn_start_info);
    surfaceView = view.findViewById(R.id.camerapreview);
    camSerialNum = view.findViewById(R.id.cam_serialnum);

    barcodeDetector = new BarcodeDetector.Builder(getContext())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build();
    cameraSource = new CameraSource.Builder(getContext(),barcodeDetector)
            .setRequestedPreviewSize(640,480)
            .build();

    surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            try {
                cameraSource.start(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
        }
    });

    barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
        @Override
        public void release() {

        }

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

            if(qrCodes.size() != 0){
                camSerialNum.post(new Runnable() {
                    @Override
                    public void run() {

                        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(100);
                        camSerialNum.setText(qrCodes.valueAt(0).displayValue);
                        btn_accept.setVisibility(View.VISIBLE);
                        btn_skip.setVisibility(View.GONE);

                        Intent i = new Intent(getActivity(), WifiDeviceActivity.class);
                        i.putExtra("BT_ADDRESS", camSerialNum.getText()); //this will be received at ledControl (class) Activity
                        startActivity(i);
                    }

                });
            }
        }
    });

    btn_accept.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openMain();
        }
    });

    btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openskipMain();
            }
        });
    return view;
    }

    public void openMain(){
        getActivity().finish();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void openskipMain(){
        getActivity().finish();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}

