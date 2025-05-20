package com.androchef.bai1_song;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 2;

    // Table name
    public static final String TABLE_SONGS = "songs";

    // Column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SINGER = "singer";
    public static final String COLUMN_TIME = "time";

    // Create table SQL query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_SONGS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_SINGER + " TEXT,"
                    + COLUMN_TIME + " REAL"
                    + ")";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Sample data as shown in the image
        String[] sampleSongs = {
                "INSERT INTO " + TABLE_SONGS + " VALUES (1, 'Bông hồng thủy tinh', 'Bằng Kiều', 4.18);",
                "INSERT INTO " + TABLE_SONGS + " VALUES (2, 'Bức Tường', 'Mỹ Linh', 4.11);",
                "INSERT INTO " + TABLE_SONGS + " VALUES (3, 'Hà Nội mùa thu', 'Quang Dũng', 3.51);",
                "INSERT INTO " + TABLE_SONGS + " VALUES (4, 'Ba tôi', 'Tùng Dương', 3.51);",
                "INSERT INTO " + TABLE_SONGS + " VALUES (5, 'Gót hồng', 'Quang Dũng', 4.12);",
                "INSERT INTO " + TABLE_SONGS + " VALUES (6, 'Đêm đông', 'Bằng Kiều', 3.27);"
        };

        for (String query : sampleSongs) {
            db.execSQL(query);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //thuc thi tao bang
        sqLiteDatabase.execSQL(CREATE_TABLE);

        // Insert 6 sample records
        insertSampleData(sqLiteDatabase);
    }

    public void insertSong(Song song){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, song.getName());
        contentValues.put(COLUMN_SINGER, song.getSinger());
        contentValues.put(COLUMN_TIME, song.getTime());
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = db.insert(TABLE_SONGS, null, contentValues);
        db.close();
    }

    @SuppressLint("Range")
    public List<Song> getAllSongOrder() {
        List<Song> songs = new ArrayList<>();
//        String selectQuery = "SELECT * FROM " + TABLE_SONGS + " ORDER BY " + COLUMN_TIME + " ASC";
        String selectQuery = "SELECT * FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                song.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                song.setSinger(cursor.getString(cursor.getColumnIndex(COLUMN_SINGER)));
                song.setTime(cursor.getDouble(cursor.getColumnIndex(COLUMN_TIME)));

                songs.add(song);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return songs;
    }

    public void deleteSong(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String selectQuery = "DELETE * FROM SONGS WHERE " + COLUMN_ID + " = " + String.valueOf(id);
//        db.execSQL(selectQuery);
        db.delete(TABLE_SONGS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int updateSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, song.getName());
        contentValues.put(COLUMN_SINGER, song.getSinger());
        contentValues.put(COLUMN_TIME, song.getTime());
        return db.update(TABLE_SONGS, contentValues, COLUMN_ID + " = ?",
                new String[]{String.valueOf(song.getId())});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);

        onCreate(sqLiteDatabase);
    }
}
