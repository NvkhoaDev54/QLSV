package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.SinhVienDAO;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;

import java.util.List;

public class GetAllSinhVienUseCase {
    private SinhVienDAO sinhVienDAO;

    public GetAllSinhVienUseCase(SinhVienDAO sinhVienDAO) {
        this.sinhVienDAO = sinhVienDAO;
    }

    public List<SinhVien> execute() {
        return sinhVienDAO.getAll();
    }

    public List<SinhVien> search(String keyword) {
        return sinhVienDAO.search(keyword);
    }

    public List<SinhVien> filterByLop(String maLop) {
        return sinhVienDAO.filterByLop(maLop);
    }

    public List<String> getAllLopNames() {
        return sinhVienDAO.getAllLopNames();
    }
}
