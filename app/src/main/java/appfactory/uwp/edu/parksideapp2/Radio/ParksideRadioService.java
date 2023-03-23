package appfactory.uwp.edu.parksideapp2.Radio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import androidx.core.app.NotificationCompat;
import appfactory.uwp.edu.parksideapp2.R;
import timber.log.Timber;

/**
 * Streaming Media Service that runs in the foreground with
 * notification and lockscreen controls.
 * <p>
 * Created by Marshall Ladd 7 AUG 2018
 */
public class ParksideRadioService extends Service implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final int ONGOING_NOTIFICATION_ID = 9001;
    public static final String ONGOING_NOTIFICATION_NAME = "WIPZ_SERVICE_NOTIFICATION";
    private static final String TAG = "FOREGROUND_SERVICE";
    public static String ACTION_BUFFERING_COMPLETE = "BUFFERING_COMPLETE";
    private WifiManager.WifiLock wifiLock;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Notification notification;

    public ParksideRadioService() {
    }

    /**
     * Called with startService(Intent)
     * <p>
     * Intent passed should have a setAction(action) applied
     *
     * @param intent  Action to be executed
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Handle requested action from Intent
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_START_FOREGROUND_SERVICE:
                        if (mediaPlayer == null) {
                            initMediaPlayer();
                        } else if (mediaPlayer.isPlaying()) {
                            pauseMedia();
                        } else {
                            playMedia();
                        }
                        break;
                    case ACTION_STOP_FOREGROUND_SERVICE:
                        stopMedia();
                        break;
                    case ACTION_PLAY:
                        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                            playMedia();
                        }
                        break;
                    case ACTION_PAUSE:
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            pauseMedia();
                        }
                        break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Launches before all commands and intents when first created.
     * Creates notification and promotes service to the foreground
     */
    @Override
    public void onCreate() {
        super.onCreate();
        createNotification();
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    /**
     * Called when service is completely killed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                stopMedia();
            }
            mediaPlayer.release();
        }
        // removeAudioFocus() should be called here, or in onCompletion()
        removeAudioFocus();
    }

    /**
     * Required for services, even un-bound.
     * NOT USED IN THIS SERVICE
     *
     * @param intent
     * @return null as this service is not bound
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Requests audio focus from the system then
     * creates the media player asynchronously
     */
    private void initMediaPlayer() {
        if (requestAudioFocus()) {
            // build media player
            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
            }

            try {
                mediaPlayer.setDataSource(getString(R.string.RADIO_STREAM_URL));
            } catch (IOException e) {
                e.printStackTrace();
                stopSelf();
            }

            // CPU Wakelock
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            // WIFI Wakelock
            wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "WIPZ lock");
            wifiLock.acquire();

            // Prepare Radio in background
            mediaPlayer.prepareAsync();
        } else {
            stopSelf();
            Toast.makeText(getApplicationContext(), "Couldn't gain audio focus. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when prepareAsync() finishes
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
        Intent bufferCompleteBroadcast = new Intent();
        bufferCompleteBroadcast.setAction(ACTION_BUFFERING_COMPLETE);
        sendBroadcast(bufferCompleteBroadcast);
    }

    /**
     * Creates required Notification
     * for a foreground service.
     */
    private void createNotification() {
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(getString(R.string.radio_id));
        bigTextStyle.bigText("");
        notificationBuilder.setStyle(bigTextStyle);
        notificationBuilder.setShowWhen(false);
        notificationBuilder.setSmallIcon(R.drawable.ic_radio);
        notificationBuilder.setPriority(Notification.PRIORITY_LOW);
        notificationBuilder.setFullScreenIntent(pendingIntent, true);

        // Play Button
        Intent playIntent = new Intent(this, ParksideRadioService.class);
        playIntent.setAction(ACTION_START_FOREGROUND_SERVICE);
        PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
        NotificationCompat.Action playAction = new NotificationCompat.Action(R.drawable.ic_play_pause_white_48dp, "Play/Pause", pendingPlayIntent);
        notificationBuilder.addAction(playAction);

        // Stop Button
        Intent stopIntent = new Intent(this, ParksideRadioService.class);
        stopIntent.setAction(ACTION_STOP_FOREGROUND_SERVICE);
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, 0);
        NotificationCompat.Action stopAction = new NotificationCompat.Action(R.drawable.ic_stop_white_48dp, "Stop", pendingStopIntent);
        notificationBuilder.addAction(stopAction);

        // Build notification Android SDK >= 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.radio_id), ONGOING_NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(getString(R.string.radio_id));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            notification = notificationBuilder.setChannelId(getString(R.string.radio_id)).build();
        }
        // Build notification Android SDK < 26
        else {
            notification = notificationBuilder.build();
        }
    }

    /**
     * payMedia() is also used in place of resumeMedia()
     * since it is a streaming app and always resumes at
     * the same spot.
     */
    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        wifiLock.release();
        stopSelf();
        stopForeground(true);
        removeAudioFocus();
    }

    private void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        Timber.d("onAudioFocusChange: called");
        switch (focusChange) {
            // resume
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mediaPlayer == null) {
                    initMediaPlayer();
                } else if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            // audio focus lost for unknown time
            case AudioManager.AUDIOFOCUS_LOSS:
                mediaPlayer.stop();
                break;
            // lost focus, probably will regain again soon
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            // lost focus for notification of something like that
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(0.1f, 0.1f);
                }
                break;
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .setAcceptsDelayedFocusGain(false)
                    .setOnAudioFocusChangeListener(this)
                    .build();
            int result = audioManager.requestAudioFocus(audioFocusRequest);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                return true;
            }
        } else {
            int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                return true;
            }
        }
        // request failed
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    /**
     * Service has finished running
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        stopMedia();
        stopSelf();
        stopForeground(true);
        // removeAudioFocus() should be called here, or in onDestroy
        removeAudioFocus();
    }

    /**
     * Error Handling
     *
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Intent bufferCompleteBroadcast = new Intent();
        bufferCompleteBroadcast.setAction(ACTION_BUFFERING_COMPLETE);
        sendBroadcast(bufferCompleteBroadcast);
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Timber.e("MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Timber.e("MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Timber.e("MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }
}
