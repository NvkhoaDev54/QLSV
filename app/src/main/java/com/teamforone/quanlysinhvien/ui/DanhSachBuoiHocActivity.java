package com.teamforone.quanlysinhvien.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.teamforone.quanlysinhvien.data.dao.BuoiHocDAO;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;
import com.teamforone.quanlysinhvien.domain.uiadapters.BuoiHocAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DanhSachBuoiHocActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private Spinner spinnerLop;
    private Button btnResetFilter;
    private FloatingActionButton fabAdd, fabHome;

    private BuoiHocAdapter adapter;
    private List<BuoiHoc> buoiHocList;
    private List<BuoiHoc> filteredList;
    private BuoiHocDAO buoiHocDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_buoi_hoc);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadData();
        setupListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);
        spinnerLop = findViewById(R.id.spinnerLop);
        btnResetFilter = findViewById(R.id.btnResetFilter);
        fabAdd = findViewById(R.id.fabAdd);
        fabHome = findViewById(R.id.fabHome);

        buoiHocDAO = new BuoiHocDAO(this);
        buoiHocList = new ArrayList<>();
        filteredList = new ArrayList<>();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh Sách Buổi Học");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new BuoiHocAdapter(this, filteredList, buoiHoc -> {
            // Click vào item để đi tới màn hình Điểm danh
            Intent intent = new Intent(DanhSachBuoiHocActivity.this, DiemDanhActivity.class);
            intent.putExtra("buoiHocId", buoiHoc.getId());
            intent.putExtra("tenMonHoc", buoiHoc.getTenMonHoc());
            intent.putExtra("tenLop", buoiHoc.getTenLop());
            // ... có thể thêm các extra khác nếu cần
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        // Lấy tất cả dữ liệu từ Database thông qua DAO
        List<BuoiHoc> allFromDb = buoiHocDAO.getAllBuoiHoc();

        buoiHocList.clear();
        if (allFromDb != null) {
            for (BuoiHoc bh : allFromDb) {
                if (bh.getId() > 0) buoiHocList.add(bh);
            }
        }

        filteredList.clear();
        filteredList.addAll(buoiHocList);

        updateUI();
        setupSpinner(); // Cập nhật lại danh sách lọc ở Spinner
    }

    private void setupSpinner() {
        Set<String> lopSet = new HashSet<>();
        lopSet.add("Tất cả lớp");
        for (BuoiHoc bh : buoiHocList) {
            if (bh.getTenLop() != null) lopSet.add(bh.getTenLop());
        }

        List<String> lopNames = new ArrayList<>(lopSet);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lopNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(spinnerAdapter);

        spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByLop(lopNames.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterByLop(String lopName) {
        filteredList.clear();
        if (lopName.equals("Tất cả lớp")) {
            filteredList.addAll(buoiHocList);
        } else {
            for (BuoiHoc bh : buoiHocList) {
                if (lopName.equals(bh.getTenLop())) filteredList.add(bh);
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
        adapter.notifyDataSetChanged();
    }

    private void setupListeners() {
        btnResetFilter.setOnClickListener(v -> {
            spinnerLop.setSelection(0);
            filterByLop("Tất cả lớp");
        });

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, TaoBuoiHocActivity.class));
        });

        fabHome.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Load lại data mỗi khi quay lại từ màn hình thêm buổi học
    }
}
