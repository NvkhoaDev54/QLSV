package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.BuoiHocDAO;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;

public class CreateBuoiHocUseCase {
    private BuoiHocDAO buoiHocDAO;

    public CreateBuoiHocUseCase(BuoiHocDAO buoiHocDAO) {
        this.buoiHocDAO = buoiHocDAO;
    }

    public boolean execute(BuoiHoc buoiHoc) {
        if (buoiHoc == null) {
            return false;
        }

        if (buoiHoc.getMaMonHoc() == null || buoiHoc.getMaMonHoc().trim().isEmpty()) {
            return false;
        }

        if (buoiHoc.getMaLop() == null || buoiHoc.getMaLop().trim().isEmpty()) {
            return false;
        }

        if (buoiHoc.getNgayHoc() == null || buoiHoc.getNgayHoc().trim().isEmpty()) {
            return false;
        }

        if (buoiHoc.getTietBatDau() <= 0 || buoiHoc.getTietKetThuc() <= 0) {
            return false;
        }

        if (buoiHoc.getTietBatDau() > buoiHoc.getTietKetThuc()) {
            return false;
        }

        return buoiHocDAO.insert(buoiHoc);
    }
}