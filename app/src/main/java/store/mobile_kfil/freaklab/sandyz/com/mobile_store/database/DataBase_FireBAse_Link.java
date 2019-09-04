package store.mobile_kfil.freaklab.sandyz.com.mobile_store.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase_FireBAse_Link extends SQLiteOpenHelper {

    Context myContext;

    private static final String DB_NAME="FIREBASE_LINK.db";
    private static final String Table_NAME="FIREBASE_LINK";
    private static final String id = "_id";
    private static final int Version=1;
    private static final String EXCEL_link = "link";
    private static final String Db_Link="Db_link";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase database;


    public DataBase_FireBAse_Link( Context myContext) {
        super(myContext,DB_NAME,null,DATABASE_VERSION);
        this.myContext = myContext;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                Table_NAME+
                "("+
                id +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Db_Link +" TEXT)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_NAME);


    }


    public String get_DB_FIREBASE_link(){
        database=this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + Table_NAME;
        Cursor cursor=database.rawQuery(selectQuery,null);

        String result="";
        if (cursor.moveToFirst()) {
            do {
                result = cursor.getString(1);
            }while (cursor.moveToNext());

        }
        // cursor.close();

        return result;
    }



    public void save_DB_FIREBASE_LINK(String LINK){
        database=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Db_Link,LINK);

        this.database.insert(Table_NAME,null,values);

    }

    public void updae_DB_FIREBASE_LINK(String LINK){

        database=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Db_Link,LINK);

        database.update(Table_NAME,values,"_id = ?",new String[]{String.valueOf(1)});

    }


    }

