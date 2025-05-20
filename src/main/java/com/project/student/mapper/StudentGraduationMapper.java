package com.project.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.domain.StudentGraduation;
import org.apache.ibatis.annotations.Param;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 毕业服务mapper
 * @date 2025/04/29 08:48
 */
public interface StudentGraduationMapper extends BaseMapper<StudentGraduation> {
    Page<StudentGraduation> getStudentGraduationByTeacher(Page<StudentGraduation> page,@Param("ew") StudentGraduation studentGraduation);
}