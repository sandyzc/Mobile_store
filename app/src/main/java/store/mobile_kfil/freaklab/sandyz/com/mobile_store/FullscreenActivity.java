package store.mobile_kfil.freaklab.sandyz.com.mobile_store;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;
import store.mobile_kfil.freaklab.sandyz.com.mobile_store.database.DataBase_FireBAse_Link;
import store.mobile_kfil.freaklab.sandyz.com.mobile_store.database.XlsConec;


public class FullscreenActivity extends AppCompatActivity {


    String code;
    EditText searchData;
    Button searc_Button;
    TextView version_info;
    ProgressBar progressBar;
    XlsConec dataXlsConec;
    MyService uploadService;
    private boolean isBound;




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        Fabric.with(this, new Crashlytics());



        dataXlsConec=new XlsConec(this);

        progressBar = findViewById(R.id.main_screen_progressbar);

        //displaying app version
        version_info = findViewById(R.id.versi);
        String version_name = BuildConfig.VERSION_NAME;
        version_info.setText("V " + version_name);

        searchData = findViewById(R.id.query_data);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        final DataBase_FireBAse_Link fireBAse_link = new DataBase_FireBAse_Link(this);


        FirebaseDatabase myFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myDatabaseReference = myFirebaseDatabase.getReference("DB_LINK");
        //get data from firebasae dataBase for the link of the excel file
        myDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String FIREBASE_URL = dataSnapshot.getValue(String.class);
                //getting the link from db
                String link_in_db = fireBAse_link.get_FIREBASE_link();
                //check weather launching app for firat time ...if yes save the db link into the DataBAse so that no duplicates is saved
                if (fireBAse_link.get_FIREBASE_link() == null) {
                    fireBAse_link.save_FIREBASE_LINK(FIREBASE_URL);

                    try {
                        getDataFromFireBase(FIREBASE_URL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                //check weather the file is already downloaded if yes user will know
                else if (link_in_db.equals(FIREBASE_URL)) {
                    Toast.makeText(FullscreenActivity.this, " DATA IS UPDATED", Toast.LENGTH_LONG).show();
                }
                //if new update is available its link is updated in DB
                else {
                    fireBAse_link.updae_FIREBASE_LINK(FIREBASE_URL);
                    dataXlsConec.deleteData();
                    try {
                        getDataFromFireBase(FIREBASE_URL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchData.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    doMagic();
                }
                return handled;
            }
        });

        searc_Button = findViewById(R.id.searchButton);
        searc_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doMagic();
            }
        });
    }

    private void insertexcelData(final String FilePath) {
         final Thread thread = new Thread() {

            Context mycontext = FullscreenActivity.this;

            @Override
            public void run() {
                super.run();

                try {
                    AssetManager am = mycontext.getAssets();
                    InputStream inStream;
                    Workbook wb = null;
                    try {
                        inStream = new FileInputStream(FilePath);
                        wb = new HSSFWorkbook(inStream);
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    XlsConec dbAdapter = new XlsConec(mycontext);
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

        };

        thread.start();
    }


    private void getDataFromFireBase(String FIREBASE_URL) throws IOException {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(FIREBASE_URL);
        final File localFile = File.createTempFile("data","xls");
        mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                String size = String.valueOf(taskSnapshot.getTotalByteCount() / 1000000);
                Toast.makeText(FullscreenActivity.this, "Downloaded " + size + " MB", Toast.LENGTH_LONG).show();
                uploadService.insertexcelData(localFile.getPath());
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int currentprogress = (int) progress;
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(currentprogress);

                if (currentprogress==100){
                    progressBar.setVisibility(View.GONE);
                }

            }
        });


    }
    // we dont want to finish the activity we instead put it into pause
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void bindToService(){
            Intent bindIntent= new Intent(this,MyService.class);
            startService(bindIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        bindToService();
    }

    private void doMagic() {

        if (searchData.getText().length() <= 0) {
            searchData.setError("Enter KeyWOrd");
        }
        else if (dataXlsConec.getCodes().size()<1){
            Toast.makeText(this,"Please wait data to upload",Toast.LENGTH_SHORT).show();
        }
        else {

            Intent letsSearch = new Intent(FullscreenActivity.this, SearchButton.class);
            Bundle data = new Bundle();
            code = searchData.getText().toString();
            data.putString("Code", code);
            letsSearch.putExtras(data);
            startActivity(letsSearch);
        }

    }

}




