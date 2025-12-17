package com.teamforone.quanlysinhvien.domain.model;

public class BuoiHoc {
    private int id;
    private String maMonHoc;
    private String tenMonHoc;
    private String maGV;
    private String hoTenGV;
    private String maLop;
    private String tenLop;
    private String ngayHoc;
    private int tietBatDau;
    private int tietKetThuc;
    private String ghiChu;

    public BuoiHoc() {}

    public BuoiHoc(int id, String maMonHoc, String tenMonHoc, String maGV,
                   String hoTenGV, String maLop, String tenLop, String ngayHoc,
                   int tietBatDau, int tietKetThuc, String ghiChu) {
        this.id = id;
        this.maMonHoc = maMonHoc;
        this.tenMonHoc = tenMonHoc;
        this.maGV = maGV;
        this.hoTenGV = hoTenGV;
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.ngayHoc = ngayHoc;
        this.tietBatDau = tietBatDau;
        this.tietKetThuc = tietKetThuc;
        this.ghiChu = ghiChu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaMonHoc() { return maMonHoc; }
    public void setMaMonHoc(String maMonHoc) { this.maMonHoc = maMonHoc; }

    public String getTenMonHoc() { return tenMonHoc; }
    public void setTenMonHoc(String tenMonHoc) { this.tenMonHoc = tenMonHoc; }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getHoTenGV() { return hoTenGV; }
    public void setHoTenGV(String hoTenGV) { this.hoTenGV = hoTenGV; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getNgayHoc() { return ngayHoc; }
    public void setNgayHoc(String ngayHoc) { this.ngayHoc = ngayHoc; }

    public int getTietBatDau() { return tietBatDau; }
    public void setTietBatDau(int tietBatDau) { this.tietBatDau = tietBatDau; }

    public int getTietKetThuc() { return tietKetThuc; }
    public void setTietKetThuc(int tietKetThuc) { this.tietKetThuc = tietKetThuc; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    @Override
    public String toString() {
        return "BuoiHoc{" +
                "id=" + id +
                ", maMonHoc='" + maMonHoc + '\'' +
                ", tenMonHoc='" + tenMonHoc + '\'' +
                ", ngayHoc='" + ngayHoc + '\'' +
                ", tiet=" + tietBatDau + "-" + tietKetThuc +
                '}';
    }
}