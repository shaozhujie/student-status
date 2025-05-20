package com.project.student.controller.classes;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.BusinessType;
import com.project.student.common.enums.ResultCode;
import com.project.student.config.utils.ShiroUtils;
import com.project.student.domain.*;
import com.project.student.service.StudentClassService;
import com.project.student.service.StudentMajorService;
import com.project.student.service.StudentStatusService;
import com.project.student.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 班级controller
 * @date 2025/04/23 10:45
 */
@Controller
@ResponseBody
@RequestMapping("class")
public class StudentClassController {

    @Autowired
    private StudentClassService studentClassService;
    @Autowired
    private StudentMajorService studentMajorService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentStatusService studentStatusService;

    /** 分页获取班级 */
    @PostMapping("getStudentClassPage")
    public Result getStudentClassPage(@RequestBody StudentClass studentClass) {
        User userInfo = ShiroUtils.getUserInfo();
        Page<StudentClass> page = new Page<>(studentClass.getPageNumber(),studentClass.getPageSize());
        QueryWrapper<StudentClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(studentClass.getMajorId()),StudentClass::getMajorId,studentClass.getMajorId())
                .like(StringUtils.isNotBlank(studentClass.getName()),StudentClass::getName,studentClass.getName());
        if (userInfo.getUserType() == 2) {
            String classId = userInfo.getClassId();
            String[] split = classId.split(",");
            queryWrapper.lambda().in(StudentClass::getId,split);
        }
        Page<StudentClass> studentClassPage = studentClassService.page(page, queryWrapper);
        for (StudentClass classes : studentClassPage.getRecords()) {
            StudentMajor major = studentMajorService.getById(classes.getMajorId());
            classes.setMajorId(major.getName());
        }
        return Result.success(studentClassPage);
    }

    @GetMapping("getStudentClassByMajorId")
    public Result getStudentClassByMajorId(@RequestParam("majorId") String majorId) {
        QueryWrapper<StudentClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(majorId),StudentClass::getMajorId,majorId);
        List<StudentClass> classList = studentClassService.list(queryWrapper);
        return Result.success(classList);
    }

    /** 根据id获取班级 */
    @GetMapping("getStudentClassById")
    public Result getStudentClassById(@RequestParam("id")String id) {
        StudentClass studentClass = studentClassService.getById(id);
        return Result.success(studentClass);
    }

    @GetMapping("getStudentClassList")
    public Result getStudentClassList() {
        List<StudentClass> classList = studentClassService.list();
        return Result.success(classList);
    }

    @GetMapping("getClassByTeacher")
    public Result getClassByTeacher() {
        User userInfo = ShiroUtils.getUserInfo();
        String classId = userInfo.getClassId();
        if (StringUtils.isNotBlank(classId)) {
            String[] split = classId.split(",");
            QueryWrapper<StudentClass> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .in(StudentClass::getId,split);
            List<StudentClass> classes = studentClassService.list(queryWrapper);
            return Result.success(classes);
        } else {
            ArrayList<User> arrayList = new ArrayList<>();
            return Result.success(arrayList);
        }
    }

    /** 保存班级 */
    @PostMapping("saveStudentClass")
    public Result saveStudentClass(@RequestBody StudentClass studentClass) {
        boolean save = studentClassService.save(studentClass);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑班级 */
    @PostMapping("editStudentClass")
    public Result editStudentClass(@RequestBody StudentClass studentClass) {
        boolean save = studentClassService.updateById(studentClass);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除班级 */
    @GetMapping("removeStudentClass")
    public Result removeStudentClass(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().like(User::getClassId,id);
                int count1 = userService.count(queryWrapper1);
                if (count1 > 0) {
                    return Result.fail("班级下存在用户无法删除");
                }
                QueryWrapper<StudentStatus> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(StudentStatus::getClassId,id);
                int count2 = studentStatusService.count(queryWrapper2);
                if (count2 > 0) {
                    return Result.fail("班级下存在学籍信息无法删除");
                }
                studentClassService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("班级id不能为空！");
        }
    }

}
