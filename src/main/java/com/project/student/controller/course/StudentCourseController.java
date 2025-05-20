package com.project.student.controller.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.BusinessType;
import com.project.student.common.enums.ResultCode;
import com.project.student.domain.Result;
import com.project.student.domain.StudentCourse;
import com.project.student.domain.StudentScore;
import com.project.student.service.StudentCourseService;
import com.project.student.service.StudentScoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程controller
 * @date 2025/04/28 08:51
 */
@Controller
@ResponseBody
@RequestMapping("course")
public class StudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired
    private StudentScoreService studentScoreService;

    /** 分页获取课程 */
    @PostMapping("getStudentCoursePage")
    public Result getStudentCoursePage(@RequestBody StudentCourse studentCourse) {
        Page<StudentCourse> page = new Page<>(studentCourse.getPageNumber(),studentCourse.getPageSize());
        QueryWrapper<StudentCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(studentCourse.getName()),StudentCourse::getName,studentCourse.getName())
                .orderByDesc(StudentCourse::getCreateTime);
        Page<StudentCourse> studentCoursePage = studentCourseService.page(page, queryWrapper);
        return Result.success(studentCoursePage);
    }

    @GetMapping("getStudentCourseList")
    public Result getStudentCourseList() {
        List<StudentCourse> list = studentCourseService.list();
        return Result.success(list);
    }

    /** 根据id获取课程 */
    @GetMapping("getStudentCourseById")
    public Result getStudentCourseById(@RequestParam("id")String id) {
        StudentCourse studentCourse = studentCourseService.getById(id);
        return Result.success(studentCourse);
    }

    /** 保存课程 */
    @PostMapping("saveStudentCourse")
    public Result saveStudentCourse(@RequestBody StudentCourse studentCourse) {
        boolean save = studentCourseService.save(studentCourse);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑课程 */
    @PostMapping("editStudentCourse")
    public Result editStudentCourse(@RequestBody StudentCourse studentCourse) {
        boolean save = studentCourseService.updateById(studentCourse);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除课程 */
    @GetMapping("removeStudentCourse")
    public Result removeStudentCourse(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                studentCourseService.removeById(id);
                QueryWrapper<StudentScore> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(StudentScore::getTaskId,id);
                int count = studentScoreService.count(queryWrapper);
                if (count > 0) {
                    return Result.fail("该课程存在成绩无法删除");
                }
            }
            return Result.success();
        } else {
            return Result.fail("课程id不能为空！");
        }
    }

}
