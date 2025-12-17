package com.teamforone.quanlysinhvien.data.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.teamforone.quanlysinhvien.data.db.DatabaseHelper;
import com.teamforone.quanlysinhvien.domain.model.AttendanceReport;

import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    private DatabaseHelper dbHelper;

    public ReportDAOImpl(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<AttendanceReport> getAttendanceReport() {
        List<AttendanceReport> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql =
                "SELECT sv.maSV, sv.hoTen, mh.tenMH, " +
                        "COUNT(dd.id) AS tongBuoi, " +
                        "SUM(CASE WHEN dd.trangThai='Có mặt' THEN 1 ELSE 0 END) AS coMat, " +
                        "SUM(CASE WHEN dd.trangThai='Vắng' THEN 1 ELSE 0 END) AS vang, " +
                        "ROUND(SUM(CASE WHEN dd.trangThai='Có mặt' THEN 1 ELSE 0 END) * 100.0 / COUNT(dd.id), 2) AS tyLe " +
                        "FROM DIEMDANH dd " +
                        "JOIN BUOI_HOC bh ON dd.buoiHocId = bh.id " +
                        "JOIN SINHVIEN sv ON dd.maSV = sv.maSV " +
                        "JOIN MONHOC mh ON bh.maMH = mh.maMH " +
                        "GROUP BY sv.maSV, mh.maMH";

        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            list.add(new AttendanceReport(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getInt(3),
                    c.getInt(4),
                    c.getInt(5),
                    c.getDouble(6)
            ));
        }
        c.close();
        return list;
    }
}
