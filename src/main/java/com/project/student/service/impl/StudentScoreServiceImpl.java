package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.StudentScore;
import com.project.student.mapper.StudentScoreMapper;
import com.project.student.service.StudentScoreService;
import org.springframework.stereotype.Service;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 成绩service实现类
 * @date 2025/04/28 10:28
 */
@Service
public class StudentScoreServiceImpl extends ServiceImpl<StudentScoreMapper, StudentScore> implements StudentScoreService {
    @Override
    public Page<StudentScore> getTeacherScorePage(StudentScore studentScore) {
        Page<StudentScore> page = new Page<>(studentScore.getPageNumber(),studentScore.getPageSize());
        Page<StudentScore> studentScorePage = baseMapper.getTeacherScorePage(page,studentScore);
        return studentScorePage;
    }
}
