package com.surti.khaman.house.WorkerDirectory;


import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.surti.khaman.house.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public class FileUploadWorker extends Worker {
    @NotNull
    public static final String CHANNEL_ID = "channel_id";
    public static final int NOTIFICATION_ID = 1;
    @NotNull
    public static final FileUploadWorker.Companion Companion = new FileUploadWorker.Companion((DefaultConstructorMarker) null);

    public FileUploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NotNull
    public Result doWork() {
        Log.i("test_response", "DoWork : Success function called");
        this.issueNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("test_response", "Inside oreo");

            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File downloadDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File file_name_surtikhamanhouse = new File(downloadDirectory,MainActivity.file_name_surtikhamanhouse);

//        Uri file_surtikhaman_uri = Uri.fromFile(file);

            Uri file_surtikhaman_uri = FileProvider.getUriForFile(
                    getApplicationContext(),
                    MainActivity.provider_name, //(use your app signature + ".provider" )
                    file_name_surtikhamanhouse);

            Log.i("test_response", "Inside oreo URI : "+file_surtikhaman_uri.toString());

            UploadPDF.uploadFiles(file_surtikhaman_uri, getApplicationContext());
        }
        Result result = Result.success();
        Intrinsics.checkNotNullExpressionValue(result, "Result.success()");
        return result;
    }

    private void issueNotification()
    {
        // Create the notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            makeNotificationChannel(
                    "CHANNEL_1", "Example channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
        }

        // Creating the notification builder
        NotificationCompat.Builder notificationBuilder
                = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_1");

        // Setting the notification's properties
        notificationBuilder
                .setSmallIcon(com.surti.khaman.house.R.drawable.ic_left_menu_dashboard_24)
                .setContentTitle("Surti Khaman House")
                .setContentText("Data Updated Successfully")
                .setNumber(3);

        // Getting the notification manager and send the
        // notification
        NotificationManager notificationManager
                = (NotificationManager) getApplicationContext().getSystemService(
                NOTIFICATION_SERVICE);

        // it is better to not use 0 as notification id, so
        // used 1.
        notificationManager.notify(
                1, notificationBuilder.build());
    }

    // Helper method to create a notification channel for
    // Android 8.0+
    private void makeNotificationChannel(String channelId,
                                         String channelName,
                                         int importance)
    {
        NotificationChannel channel
                = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    channelId, channelName, importance);
        }
        NotificationManager notificationManager
                = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static final class Companion {
        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    public File getFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File downloadDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadDirectory, "SKH_Expenses.pdf");
        return file;
    }
}
