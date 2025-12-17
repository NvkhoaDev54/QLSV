package com.teamforone.quanlysinhvien.data.dao;

import com.teamforone.quanlysinhvien.domain.model.AttendanceReport;
import java.util.List;

public interface ReportDAO {
    List<AttendanceReport> getAttendanceReport();
}
