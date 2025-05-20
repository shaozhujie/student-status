package com.project.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.domain.StudentScore;
import org.apache.ibatis.annotations.Param;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 成绩mapper
 * @date 2025/04/28 10:28
 */
public interface StudentScoreMapper extends BaseMapper<StudentScore> {
    Page<StudentScore> getTeacherScorePage(Page<StudentScore> page, @Param("ew") StudentScore studentScore);
}
