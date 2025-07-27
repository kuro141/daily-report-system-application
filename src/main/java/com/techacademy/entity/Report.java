/*package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "report")
@SQLRestriction("delete_flg = false")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// 多対一でEmployeeを参照（社員番号＝codeを外部キー）
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_code", referencedColumnName = "code", nullable = false)
	@NotNull
	private Employee employee;

	@Column(name = "report_date", nullable = false)
	@NotNull
	private LocalDate reportDate;

	@Column(name = "start_time")
	private LocalTime startTime;

	@Column(name = "end_time")
	private LocalTime endTime;

	@Column(length = 500)
	@Size(max = 500)
	private String content;

	// 論理削除用のフラグ
	@Column(name = "delete_flg", nullable = false)
	private boolean deleteFlg = false;

	// 登録日時
	@Column(nullable = false)
	private LocalDateTime createdAt;


	// 更新日時
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Column(length = 100, nullable = false)
	@Size(max = 100)
	@NotNull
	private String title;

}
*/

package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.SQLRestriction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "report")
@SQLRestriction("delete_flg = false")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// 多対一でEmployeeを参照（社員番号＝codeを外部キー）
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_code", referencedColumnName = "code", nullable = false)
	@NotNull
	private Employee employee;

	@Column(name = "report_date", nullable = false)
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate reportDate;

	@Column(name = "start_time")
	private LocalTime startTime;

	@Column(name = "end_time")
	private LocalTime endTime;

	@Column(length = 500)
	@Size(max = 500)
	@NotEmpty(message = "内容は必須入力です")
	private String content;

	// 論理削除用のフラグ
	@Column(name = "delete_flg", nullable = false)
	private boolean deleteFlg = false;

	// 登録日時
	@Column(nullable = false)
	private LocalDateTime createdAt;

	// 更新日時
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Column(length = 100, nullable = false)
	@Size(max = 100)
	@NotEmpty(message = "タイトルは必須入力です")
	private String title;

}