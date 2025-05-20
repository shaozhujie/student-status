package com.project.student.controller.major;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.BusinessType;
import com.project.student.common.enums.ResultCode;
import com.project.student.domain.*;
import com.project.student.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 专业controller
 * @date 2025/04/23 10:19
 */
@Controller
@ResponseBody
@RequestMapping("major")
public class StudentMajorController {

    @Autowired
    private StudentMajorService studentMajorService;
    @Autowired
    private StudentCollegeService studentCollegeService;
    @Autowired
    private StudentClassService studentClassService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentStatusService studentStatusService;

    /** 分页获取专业 */
    @PostMapping("getStudentMajorPage")
    public Result getStudentMajorPage(@RequestBody StudentMajor studentMajor) {
        Page<StudentMajor> page = new Page<>(studentMajor.getPageNumber(),studentMajor.getPageSize());
        QueryWrapper<StudentMajor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(studentMajor.getCollegeId()),StudentMajor::getCollegeId,studentMajor.getCollegeId())
                .like(StringUtils.isNotBlank(studentMajor.getName()),StudentMajor::getName,studentMajor.getName());
        Page<StudentMajor> studentMajorPage = studentMajorService.page(page, queryWrapper);
        for (StudentMajor major : studentMajorPage.getRecords()) {
            StudentCollege college = studentCollegeService.getById(major.getCollegeId());
            major.setCollegeId(college.getName());
        }
        return Result.success(studentMajorPage);
    }

    @GetMapping("getStudentMajorList")
    public Result getStudentMajorList() {
        List<StudentMajor> majorList = studentMajorService.list();
        return Result.success(majorList);
    }

    @GetMapping("getStudentMajorByCollegeId")
    public Result getStudentMajorByCollegeId(@RequestParam("collegeId")String collegeId) {
        QueryWrapper<StudentMajor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(collegeId),StudentMajor::getCollegeId,collegeId);
        List<StudentMajor> majorList = studentMajorService.list(queryWrapper);
        return Result.success(majorList);
    }

    /** 根据id获取专业 */
    @GetMapping("getStudentMajorById")
    public Result getStudentMajorById(@RequestParam("id")String id) {
        StudentMajor studentMajor = studentMajorService.getById(id);
        return Result.success(studentMajor);
    }

    /** 保存专业 */
    @PostMapping("saveStudentMajor")
    public Result saveStudentMajor(@RequestBody StudentMajor studentMajor) {
        boolean save = studentMajorService.save(studentMajor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑专业 */
    @PostMapping("editStudentMajor")
    public Result editStudentMajor(@RequestBody StudentMajor studentMajor) {
        boolean save = studentMajorService.updateById(studentMajor);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除专业 */
    @GetMapping("removeStudentMajor")
    public Result removeStudentMajor(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                QueryWrapper<StudentClass> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(StudentClass::getMajorId,id);
                int count = studentClassService.count(queryWrapper);
                if (count > 0) {
                    return Result.fail("专业下存在班级无法删除");
                }
                QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(User::getMajorId,id);
                int count1 = userService.count(queryWrapper1);
                if (count1 > 0) {
                    return Result.fail("专业下存在用户无法删除");
                }
                QueryWrapper<StudentStatus> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(StudentStatus::getMajorId,id);
                int count2 = studentStatusService.count(queryWrapper2);
                if (count2 > 0) {
                    return Result.fail("专业下存在学籍信息无法删除");
                }
                studentMajorService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("专业id不能为空！");
        }
    }

}