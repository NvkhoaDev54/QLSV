package com.teamforone.quanlysinhvien.service;

import android.content.Context;

import com.teamforone.quanlysinhvien.data.dao.SinhVienDAO;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;
import com.teamforone.quanlysinhvien.domain.usecase.AddSinhVienUseCase;
import com.teamforone.quanlysinhvien.domain.usecase.DeleteSinhVienUseCase;
import com.teamforone.quanlysinhvien.domain.usecase.GetAllSinhVienUseCase;
import com.teamforone.quanlysinhvien.domain.usecase.UpdateSinhVienUseCase;
import com.teamforone.quanlysinhvien.util.AppLogger;
import com.teamforone.quanlysinhvien.util.Constants;

import java.util.List;

public class SinhVienService {
    private static SinhVienService instance;

    private GetAllSinhVienUseCase getAllSinhVienUseCase;
    private AddSinhVienUseCase addSinhVienUseCase;
    private UpdateSinhVienUseCase updateSinhVienUseCase;
    private DeleteSinhVienUseCase deleteSinhVienUseCase;

    private SinhVienService(Context context) {
        SinhVienDAO sinhVienDAO = new SinhVienDAO(context);

        this.getAllSinhVienUseCase = new GetAllSinhVienUseCase(sinhVienDAO);
        this.addSinhVienUseCase = new AddSinhVienUseCase(sinhVienDAO);
        this.updateSinhVienUseCase = new UpdateSinhVienUseCase(sinhVienDAO);
        this.deleteSinhVienUseCase = new DeleteSinhVienUseCase(sinhVienDAO);

        AppLogger.d(Constants.LOG_TAG_SERVICE, "SinhVienService initialized");
    }

    public static synchronized SinhVienService getInstance(Context context) {
        if (instance == null) {
            instance = new SinhVienService(context.getApplicationContext());
        }
        return instance;
    }

    // Get all sinh vien
    public List<SinhVien> getAllSinhVien() {
        AppLogger.d(Constants.LOG_TAG_SERVICE, "Getting all sinh vien");
        return getAllSinhVienUseCase.execute();
    }

    // Search sinh vien
    public List<SinhVien> searchSinhVien(String keyword) {
        AppLogger.d(Constants.LOG_TAG_SERVICE, "Searching sinh vien with keyword: " + keyword);
        return getAllSinhVienUseCase.search(keyword);
    }

    // Filter by lop
    public List<SinhVien> filterByLop(String maLop) {
        AppLogger.d(Constants.LOG_TAG_SERVICE, "Filtering sinh vien by lop: " + maLop);
        return getAllSinhVienUseCase.filterByLop(maLop);
    }

    // Get all lop names
    public List<String> getAllLopNames() {
        AppLogger.d(Constants.LOG_TAG_SERVICE, "Getting all lop names");
        return getAllSinhVienUseCase.getAllLopNames();
    }

    // Add sinh vien
    public boolean addSinhVien(SinhVien sinhVien) {
        AppLogger.d(Constants.LOG_TAG_SERVICE, "Adding sinh vien: " + sinhVien);
        return addSinhVienUseCase.execute(sinhVien);
    }

    // Check if exists
    public boolean exists(String maSV) {
        return addSinhVienUseCase.exists(maSV);
    }

    // Update sinh vien
    public boolean updateSinhVien(SinhVien sinhVien) {
        AppLogger.d(Constants.LOG_TAG_SERVICE, "Updating sinh vien: " + sinhVien);
        return updateSinhVienUseCase.execute(sinhVien);
    }

    // Delete sinh vien
    public boolean deleteSinhVien(String maSV) {
        AppLogger.d(Constants.LOG_TAG_SERVICE, "Deleting sinh vien: " + maSV);
        return deleteSinhVienUseCase.execute(maSV);
    }
}
