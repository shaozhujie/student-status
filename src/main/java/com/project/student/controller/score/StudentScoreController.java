package com.project.student.controller.score;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.ResultCode;
import com.project.student.config.utils.ShiroUtils;
import com.project.student.domain.*;
import com.project.student.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 成绩controller
 * @date 2025/04/28 10:28
 */
@Controller
@ResponseBody
@RequestMapping("score")
public class StudentScoreController {

    @Autowired
    private StudentScoreService studentScoreService;
    @Autowired
    private StudentSemesterService studentSemesterService;
    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired
    private UserService userService;

    /** 分页获取成绩 */
    @PostMapping("getStudentScorePage")
    public Result getStudentScorePage(@RequestBody StudentScore studentScore) {
        User userInfo = ShiroUtils.getUserInfo();
        if (userInfo.getUserType() == 1) {
            studentScore.setStudentId(userInfo.getId());
        }
        Page<StudentScore> page = new Page<>(studentScore.getPageNumber(),studentScore.getPageSize());
        QueryWrapper<StudentScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(studentScore.getTaskId()),StudentScore::getTaskId,studentScore.getTaskId())
                .eq(StringUtils.isNotBlank(studentScore.getSemesterId()),StudentScore::getSemesterId,studentScore.getSemesterId())
                .eq(StringUtils.isNotBlank(studentScore.getStudentId()),StudentScore::getStudentId,studentScore.getStudentId());
        Page<StudentScore> studentScorePage = studentScoreService.page(page, queryWrapper);
        for (StudentScore score : studentScorePage.getRecords()) {
            StudentSemester semester = studentSemesterService.getById(score.getSemesterId());
            score.setSemesterId(semester.getName());
            StudentCourse course = studentCourseService.getById(score.getTaskId());
            score.setTaskId(course.getName());
            User user = userService.getById(score.getStudentId());
            score.setStudentId(user.getUserName());
        }
        return Result.success(studentScorePage);
    }

    @PostMapping("getTeacherScorePage")
    public Result getTeacherScorePage(@RequestBody StudentScore studentScore) {
        User userInfo = ShiroUtils.getUserInfo();
        if (StringUtils.isNotBlank(userInfo.getClassId())) {
            studentScore.setClassIds(Arrays.asList(userInfo.getClassId().split(",")));
        }
        Page<StudentScore> studentScorePage = studentScoreService.getTeacherScorePage(studentScore);
        for (StudentScore score : studentScorePage.getRecords()) {
            StudentSemester semester = studentSemesterService.getById(score.getSemesterId());
            score.setSemesterId(semester.getName());
            StudentCourse course = studentCourseService.getById(score.getTaskId());
            score.setTaskId(course.getName());
        }
        return Result.success(studentScorePage);
    }

    /** 根据id获取成绩 */
    @GetMapping("getStudentScoreById")
    public Result getStudentScoreById(@RequestParam("id")String id) {
        StudentScore studentScore = studentScoreService.getById(id);
        return Result.success(studentScore);
    }

    /** 保存成绩 */
    @PostMapping("saveStudentScore")
    public Result saveStudentScore(@RequestBody StudentScore studentScore) {
        boolean save = studentScoreService.save(studentScore);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑成绩 */
    @PostMapping("editStudentScore")
    public Result editStudentScore(@RequestBody StudentScore studentScore) {
        boolean save = studentScoreService.updateById(studentScore);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除成绩 */
    @GetMapping("removeStudentScore")
    public Result removeStudentScore(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                studentScoreService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("成绩id不能为空！");
        }
    }

}