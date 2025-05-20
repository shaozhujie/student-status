package com.project.student.controller.status;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.ResultCode;
import com.project.student.domain.*;
import com.project.student.service.StudentClassService;
import com.project.student.service.StudentCollegeService;
import com.project.student.service.StudentMajorService;
import com.project.student.service.StudentStatusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学籍controller
 * @date 2025/04/28 09:29
 */
@Controller
@ResponseBody
@RequestMapping("status")
public class StudentStatusController {

    @Autowired
    private StudentStatusService studentStatusService;
    @Autowired
    private StudentCollegeService studentCollegeService;
    @Autowired
    private StudentMajorService studentMajorService;
    @Autowired
    private StudentClassService studentClassService;

    /** 分页获取学籍 */
    @PostMapping("getStudentStatusPage")
    public Result getStudentStatusPage(@RequestBody StudentStatus studentStatus) {
        Page<StudentStatus> page = new Page<>(studentStatus.getPageNumber(),studentStatus.getPageSize());
        QueryWrapper<StudentStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(studentStatus.getIdCard()),StudentStatus::getIdCard,studentStatus.getIdCard())
                .like(StringUtils.isNotBlank(studentStatus.getAddress()),StudentStatus::getAddress,studentStatus.getAddress())
                .like(StringUtils.isNotBlank(studentStatus.getName()),StudentStatus::getName,studentStatus.getName())
                .like(StringUtils.isNotBlank(studentStatus.getIdNumber()),StudentStatus::getIdNumber,studentStatus.getIdNumber())
                .eq(StringUtils.isNotBlank(studentStatus.getCollegeId()),StudentStatus::getCollegeId,studentStatus.getCollegeId())
                .eq(StringUtils.isNotBlank(studentStatus.getMajorId()),StudentStatus::getMajorId,studentStatus.getMajorId())
                .eq(StringUtils.isNotBlank(studentStatus.getClassId()),StudentStatus::getClassId,studentStatus.getClassId())
                .eq(StringUtils.isNotBlank(studentStatus.getCreateBy()),StudentStatus::getCreateBy,studentStatus.getCreateBy())
                .orderByDesc(StudentStatus::getCreateTime);
        Page<StudentStatus> studentStatusPage = studentStatusService.page(page, queryWrapper);
        for (StudentStatus status : studentStatusPage.getRecords()) {
            StudentCollege college = studentCollegeService.getById(status.getCollegeId());
            status.setCollegeId(college.getName());
            StudentMajor major = studentMajorService.getById(status.getMajorId());
            status.setMajorId(major.getName());
            StudentClass aClass = studentClassService.getById(status.getClassId());
            status.setClassId(aClass.getName());
        }
        return Result.success(studentStatusPage);
    }

    /** 根据id获取学籍 */
    @GetMapping("getStudentStatusById")
    public Result getStudentStatusById(@RequestParam("id")String id) {
        StudentStatus studentStatus = studentStatusService.getById(id);
        return Result.success(studentStatus);
    }

    /** 保存学籍 */
    @PostMapping("saveStudentStatus")
    public Result saveStudentStatus(@RequestBody StudentStatus studentStatus) {
        boolean save = studentStatusService.save(studentStatus);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑学籍 */
    @PostMapping("editStudentStatus")
    public Result editStudentStatus(@RequestBody StudentStatus studentStatus) {
        boolean save = studentStatusService.updateById(studentStatus);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除学籍 */
    @GetMapping("removeStudentStatus")
    public Result removeStudentStatus(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                studentStatusService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("学籍id不能为空！");
        }
    }

}
