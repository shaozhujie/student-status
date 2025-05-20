package com.project.student.controller.graduation;

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
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 毕业服务controller
 * @date 2025/04/29 08:48
 */
@Controller
@ResponseBody
@RequestMapping("graduation")
public class StudentGraduationController {

    @Autowired
    private StudentGraduationService studentGraduationService;
    @Autowired
    private StudentScoreService studentScoreService;
    @Autowired
    private StudentDefenseService studentDefenseService;
    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired
    private UserService userService;

    /** 分页获取毕业服务 */
    @PostMapping("getStudentGraduationPage")
    public Result getStudentGraduationPage(@RequestBody StudentGraduation studentGraduation) {
        User userInfo = ShiroUtils.getUserInfo();
        if (userInfo.getUserType() == 1) {
            studentGraduation.setUserId(userInfo.getId());
        }
        Page<StudentGraduation> page = new Page<>(studentGraduation.getPageNumber(),studentGraduation.getPageSize());
        QueryWrapper<StudentGraduation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(studentGraduation.getUserId()),StudentGraduation::getUserId,studentGraduation.getUserId())
                .eq(studentGraduation.getState() != null,StudentGraduation::getState,studentGraduation.getState())
                .eq(StringUtils.isNotBlank(studentGraduation.getCreateBy()),StudentGraduation::getCreateBy,studentGraduation.getCreateBy());
        Page<StudentGraduation> studentGraduationPage = studentGraduationService.page(page, queryWrapper);
        return Result.success(studentGraduationPage);
    }

    @PostMapping("getStudentGraduationByTeacher")
    public Result getStudentGraduationByTeacher(@RequestBody StudentGraduation studentGraduation) {
        User userInfo = ShiroUtils.getUserInfo();
        if (StringUtils.isNotBlank(userInfo.getClassId())) {
            studentGraduation.setClassIds(Arrays.asList(userInfo.getClassId().split(",")));
        }
        Page<StudentGraduation> studentGraduationPage = studentGraduationService.getStudentGraduationByTeacher(studentGraduation);
        for (StudentGraduation graduation : studentGraduationPage.getRecords()) {
            QueryWrapper<StudentDefense> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(StudentDefense::getUserId,graduation.getUserId()).orderByDesc(StudentDefense::getCreateTime).last("limit 1");
            StudentDefense defense = studentDefenseService.getOne(queryWrapper);
            if (defense == null) {
                graduation.setDesign(0F);
            } else {
                graduation.setDesign(defense.getScore());
            }
            //获取成绩
            QueryWrapper<StudentScore> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(StudentScore::getStudentId,graduation.getUserId());
            List<StudentScore> scoreList = studentScoreService.list(wrapper);
            Float total = 0f;
            for (StudentScore score : scoreList) {
                if (score.getScore() >= 60) {
                    StudentCourse course = studentCourseService.getById(score.getTaskId());
                    total += course.getScore();
                }
            }
            graduation.setScore(total);
        }
        return Result.success(studentGraduationPage);
    }

    /** 根据id获取毕业服务 */
    @GetMapping("getStudentGraduationById")
    public Result getStudentGraduationById(@RequestParam("id")String id) {
        StudentGraduation studentGraduation = studentGraduationService.getById(id);
        return Result.success(studentGraduation);
    }

    /** 保存毕业服务 */
    @PostMapping("saveStudentGraduation")
    public Result saveStudentGraduation(@RequestBody StudentGraduation studentGraduation) {
        User userInfo = ShiroUtils.getUserInfo();
        studentGraduation.setUserId(userInfo.getId());
        boolean save = studentGraduationService.save(studentGraduation);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑毕业服务 */
    @PostMapping("editStudentGraduation")
    @Transactional(rollbackFor = Exception.class)
    public Result editStudentGraduation(@RequestBody StudentGraduation studentGraduation) {
        User userInfo = ShiroUtils.getUserInfo();
        StudentGraduation graduation = studentGraduationService.getById(studentGraduation.getId());
        if (userInfo.getUserType() == 0) {
            //如果是管理员通过后更改用户状态
            if (studentGraduation.getState() == 3) {
                User user = userService.getById(graduation.getUserId());
                user.setGraduation(1);
                user.setStatus(1);
                userService.updateById(user);
            }
        }
        boolean save = studentGraduationService.updateById(studentGraduation);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除毕业服务 */
    @GetMapping("removeStudentGraduation")
    public Result removeStudentGraduation(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                studentGraduationService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("毕业服务id不能为空！");
        }
    }

}