package com.cookiehunterrr.profilelookup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper
{
    private Context context;
    private static final String TAG = "Database";
    private static final String DATABASE_NAME = "Database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "cached_user_data";

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
        String query = String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT);",
                TABLE_NAME, COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_AVATAR, COLUMN_AVATAR_HASH);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<DBUser> getCachedUsers()
    {
        ArrayList<DBUser> users = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s",
                TABLE_NAME, COLUMN_USER_ID);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() < 1) return users;

        while (cursor.moveToNext())
        {
            users.add(new DBUser(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
        return users;
    }

    public void deleteUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM %s WHERE %s = '%s'";

        String query = String.format(queryString,
                TABLE_NAME, COLUMN_USER_ID, userId);
        db.execSQL(query);
    }

    private void updateUser(User user)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = '%s' WHERE %s = '%s'",
                TABLE_NAME, COLUMN_USERNAME, user.getName(),
                COLUMN_AVATAR, user.getAvatarLink(),
                COLUMN_AVATAR_HASH, user.getAvatarHash(),
                COLUMN_USER_ID, user.getId());
        db.execSQL(query);
    }

    public void addUser(User user)
    {
        if (isUserExists(user.getId()))
        {
            updateUser(user);
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, user.getId());
        cv.put(COLUMN_USERNAME, user.getName());
        cv.put(COLUMN_AVATAR, user.getAvatarLink());
        cv.put(COLUMN_AVATAR_HASH, user.getAvatarHash());

        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) Toast.makeText(context, "Failed adding user info", Toast.LENGTH_SHORT).show();
    }

    private boolean isUserExists(String userId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'",
                TABLE_NAME, COLUMN_USER_ID, userId);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0)
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
