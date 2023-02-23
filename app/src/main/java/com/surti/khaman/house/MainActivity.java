package com.surti.khaman.house;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.surti.khaman.house.databinding.ActivityMainBinding;
import com.surti.khaman.house.ui.dashboard.DashboardFragment;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements PermissionUtil.PermissionsCallBack {

    public static String firebase_storage_bill_file_name = "Firebase_Bills_File";
    public static String firebase_storage_expenses_file_name = "Expenses_File";
    public static String provider_name = "com.surti.khaman.house.provider";
    public static String download_skh_directory = "skh";
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



        // Checking old data freshly want to write or not
        if(DashboardFragment.get_SharedPreference_Old_data_bill_file_already_written_or_not(MainActivity.this) == 0) {
            // If directory available,than read data , save that than delete file
            if (check_skh_directory_previously_available_or_not()) {
                // Checking Uri already set or not
                String uri_skh_bill_file_string = DashboardFragment.get_uri_skh_bill_file_sharedpreference(MainActivity.this);

                // If Uri not set than , ask for permission
                if(uri_skh_bill_file_string.matches("no")){
                    show_download_directory_ask_user_to_give_permission(MainActivity.this);
                }else{
                    // If already permission is given than save that Uri
                    set_Old_skh_bill_file_Data(uri_skh_bill_file_string, MainActivity.this);
                }
            }else {
                DashboardFragment.set_uri_skh_bill_file_sharedpreference("no_need", MainActivity.this);
            }
        }



    }

    // Read Old Data
    // Check skh directory available or not, if available than grant the permission, read the data and delete old file, and create new file
//=============================================================================================================

    // Check Directory is present or not
    //—-------------------------------------------------------------------------------------------------------------------------------------
    public static boolean check_skh_directory_previously_available_or_not(){
        File download_directory_file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File dir_skh = new File(download_directory_file, MainActivity.download_skh_directory);
        if(!dir_skh.exists()) {
            return false;
        }else {
            return true;
        }
    }
    //—-------------------------------------------------------------------------------------------------------------------------------------


    // Open Folder for Allow Permission
    //—-------------------------------------------------------------------------------------------------------------------------------------
    public void openSomeActivityForResult() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        openFolderToAllowPermissionActivityResultLauncher.launch(i);
    }
   //—---------------------------------------------------------------------------------------------------------------------------------------

    // OnActivity Result to get file data and file Uri
    //—------------------------------------------------------------------------------------------------------------------------------------
    ActivityResultLauncher<Intent> openFolderToAllowPermissionActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri uri = data.getData();

                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        DocumentFile documentFile = DocumentFile.fromTreeUri(MainActivity.this, uri);

                        DocumentFile bill_file_uri = null, expenses_file_uri = null;
                        for(DocumentFile file : documentFile.listFiles()){
                            // Checking skh bill file is available or not
                            //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=
                            if(file.getName().matches(MainActivity.file_name_surtikhamanhouse)){
                                DashboardFragment.set_uri_skh_bill_file_sharedpreference(String.valueOf(file.getUri()), MainActivity.this);
                                set_Old_skh_bill_file_Data(String.valueOf(file.getUri()), MainActivity.this);
                                bill_file_uri = file;

                                Log.i("test_response", "BILL FILE FOUND");
                            }
                            //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=


                            //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=
                            if(file.getName().matches(MainActivity.file_name_skh_expenses)){
                                DashboardFragment.set_uri_skh_expenses_file_sharedpreference(String.valueOf(file.getUri()), MainActivity.this);
                                set_Old_skh_expenses_file_Data(String.valueOf(file.getUri()), MainActivity.this);
                                expenses_file_uri = file;

                                Log.i("test_response", "EXPENSES FILE FOUND");
                            }
                            //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-==-=-=-=-=-=

                            Log.i("test_response", "File Name : "+file.getName());

                            if(bill_file_uri != null) {
                                try {
                                    bill_file_uri.delete();
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }

                            if(expenses_file_uri != null) {
                                try {
                                    expenses_file_uri.delete();
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }

                        }
                    }
                }
            });
     //—------------------------------------------------------------------------------------------------------------------------------------


    // Read old File text and return file content
    //—------------------------------------------------------------------------------------------------------------------------------------
    private static String readTextFromUri(Uri uri, Context context) throws IOException {
        String old_file_content = "";
        PdfReader reader = null;

        try {
            reader = new PdfReader(context.getContentResolver().openInputStream(uri), DashboardFragment.get_password_sharedpreference(context).getBytes());

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                // pageNumber = 1
                String textFromPage = PdfTextExtractor.getTextFromPage(reader, i);
                old_file_content = old_file_content + "\n" + textFromPage;
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("test_response", "FILE CONTENT : "+old_file_content);

        return old_file_content;
    }
//—------------------------------------------------------------------------------------------------------------------------------------


    // set old bill file data on sharedpreference
    //—------------------------------------------------------------------------------------------------------------------------------------
    public static void set_Old_skh_bill_file_Data(String uri_skh_bill_file_string,Context context){
        try{
            if(DashboardFragment.get_SharedPreference_Old_data_bill_file_already_written_or_not(context) == 0) {
                DashboardFragment.set_SharedPreference_Old_data_bill_file_already_written_or_not(1, context);
                String latest_old_bill_file_data = readTextFromUri(Uri.parse(uri_skh_bill_file_string), context);
                String previous_file_data = "";
                if(!latest_old_bill_file_data.isEmpty()) {
                    previous_file_data =
                            "\n======OLD_DATA_START====OLD_DATA_START====OLD_DATA_START======\n"
                                    + latest_old_bill_file_data
                                    + "\n=======OLD_DATA_END====OLD_DATA_END====OLD_DATA_END======\n";
                }
                DashboardFragment.set_SharedPreference_Old_data_bill_file_String(previous_file_data, context);
            }

        }catch (Exception e){
            e.getMessage();
        }
    }
    //—------------------------------------------------------------------------------------------------------------------------------------


    // set old expenses file data on sharedpreference
    //—------------------------------------------------------------------------------------------------------------------------------------
    public static void set_Old_skh_expenses_file_Data(String uri_skh_expenses_file_string,Context context){
        try{
            if(DashboardFragment.get_SharedPreference_Old_data_expenses_file_already_written_or_not(context) == 0) {
                DashboardFragment.set_SharedPreference_Old_data_expenses_file_already_written_or_not(1, context);
                String latest_old_expenses_file_data = readTextFromUri(Uri.parse(uri_skh_expenses_file_string), context);
                String previous_file_data = "";
                if(!latest_old_expenses_file_data.isEmpty()) {
                    previous_file_data =
                            "\n======OLD_DATA_START====OLD_DATA_START====OLD_DATA_START======\n"
                                    + latest_old_expenses_file_data
                                    + "\n=======OLD_DATA_END====OLD_DATA_END====OLD_DATA_END======\n";
                }
                DashboardFragment.set_SharedPreference_Old_data_expenses_file_String(previous_file_data, context);
            }

        }catch (Exception e){
            e.getMessage();
        }
    }
    //—------------------------------------------------------------------------------------------------------------------------------------



//=============================================================================================================


//=============================================================================================================
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

    //=============================================================================================================

    //==============================================================================================
    public void show_download_directory_ask_user_to_give_permission(Context context){
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

        tv_permission_details.setText(context.getResources().getString(R.string.folder_access_permission_details));
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult();
                dialog.dismiss();
            }
        });
        //-------------------------------------------------------------------------------
        dialog.show();
    }
    //==============================================================================================
}