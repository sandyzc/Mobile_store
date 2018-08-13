package store.mobile_kfil.freaklab.sandyz.com.mobile_store.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import store.mobile_kfil.freaklab.sandyz.com.mobile_store.Beans;


public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "ItemCode.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TableName = "ItemCode";
    public static final String id = "_id";
    public static final int version = 1;


    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
                search_Data.setFdy_stock(cursor.getString(4));
                search_Data.setPip_location(cursor.getString(5));
                search_Data.setFdy_location(cursor.getString(6));
                search_result.add(search_Data);
            } while (cursor.moveToNext());
        }
        database.close();
        cursor.close();

        return search_result;

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
                search_Data.setFdy_stock(cursor.getString(4));
                search_Data.setPip_location(cursor.getString(5));
                search_Data.setFdy_location(cursor.getString(6));
                search_result.add(search_Data);
            } while (cursor.moveToNext());
        }
        database.close();
        cursor.close();

        return search_result;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
