package store.mobile_kfil.freaklab.sandyz.com.mobile_store.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class XlsConec extends SQLiteOpenHelper {


    private static final String TableName = "ItemCode";
    public static final String id = "_id";
    private static final String CODE = "code";
    private static final String Description = "description";
    public static final int version = 1;
    private static final String pip_stock = "pip_stock";
    private static final String pip_stock_uom = "pip_stock_uom";
    private static final String fdy_stock = "fdy_stock";
    private static final String fdy_stock_uom = "fdy_stock_uom";
    private static final String pip_location = "pip_location";
    private static final String fdy_location = "fdy_location";
    private static final String DbNAme = "ItemCode.db";

    private SQLiteDatabase database = null;

    public XlsConec(Context context) {
        super(context, DbNAme, null, version);

    }

    public void open() {
        if (this.database == null) {
            try {
                this.database = this.getWritableDatabase();
            } catch (SQLException ignored) {

            }
        }
    }

    public void close() {
        if (this.database != null) {
            this.database.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_sql = "CREATE TABLE IF NOT EXISTS "
                + TableName + "("
                + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CODE + " TEXT,"
                + Description + " TEXT,"
                + pip_stock + " TEXT,"
                + pip_stock_uom + " TEXT,"
                + fdy_stock + " TEXT,"
                + fdy_stock_uom + " TEXT,"
                + pip_location + " TEXT,"
                + fdy_location + " TEXT)";

        sqLiteDatabase.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableName);
    }

    public int insert(String table, ContentValues values) {
        return (int) this.database.insert(table, null, values);
    }

    public ArrayList<HashMap<String, String>> getCodes() {
        ArrayList<HashMap<String, String>> codeList;

        codeList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TableName;

        database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(CODE, cursor.getString(1));
                map.put(Description, cursor.getString(2));
                map.put(pip_stock, cursor.getString(3));
                map.put(pip_stock_uom, cursor.getString(4));
                map.put(fdy_stock, cursor.getString(5));
                map.put(fdy_stock_uom, cursor.getString(6));
                map.put(pip_location, cursor.getString(7));
                map.put(fdy_location, cursor.getString(8));
                codeList.add(map);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return codeList;
    }

}





