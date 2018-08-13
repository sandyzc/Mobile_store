package store.mobile_kfil.freaklab.sandyz.com.mobile_store.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by santosh on 16-12-2017.
 */

public class Check_upgarde_status extends SQLiteOpenHelper {

    private Context ctx;
    SQLiteDatabase database;
    private static String Table_name="Db_check";
    private static String ID="_id";
    private static String Column_One="db_version";
    private static final int Version=1;
    private static final String Db_name="Db_check.db"
;



    public Check_upgarde_status(Context context) {
        super(context,Db_name,null,Version);
        ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String create_Sql=" CREATE TABLE IF NOT EXISTS " + Table_name + "("
                + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + Column_One + " INTEGER )";
        Log.i("creating database","database created.......");

        sqLiteDatabase.execSQL(create_Sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_name);

    }

    public void openDb(){
        if (this.database==null){
            this.database.isOpen();
        }
    }

    public void closeDb(){
        if (this.database!=null){
            this.database.close();
        }
    }

    public long insert(int version){
        ContentValues values= new ContentValues();
        values.put(Column_One,version);

        return this.database.insertOrThrow(Table_name,null,values);
    }

    public int updateDbVersion(int where,int version){
    database=this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(Column_One,version);

   int count = database.update(Table_name,values,ID+"=?",new String[]{String.valueOf(where)});

   database.close();
   return count;

    }
}
