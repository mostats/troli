package us.troli.scanner;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;

import static android.content.Context.MODE_WORLD_READABLE;


public class MainActivity extends ActionBarActivity {
    SharedPreferences sharedPref;
    String fname = "Ascan.txt";
    String filepath;
    Button listbtn;
    private ArrayList<String> barCodeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scanBarcode(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void scanBarcodeCustomLayout(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientation.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan something");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void scanBarcodeFrontCamera(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        integrator.initiateScan();
    }

    public void scanContinuous(View view) {
        Intent intent = new Intent(this, ContinuousScan.class);
        startActivity(intent);
    }

    //public void scanToolbar(View view) {
    //    new IntentIntegrator(this).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
    //}

    //public void scanCustomScanner(View view) {
      //  new IntentIntegrator(this).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    //}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Blist1", "blist:"+barCodeList.size());
                barCodeList.add(result.getContents());
                Log.d("Blist2", "blist:" + barCodeList.size());
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MainActivity", "Weird");
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    protected boolean saveScanDataold(String result) {

            String fpath = fname+".txt";
            Context context = getApplicationContext();
            File file = new File(context.getFilesDir(), fpath);
            //File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                try {
                    file.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.d("File exists",file.getAbsolutePath());
            }
            FileOutputStream outputStream;
            try {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(result);
                bw.close();
                filepath = file.getAbsolutePath();
                Log.d("Success", filepath);
                return true;
            } catch (Exception e) {
                e.printStackTrace();

                return false;
            }
    }

    // For future---
    public void iamDone(View view) {
        Intent intent = new Intent(this, GDriveActivity.class);
        listbtn = (Button)findViewById(R.id.listbutton);
        intent.putStringArrayListExtra("Barcodelist", barCodeList);
        startActivity(intent);
    }

    /**
     * Sample of scanning from a Fragment
     */
 /*
    public static class ScanFragment extends Fragment {
        private String toast;

        public ScanFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            displayToast();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main, container, false);
            Button scan = (Button) view.findViewById(R.id.scan_from_fragment);
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanFromFragment();
                }
            });
            return view;
        }

        public void scanFromFragment() {
            IntentIntegrator.forSupportFragment(this).initiateScan();
        }

        private void displayToast() {
            if(getActivity() != null && toast != null) {
                Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
                toast = null;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    toast = "Cancelled from fragment";
                } else {
                    toast = "Scanned from fragment: " + result.getContents();
                }

                // At this point we may or may not have a reference to the activity
                displayToast();
            }
        }
    } */
}
