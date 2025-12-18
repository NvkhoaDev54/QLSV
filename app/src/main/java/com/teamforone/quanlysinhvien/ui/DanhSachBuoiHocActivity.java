package com.teamforone.quanlysinhvien.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;
import com.teamforone.quanlysinhvien.domain.uiadapters.BuoiHocAdapter;
import com.teamforone.quanlysinhvien.service.BuoiHocService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DanhSachBuoiHocActivity extends AppCompatActivity {

    private static final String TAG = "QUANLYSV_DEBUG";

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private Spinner spinnerLop;
    private Button btnResetFilter;
    private FloatingActionButton fabAdd, fabHome;

    private BuoiHocAdapter adapter;
    private List<BuoiHoc> allBuoiHoc;
    private List<BuoiHoc> filteredList;

    private BuoiHocService buoiHocService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_buoi_hoc);

        // Đã lược bỏ phần phân quyền User/Role
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);
        spinnerLop = findViewById(R.id.spinnerLop);
        btnResetFilter = findViewById(R.id.btnResetFilter);
        fabAdd = findViewById(R.id.fabAdd);
        fabHome = findViewById(R.id.fabHome);

        buoiHocService = BuoiHocService.getInstance(this);

        allBuoiHoc = new ArrayList<>();
        filteredList = new ArrayList<>();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh Sách Buổi Học");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        // Khởi tạo Adapter với filteredList (danh sách sẽ thay đổi khi lọc)
        adapter = new BuoiHocAdapter(this, filteredList, buoiHoc -> {
            // Khi bấm vào item sẽ chuyển sang màn hình điểm danh
            Intent intent = new Intent(this, DiemDanhActivity.class);
            intent.putExtra("buoiHocId", buoiHoc.getId());
            intent.putExtra("tenMonHoc", buoiHoc.getTenMonHoc());
            intent.putExtra("tenLop", buoiHoc.getTenLop());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        // 1. Lấy dữ liệu từ database thông qua Service
        List<BuoiHoc> data = buoiHocService.getAllBuoiHoc();

        // 2. Debug Log: Kiểm tra xem Database có thực sự trả về dữ liệu không
        if (data == null || data.isEmpty()) {
            Log.e(TAG, "DỮ LIỆU TRỐNG: Hãy kiểm tra bảng BUOI_HOC, LOP và MONHOC!");
        } else {
            Log.d(TAG, "TÌM THẤY: " + data.size() + " buổi học.");
        }

        allBuoiHoc.clear();
        if (data != null) {
            allBuoiHoc.addAll(data);
        }

        // 3. Mặc định hiển thị toàn bộ danh sách khi vừa vào
        filteredList.clear();
        filteredList.addAll(allBuoiHoc);

        // 4. Cập nhật Spinner (dựa trên các lớp thực tế có trong data)
        setupSpinner();

        // 5. Cập nhật giao diện ẩn/hiện bảng
        updateUI();
    }

    private void setupSpinner() {
        Set<String> lopSet = new HashSet<>();
        lopSet.add("Tất cả lớp");

        // Chỉ thêm những lớp nào thực sự có buổi học
        for (BuoiHoc bh : allBuoiHoc) {
            if (bh.getTenLop() != null && !bh.getTenLop().isEmpty()) {
                lopSet.add(bh.getTenLop());
            }
        }

        List<String> lopNames = new ArrayList<>(lopSet);
        Collections.sort(lopNames);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lopNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLop.setAdapter(spinnerAdapter);

        // Lắng nghe sự kiện chọn trên Spinner để lọc
        spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByLop(lopNames.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterByLop(String lop) {
        filteredList.clear();
        if ("Tất cả lớp".equals(lop)) {
            filteredList.addAll(allBuoiHoc);
        } else {
            for (BuoiHoc bh : allBuoiHoc) {
                if (lop.equals(bh.getTenLop())) {
                    filteredList.add(bh);
                }
            }
        }
        updateUI();
    }

    private void updateUI() {
        if (filteredList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
        // Báo cho Adapter biết dữ liệu đã thay đổi để vẽ lại màn hình
        adapter.notifyDataSetChanged();
    }

    private void setupListeners() {
        // Nút reset bộ lọc
        btnResetFilter.setOnClickListener(v -> {
            spinnerLop.setSelection(0);
            filterByLop("Tất cả lớp");
        });

        // Nút nổi để mở trang tạo buổi học
        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, TaoBuoiHocActivity.class))
        );

        // Nút về trang chủ/đóng
        fabHome.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Quan trọng: Nạp lại dữ liệu mỗi khi quay lại màn hình này
        loadData();
    }
}
