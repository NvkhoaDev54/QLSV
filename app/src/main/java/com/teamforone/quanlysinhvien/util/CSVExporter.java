package com.teamforone.quanlysinhvien.util;

import android.content.Context;

import com.teamforone.quanlysinhvien.domain.model.AttendanceReport;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CSVExporter {

    public static File exportAttendance(Context ctx, List<AttendanceReport> list)
            throws Exception {

        File file = new File(ctx.getExternalFilesDir(null),
                "bao_cao_chuyen_can.csv");

        FileWriter writer = new FileWriter(file);
        writer.write("MaSV,TenSV,MonHoc,TongBuoi,CoMat,Vang,TyLe\n");

        for (AttendanceReport r : list) {
            writer.write(
                    r.getMaSV() + "," +
                            r.getTenSV() + "," +
                            r.getTenMH() + "," +
                            r.getTongBuoi() + "," +
                            r.getSoBuoiCoMat() + "," +
                            r.getSoBuoiVang() + "," +
                            r.getTyLeChuyenCan() + "\n"
            );
        }
        writer.close();
        return file;
    }
}
