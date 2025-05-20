package com.project.student.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.student.domain.StudentGraduation;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 毕业服务service
 * @date 2025/04/29 08:48
 */
public interface StudentGraduationService extends IService<StudentGraduation> {
    Page<StudentGraduation> getStudentGraduationByTeacher(StudentGraduation studentGraduation);
}
