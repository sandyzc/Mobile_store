package store.mobile_kfil.freaklab.sandyz.com.mobile_store;


import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import store.mobile_kfil.freaklab.sandyz.com.mobile_store.database.XlsConec;

public class Excel extends ListActivity {

    TextView lbl;
    XlsConec controller = new XlsConec(this);
    Button btnimport;
    ListView lv;
    public static final int requestcode = 1;
    static String tableName;
    public static final String id = "_id";// 0 integer
    public static final String CODE = "code";
    public static final String Description = "description";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnimport = findViewById(R.id.btnupload);

        lbl = findViewById(R.id.txtresulttext);
        lv = getListView();
        tableName = "info";


        btnimport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("gagt/sdf");
                try {
                    startActivityForResult(fileintent, requestcode);
                } catch (ActivityNotFoundException e) {
                    lbl.setText("No activity can handle picking a file. Showing alternatives.");
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case requestcode:
                String FilePath = data.getData().getPath();
                try {
                    if (resultCode == RESULT_OK) {
                        AssetManager am = this.getAssets();
                        InputStream inStream;
                        Workbook wb = null;
                        try {
                            inStream = new FileInputStream(FilePath);
                            wb = new HSSFWorkbook(inStream);
                            inStream.close();
                        } catch (IOException e) {
                            lbl.setText("First " + e.getMessage().toString());
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


                    }
                } catch (Exception ex) {
                    lbl.setText(ex.getMessage().toString() + "Second");
                }

                ArrayList<HashMap<String, String>> myList = controller
                        .getCodes();
                if (myList.size() != 0) {
                    ListView lv = getListView();
                    ListAdapter adapter = new SimpleAdapter(Excel.this, myList,
                            R.layout.v, new String[]{CODE, Description},
                            new int[]{R.id.txtcode, R.id.txtdescprip});
                    setListAdapter(adapter);
                }
        }
    }

    public void onLineDbUpdate(String file) {

        try {
            AssetManager am = this.getAssets();
            InputStream inputStream;
            Workbook workbook = null;
            try {
                inputStream = new FileInputStream(file);
                workbook = new HSSFWorkbook(inputStream);
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            XlsConec dbAdapter = new XlsConec(this);
            Sheet sheet1 = null;
            if (workbook != null) {
                sheet1 = workbook.getSheetAt(0);
            }

            if (sheet1 == null) {
                return;
            }
            dbAdapter.open();
            dbAdapter.open();
            Excel2SQLiteHelper.insertExcelToSqlite(dbAdapter, sheet1);

            dbAdapter.close();
        } catch (Exception ex) {

        }
    }
}