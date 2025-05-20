package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.StudentGraduation;
import com.project.student.domain.StudentService;
import com.project.student.mapper.StudentGraduationMapper;
import com.project.student.service.StudentGraduationService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 毕业服务service实现类
 * @date 2025/04/29 08:48
 */
@Service
public class StudentGraduationServiceImpl extends ServiceImpl<StudentGraduationMapper, StudentGraduation> implements StudentGraduationService {
    @Override
    public Page<StudentGraduation> getStudentGraduationByTeacher(StudentGraduation studentGraduation) {
        Page<StudentGraduation> page = new Page<>(studentGraduation.getPageNumber(),studentGraduation.getPageSize());
        return baseMapper.getStudentGraduationByTeacher(page,studentGraduation);
    }
}
