package com.teamforone.quanlysinhvien.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.SinhVien;
import com.teamforone.quanlysinhvien.service.SinhVienService;
import com.teamforone.quanlysinhvien.util.AppLogger;
import com.teamforone.quanlysinhvien.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SinhVienAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private SinhVienAdapter adapter;
    private SinhVienService sinhVienService;
    private List<SinhVien> sinhVienList;

    private EditText etSearch;
    private Spinner spinnerLop;
    private TextView tvEmpty;
    private Button btnSearch, btnResetFilter;
    private FloatingActionButton fabAdd;

    private String currentFilter = Constants.FILTER_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlysinhvien);

        AppLogger.d(Constants.LOG_TAG_UI, "MainActivity created");

        setupToolbar();
        initViews();
        initService();
        setupRecyclerView();
        setupSpinner();
        setupListeners();
        loadData();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        spinnerLop = findViewById(R.id.spinnerLop);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnSearch = findViewById(R.id.btnSearch);
        btnResetFilter = findViewById(R.id.btnResetFilter);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void initService() {
        sinhVienService = SinhVienService.getInstance(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sinhVienList = new ArrayList<>();
        adapter = new SinhVienAdapter(sinhVienList, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupSpinner() {
        List<String> lopList = sinhVienService.getAllLopNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                lopList
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(spinnerAdapter);
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddSinhVienActivity.class);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> performSearch());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    loadData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                currentFilter = selected;
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnResetFilter.setOnClickListener(v -> resetFilters());
    }

    private void loadData() {
        AppLogger.d(Constants.LOG_TAG_UI, "Loading all data");
        sinhVienList = sinhVienService.getAllSinhVien();
        updateUI();
    }

    private void performSearch() {
        String keyword = etSearch.getText().toString().trim();
        if (!keyword.isEmpty()) {
            AppLogger.d(Constants.LOG_TAG_UI, "Performing search: " + keyword);
            sinhVienList = sinhVienService.searchSinhVien(keyword);
            updateUI();
        } else {
            Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyFilter() {
        if (currentFilter.equals(Constants.FILTER_ALL)) {
            loadData();
        } else {
            String maLop = currentFilter.split(" - ")[0];
            AppLogger.d(Constants.LOG_TAG_UI, "Applying filter: " + maLop);
            sinhVienList = sinhVienService.filterByLop(maLop);
            updateUI();
        }
    }

    private void resetFilters() {
        spinnerLop.setSelection(0);
        etSearch.setText("");
        loadData();
        Toast.makeText(this, "Đã reset bộ lọc", Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        if (sinhVienList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
        adapter.updateList(sinhVienList);
        AppLogger.d(Constants.LOG_TAG_UI, "UI updated with " + sinhVienList.size() + " items");
    }

    @Override
    public void onEditClick(SinhVien sinhVien) {
        AppLogger.d(Constants.LOG_TAG_UI, "Edit clicked for: " + sinhVien.getMaSV());
        Intent intent = new Intent(MainActivity.this, EditSinhVienActivity.class);
        intent.putExtra(Constants.EXTRA_MA_SV, sinhVien.getMaSV());
        intent.putExtra(Constants.EXTRA_HO_TEN, sinhVien.getHoTen());
        intent.putExtra(Constants.EXTRA_NGAY_SINH, sinhVien.getNgaySinh());
        intent.putExtra(Constants.EXTRA_GIOI_TINH, sinhVien.getGioiTinh());
        intent.putExtra(Constants.EXTRA_DIA_CHI, sinhVien.getDiaChi());
        intent.putExtra(Constants.EXTRA_MA_LOP, sinhVien.getMaLop());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(SinhVien sinhVien) {
        showDeleteConfirmDialog(sinhVien);
    }

    private void showDeleteConfirmDialog(SinhVien sinhVien) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sinh viên " + sinhVien.getHoTen() +
                        " (Mã: " + sinhVien.getMaSV() + ") không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSinhVien(sinhVien.getMaSV());
                    }
                })
                .setNegativeButton("Hủy", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteSinhVien(String maSV) {
        if (sinhVienService.deleteSinhVien(maSV)) {
            Toast.makeText(this, Constants.MSG_DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            Toast.makeText(this, Constants.MSG_DELETE_FAILED, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}