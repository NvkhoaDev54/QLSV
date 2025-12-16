package com.teamforone.quanlysinhvien.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.teamforone.quanlysinhvien.util.AppLogger;
import com.teamforone.quanlysinhvien.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context.getApplicationContext();
    }

    /**
     * Copy DB từ assets nếu chưa tồn tại
     */
    public void prepareDatabase() {
        File dbFile = context.getDatabasePath(DatabaseProvider.getDbName());
        if (dbFile.exists()) return;

        dbFile.getParentFile().mkdirs();

        try (
                InputStream is = context.getAssets()
                        .open(DatabaseProvider.getAssetDbPath());
                OutputStream os = new FileOutputStream(dbFile)
        ) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException("Copy database failed", e);
        }
    }

    /**
     * Mở DB (đảm bảo DB đã được copy)
     */
    public SQLiteDatabase openDatabase() {
        prepareDatabase();
        return SQLiteDatabase.openDatabase(
                context.getDatabasePath(DatabaseProvider.getDbName()).getPath(),
                null,
                SQLiteDatabase.OPEN_READWRITE
        );
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AppLogger.d(Constants.LOG_TAG_DB, "Creating database tables...");

        db.execSQL("PRAGMA foreign_keys = ON");

        // Create LOP table
        String createLopTable = "CREATE TABLE " + Constants.TABLE_LOP + " (" +
                Constants.COL_MA_LOP + " TEXT PRIMARY KEY," +
                Constants.COL_TEN_LOP + " TEXT NOT NULL," +
                Constants.COL_KHOA + " TEXT NOT NULL)";
        db.execSQL(createLopTable);
        AppLogger.d(Constants.LOG_TAG_DB, "Created LOP table");

        // Create SINHVIEN table
        String createSinhVienTable = "CREATE TABLE " + Constants.TABLE_SINHVIEN + " (" +
                Constants.COL_MA_SV + " TEXT PRIMARY KEY," +
                Constants.COL_HO_TEN + " TEXT NOT NULL," +
                Constants.COL_NGAY_SINH + " TEXT," +
                Constants.COL_GIOI_TINH + " TEXT," +
                Constants.COL_DIA_CHI + " TEXT," +
                Constants.COL_MA_LOP + " TEXT," +
                "FOREIGN KEY (" + Constants.COL_MA_LOP + ") REFERENCES " +
                Constants.TABLE_LOP + "(" + Constants.COL_MA_LOP + "))";
        db.execSQL(createSinhVienTable);
        AppLogger.d(Constants.LOG_TAG_DB, "Created SINHVIEN table");

        // Insert sample data
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        AppLogger.d(Constants.LOG_TAG_DB, "Inserting sample data...");

        db.execSQL("INSERT INTO " + Constants.TABLE_LOP + " VALUES ('CNTT01', 'Công nghệ thông tin 1', 'CNTT')");
        db.execSQL("INSERT INTO " + Constants.TABLE_LOP + " VALUES ('CNTT02', 'Công nghệ thông tin 2', 'CNTT')");
        db.execSQL("INSERT INTO " + Constants.TABLE_LOP + " VALUES ('KTPM01', 'Kỹ thuật phần mềm 1', 'CNTT')");
        db.execSQL("INSERT INTO " + Constants.TABLE_LOP + " VALUES ('KHMT01', 'Khoa học máy tính 1', 'CNTT')");

        AppLogger.d(Constants.LOG_TAG_DB, "Sample data inserted successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AppLogger.d(Constants.LOG_TAG_DB, "Upgrading database from version " + oldVersion + " to " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SINHVIEN);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_LOP);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }
}
