package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.DiemDanhDAO;
import com.teamforone.quanlysinhvien.domain.model.DiemDanh;

public class DiemDanhSinhVienUseCase {
    private DiemDanhDAO diemDanhDAO;

    public DiemDanhSinhVienUseCase(DiemDanhDAO diemDanhDAO) {
        this.diemDanhDAO = diemDanhDAO;
    }

    public boolean execute(DiemDanh diemDanh) {
        if (diemDanh == null) {
            return false;
        }

        if (diemDanh.getBuoiHocId() <= 0) {
            return false;
        }

        if (diemDanh.getMaSV() == null || diemDanh.getMaSV().trim().isEmpty()) {
            return false;
        }

        // Check if already exists, then update instead
        if (diemDanhDAO.exists(diemDanh.getBuoiHocId(), diemDanh.getMaSV())) {
            return diemDanhDAO.update(diemDanh);
        }

        return diemDanhDAO.insert(diemDanh);
    }
}