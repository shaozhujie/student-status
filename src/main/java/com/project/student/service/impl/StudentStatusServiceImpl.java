package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.StudentStatus;
import com.project.student.mapper.StudentStatusMapper;
import com.project.student.service.StudentStatusService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学籍service实现类
 * @date 2025/04/28 09:29
 */
@Service
public class StudentStatusServiceImpl extends ServiceImpl<StudentStatusMapper, StudentStatus> implements StudentStatusService {
}
