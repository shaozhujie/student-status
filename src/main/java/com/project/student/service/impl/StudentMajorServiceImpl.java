package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.StudentMajor;
import com.project.student.mapper.StudentMajorMapper;
import com.project.student.service.StudentMajorService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 专业service实现类
 * @date 2025/04/23 10:19
 */
@Service
public class StudentMajorServiceImpl extends ServiceImpl<StudentMajorMapper, StudentMajor> implements StudentMajorService {
}
