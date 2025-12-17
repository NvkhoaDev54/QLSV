package com.teamforone.quanlysinhvien.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.data.dao.ReportDAO;
import com.teamforone.quanlysinhvien.data.dao.ReportDAOImpl;
import com.teamforone.quanlysinhvien.data.db.DatabaseHelper;
import com.teamforone.quanlysinhvien.domain.model.AttendanceReport;
import com.teamforone.quanlysinhvien.domain.usecase.GetAttendanceReportUseCase;
import com.teamforone.quanlysinhvien.ui.adapter.AttendanceReportAdapter;
import com.teamforone.quanlysinhvien.util.CSVExporter;

import java.util.List;

public class ReportActivity extends AppCompatActivity {
    TextView tvTotalStudents, tvTotalSessions, tvAvgAttendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        tvTotalSessions = findViewById(R.id.tvTotalSessions);
        tvAvgAttendance = findViewById(R.id.tvAvgAttendance);

        RecyclerView rv = findViewById(R.id.rvReport);
        Button btnExport = findViewById(R.id.btnExport);

        rv.setLayoutManager(new LinearLayoutManager(this));

        // === LUỒNG CHUẨN ===
        DatabaseHelper dbHelper =
                DatabaseHelper.getInstance(this, "QLSV.db");

        ReportDAO dao = new ReportDAOImpl(dbHelper);
        GetAttendanceReportUseCase useCase =
                new GetAttendanceReportUseCase(dao);

        List<AttendanceReport> list = useCase.execute();
        rv.setAdapter(new AttendanceReportAdapter(list));
        showSummary(list);


        btnExport.setOnClickListener(v -> {
            try {
                CSVExporter.exportAttendance(this, list);
                Toast.makeText(this,
                        "Xuất báo cáo thành công!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this,
                        "Lỗi xuất file!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showSummary(List<AttendanceReport> list) {
        if (list.isEmpty()) return;

        int totalStudents = list.size();
        int totalSessions = 0;
        double totalPercent = 0;

        for (AttendanceReport r : list) {
            totalSessions += r.getTongBuoi();
            totalPercent += r.getTyLeChuyenCan();
        }

        tvTotalStudents.setText("SV: " + totalStudents);
        tvTotalSessions.setText("Buổi: " + totalSessions);
        tvAvgAttendance.setText(
                "Chuyên cần: " + (totalPercent / totalStudents) + "%"
        );
    }

}
