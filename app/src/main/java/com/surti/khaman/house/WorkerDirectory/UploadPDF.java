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

import java.util.concurrent.TimeUnit;

import kotlin.jvm.internal.Intrinsics;

public class UploadPDF {

    public static StorageReference storageReference;
    public static DatabaseReference databaseReference;

    @RequiresApi(26)
    public static final void myWorkManager(Context context) {
        Constraints var10000 = (new Constraints.Builder()).setRequiresCharging(false).setRequiredNetworkType(NetworkType.NOT_REQUIRED).setRequiresCharging(false).setRequiresBatteryNotLow(true).build();
        Intrinsics.checkNotNullExpressionValue(var10000, "Constraints.Builder()\n  …rue)\n            .build()");
        Constraints constraints = var10000;
        WorkRequest var3 = ((androidx.work.PeriodicWorkRequest.Builder)(new androidx.work.PeriodicWorkRequest.Builder(FileUploadWorker.class, 15L, TimeUnit.MINUTES)).setConstraints(constraints)).build();
        Intrinsics.checkNotNullExpressionValue(var3, "PeriodicWorkRequest.Buil…nts)\n            .build()");
        PeriodicWorkRequest myRequest = (PeriodicWorkRequest)var3;
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("my_id", ExistingPeriodicWorkPolicy.KEEP, myRequest);
    }


    public static void uploadFiles(Uri data, Context context) {
//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle("Uploading .....");
//        progressDialog.show();
        Log.i("test_response", "uploadFiles() : "+data.toString());




        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                initFirebase(context);

                // Database
                storageReference = FirebaseStorage.getInstance().getReference();
                databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
                StorageReference reference = storageReference.child("Uploads");
                reference.putFile(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Log.i("test_response", "uploadFiles() : "+data.toString());

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete());
                                Uri url = uriTask.getResult();

                                FileUploadModelClass pdfClass = new FileUploadModelClass(MainActivity.firebase_storage_file_name, url.toString());
                                databaseReference.child(databaseReference.push().getKey()).setValue(pdfClass);

                                Log.i("test_response", "File Uploaded SUccessfully : "+data.toString());

//                                Toast.makeText(context, "File Uploaded SUccessfully", Toast.LENGTH_LONG).show();

//                        progressDialog.dismiss();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                Log.i("test_response", "Upload:"+(int) progress+"%");
//                        double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
//                        progressDialog.setMessage("Upload:"+(int) progress+"%");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("test_response", "Error : "+e.getMessage().toString());
//                                Toast.makeText(context, "Failed To Upload : "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


    }

    private static void initFirebase(Context context) {
        FirebaseApp.initializeApp(context);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());
    }


}
