package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.SinhVienDAO;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;

public class UpdateSinhVienUseCase {
    private SinhVienDAO sinhVienDAO;

    public UpdateSinhVienUseCase(SinhVienDAO sinhVienDAO) {
        this.sinhVienDAO = sinhVienDAO;
    }

    public boolean execute(SinhVien sinhVien) {
        // Business logic validation
        if (sinhVien == null) {
            return false;
        }

        if (sinhVien.getMaSV() == null || sinhVien.getMaSV().trim().isEmpty()) {
            return false;
        }

        if (sinhVien.getHoTen() == null || sinhVien.getHoTen().trim().isEmpty()) {
            return false;
        }

        // Check if exists before updating
        if (!sinhVienDAO.exists(sinhVien.getMaSV())) {
            return false;
        }

        return sinhVienDAO.update(sinhVien);
    }
}
