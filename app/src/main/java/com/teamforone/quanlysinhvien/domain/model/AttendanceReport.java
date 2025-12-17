package com.teamforone.quanlysinhvien.domain.model;

public class AttendanceReport {
    private String maSV;
    private String tenSV;
    private String tenMH;
    private int tongBuoi;
    private int soBuoiCoMat;
    private int soBuoiVang;
    private double tyLeChuyenCan;

    public AttendanceReport(String maSV, String tenSV, String tenMH,
                            int tongBuoi, int soBuoiCoMat, int soBuoiVang,
                            double tyLeChuyenCan) {
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.tenMH = tenMH;
        this.tongBuoi = tongBuoi;
        this.soBuoiCoMat = soBuoiCoMat;
        this.soBuoiVang = soBuoiVang;
        this.tyLeChuyenCan = tyLeChuyenCan;
    }

    public String getMaSV() { return maSV; }
    public String getTenSV() { return tenSV; }
    public String getTenMH() { return tenMH; }
    public int getTongBuoi() { return tongBuoi; }
    public int getSoBuoiCoMat() { return soBuoiCoMat; }
    public int getSoBuoiVang() { return soBuoiVang; }
    public double getTyLeChuyenCan() { return tyLeChuyenCan; }
}
