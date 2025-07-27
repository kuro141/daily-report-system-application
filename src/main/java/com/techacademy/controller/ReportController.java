package com.techacademy.controller;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("reports")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private EmployeeService employeeService;

	// 一覧表示
	@GetMapping
	public String list(Model model) {
		List<Report> reportList = reportService.getReportList();
		model.addAttribute("reportList", reportList);
		model.addAttribute("listSize", reportList.size());
		return "reports/list";
	}

	// 登録画面の表示
	@GetMapping("/add")
	public String newReport(@AuthenticationPrincipal UserDetail userDetail, Model model) {
		Report report = new Report();
		report.setEmployee(userDetail.getEmployee()); // ログインユーザーを紐付ける

		model.addAttribute("report", report);
		model.addAttribute("employee", userDetail.getEmployee()); // 氏名表示用
		return "reports/new";
	}


	// 登録処理
/*	@PostMapping("/add")
	public String createReport(@ModelAttribute("report") @Validated Report report, BindingResult result,
			@AuthenticationPrincipal UserDetail userDetail, Model model) {

// ログインユーザーを再セット（セキュリティ上、formのemployeeを信用しない）
		report.setEmployee(userDetail.getEmployee());

		if (result.hasErrors()) {
			model.addAttribute("employee", userDetail.getEmployee());
			return "reports/new";
		}

		reportService.save(report);
		return "redirect:/reports";
	}
*/

	@PostMapping("/add")
	public String createReport(@ModelAttribute("report") @Validated Report report, BindingResult result,
	        @AuthenticationPrincipal UserDetail userDetail, Model model) {

	    // ログインユーザーを再セット（セキュリティ上、formのemployeeを信用しない）
	    report.setEmployee(userDetail.getEmployee());

	    // 既に登録済みかチェック（同日・同従業員）
	    if (reportService.existsByEmployeeAndDate(userDetail.getEmployee(), report.getReportDate())) {
	        result.rejectValue("reportDate", "duplicate", "既に登録されている日付です");
	    }

	    if (result.hasErrors()) {
	        model.addAttribute("employee", userDetail.getEmployee());
	        return "reports/new";
	    }

	    reportService.save(report);
	    return "redirect:/reports";
	}


	// 詳細表示
	@GetMapping("/{id}")
	public String detail(@PathVariable("id") Integer id, Model model) {
		Report report = reportService.getReport(id);
		model.addAttribute("report", report);
		return "reports/detail";
	}

	// 更新画面の表示
	@GetMapping("/{id}/update")
	public String edit(@PathVariable("id") Integer id, Model model) {
		Report report = reportService.getReport(id);
		model.addAttribute("report", report);
		model.addAttribute("employeeList", employeeService.findAll());
		return "reports/edit";
	}

	// 更新処理
	@PostMapping("/{id}/update")
	public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute Report report, BindingResult result,
	                     @AuthenticationPrincipal UserDetail userDetail, Model model) {

	    report.setEmployee(userDetail.getEmployee());

	    if (reportService.isDuplicateForUpdate(userDetail.getEmployee(), report.getReportDate(), id)) {
	        result.rejectValue("reportDate", "duplicate", "既に登録されている日付です");
	    }

	    if (result.hasErrors()) {
	        model.addAttribute("employeeList", employeeService.findAll());
	        return "reports/edit";
	    }

	    reportService.update(report);
	    return "redirect:/reports";
	}


	// 削除処理
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable("id") Integer id) {
		reportService.delete(id);
		return "redirect:/reports";
	}

}
