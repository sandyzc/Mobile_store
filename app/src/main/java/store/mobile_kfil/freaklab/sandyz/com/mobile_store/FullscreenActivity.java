package store.mobile_kfil.freaklab.sandyz.com.mobile_store;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import store.mobile_kfil.freaklab.sandyz.com.mobile_store.database.XlsConec;


public class FullscreenActivity extends AppCompatActivity {


    String code;
    EditText searchData;
    Button searc_Button;
    TextView version_info;
    ProgressBar progressBar;
    private  DatabaseReference database;
    private static final String FIREBASE_URL=  "https://firebasestorage.googleapis.com/v0/b/mobilestore-e1bf4.appspot.com/o/data.xls?alt=media&token=c76ab003-403f-4c4f-9f4c-208141562e85";


    private static final String TAG = "Main Screen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        progressBar=findViewById(R.id.main_screen_progressbar);

        Thread_try mythread = new Thread_try();
        mythread.run();
//        database = FirebaseDatabase.getInstance().getReference("date");
//
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String value=dataSnapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });





        //displaying app version
        version_info = findViewById(R.id.versi);
        String version_name = BuildConfig.VERSION_NAME;
        version_info.setText("V " + version_name);

        searchData = findViewById(R.id.query_data);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        searc_Button = findViewById(R.id.searchButton);
        searc_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchData.getText().length() <= 0) {
                    searchData.setError("Enter KeyWOrd");
                } else {

                    Intent letsSearch = new Intent(FullscreenActivity.this, SearchButton.class);
                    Bundle data = new Bundle();
                    code = searchData.getText().toString();
                    data.putString("Code", code);
                    letsSearch.putExtras(data);
                    startActivity(letsSearch);
                }
            }
        });
        searc_Button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent upload_data = new Intent(FullscreenActivity.this, Excel.class);
                startActivity(upload_data);
                return false;
            }
        });


    }

    private void insertexcelData(String FilePath) {


        try {
            AssetManager am = this.getAssets();
            InputStream inStream;
            Workbook wb = null;
            try {
                inStream = new FileInputStream(FilePath);
                wb = new HSSFWorkbook(inStream);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            XlsConec dbAdapter = new XlsConec(this);
            Sheet sheet1 = null;
            if (wb != null) {
                sheet1 = wb.getSheetAt(0);
            }

            if (sheet1 == null) {
                return;
            }

            dbAdapter.open();
            Excel2SQLiteHelper.insertExcelToSqlite(dbAdapter, sheet1);


        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }




    private class Thread_try extends Thread{

        @Override
        public void run() {
            super.run();
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(FIREBASE_URL);
            try {
                final File localFile = File.createTempFile("DATA", ".xls");
                mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        String size = String.valueOf(taskSnapshot.getBytesTransferred()/1024);
                        insertexcelData(localFile.getPath());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(FullscreenActivity.this,"Data updated ",Toast.LENGTH_LONG).show();

        }
    }

}




