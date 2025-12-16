package com.teamforone.quanlysinhvien.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import com.teamforone.quanlysinhvien.BuildConfig;

import java.nio.charset.StandardCharsets;

public final class DatabaseProvider {

    private DatabaseHelper databaseHelper;
    private static DatabaseProvider instance;

    private DatabaseProvider() {}

    private DatabaseProvider(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public static synchronized DatabaseProvider getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseProvider(context.getApplicationContext());
        }
        return instance;
    }

    public SQLiteDatabase getReadableDatabase() {
        return databaseHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return databaseHelper.getWritableDatabase();
    }

    public void close() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    /**
     * Lấy path file DB trong assets (ví dụ: "databases/QLSV.db")
     */
    public static String getAssetDbPath() {
        return new String(
                Base64.decode(BuildConfig.DB_NAME_ENCODED, Base64.NO_WRAP),
                StandardCharsets.UTF_8
        );
    }

    /**
     * Lấy tên file DB (ví dụ: "QLSV.db")
     */
    public static String getDbName() {
        String fullPath = getAssetDbPath();
        return fullPath.substring(fullPath.lastIndexOf('/') + 1);
    }
}
