package appfactory.uwp.edu.parksideapp2.Detail;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

import timber.log.Timber;

public class MyService extends Service implements MediaPlayer.OnPreparedListener{
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_STOP = "ACTION_STOP";

    Boolean isStart = false;
    MediaPlayer mMediaPlayer;
    WifiManager.WifiLock wifiLock;
    public static String URL;

    @Override
    public void onCreate() {
        Timber.d("onCreate()__ initialization");
        // The service is being created
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("onStartCommand()__ checking which button being click");
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_START:
                        initMediaPlayer();
                        break;
                    case ACTION_PLAY:
                        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                            playMedia();
                        }
                        break;
                    case ACTION_PAUSE:
                        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                            pauseMedia();
                        }
                        break;
                    case ACTION_STOP:
                        stopMedia();
                        break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        Timber.d("onPrepared()__ finish prepared, now start play");
        player.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                stopMedia();
            }
            mMediaPlayer.release();
        }
    }

    private void initMediaPlayer() {
        if (isStart == false) {
            // The service is starting, due to a call to startService()
            Timber.d("initMediaPlayer()__if() media is prepare and start");
            try {

                mMediaPlayer.setDataSource(URL);
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                        .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

                wifiLock.acquire();
                isStart = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void playMedia() {
        if (!mMediaPlayer.isPlaying()) {
            Timber.d("playMedia()__if() media starting");
            mMediaPlayer.start();
        }
    }

    private void pauseMedia() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    private void stopMedia() {
        if (mMediaPlayer == null) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        wifiLock.release();
        stopSelf();
        // Remove this service from foreground state, allowing it to be killed if more memory is needed.
        stopForeground(true);
    }

    public static void setURL(String url) {
        URL = url;
    }

}

