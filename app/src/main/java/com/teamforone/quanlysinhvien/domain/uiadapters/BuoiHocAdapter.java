package com.teamforone.quanlysinhvien.domain.uiadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.teamforone.quanlysinhvien.R;
import com.teamforone.quanlysinhvien.domain.model.BuoiHoc;

import java.util.List;

public class BuoiHocAdapter extends RecyclerView.Adapter<BuoiHocAdapter.ViewHolder> {

    private final Context context;
    private final List<BuoiHoc> buoiHocList;
    private final OnBuoiHocClickListener clickListener;

    public interface OnBuoiHocClickListener {
        void onBuoiHocClick(BuoiHoc buoiHoc);
    }

    public BuoiHocAdapter(Context context,
                          List<BuoiHoc> buoiHocList,
                          OnBuoiHocClickListener clickListener) {
        this.context = context;
        this.buoiHocList = buoiHocList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_buoi_hoc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuoiHoc bh = buoiHocList.get(position);

        // Hiển thị dữ liệu lên CardView
        holder.tvMonHoc.setText(safe(bh.getTenMonHoc()));
        holder.tvLop.setText("Lớp: " + safe(bh.getTenLop()));
        holder.tvNgayHoc.setText("Ngày: " + safe(bh.getNgayHoc()));

        // Hiển thị tiết học: Tiết 1 - 3
        String tietHoc = String.format("Tiết: %d - %d", bh.getTietBatDau(), bh.getTietKetThuc());
        holder.tvTiet.setText(tietHoc);

        // Sự kiện click để vào điểm danh
        holder.cardView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onBuoiHocClick(bh);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buoiHocList != null ? buoiHocList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvMonHoc, tvLop, tvNgayHoc, tvTiet;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvMonHoc = itemView.findViewById(R.id.tvMonHoc);
            tvLop = itemView.findViewById(R.id.tvLop);
            tvNgayHoc = itemView.findViewById(R.id.tvNgayHoc);
            tvTiet = itemView.findViewById(R.id.tvTiet);

            // Lưu ý: Đảm bảo trong item_buoi_hoc.xml CÓ các ID này
            // và KHÔNG CẦN tvGiangVien nữa.
        }
    }

    private String safe(String s) {
        return (s == null || s.trim().isEmpty()) ? "N/A" : s;
    }
}
