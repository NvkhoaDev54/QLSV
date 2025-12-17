package com.teamforone.quanlysinhvien.domain.model;

public class DiemDanh {
    private int id;
    private int buoiHocId;
    private String maSV;
    private String hoTenSV;
    private boolean coMat;
    private String ghiChu;

    public DiemDanh() {}

    public DiemDanh(int id, int buoiHocId, String maSV, String hoTenSV,
                    boolean coMat, String ghiChu) {
        this.id = id;
        this.buoiHocId = buoiHocId;
        this.maSV = maSV;
        this.hoTenSV = hoTenSV;
        this.coMat = coMat;
        this.ghiChu = ghiChu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBuoiHocId() { return buoiHocId; }
    public void setBuoiHocId(int buoiHocId) { this.buoiHocId = buoiHocId; }

    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getHoTenSV() { return hoTenSV; }
    public void setHoTenSV(String hoTenSV) { this.hoTenSV = hoTenSV; }

    public boolean isCoMat() { return coMat; }
    public void setCoMat(boolean coMat) { this.coMat = coMat; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    @Override
    public String toString() {
        return "DiemDanh{" +
                "id=" + id +
                ", maSV='" + maSV + '\'' +
                ", hoTenSV='" + hoTenSV + '\'' +
                ", coMat=" + coMat +
                '}';
    }
}