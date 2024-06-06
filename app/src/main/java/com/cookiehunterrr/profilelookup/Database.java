package com.cookiehunterrr.profilelookup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper
{
    private Context context;
    private static final String TAG = "Database";
    private static final String DATABASE_NAME = "Database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_1 = "cached_user_data";
    private static final String TABLE_NAME_2 = "cached_user_avatars";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "user_username";
    private static final String COLUMN_AVATAR = "user_avatar";
    private static final String COLUMN_AVATAR_HASH = "user_avatar_hash";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT);",
                TABLE_NAME_1, COLUMN_USER_ID, COLUMN_USERNAME);
        db.execSQL(query);

        query = String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT, %s BLOB);",
                TABLE_NAME_2, COLUMN_USER_ID, COLUMN_AVATAR_HASH, COLUMN_AVATAR);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }

    public void addUser(String userId, String userName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'",
                TABLE_NAME_1, COLUMN_USER_ID, userId);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0)
        {
            // TODO: Обновить информацию о пользователе
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, userId);
        cv.put(COLUMN_USERNAME, userName);

        long result = db.insert(TABLE_NAME_1, null, cv);

        if (result == -1) Toast.makeText(context, "Failed adding user info", Toast.LENGTH_SHORT).show();
    }

    public void addUserAvatarData(String userId, String userAvatarHash, byte[] userAvatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'",
                TABLE_NAME_2, COLUMN_USER_ID, userId);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0)
        {
            // TODO: Обновить аватар
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, userId);
        cv.put(COLUMN_AVATAR_HASH, userAvatarHash);
        cv.put(COLUMN_AVATAR, userAvatar);

        long result = db.insert(TABLE_NAME_2, null, cv);

        if (result == -1) Toast.makeText(context, "Failed adding user avatar", Toast.LENGTH_SHORT).show();
    }
}
