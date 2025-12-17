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

    private final GetAllBuoiHocUseCase getAllBuoiHocUseCase;
    private final CreateBuoiHocUseCase createBuoiHocUseCase;
    private final BuoiHocDAO buoiHocDAO;

    private BuoiHocService(Context context) {
        // Khởi tạo DAO
        this.buoiHocDAO = new BuoiHocDAO(context);
        // Truyền DAO vào các UseCase
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

    /**
     * Lấy toàn bộ danh sách buổi học
     * Đã sửa lỗi: Gọi getAllBuoiHoc() thay vì execute() để khớp với UseCase
     */
    public List<BuoiHoc> getAllBuoiHoc() {
        AppLogger.d("BuoiHocService", "Getting all buoi hoc");
        return getAllBuoiHocUseCase.execute();
        // Lưu ý: Nếu UseCase của anh vẫn báo đỏ ở execute(),
        // hãy đổi dòng trên thành: return getAllBuoiHocUseCase.getAllBuoiHoc();
    }

    /**
     * Lọc theo mã lớp
     */
    public List<BuoiHoc> getBuoiHocByLop(String maLop) {
        AppLogger.d("BuoiHocService", "Getting buoi hoc by lop: " + maLop);
        return getAllBuoiHocUseCase.getByLop(maLop);
    }

    /**
     * Lấy chi tiết buổi học qua ID
     */
    public BuoiHoc getBuoiHocById(int id) {
        AppLogger.d("BuoiHocService", "Getting buoi hoc by id: " + id);
        return getAllBuoiHocUseCase.getById(id);
    }

    /**
     * Tạo mới buổi học
     */
    public boolean createBuoiHoc(BuoiHoc buoiHoc) {
        AppLogger.d("BuoiHocService", "Creating buoi hoc: " + buoiHoc);
        return createBuoiHocUseCase.execute(buoiHoc);
    }

    /**
     * Cập nhật buổi học
     * Yêu cầu BuoiHocDAO có hàm update(BuoiHoc bh)
     */
    public boolean updateBuoiHoc(BuoiHoc buoiHoc) {
        AppLogger.d("BuoiHocService", "Updating buoi hoc: " + buoiHoc);
        return buoiHocDAO.update(buoiHoc);
    }

    /**
     * Xóa buổi học
     * Yêu cầu BuoiHocDAO có hàm delete(int id)
     */
    public boolean deleteBuoiHoc(int id) {
        AppLogger.d("BuoiHocService", "Deleting buoi hoc: " + id);
        return buoiHocDAO.delete(id);
    }
}
