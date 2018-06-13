package io.github.senggruppe.quicknotes.core;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

public class Audio {
    File audioMessage;

    @TargetApi(26)
    public Audio(Context ctx, String name) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        audioMessage = new File(ctx.getFilesDir().getAbsolutePath() + "\\" + name);
        recorder.setOutputFile(audioMessage);
    }

    public void startRecording(MediaRecorder recorder) throws IOException {
        recorder.prepare();
        recorder.start();
    }

    public File stopRecording(MediaRecorder recorder){
        recorder.stop();
        recorder.release();
        return audioMessage;
    }
}
