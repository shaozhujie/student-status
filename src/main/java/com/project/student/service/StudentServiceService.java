package com.project.student.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.student.domain.StudentService;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学籍服务service
 * @date 2025/04/28 11:29
 */
public interface StudentServiceService extends IService<StudentService> {
    Page<StudentService> getServiceByTeacher(StudentService studentService);
}
