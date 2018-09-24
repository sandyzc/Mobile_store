package store.mobile_kfil.freaklab.sandyz.com.mobile_store.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import store.mobile_kfil.freaklab.sandyz.com.mobile_store.Beans;


public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static String DATABASE_NAME = "ItemCode.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TableName = "ItemCode";
    public static final String id = "_id";
    public static final int version = 1;
    private static String DB_PATH = "";
    Context myContext;
    int data;

    public DatabaseOpenHelper(Context context, String storageDirectory) {
        super(context, DATABASE_NAME, storageDirectory,null, DATABASE_VERSION);
        this.myContext = context;
    }

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext=context;
    }


    public ArrayList<Beans> mouldLine(String code ) {

        String Description = "description";
        code= "%"+code+"%";
        String wheer= Description+" LIKE ?";

        String[] whereArgs = new String[]{code};

        ArrayList<Beans> search_result = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(TableName,null,wheer,whereArgs,null,null,null);

        if (cursor.moveToFirst()) {

            do {
                Beans search_Data = new Beans();
                search_Data.setCode(cursor.getString(1));
                search_Data.setDescp(cursor.getString(2));
                search_Data.setPip_stock(cursor.getString(3));
              //  search_Data.setPip_stock_uom(cursor.getString(4));
                search_Data.setFdy_stock(cursor.getString(5));
             //   search_Data.setFdy_stock_uom(cursor.getString(6));
                search_Data.setPip_location(cursor.getString(7));
                search_Data.setFdy_location(cursor.getString(8));
                search_result.add(search_Data);
            } while (cursor.moveToNext());
        }
        database.close();
        cursor.close();
        data=search_result.size();

        return search_result;

    }

    public int dataLength(){

        return data;
    }




    public ArrayList<Beans> codeSearch(String code ) {

        String Description = "code";
        code= "%"+code+"%";
        String wheer= Description+" LIKE ?";

        String[] whereArgs = new String[]{code};

        ArrayList<Beans> search_result = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(TableName,null,wheer,whereArgs,null,null,null);

        if (cursor.moveToFirst()) {


            do {
                Beans search_Data = new Beans();
                search_Data.setCode(cursor.getString(1));
                search_Data.setDescp(cursor.getString(2));
                search_Data.setPip_stock(cursor.getString(3));
                search_Data.setPip_stock_uom(cursor.getString(4));
                search_Data.setFdy_stock(cursor.getString(5));
                search_Data.setFdy_stock_uom(cursor.getString(6));
                search_Data.setPip_location(cursor.getString(7));
                search_Data.setFdy_location(cursor.getString(8));
                search_result.add(search_Data);
            } while (cursor.moveToNext());
        }
        database.close();
        cursor.close();

        return search_result;

    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }


    public void updateDatabaseFromFireBase(File dbFile) throws IOException {
        DATABASE_NAME="ItemCode.db";
        DB_PATH="/data/data/store.mobile_kfil.freaklab.sandyz.com.mobile_store/databases/";

        //downloaded db
        InputStream inputStream=new FileInputStream(dbFile);

        String outputFileName=DB_PATH+DATABASE_NAME;

        OutputStream outputStream= new FileOutputStream(outputFileName);

        //transfer data
        byte[] buBytes=new byte[1024];
        int length;
        while ((length=inputStream.read(buBytes))>0){
            outputStream.write(buBytes,0,length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        Toast.makeText(myContext,"data updated",Toast.LENGTH_SHORT).show();

    }

}
