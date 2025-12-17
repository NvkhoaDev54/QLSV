package com.teamforone.quanlysinhvien.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.teamforone.quanlysinhvien.data.db.DatabaseProvider;
import com.teamforone.quanlysinhvien.domain.model.DiemDanh;
import com.teamforone.quanlysinhvien.util.AppLogger;

import java.util.ArrayList;
import java.util.List;

public class DiemDanhDAO {
    private DatabaseProvider databaseProvider;

    public DiemDanhDAO(Context context) {
        databaseProvider = DatabaseProvider.getInstance(context);
    }

    public boolean insert(DiemDanh diemDanh) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("buoiHocId", diemDanh.getBuoiHocId());
            values.put("maSV", diemDanh.getMaSV());
            values.put("coMat", diemDanh.isCoMat() ? 1 : 0);
            values.put("ghiChu", diemDanh.getGhiChu());

            long result = db.insert("DIEM_DANH", null, values);
            if (result != -1) {
                AppLogger.d("DiemDanhDAO", "Inserted: " + diemDanh);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e("DiemDanhDAO", "Error inserting diem danh", e);
        }
        return false;
    }

    public boolean update(DiemDanh diemDanh) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("coMat", diemDanh.isCoMat() ? 1 : 0);
            values.put("ghiChu", diemDanh.getGhiChu());

            int result = db.update("DIEM_DANH", values, "id = ?",
                    new String[]{String.valueOf(diemDanh.getId())});
            if (result > 0) {
                AppLogger.d("DiemDanhDAO", "Updated: " + diemDanh);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e("DiemDanhDAO", "Error updating diem danh", e);
        }
        return false;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            int result = db.delete("DIEM_DANH", "id = ?",
                    new String[]{String.valueOf(id)});
            if (result > 0) {
                AppLogger.d("DiemDanhDAO", "Deleted diem danh: " + id);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e("DiemDanhDAO", "Error deleting diem danh", e);
        }
        return false;
    }

    public List<DiemDanh> getByBuoiHoc(int buoiHocId) {
        List<DiemDanh> list = new ArrayList<>();
        SQLiteDatabase db = databaseProvider.getReadableDatabase();

        String query = "SELECT d.*, s.hoTen " +
                "FROM DIEM_DANH d " +
                "LEFT JOIN SINHVIEN s ON d.maSV = s.maSV " +
                "WHERE d.buoiHocId = ? " +
                "ORDER BY s.hoTen";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(buoiHocId)});
            if (cursor.moveToFirst()) {
                do {
                    list.add(extractDiemDanhFromCursor(cursor));
                } while (cursor.moveToNext());
            }
            AppLogger.d("DiemDanhDAO", "Retrieved " + list.size() + " diem danh records");
        } catch (Exception e) {
            AppLogger.e("DiemDanhDAO", "Error getting diem danh", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    public boolean exists(int buoiHocId, String maSV) {
        SQLiteDatabase db = databaseProvider.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT 1 FROM DIEM_DANH WHERE buoiHocId = ? AND maSV = ?",
                    new String[]{String.valueOf(buoiHocId), maSV});
            return cursor.getCount() > 0;
        } catch (Exception e) {
            AppLogger.e("DiemDanhDAO", "Error checking existence", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    public boolean deleteByBuoiHoc(int buoiHocId) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            int result = db.delete("DIEM_DANH", "buoiHocId = ?",
                    new String[]{String.valueOf(buoiHocId)});
            AppLogger.d("DiemDanhDAO", "Deleted " + result + " records for buoi hoc: " + buoiHocId);
            return true;
        } catch (Exception e) {
            AppLogger.e("DiemDanhDAO", "Error deleting by buoi hoc", e);
        }
        return false;
    }

    private DiemDanh extractDiemDanhFromCursor(Cursor cursor) {
        DiemDanh dd = new DiemDanh();
        dd.setId(cursor.getInt(0));
        dd.setBuoiHocId(cursor.getInt(1));
        dd.setMaSV(cursor.getString(2));
        dd.setCoMat(cursor.getInt(3) == 1);
        dd.setGhiChu(cursor.getString(4));
        dd.setHoTenSV(cursor.getString(5));
        return dd;
    }
}