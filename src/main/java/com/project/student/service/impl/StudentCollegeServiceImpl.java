package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.StudentCollege;
import com.project.student.mapper.StudentCollegeMapper;
import com.project.student.service.StudentCollegeService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学院service实现类
 * @date 2025/04/23 10:07
 */
@Service
public class StudentCollegeServiceImpl extends ServiceImpl<StudentCollegeMapper, StudentCollege> implements StudentCollegeService {
}
