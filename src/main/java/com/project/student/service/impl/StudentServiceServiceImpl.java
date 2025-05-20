package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.StudentScore;
import com.project.student.domain.StudentService;
import com.project.student.mapper.StudentServiceMapper;
import com.project.student.service.StudentServiceService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学籍服务service实现类
 * @date 2025/04/28 11:29
 */
@Service
public class StudentServiceServiceImpl extends ServiceImpl<StudentServiceMapper, StudentService> implements StudentServiceService {
    @Override
    public Page<StudentService> getServiceByTeacher(StudentService studentService) {
        Page<StudentService> page = new Page<>(studentService.getPageNumber(),studentService.getPageSize());
        Page<StudentService> serviceByTeacher = baseMapper.getServiceByTeacher(page,studentService);
        return serviceByTeacher;
    }
}
