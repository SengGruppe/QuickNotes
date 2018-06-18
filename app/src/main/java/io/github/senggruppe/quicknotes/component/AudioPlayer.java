package io.github.senggruppe.quicknotes.component;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.util.Utils;

public class AudioPlayer extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private ImageView btn;
    private SeekBar progress;
    private final MediaPlayer player = new MediaPlayer();
    private File audioFile;

    public AudioPlayer(Context context) {
        super(context);
        init(context);
    }

    public AudioPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AudioPlayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AudioPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(mediaPlayer -> {
            player.prepareAsync();
            btn.setImageResource(R.drawable.ic_pause_audio);
        });

        setOrientation(HORIZONTAL);

        btn = new ImageView(context);
        btn.setLayoutParams(new LayoutParams(Utils.dpToPx(context, 40), Utils.dpToPx(context, 40)));
        btn.setImageResource(R.drawable.ic_play_audio);
        btn.setOnClickListener(this);
        addView(btn);

        progress = new SeekBar(context);//, null, android.R.attr.progressBarStyleHorizontal);
        progress.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        progress.setOnSeekBarChangeListener(this);
        //progress.setIndeterminate(false);
        addView(progress);
        Utils.setViewAndChildrenEnabled(this, false);
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) throws IOException {
        this.audioFile = audioFile;
        player.reset();
        player.setDataSource(audioFile.getAbsolutePath());
        player.prepareAsync();
    }

    /*
    public void startAudio(){
        try {
            player.setDataSource(audioFile.getAbsolutePath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            //new AlertDialog.Builder(ctx).setTitle("IOException while trying to load audio file!");
            Crashlytics.logException(e);
        }
    }

    public void stopAudio(){
        if (player != null) {
            player.release();
            player = null;
        }
    }
     */

    @Override
    public void onClick(View view) {
        if (player.isPlaying()) {
            player.pause();
            btn.setImageResource(R.drawable.ic_play_audio);
        } else {
            player.start();
            new ProgressUpdater();
            btn.setImageResource(R.drawable.ic_pause_audio);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int pos, boolean fromUser) {
        player.seekTo(pos);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void release() {
        player.release();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        progress.setMax(player.getDuration());
        progress.setProgress(0);
        Utils.setViewAndChildrenEnabled(this, true);
    }

    private class ProgressUpdater implements Runnable {
        private Handler h = new Handler();

        public ProgressUpdater() {
            h.post(this);
        }

        @Override
        public void run() {
            if (!player.isPlaying()) return;
            progress.setProgress(player.getDuration());
            h.postDelayed(this, 500);
        }
    }
}
