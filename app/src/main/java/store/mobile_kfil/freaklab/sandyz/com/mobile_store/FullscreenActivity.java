package store.mobile_kfil.freaklab.sandyz.com.mobile_store;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.android.gms.tasks.OnSuccessListener;
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
import store.mobile_kfil.freaklab.sandyz.com.mobile_store.database.DatabaseOpenHelper;
import store.mobile_kfil.freaklab.sandyz.com.mobile_store.database.XlsConec;


public class FullscreenActivity extends AppCompatActivity {


    String code;
    EditText searchData;
    Button searc_Button;
    TextView version_info;
    ProgressBar progressBar;
    XlsConec dataXlsConec;
    DatabaseOpenHelper databaseOpenHelper;
    DataBase_FireBAse_Link fireBAse_link;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        Fabric.with(this, new Crashlytics());
        databaseOpenHelper= new DatabaseOpenHelper(this);
        fireBAse_link = new DataBase_FireBAse_Link(this);


        dataXlsConec=new XlsConec(this);

        progressBar = findViewById(R.id.main_screen_progressbar);


        //displaying app version
        version_info = findViewById(R.id.versi);
        String version_name = BuildConfig.VERSION_NAME;
        version_info.setText("V " + version_name);

        searchData = findViewById(R.id.query_data);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        getDbFile();

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

    private void getDbFile(){

        //check weather if its a db file(testing for now)
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference_for_db_file=firebaseDatabase.getReference("DB_FILE");

        databaseReference_for_db_file.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String FireBase_url=dataSnapshot.getValue(String.class);


                if (fireBAse_link.get_DB_FIREBASE_link()==null) {

                    fireBAse_link.save_DB_FIREBASE_LINK(FireBase_url);

                    try {
                        getDbFromFireBase(FireBase_url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else if (fireBAse_link.get_DB_FIREBASE_link().equals(FireBase_url)&&dataXlsConec.getCodes().size()<30000){
                    Toast.makeText(FullscreenActivity.this," Data is already updated ",Toast.LENGTH_LONG).show();
                }
                else{
                    fireBAse_link.updae_DB_FIREBASE_LINK(FireBase_url);
                    try {
                        getDbFromFireBase(FireBase_url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FullscreenActivity.this," Unfortunatly Couldn't Download the update Kindly Contact +919019677948 for help ",Toast.LENGTH_LONG).show();
            }
        });

    }

    //method to be used if downloading direct db file from server
    private void getDbFromFireBase(String FIREBASE_URL) throws IOException {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(FIREBASE_URL);
        final File localFile = File.createTempFile("ItemCode","db");
        mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                String size = String.valueOf(taskSnapshot.getTotalByteCount() / 1000000);
                Toast.makeText(FullscreenActivity.this, "Downloaded " + size + " MB", Toast.LENGTH_LONG).show();
                try {
                    FullscreenActivity.this.deleteDatabase("ItemCode.db");
                    databaseOpenHelper.updateDatabaseFromFireBase(localFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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



    //method to search the keyword
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




