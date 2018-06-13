package io.github.senggruppe.quicknotes.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {
    File audioMessage;
    MediaRecorder recorder;
    private final int audioPermission = 200;
    private boolean permissionToRecordAccepted = false;

    @TargetApi(26)
    public RecordActivity(String name) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, audioPermission);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, audioPermission);
            }
        }
        //If permission is granted, then go ahead recording audio
        else {

            //Go ahead with recording audio now
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            audioMessage = new File(this.getFilesDir().getAbsolutePath() + "\\" + name + ".3gp" );
            recorder.setOutputFile(audioMessage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case audioPermission:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    public void startRecording() throws IOException {
        recorder.prepare();
        recorder.start();
    }

    public File stopRecording(){
        recorder.stop();
        recorder.release();
        return audioMessage;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

}
