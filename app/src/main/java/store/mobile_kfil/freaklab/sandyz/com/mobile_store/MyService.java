package store.mobile_kfil.freaklab.sandyz.com.mobile_store;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

import store.mobile_kfil.freaklab.sandyz.com.mobile_store.database.XlsConec;

public class MyService extends Service {

    private final IBinder locIBinder = new MyLocalBinder();
    private boolean isBound;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return locIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Application Closed....!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void insertexcelData(final String FilePath) {
        final Thread thread = new Thread() {

            Context mycontext = getApplicationContext();

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



    public class MyLocalBinder extends Binder{
        MyService getMyService(){

            return MyService.this;
        }
    }
}
