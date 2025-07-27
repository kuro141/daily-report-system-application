package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

	private final ReportRepository reportRepository;

	public ReportService(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	// 一覧取得
	public List<Report> getReportList() {
		return reportRepository.findAll();
	}

	// IDで1件取得
	public Report getReport(Integer id) {
		Optional<Report> optional = reportRepository.findById(id);
		return optional.orElse(null);
	}

	// 新規登録
	@Transactional
	public Report save(Report report) {
		LocalDateTime now = LocalDateTime.now();
		report.setCreatedAt(now);
		report.setUpdatedAt(now);
		report.setDeleteFlg(false);
		return reportRepository.save(report);
	}

	public boolean existsByEmployeeAndDate(Employee employee, LocalDate date) {
	    return reportRepository.existsByEmployeeAndReportDate(employee, date);
	}



	// 更新処理
	@Transactional
	public Report update(Report updatedReport) {
		Report existingReport = getReport(updatedReport.getId());
		if (existingReport != null) {
			existingReport.setTitle(updatedReport.getTitle());
			existingReport.setContent(updatedReport.getContent());
			existingReport.setReportDate(updatedReport.getReportDate());
			existingReport.setStartTime(updatedReport.getStartTime());
			existingReport.setEndTime(updatedReport.getEndTime());
			existingReport.setEmployee(updatedReport.getEmployee());
			existingReport.setUpdatedAt(LocalDateTime.now());
			return reportRepository.save(existingReport);
		}
		return null;
	}

	public boolean isDuplicateForUpdate(Employee employee, LocalDate reportDate, Integer excludeId) {
	    return reportRepository.existsByEmployeeAndReportDateAndIdNot(employee, reportDate, excludeId);
	}


	// 論理削除
	@Transactional
	public void delete(Integer id) {
		Report report = getReport(id);
		if (report != null) {
			report.setDeleteFlg(true);
			report.setUpdatedAt(LocalDateTime.now());
			reportRepository.save(report);
		}
	}
}
