package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.SinhVienDAO;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;

public class AddSinhVienUseCase {
    private SinhVienDAO sinhVienDAO;

    public AddSinhVienUseCase(SinhVienDAO sinhVienDAO) {
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

        // Check if already exists
        if (sinhVienDAO.exists(sinhVien.getMaSV())) {
            return false;
        }

        return sinhVienDAO.insert(sinhVien);
    }

    public boolean exists(String maSV) {
        return sinhVienDAO.exists(maSV);
    }
}
