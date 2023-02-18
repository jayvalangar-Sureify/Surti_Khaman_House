package com.surti.khaman.house;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.surti.khaman.house.databinding.ActivityMainBinding;
import com.surti.khaman.house.ui.dashboard.DashboardFragment;

public class MainActivity extends AppCompatActivity implements PermissionUtil.PermissionsCallBack {

    public static String firebase_storage_bill_file_name = "Firebase_Bills_File";
    public static String firebase_storage_expenses_file_name = "Expenses_File";
    public static String provider_name = "com.surti.khaman.house.provider";
    public static String file_name_surtikhamanhouse = "SKH_Bills.pdf";
    public static String file_name_skh_expenses = "SKH_Expenses.pdf";

    public static String skh_phone_number = "9137272150";

    // Wholesale
    //----------------------------------------------------------------------------------------------
    public static String file_name_wholesale_bill = "SKH_wholesale_Bills.pdf";
    //----------------------------------------------------------------------------------------------

    public static final int PERMISSION_BLUETOOTH = 01;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 02;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 03;
    public static final int PERMISSION_BLUETOOTH_SCAN = 04;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_shop_menu, R.id.nav_expenses, R.id.nav_wholesale, R.id.nav_wholesale_menu, R.id.nav_box_dashboard, R.id.nav_admin, R.id.nav_info)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        DashboardFragment.set_password_sharedpreference(skh_phone_number, MainActivity.this);

        requestPermissions();

        // Show File Access Permission
        if (Build.VERSION.SDK_INT >= 30){
            if(!Environment.isExternalStorageManager()) {
                show_file_access_permission_dialog_box(MainActivity.this);
            }
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.checkAndRequestPermissions(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN)) {

                Log.i("test_permission", "Permissions are granted. Good to go!");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }

    @Override
    public void permissionsGranted() {
//        Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
        Log.i("test_response", "Permissions granted!");
    }

    @Override
    public void permissionsDenied() {
        Toast.makeText(this, "Permissions Denied!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.action_send_mail:
                DashboardFragment.sendEmail(MainActivity.this, MainActivity.file_name_surtikhamanhouse);
                return true;
            case R.id.delete_file:
                 DashboardFragment.show_delete_passord_popup(MainActivity.this, file_name_surtikhamanhouse);
                 return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //==============================================================================================
    public static void show_file_access_permission_dialog_box(Context context){
        Dialog dialog = new Dialog(context);
        //==================================================================================
        dialog.setContentView(R.layout.permission_popup_information);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        //-------------------------------------------------------------------------------
        TextView tv_permission_details;
        Button btn_cancel, btn_grant;
        tv_permission_details = (TextView) dialog.findViewById(R.id.tv_permission_details);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_grant = (Button) dialog.findViewById(R.id.btn_grant);

        tv_permission_details.setText(context.getResources().getString(R.string.file_access_permission_details));
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    try {
                        Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                        context.startActivity(intent);
                    } catch (Exception ex){
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        context.startActivity(intent);
                    }
                    dialog.dismiss();
                }
            }
        });
        //-------------------------------------------------------------------------------
        dialog.show();
    }
    //==============================================================================================
}