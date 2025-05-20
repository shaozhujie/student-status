package com.project.student.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.student.domain.StudentScore;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 成绩service
 * @date 2025/04/28 10:28
 */
public interface StudentScoreService extends IService<StudentScore> {
    Page<StudentScore> getTeacherScorePage(StudentScore studentScore);
}
