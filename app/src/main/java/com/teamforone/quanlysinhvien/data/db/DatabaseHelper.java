package com.teamforone.quanlysinhvien.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper instance;
    private final Context context;
    private final String databaseName;

    // Phiên bản DB, dùng để nâng cấp nếu cần
    private static final int DATABASE_VERSION = 3;

    private DatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
        this.databaseName = databaseName;
    }

    // Singleton
    public static synchronized DatabaseHelper getInstance(Context context, String databaseName) {
        if (instance == null) {
            instance = new DatabaseHelper(context, databaseName);
        }
        return instance;
    }

    /**
     * Copy DB từ assets nếu chưa có trong internal storage
     */
    public void prepareDatabase() {
        File dbFile = context.getDatabasePath(databaseName);
        if (dbFile.exists()) {
            Log.d(TAG, "Database already exists: " + dbFile.getPath());
            return; // DB đã có
        }

        try {
            // Tạo thư mục nếu chưa tồn tại
            File parent = dbFile.getParentFile();
            if (!parent.exists()) parent.mkdirs();

            // Copy DB từ assets/databases/
            try (InputStream is = context.getAssets().open("databases/" + databaseName);
                 OutputStream os = new FileOutputStream(dbFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                Log.d(TAG, "Database copied from assets successfully.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error copying database from assets", e);
            throw new RuntimeException("Failed to copy database from assets", e);
        }
    }

    /**
     * Mở database đọc/ghi
     */
    public SQLiteDatabase openDatabase() {
        prepareDatabase();
        File dbFile = context.getDatabasePath(databaseName);
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không tạo DB mới vì chúng ta copy từ assets
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp nếu cần
        db.execSQL("DROP TABLE IF EXISTS USERS");
        onCreate(db);
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        prepareDatabase();
        return super.getReadableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        prepareDatabase();
        return super.getWritableDatabase();
    }
}