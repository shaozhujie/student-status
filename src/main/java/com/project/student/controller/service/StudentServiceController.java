package com.project.student.controller.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.ResultCode;
import com.project.student.config.utils.ShiroUtils;
import com.project.student.domain.Result;
import com.project.student.domain.StudentService;
import com.project.student.domain.User;
import com.project.student.service.StudentServiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学籍服务controller
 * @date 2025/04/28 11:29
 */
@Controller
@ResponseBody
@RequestMapping("service")
public class StudentServiceController {

    @Autowired
    private StudentServiceService studentServiceService;

    /** 分页获取学籍服务 */
    @PostMapping("getStudentServicePage")
    public Result getStudentServicePage(@RequestBody StudentService studentService) {
        User userInfo = ShiroUtils.getUserInfo();
        if (userInfo.getUserType() == 1) {
            studentService.setUserId(userInfo.getId());
        }
        Page<StudentService> page = new Page<>(studentService.getPageNumber(),studentService.getPageSize());
        QueryWrapper<StudentService> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(studentService.getUserId() != null,StudentService::getUserId,studentService.getUserId())
                .eq(studentService.getType() != null,StudentService::getType,studentService.getType())
                .like(StringUtils.isNotBlank(studentService.getContent()),StudentService::getContent,studentService.getContent())
                .eq(studentService.getState() != null,StudentService::getState,studentService.getState())
                .like(StringUtils.isNotBlank(studentService.getCreateBy()),StudentService::getCreateBy,studentService.getCreateBy());
        Page<StudentService> studentServicePage = studentServiceService.page(page, queryWrapper);
        return Result.success(studentServicePage);
    }

    @PostMapping("getServiceByTeacher")
    public Result getServiceByTeacher(@RequestBody StudentService studentService) {
        User userInfo = ShiroUtils.getUserInfo();
        if (StringUtils.isNotBlank(userInfo.getClassId())) {
            studentService.setClassIds(Arrays.asList(userInfo.getClassId().split(",")));
        }
        Page<StudentService> studentServicePage = studentServiceService.getServiceByTeacher(studentService);
        return Result.success(studentServicePage);
    }

    /** 根据id获取学籍服务 */
    @GetMapping("getStudentServiceById")
    public Result getStudentServiceById(@RequestParam("id")String id) {
        StudentService studentService = studentServiceService.getById(id);
        return Result.success(studentService);
    }

    /** 保存学籍服务 */
    @PostMapping("saveStudentService")
    public Result saveStudentService(@RequestBody StudentService studentService) {
        User userInfo = ShiroUtils.getUserInfo();
        studentService.setUserId(userInfo.getId());
        boolean save = studentServiceService.save(studentService);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑学籍服务 */
    @PostMapping("editStudentService")
    public Result editStudentService(@RequestBody StudentService studentService) {
        boolean save = studentServiceService.updateById(studentService);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除学籍服务 */
    @GetMapping("removeStudentService")
    public Result removeStudentService(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                studentServiceService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("学籍服务id不能为空！");
        }
    }

}
