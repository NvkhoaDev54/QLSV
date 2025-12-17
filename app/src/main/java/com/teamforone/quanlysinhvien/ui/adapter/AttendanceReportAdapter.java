package com.teamforone.quanlysinhvien.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.AttendanceReport;

import java.util.List;

public class AttendanceReportAdapter
        extends RecyclerView.Adapter<AttendanceReportAdapter.ViewHolder> {

    private List<AttendanceReport> list;

    public AttendanceReportAdapter(List<AttendanceReport> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        AttendanceReport r = list.get(position);

        h.tvTenSV.setText(r.getTenSV());
        h.tvMonHoc.setText("Môn: " + r.getTenMH());
        h.tvThongKe.setText(
                "Tổng: " + r.getTongBuoi() +
                        " | Có mặt: " + r.getSoBuoiCoMat() +
                        " | Vắng: " + r.getSoBuoiVang() +
                        " | CC: " + r.getTyLeChuyenCan() + "%"
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSV, tvMonHoc, tvThongKe;

        ViewHolder(View v) {
            super(v);
            tvTenSV = v.findViewById(R.id.tvTenSV);
            tvMonHoc = v.findViewById(R.id.tvMonHoc);
            tvThongKe = v.findViewById(R.id.tvThongKe);
        }
    }
}
