package com.project.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.domain.StudentService;
import org.apache.ibatis.annotations.Param;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学籍服务mapper
 * @date 2025/04/28 11:29
 */
public interface StudentServiceMapper extends BaseMapper<StudentService> {
    Page<StudentService> getServiceByTeacher(Page<StudentService> page, @Param("ew") StudentService studentService);
}
