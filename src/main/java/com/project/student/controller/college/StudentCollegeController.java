package com.project.student.controller.college;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.BusinessType;
import com.project.student.common.enums.ResultCode;
import com.project.student.domain.*;
import com.project.student.service.StudentCollegeService;
import com.project.student.service.StudentMajorService;
import com.project.student.service.StudentStatusService;
import com.project.student.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学院controller
 * @date 2025/04/23 10:07
 */
@Controller
@ResponseBody
@RequestMapping("college")
public class StudentCollegeController {

    @Autowired
    private StudentCollegeService studentCollegeService;
    @Autowired
    private StudentMajorService studentMajorService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentStatusService studentStatusService;

    /** 分页获取学院 */
    @PostMapping("getStudentCollegePage")
    public Result getStudentCollegePage(@RequestBody StudentCollege studentCollege) {
        Page<StudentCollege> page = new Page<>(studentCollege.getPageNumber(),studentCollege.getPageSize());
        QueryWrapper<StudentCollege> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(studentCollege.getName()),StudentCollege::getName,studentCollege.getName());
        Page<StudentCollege> studentCollegePage = studentCollegeService.page(page, queryWrapper);
        return Result.success(studentCollegePage);
    }

    @GetMapping("getStudentCollegeList")
    public Result getStudentCollegeList() {
        List<StudentCollege> collegeList = studentCollegeService.list();
        return Result.success(collegeList);
    }

    /** 根据id获取学院 */
    @GetMapping("getStudentCollegeById")
    public Result getStudentCollegeById(@RequestParam("id")String id) {
        StudentCollege studentCollege = studentCollegeService.getById(id);
        return Result.success(studentCollege);
    }

    /** 保存学院 */
    @PostMapping("saveStudentCollege")
    public Result saveStudentCollege(@RequestBody StudentCollege studentCollege) {
        boolean save = studentCollegeService.save(studentCollege);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑学院 */
    @PostMapping("editStudentCollege")
    public Result editStudentCollege(@RequestBody StudentCollege studentCollege) {
        boolean save = studentCollegeService.updateById(studentCollege);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除学院 */
    @GetMapping("removeStudentCollege")
    public Result removeStudentCollege(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                QueryWrapper<StudentMajor> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(StudentMajor::getCollegeId,id);
                int count = studentMajorService.count(queryWrapper);
                if (count > 0) {
                    return Result.fail("学院下存在专业无法删除");
                }
                QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(User::getCollegeId,id);
                int count1 = userService.count(queryWrapper1);
                if (count1 > 0) {
                    return Result.fail("学院下存在用户无法删除");
                }
                QueryWrapper<StudentStatus> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(StudentStatus::getCollegeId,id);
                int count2 = studentStatusService.count(queryWrapper2);
                if (count2 > 0) {
                    return Result.fail("学院下存在学籍信息无法删除");
                }
                studentCollegeService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("学院id不能为空！");
        }
    }

}