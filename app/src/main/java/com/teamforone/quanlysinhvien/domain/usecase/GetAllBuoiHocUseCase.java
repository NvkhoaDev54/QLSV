package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.BuoiHocDAO;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;

import java.util.List;

public class GetAllBuoiHocUseCase {
    private final BuoiHocDAO buoiHocDAO;

    public GetAllBuoiHocUseCase(BuoiHocDAO buoiHocDAO) {
        this.buoiHocDAO = buoiHocDAO;
    }

    // Lấy toàn bộ danh sách buổi học
    public List<BuoiHoc> execute() {
        return buoiHocDAO.getAllBuoiHoc();
    }

    // Lấy danh sách buổi học theo mã lớp
    public List<BuoiHoc> getByLop(String maLop) {
        return buoiHocDAO.getByLop(maLop);
    }

    // Lấy chi tiết một buổi học theo ID
    public BuoiHoc getById(int id) {
        return buoiHocDAO.getById(id);
    }
}
