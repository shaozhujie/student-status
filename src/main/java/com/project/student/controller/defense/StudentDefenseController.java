package com.project.student.controller.defense;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.ResultCode;
import com.project.student.config.utils.ShiroUtils;
import com.project.student.domain.Result;
import com.project.student.domain.StudentDefense;
import com.project.student.domain.User;
import com.project.student.service.StudentDefenseService;
import com.project.student.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 毕设controller
 * @date 2025/04/29 08:58
 */
@Controller
@ResponseBody
@RequestMapping("defense")
public class StudentDefenseController {

    @Autowired
    private StudentDefenseService studentDefenseService;
    @Autowired
    private UserService userService;

    /** 分页获取毕设 */
    @PostMapping("getStudentDefensePage")
    public Result getStudentDefensePage(@RequestBody StudentDefense studentDefense) {
        Page<StudentDefense> page = new Page<>(studentDefense.getPageNumber(),studentDefense.getPageSize());
        QueryWrapper<StudentDefense> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(studentDefense.getUserId()),StudentDefense::getUserId,studentDefense.getUserId());
        Page<StudentDefense> studentDefensePage = studentDefenseService.page(page, queryWrapper);
        for (StudentDefense defense : studentDefensePage.getRecords()) {
            User user = userService.getById(defense.getUserId());
            defense.setUserId(user.getUserName());
        }
        return Result.success(studentDefensePage);
    }

    /** 根据id获取毕设 */
    @GetMapping("getStudentDefenseById")
    public Result getStudentDefenseById(@RequestParam("id")String id) {
        StudentDefense studentDefense = studentDefenseService.getById(id);
        return Result.success(studentDefense);
    }

    /** 保存毕设 */
    @PostMapping("saveStudentDefense")
    public Result saveStudentDefense(@RequestBody StudentDefense studentDefense) {
        boolean save = studentDefenseService.save(studentDefense);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑毕设 */
    @PostMapping("editStudentDefense")
    public Result editStudentDefense(@RequestBody StudentDefense studentDefense) {
        boolean save = studentDefenseService.updateById(studentDefense);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除毕设 */
    @GetMapping("removeStudentDefense")
    public Result removeStudentDefense(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                studentDefenseService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("毕设id不能为空！");
        }
    }

}
