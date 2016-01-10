package us.troli.scanner;
/**
 * * Created by anilj on 12/15/15.
 */
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * An activity to illustrate how to create a file.
 */
public class GDriveActivity extends BaseScanActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy:HH:mm");
    String currenttime;
    ArrayList<String> fullfilename;
    private static final String TAG = "CreateFileActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gdriveactivity_main);
        Intent intent = getIntent();
        fullfilename = intent.getStringArrayListExtra("Barcodelist");
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        // create new contents resource
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback);
    }

    final private ResultCallback<DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create new file contents");
                        return;
                    }
                    final DriveContents driveContents = result.getDriveContents();

                    // Perform I/O off the UI thread.
                    new Thread() {
                        @Override
                        public void run() {
                            // write content to DriveContents
                            currenttime = sdf.format(new Date());
                            OutputStream outputStream = driveContents.getOutputStream();
                            Writer writer = new OutputStreamWriter(outputStream);

                            try {
                                writer.write("Date, scancode");
                                writer.write("\n");
                                //Iterator<String> iterator = fullfilename.iterator();
                                for(int i=0; i<fullfilename.size();i++) {

                                    writer.write(currenttime +","+fullfilename.get(i));

                                    writer.write("\n");

                                }
                                writer.close();

                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                            }
                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle("barcodelist.csv")
                                    .setMimeType("text/plain")
                                    .setStarred(true).build();

                            // create a file on root folder
                            Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                    .createFile(getGoogleApiClient(), changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }
                    }.start();
                }
            };

    final private ResultCallback<DriveFileResult> fileCallback = new
            ResultCallback<DriveFileResult>() {
                @Override
                public void onResult(DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create the file");
                        return;
                    }
                    else {
                        showMessage("Synced your ietms. Click back button to go back");
                    }
                    Log.d("MYDrive","Created a file with content: " + result.getDriveFile().getDriveId());
                }
            };


}
