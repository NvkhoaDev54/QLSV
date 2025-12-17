package com.teamforone.quanlysinhvien.service;

import android.content.Context;

import com.teamforone.quanlysinhvien.data.dao.DiemDanhDAO;
import com.teamforone.quanlysinhvien.domain.model.DiemDanh;
import com.teamforone.quanlysinhvien.domain.usecase.DiemDanhSinhVienUseCase;
import com.teamforone.quanlysinhvien.domain.usecase.GetDiemDanhByBuoiHocUseCase;
import com.teamforone.quanlysinhvien.util.AppLogger;

import java.util.List;

public class DiemDanhService {
    private static DiemDanhService instance;

    private DiemDanhSinhVienUseCase diemDanhSinhVienUseCase;
    private GetDiemDanhByBuoiHocUseCase getDiemDanhByBuoiHocUseCase;
    private DiemDanhDAO diemDanhDAO;

    private DiemDanhService(Context context) {
        diemDanhDAO = new DiemDanhDAO(context);
        this.diemDanhSinhVienUseCase = new DiemDanhSinhVienUseCase(diemDanhDAO);
        this.getDiemDanhByBuoiHocUseCase = new GetDiemDanhByBuoiHocUseCase(diemDanhDAO);
        AppLogger.d("DiemDanhService", "DiemDanhService initialized");
    }

    public static synchronized DiemDanhService getInstance(Context context) {
        if (instance == null) {
            instance = new DiemDanhService(context.getApplicationContext());
        }
        return instance;
    }

    public List<DiemDanh> getDiemDanhByBuoiHoc(int buoiHocId) {
        AppLogger.d("DiemDanhService", "Getting diem danh for buoi hoc: " + buoiHocId);
        return getDiemDanhByBuoiHocUseCase.execute(buoiHocId);
    }

    public boolean saveDiemDanh(DiemDanh diemDanh) {
        AppLogger.d("DiemDanhService", "Saving diem danh: " + diemDanh);
        return diemDanhSinhVienUseCase.execute(diemDanh);
    }

    public boolean deleteDiemDanh(int id) {
        AppLogger.d("DiemDanhService", "Deleting diem danh: " + id);
        return diemDanhDAO.delete(id);
    }

    public boolean deleteByBuoiHoc(int buoiHocId) {
        AppLogger.d("DiemDanhService", "Deleting all diem danh for buoi hoc: " + buoiHocId);
        return diemDanhDAO.deleteByBuoiHoc(buoiHocId);
    }
}