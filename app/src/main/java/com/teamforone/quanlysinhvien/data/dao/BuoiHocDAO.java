package com.teamforone.quanlysinhvien.data.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.teamforone.quanlysinhvien.data.db.DatabaseProvider;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;
import com.teamforone.quanlysinhvien.util.AppLogger;

import java.util.ArrayList;
import java.util.List;

public class BuoiHocDAO {
    private DatabaseProvider databaseProvider;

    public BuoiHocDAO(Context context) {
        databaseProvider = DatabaseProvider.getInstance(context);
    }

    public boolean insert(BuoiHoc buoiHoc) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("maMonHoc", buoiHoc.getMaMonHoc());
            values.put("maGV", buoiHoc.getMaGV());
            values.put("maLop", buoiHoc.getMaLop());
            values.put("ngayHoc", buoiHoc.getNgayHoc());
            values.put("tietBatDau", buoiHoc.getTietBatDau());
            values.put("tietKetThuc", buoiHoc.getTietKetThuc());
            values.put("ghiChu", buoiHoc.getGhiChu());

            long result = db.insert("BUOI_HOC", null, values);
            if (result != -1) {
                AppLogger.d("BuoiHocDAO", "Inserted: " + buoiHoc);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error inserting buoi hoc", e);
        }
        return false;
    }

    public boolean update(BuoiHoc buoiHoc) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("maMonHoc", buoiHoc.getMaMonHoc());
            values.put("maGV", buoiHoc.getMaGV());
            values.put("maLop", buoiHoc.getMaLop());
            values.put("ngayHoc", buoiHoc.getNgayHoc());
            values.put("tietBatDau", buoiHoc.getTietBatDau());
            values.put("tietKetThuc", buoiHoc.getTietKetThuc());
            values.put("ghiChu", buoiHoc.getGhiChu());

            int result = db.update("BUOI_HOC", values, "id = ?",
                    new String[]{String.valueOf(buoiHoc.getId())});
            if (result > 0) {
                AppLogger.d("BuoiHocDAO", "Updated: " + buoiHoc);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error updating buoi hoc", e);
        }
        return false;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            int result = db.delete("BUOI_HOC", "id = ?",
                    new String[]{String.valueOf(id)});
            if (result > 0) {
                AppLogger.d("BuoiHocDAO", "Deleted buoi hoc: " + id);
                return true;
            }
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error deleting buoi hoc", e);
        }
        return false;
    }

    public List<BuoiHoc> getAll() {
        List<BuoiHoc> list = new ArrayList<>();
        SQLiteDatabase db = databaseProvider.getReadableDatabase();

        String query = "SELECT b.*, m.tenMonHoc, g.hoTen as hoTenGV, l.tenLop " +
                "FROM BUOI_HOC b " +
                "LEFT JOIN MONHOC m ON b.maMonHoc = m.maMonHoc " +
                "LEFT JOIN GIANGVIEN g ON b.maGV = g.maGV " +
                "LEFT JOIN LOP l ON b.maLop = l.maLop " +
                "ORDER BY b.ngayHoc DESC, b.tietBatDau";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    list.add(extractBuoiHocFromCursor(cursor));
                } while (cursor.moveToNext());
            }
            AppLogger.d("BuoiHocDAO", "Retrieved " + list.size() + " buoi hoc");
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error getting all buoi hoc", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    public List<BuoiHoc> getByLop(String maLop) {
        List<BuoiHoc> list = new ArrayList<>();
        SQLiteDatabase db = databaseProvider.getReadableDatabase();

        String query = "SELECT b.*, m.tenMonHoc, g.hoTen as hoTenGV, l.tenLop " +
                "FROM BUOI_HOC b " +
                "LEFT JOIN MONHOC m ON b.maMonHoc = m.maMonHoc " +
                "LEFT JOIN GIANGVIEN g ON b.maGV = g.maGV " +
                "LEFT JOIN LOP l ON b.maLop = l.maLop " +
                "WHERE b.maLop = ? " +
                "ORDER BY b.ngayHoc DESC, b.tietBatDau";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{maLop});
            if (cursor.moveToFirst()) {
                do {
                    list.add(extractBuoiHocFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error getting buoi hoc by lop", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    public BuoiHoc getById(int id) {
        SQLiteDatabase db = databaseProvider.getReadableDatabase();
        String query = "SELECT b.*, m.tenMonHoc, g.hoTen as hoTenGV, l.tenLop " +
                "FROM BUOI_HOC b " +
                "LEFT JOIN MONHOC m ON b.maMonHoc = m.maMonHoc " +
                "LEFT JOIN GIANGVIEN g ON b.maGV = g.maGV " +
                "LEFT JOIN LOP l ON b.maLop = l.maLop " +
                "WHERE b.id = ?";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                return extractBuoiHocFromCursor(cursor);
            }
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error getting buoi hoc by id", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    private BuoiHoc extractBuoiHocFromCursor(Cursor cursor) {
        BuoiHoc bh = new BuoiHoc();
        bh.setId(cursor.getInt(0));
        bh.setMaMonHoc(cursor.getString(1));
        bh.setMaGV(cursor.getString(2));
        bh.setMaLop(cursor.getString(3));
        bh.setNgayHoc(cursor.getString(4));
        bh.setTietBatDau(cursor.getInt(5));
        bh.setTietKetThuc(cursor.getInt(6));
        bh.setGhiChu(cursor.getString(7));
        bh.setTenMonHoc(cursor.getString(8));
        bh.setHoTenGV(cursor.getString(9));
        bh.setTenLop(cursor.getString(10));
        return bh;
    }
}