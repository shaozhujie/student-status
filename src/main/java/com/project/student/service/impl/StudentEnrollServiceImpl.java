package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.StudentEnroll;
import com.project.student.mapper.StudentEnrollMapper;
import com.project.student.service.StudentEnrollService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 招生信息service实现类
 * @date 2025/04/28 11:17
 */
@Service
public class StudentEnrollServiceImpl extends ServiceImpl<StudentEnrollMapper, StudentEnroll> implements StudentEnrollService {
}