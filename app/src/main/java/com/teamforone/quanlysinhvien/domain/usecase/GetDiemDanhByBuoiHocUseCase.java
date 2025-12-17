package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.DiemDanhDAO;
import com.teamforone.quanlysinhvien.domain.model.DiemDanh;

import java.util.List;

public class GetDiemDanhByBuoiHocUseCase {
    private DiemDanhDAO diemDanhDAO;

    public GetDiemDanhByBuoiHocUseCase(DiemDanhDAO diemDanhDAO) {
        this.diemDanhDAO = diemDanhDAO;
    }

    public List<DiemDanh> execute(int buoiHocId) {
        if (buoiHocId <= 0) {
            return null;
        }
        return diemDanhDAO.getByBuoiHoc(buoiHocId);
    }
}