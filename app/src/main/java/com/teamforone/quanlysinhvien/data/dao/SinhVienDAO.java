package com.teamforone.quanlysinhvien.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.teamforone.quanlysinhvien.data.db.DatabaseProvider;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;
import com.teamforone.quanlysinhvien.util.AppLogger;
import com.teamforone.quanlysinhvien.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {
    private DatabaseProvider databaseProvider;

    public SinhVienDAO(Context context) {
        databaseProvider = DatabaseProvider.getInstance(context);
    }

    // Create
    public boolean insert(SinhVien sinhVien) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.COL_MA_SV, sinhVien.getMaSV());
            values.put(Constants.COL_HO_TEN, sinhVien.getHoTen());
            values.put(Constants.COL_NGAY_SINH, sinhVien.getNgaySinh());
            values.put(Constants.COL_GIOI_TINH, sinhVien.getGioiTinh());
            values.put(Constants.COL_DIA_CHI, sinhVien.getDiaChi());
            values.put(Constants.COL_MA_LOP, sinhVien.getMaLop());

            long result = db.insert(Constants.TABLE_SINHVIEN, null, values);

            if (result != -1) {
                AppLogger.d(Constants.LOG_TAG_DAO, "Inserted: " + sinhVien);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e(Constants.LOG_TAG_DAO, "Error inserting sinh vien", e);
        }
        return false;
    }

    // Update
    public boolean update(SinhVien sinhVien) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.COL_HO_TEN, sinhVien.getHoTen());
            values.put(Constants.COL_NGAY_SINH, sinhVien.getNgaySinh());
            values.put(Constants.COL_GIOI_TINH, sinhVien.getGioiTinh());
            values.put(Constants.COL_DIA_CHI, sinhVien.getDiaChi());
            values.put(Constants.COL_MA_LOP, sinhVien.getMaLop());

            int result = db.update(Constants.TABLE_SINHVIEN, values,
                    Constants.COL_MA_SV + " = ?",
                    new String[]{sinhVien.getMaSV()});

            if (result > 0) {
                AppLogger.d(Constants.LOG_TAG_DAO, "Updated: " + sinhVien);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e(Constants.LOG_TAG_DAO, "Error updating sinh vien", e);
        }
        return false;
    }

    // Delete
    public boolean delete(String maSV) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            int result = db.delete(Constants.TABLE_SINHVIEN,
                    Constants.COL_MA_SV + " = ?",
                    new String[]{maSV});

            if (result > 0) {
                AppLogger.d(Constants.LOG_TAG_DAO, "Deleted: " + maSV);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e(Constants.LOG_TAG_DAO, "Error deleting sinh vien", e);
        }
        return false;
    }

    // Get All
    public List<SinhVien> getAll() {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = databaseProvider.getReadableDatabase();

        String query = "SELECT s.*, l." + Constants.COL_TEN_LOP +
                " FROM " + Constants.TABLE_SINHVIEN + " s " +
                "LEFT JOIN " + Constants.TABLE_LOP + " l " +
                "ON s." + Constants.COL_MA_LOP + " = l." + Constants.COL_MA_LOP +
                " ORDER BY s." + Constants.COL_MA_SV;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    list.add(extractSinhVienFromCursor(cursor));
                } while (cursor.moveToNext());
            }
            AppLogger.d(Constants.LOG_TAG_DAO, "Retrieved " + list.size() + " sinh vien");
        } catch (Exception e) {
            AppLogger.e(Constants.LOG_TAG_DAO, "Error getting all sinh vien", e);
        } finally {
            if (cursor != null) cursor.close();
        }

        return list;
    }

    // Search
    public List<SinhVien> search(String keyword) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = databaseProvider.getReadableDatabase();

        String query = "SELECT s.*, l." + Constants.COL_TEN_LOP +
                " FROM " + Constants.TABLE_SINHVIEN + " s " +
                "LEFT JOIN " + Constants.TABLE_LOP + " l " +
                "ON s." + Constants.COL_MA_LOP + " = l." + Constants.COL_MA_LOP +
                " WHERE s." + Constants.COL_MA_SV + " LIKE ? " +
                "OR s." + Constants.COL_HO_TEN + " LIKE ? " +
                "ORDER BY s." + Constants.COL_MA_SV;

        String searchPattern = "%" + keyword + "%";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{searchPattern, searchPattern});

            if (cursor.moveToFirst()) {
                do {
                    list.add(extractSinhVienFromCursor(cursor));
                } while (cursor.moveToNext());
            }
            AppLogger.d(Constants.LOG_TAG_DAO, "Search '" + keyword + "' found " + list.size() + " results");
        } catch (Exception e) {
            AppLogger.e(Constants.LOG_TAG_DAO, "Error searching sinh vien", e);
        } finally {
            if (cursor != null) cursor.close();
        }

        return list;
    }

    // Filter by Lop
    public List<SinhVien> filterByLop(String maLop) {
        List<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = databaseProvider.getReadableDatabase();

        String query = "SELECT s.*, l." + Constants.COL_TEN_LOP +
                " FROM " + Constants.TABLE_SINHVIEN + " s " +
                "LEFT JOIN " + Constants.TABLE_LOP + " l " +
                "ON s." + Constants.COL_MA_LOP + " = l." + Constants.COL_MA_LOP +
                " WHERE s." + Constants.COL_MA_LOP + " = ? " +
                "ORDER BY s." + Constants.COL_MA_SV;

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{maLop});

            if (cursor.moveToFirst()) {
                do {
                    list.add(extractSinhVienFromCursor(cursor));
                } while (cursor.moveToNext());
            }
            AppLogger.d(Constants.LOG_TAG_DAO, "Filter by " + maLop + " found " + list.size() + " results");
        } catch (Exception e) {
            AppLogger.e(Constants.LOG_TAG_DAO, "Error filtering sinh vien", e);
        } finally {
            if (cursor != null) cursor.close();
        }

        return list;
    }

    // Check if exists
    public boolean exists(String maSV) {
        SQLiteDatabase db = databaseProvider.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT 1 FROM " + Constants.TABLE_SINHVIEN +
                            " WHERE " + Constants.COL_MA_SV + " = ?",
                    new String[]{maSV});
            return cursor.getCount() > 0;
        } catch (Exception e) {
            AppLogger.e(Constants.LOG_TAG_DAO, "Error checking existence", e);
        } finally {
            if (cursor != null) cursor.close();
        }

        return false;
    }

    // Get all Lop names
    // Get all Lop names
    public List<String> getAllLopNames() {
        List<String> list = new ArrayList<>();
        list.add(Constants.FILTER_ALL);

        SQLiteDatabase db = databaseProvider.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT " + Constants.COL_MA_LOP + ", " +
                    Constants.COL_TEN_LOP + " FROM " + Constants.TABLE_LOP +
                    " ORDER BY " + Constants.COL_MA_LOP, null);

            AppLogger.d(Constants.LOG_TAG_DAO, "Cursor count: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    String maLop = cursor.getString(0);
                    String tenLop = cursor.getString(1);
                    String item = maLop + " - " + tenLop;
                    list.add(item);
                    AppLogger.d(Constants.LOG_TAG_DAO, "Added lop: " + item);
                } while (cursor.moveToNext());
            } else {
                AppLogger.w(Constants.LOG_TAG_DAO, "No lop data found in database!");
            }
        } catch (Exception e) {
            AppLogger.e(Constants.LOG_TAG_DAO, "Error getting lop names", e);
        } finally {
            if (cursor != null) cursor.close();
        }

        AppLogger.d(Constants.LOG_TAG_DAO, "Total lop items: " + list.size());
        return list;
    }

    // Helper method to extract SinhVien from Cursor
    private SinhVien extractSinhVienFromCursor(Cursor cursor) {
        SinhVien sv = new SinhVien();
        sv.setMaSV(cursor.getString(0));
        sv.setHoTen(cursor.getString(1));
        sv.setNgaySinh(cursor.getString(2));
        sv.setGioiTinh(cursor.getString(3));
        sv.setDiaChi(cursor.getString(4));
        sv.setMaLop(cursor.getString(5));
        sv.setTenLop(cursor.getString(6));
        return sv;
    }
}
