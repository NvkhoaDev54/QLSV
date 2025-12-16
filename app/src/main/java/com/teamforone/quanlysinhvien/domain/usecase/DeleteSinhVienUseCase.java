package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.SinhVienDAO;

public class DeleteSinhVienUseCase {
    private SinhVienDAO sinhVienDAO;

    public DeleteSinhVienUseCase(SinhVienDAO sinhVienDAO) {
        this.sinhVienDAO = sinhVienDAO;
    }

    public boolean execute(String maSV) {
        // Business logic validation
        if (maSV == null || maSV.trim().isEmpty()) {
            return false;
        }

        // Check if exists before deleting
        if (!sinhVienDAO.exists(maSV)) {
            return false;
        }

        return sinhVienDAO.delete(maSV);
    }
}
