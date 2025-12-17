package com.teamforone.quanlysinhvien.domain.usecase;

import com.teamforone.quanlysinhvien.data.dao.ReportDAO;
import com.teamforone.quanlysinhvien.domain.model.AttendanceReport;

import java.util.List;

public class GetAttendanceReportUseCase {

    private ReportDAO reportDAO;

    public GetAttendanceReportUseCase(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public List<AttendanceReport> execute() {
        return reportDAO.getAttendanceReport();
    }
}
