package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.BuoiHocDAO;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;

import java.util.List;

public class GetAllBuoiHocUseCase {
    private BuoiHocDAO buoiHocDAO;

    public GetAllBuoiHocUseCase(BuoiHocDAO buoiHocDAO) {
        this.buoiHocDAO = buoiHocDAO;
    }

    public List<BuoiHoc> execute() {
        return buoiHocDAO.getAll();
    }

    public List<BuoiHoc> getByLop(String maLop) {
        return buoiHocDAO.getByLop(maLop);
    }

    public BuoiHoc getById(int id) {
        return buoiHocDAO.getById(id);
    }
}