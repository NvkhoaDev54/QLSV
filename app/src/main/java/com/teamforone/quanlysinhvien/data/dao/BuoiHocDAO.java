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
    private final DatabaseProvider databaseProvider;

    public BuoiHocDAO(Context context) {
        databaseProvider = DatabaseProvider.getInstance(context);
    }

    /**
     * Lấy tất cả buổi học (kèm join bảng để lấy tên môn, tên lớp, tên GV)
     */
    public List<BuoiHoc> getAllBuoiHoc() {
        List<BuoiHoc> list = new ArrayList<>();
        SQLiteDatabase db = databaseProvider.getReadableDatabase();
        // Đã xóa g.hoTen
        String query = "SELECT b.*, m.tenMH, l.tenLop " +
                "FROM BUOI_HOC b " +
                "LEFT JOIN MONHOC m ON b.maMH = m.maMH " +
                "LEFT JOIN LOP l ON b.maLop = l.maLop " +
                "ORDER BY b.ngayHoc DESC";


        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(extractBuoiHocFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error getAllBuoiHoc", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /**
     * Lọc buổi học theo mã lớp
     */
    public List<BuoiHoc> getByLop(String maLop) {
        List<BuoiHoc> list = new ArrayList<>();
        SQLiteDatabase db = databaseProvider.getReadableDatabase();
        String query = "SELECT b.*, m.tenMH, g.hoTen AS hoTenGV, l.tenLop " +
                "FROM BUOI_HOC b " +
                "LEFT JOIN MONHOC m ON b.maMH = m.maMH " +
                "LEFT JOIN LOP l ON b.maLop = l.maLop " +
                "WHERE b.maLop = ? " +
                "ORDER BY b.ngayHoc DESC";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{maLop});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(extractBuoiHocFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error getByLop", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /**
     * Lấy chi tiết buổi học theo ID
     */
    public BuoiHoc getById(int id) {
        SQLiteDatabase db = databaseProvider.getReadableDatabase();
        String query = "SELECT b.*, m.tenMH, g.hoTen AS hoTenGV, l.tenLop FROM BUOI_HOC b " +
                "LEFT JOIN MONHOC m ON b.maMH = m.maMH " +
                "LEFT JOIN LOP l ON b.maLop = l.maLop " +
                "WHERE b.id = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
            if (cursor != null && cursor.moveToFirst()) {
                return extractBuoiHocFromCursor(cursor);
            }
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error getById", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * Thêm mới buổi học
     */
    public boolean insert(BuoiHoc buoiHoc) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("maMH", buoiHoc.getMaMonHoc());
            values.put("maLop", buoiHoc.getMaLop());
            values.put("ngayHoc", buoiHoc.getNgayHoc());
            values.put("tietBatDau", buoiHoc.getTietBatDau());
            values.put("tietKetThuc", buoiHoc.getTietKetThuc());
            values.put("ghiChu", buoiHoc.getGhiChu());

            long result = db.insert("BUOI_HOC", null, values);
            return result != -1;
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error insert", e);
            return false;
        }
    }

    /**
     * Cập nhật thông tin buổi học (HÀM MỚI THÊM ĐỂ HẾT LỖI)
     */
    public boolean update(BuoiHoc buoiHoc) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("maMH", buoiHoc.getMaMonHoc());
            values.put("maLop", buoiHoc.getMaLop());
            values.put("ngayHoc", buoiHoc.getNgayHoc());
            values.put("tietBatDau", buoiHoc.getTietBatDau());
            values.put("tietKetThuc", buoiHoc.getTietKetThuc());
            values.put("ghiChu", buoiHoc.getGhiChu());

            int result = db.update("BUOI_HOC", values, "id = ?",
                    new String[]{String.valueOf(buoiHoc.getId())});
            return result > 0;
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error update", e);
            return false;
        }
    }

    /**
     * Xóa buổi học theo ID (HÀM MỚI THÊM ĐỂ HẾT LỖI)
     */
    public boolean delete(int id) {
        SQLiteDatabase db = databaseProvider.getWritableDatabase();
        try {
            int result = db.delete("BUOI_HOC", "id = ?",
                    new String[]{String.valueOf(id)});
            return result > 0;
        } catch (Exception e) {
            AppLogger.e("BuoiHocDAO", "Error delete", e);
            return false;
        }
    }

    /**
     * Hàm dùng chung để chuyển dữ liệu từ Cursor sang Object BuoiHoc
     */
    private BuoiHoc extractBuoiHocFromCursor(Cursor cursor) {
        BuoiHoc bh = new BuoiHoc();
        bh.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        bh.setMaMonHoc(cursor.getString(cursor.getColumnIndexOrThrow("maMH")));
        bh.setMaLop(cursor.getString(cursor.getColumnIndexOrThrow("maLop")));
        bh.setNgayHoc(cursor.getString(cursor.getColumnIndexOrThrow("ngayHoc")));
        bh.setTietBatDau(cursor.getInt(cursor.getColumnIndexOrThrow("tietBatDau")));
        bh.setTietKetThuc(cursor.getInt(cursor.getColumnIndexOrThrow("tietKetThuc")));
        bh.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow("ghiChu")));

        // Dữ liệu từ các bảng Join
        bh.setTenMonHoc(cursor.getString(cursor.getColumnIndexOrThrow("tenMH")));
        bh.setHoTenGV(cursor.getString(cursor.getColumnIndexOrThrow("hoTenGV")));
        bh.setTenLop(cursor.getString(cursor.getColumnIndexOrThrow("tenLop")));
        return bh;
    }
}
