package com.teamforone.quanlysinhvien.service;

import android.content.Context;

import com.teamforone.quanlysinhvien.data.dao.BuoiHocDAO;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;
import com.teamforone.quanlysinhvien.domain.usecase.CreateBuoiHocUseCase;
import com.teamforone.quanlysinhvien.domain.usecase.GetAllBuoiHocUseCase;
import com.teamforone.quanlysinhvien.util.AppLogger;

import java.util.List;

public class BuoiHocService {
    private static BuoiHocService instance;

    private GetAllBuoiHocUseCase getAllBuoiHocUseCase;
    private CreateBuoiHocUseCase createBuoiHocUseCase;
    private BuoiHocDAO buoiHocDAO;

    private BuoiHocService(Context context) {
        buoiHocDAO = new BuoiHocDAO(context);
        this.getAllBuoiHocUseCase = new GetAllBuoiHocUseCase(buoiHocDAO);
        this.createBuoiHocUseCase = new CreateBuoiHocUseCase(buoiHocDAO);
        AppLogger.d("BuoiHocService", "BuoiHocService initialized");
    }

    public static synchronized BuoiHocService getInstance(Context context) {
        if (instance == null) {
            instance = new BuoiHocService(context.getApplicationContext());
        }
        return instance;
    }

    public List<BuoiHoc> getAllBuoiHoc() {
        AppLogger.d("BuoiHocService", "Getting all buoi hoc");
        return getAllBuoiHocUseCase.execute();
    }

    public List<BuoiHoc> getBuoiHocByLop(String maLop) {
        AppLogger.d("BuoiHocService", "Getting buoi hoc by lop: " + maLop);
        return getAllBuoiHocUseCase.getByLop(maLop);
    }

    public BuoiHoc getBuoiHocById(int id) {
        AppLogger.d("BuoiHocService", "Getting buoi hoc by id: " + id);
        return getAllBuoiHocUseCase.getById(id);
    }

    public boolean createBuoiHoc(BuoiHoc buoiHoc) {
        AppLogger.d("BuoiHocService", "Creating buoi hoc: " + buoiHoc);
        return createBuoiHocUseCase.execute(buoiHoc);
    }

    public boolean updateBuoiHoc(BuoiHoc buoiHoc) {
        AppLogger.d("BuoiHocService", "Updating buoi hoc: " + buoiHoc);
        return buoiHocDAO.update(buoiHoc);
    }

    public boolean deleteBuoiHoc(int id) {
        AppLogger.d("BuoiHocService", "Deleting buoi hoc: " + id);
        return buoiHocDAO.delete(id);
    }
}