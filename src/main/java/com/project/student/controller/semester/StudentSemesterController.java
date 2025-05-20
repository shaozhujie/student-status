package com.project.student.controller.semester;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.ResultCode;
import com.project.student.domain.Result;
import com.project.student.domain.StudentScore;
import com.project.student.domain.StudentSemester;
import com.project.student.service.StudentScoreService;
import com.project.student.service.StudentSemesterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学期controller
 * @date 2025/04/28 10:13
 */
@Controller
@ResponseBody
@RequestMapping("semester")
public class StudentSemesterController {

    @Autowired
    private StudentSemesterService studentSemesterService;
    @Autowired
    private StudentScoreService studentScoreService;

    /** 分页获取学期 */
    @PostMapping("getStudentSemesterPage")
    public Result getStudentSemesterPage(@RequestBody StudentSemester studentSemester) {
        Page<StudentSemester> page = new Page<>(studentSemester.getPageNumber(),studentSemester.getPageSize());
        QueryWrapper<StudentSemester> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(studentSemester.getName()),StudentSemester::getName,studentSemester.getName());
        Page<StudentSemester> studentSemesterPage = studentSemesterService.page(page, queryWrapper);
        return Result.success(studentSemesterPage);
    }

    @GetMapping("getStudentSemesterList")
    public Result getStudentSemesterList() {
        List<StudentSemester> list = studentSemesterService.list();
        return Result.success(list);
    }

    /** 根据id获取学期 */
    @GetMapping("getStudentSemesterById")
    public Result getStudentSemesterById(@RequestParam("id")String id) {
        StudentSemester studentSemester = studentSemesterService.getById(id);
        return Result.success(studentSemester);
    }

    /** 保存学期 */
    @PostMapping("saveStudentSemester")
    public Result saveStudentSemester(@RequestBody StudentSemester studentSemester) {
        boolean save = studentSemesterService.save(studentSemester);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑学期 */
    @PostMapping("editStudentSemester")
    public Result editStudentSemester(@RequestBody StudentSemester studentSemester) {
        boolean save = studentSemesterService.updateById(studentSemester);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除学期 */
    @GetMapping("removeStudentSemester")
    public Result removeStudentSemester(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                QueryWrapper<StudentScore> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(StudentScore::getSemesterId,id);
                int count = studentScoreService.count(queryWrapper);
                if (count > 0) {
                    return Result.fail("学期下存在成绩信息无法删除");
                }
                studentSemesterService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("学期id不能为空！");
        }
    }

}
