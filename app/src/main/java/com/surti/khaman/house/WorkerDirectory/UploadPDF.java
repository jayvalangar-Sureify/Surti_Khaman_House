package com.surti.khaman.house.WorkerDirectory;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.surti.khaman.house.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.internal.Intrinsics;

public class UploadPDF {

    public static String file_upload_work_id = "file_upload_work";
    public static StorageReference storageReference;
    public static DatabaseReference databaseReference;

    @RequiresApi(26)
    public static final void myWorkManager(Context context) {
        Constraints constraints =
                (new Constraints.Builder())
                        .setRequiresCharging(false)
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresCharging(false)
                        .setRequiresBatteryNotLow(false)
                        .build();
        Intrinsics.checkNotNullExpressionValue(constraints, "Constraints.Builder()\n  …rue)\n            .build()");
        WorkRequest workRequest =
                ((androidx.work.PeriodicWorkRequest.Builder)(new androidx.work.PeriodicWorkRequest
                .Builder(FileUploadWorker.class, 1L, TimeUnit.DAYS))
                .setConstraints(constraints)).build();
        Intrinsics.checkNotNullExpressionValue(workRequest, "PeriodicWorkRequest.Buil…nts)\n            .build()");
        PeriodicWorkRequest myRequest = (PeriodicWorkRequest)workRequest;
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(file_upload_work_id, ExistingPeriodicWorkPolicy.KEEP, myRequest);
    }


//=====================================================================================================================================
    public static void upload_Bill_Files(Uri data, Context context) {
        Log.i("test_response", "upload_Bill_Files() : "+data.toString());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
//                initFirebase(context);

                // Database
                storageReference = FirebaseStorage.getInstance().getReference();
                databaseReference = FirebaseDatabase.getInstance().getReference("Uploads_Bills");
                String currentDateTime = "";
                try {
                    currentDateTime = new SimpleDateFormat("_[dd-MMM-yyyy]_[HH:mm]", Locale.getDefault()).format(new Date());
                }catch (Exception e){
                    e.getMessage();
                }
                StorageReference reference = storageReference.child("Uploads_Bills"+currentDateTime);
                reference.putFile(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i("test_response", "upload_Bill_Files() : "+data.toString());

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete());
                                Uri url = uriTask.getResult();

                                FileUploadModelClass pdfClass = new FileUploadModelClass(MainActivity.firebase_storage_bill_file_name, url.toString());
                                databaseReference.child(databaseReference.push().getKey()).setValue(pdfClass);
                                Log.i("test_response", "File Uploaded SUccessfully : "+data.toString());
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                Log.i("test_response", "Upload:"+(int) progress+"%");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("test_response", "Error : "+e.getMessage().toString());
                            }
                        });
            }
        });


    }
//=====================================================================================================================================




    //=====================================================================================================================================
    public static void upload_Expenses_Files(Uri data, Context context) {
        Log.i("test_response", "upload_Bill_Files() : "+data.toString());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
//                initFirebase(context);

                // Database
                storageReference = FirebaseStorage.getInstance().getReference();
                databaseReference = FirebaseDatabase.getInstance().getReference("Uploads_Expenses");
                String currentDateTime = "";
                try {
                    currentDateTime = new SimpleDateFormat("_yyyy_MM_dd_HH_mm", Locale.getDefault()).format(new Date());
                }catch (Exception e){
                    e.getMessage();
                }
                StorageReference reference = storageReference.child("Uploads_Expenses"+currentDateTime);
                reference.putFile(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i("test_response", "upload_Bill_Files() : "+data.toString());

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete());
                                Uri url = uriTask.getResult();

                                FileUploadModelClass pdfClass = new FileUploadModelClass(MainActivity.firebase_storage_expenses_file_name, url.toString());
                                databaseReference.child(databaseReference.push().getKey()).setValue(pdfClass);
                                Log.i("test_response", "File Uploaded SUccessfully : "+data.toString());
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                Log.i("test_response", "Upload:"+(int) progress+"%");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("test_response", "Error : "+e.getMessage().toString());
                            }
                        });
            }
        });


    }
//=====================================================================================================================================

    private static void initFirebase(Context context) {
        try {
            FirebaseApp.initializeApp(context);
            FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
            firebaseAppCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance());
        }catch (Exception e){
            e.getMessage();
        }
    }


}
