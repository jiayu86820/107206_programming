package org.secuso.privacyfriendlynotes.code_old;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import org.secuso.privacyfriendlynotes.R;

/**
 * Created by Robin on 11.06.2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "allthenotes";
    Context context;
    //創建資料表
    private static final String NOTES_TABLE_CREATE =
            "CREATE TABLE " + DbContract.NoteEntry.TABLE_NAME + " (" +
                    DbContract.NoteEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DbContract.NoteEntry.COLUMN_TYPE + " INTEGER , " +
                    DbContract.NoteEntry.COLUMN_NAME + " TEXT , " +
                    DbContract.NoteEntry.COLUMN_CONTENT + " TEXT , " +
                    DbContract.NoteEntry.COLUMN_CATEGORY + " INTEGER, " +
                    DbContract.NoteEntry.COLUMN_TAG + " TEXT , " +
                    DbContract.NoteEntry.COLUMN_TAG1 + " TEXT , " +
                    DbContract.NoteEntry.COLUMN_TAG2 + " TEXT , " +
                    DbContract.NoteEntry.COLUMN_TAG3 + " TEXT , " +
                    DbContract.NoteEntry.COLUMN_NOTICE + " TEXT , " +
                    DbContract.NoteEntry.COLUMN_PHOTO + " BLOB , " +
                    DbContract.NoteEntry.COLUMN_TRASH + " INTEGER NOT NULL DEFAULT 0);";

    private static final String CATEGORIES_TABLE_CREATE =
            "CREATE TABLE " + DbContract.CategoryEntry.TABLE_NAME + " (" +
                    DbContract.CategoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    DbContract.CategoryEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE);";

    private static final String NOTIFICATIONS_TABLE_CREATE =
            "CREATE TABLE " + DbContract.NotificationEntry.TABLE_NAME + " (" +
                    DbContract.NotificationEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DbContract.NotificationEntry.COLUMN_NOTE + " INTEGER NOT NULL, " +
                    DbContract.NotificationEntry.COLUMN_TIME + " INTEGER NOT NULL);";

    private  static final String SOUND_TABLE_CREATE =
            "CREATE TABLE " + DbContract.SoundEntry.TABLE_NAME + " (" +
                    DbContract.SoundEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DbContract.SoundEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                    DbContract.SoundEntry.COLUMN_TAG + " TEXT , " +
                    DbContract.SoundEntry.COLUMN_SOUND+ " TEXT) ;";

  public   DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void insertData2(String title, String tag1, String tag2, String time, String notice ){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Calender VALUES (NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, title);
        statement.bindString(2, tag1);
        statement.bindString(3, tag2);
        statement.bindString(4, time);
        statement.bindString(5, notice);
        statement.executeInsert();
    }


    public Cursor getData2(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void queryData2(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTES_TABLE_CREATE);
        db.execSQL(CATEGORIES_TABLE_CREATE);
        db.execSQL(NOTIFICATIONS_TABLE_CREATE);
        db.execSQL(SOUND_TABLE_CREATE);
        db.execSQL("INSERT INTO " + DbContract.CategoryEntry.TABLE_NAME + " (" + DbContract.CategoryEntry.COLUMN_NAME + ") VALUES ('" + context.getString(R.string.default_category) + "');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DbContract.NoteEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS" + DbContract.CategoryEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS" + DbContract.NotificationEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS" + DbContract.SoundEntry.TABLE_NAME + ";");

        onCreate(db);
    }
}
