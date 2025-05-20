package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.StudentSemester;
import com.project.student.mapper.StudentSemesterMapper;
import com.project.student.service.StudentSemesterService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学期service实现类
 * @date 2025/04/28 10:13
 */
@Service
public class StudentSemesterServiceImpl extends ServiceImpl<StudentSemesterMapper, StudentSemester> implements StudentSemesterService {
}
