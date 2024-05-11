package com.example.mp3background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.security.Provider;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    // Define notification channel ID
    private static final String CHANNEL_ID = "MusicPlayerChannel";
    // Define notification ID
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(false); // Do not loop the music

        // Create a notification channel (required for Android Oreo and above)
        createNotificationChannel();

        // Start the service as a foreground service
        startForeground(NOTIFICATION_ID, showNotification());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Music Channel";
            String description = "Channel for Music notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            if (intent.hasExtra("CURRENT_POSITION")) {
                int currentPosition = intent.getIntExtra("CURRENT_POSITION", 0);
                // Start playback from the saved position
                mediaPlayer.seekTo(currentPosition);
            }
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }

            if (intent.getAction() != null) {
                Log.d("MusicService", "Received action: " + intent.getAction());
                switch (intent.getAction()) {
                    case "PAUSE":
                        pauseMusic();
                        break;
                    case "RELEASE":
                        releaseMediaPlayer();
                        break;
                }
            }
        }
        return START_STICKY;
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopSelf(); // Stop the service
    }

    private Notification showNotification() {

        // Add pause action
        Intent pauseIntent = new Intent(this, MainActivity.class);
        pauseIntent.setAction("PAUSE");
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE);

        // Add release action
        Intent releaseIntent = new Intent(this, MainActivity.class);
        releaseIntent.setAction("RELEASE");
        PendingIntent releasePendingIntent = PendingIntent.getService(this, 0, releaseIntent, PendingIntent.FLAG_IMMUTABLE);

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.musique)
                .setContentTitle("Music is playing")
                .setContentText("Tap to return to the app")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.pause, "Pause", pausePendingIntent)
                .addAction(R.drawable.play, "Release", releasePendingIntent)
                .setOngoing(false); // Notification cannot be swiped away

        // Create an explicit intent for launching the app
        Intent launchIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please enable notifications.", Toast.LENGTH_LONG).show();

            showNotificationPermissionDialog();

            stopSelf(); // Stop the service since notifications are required for background music playback
            return null;
        }

        return builder.build();
    }

    private void showNotificationPermissionDialog() {
        // Open app settings
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release media player resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        // Remove the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
