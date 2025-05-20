package com.project.student.controller.enroll;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.student.common.enums.BusinessType;
import com.project.student.common.enums.ResultCode;
import com.project.student.domain.Result;
import com.project.student.domain.StudentEnroll;
import com.project.student.service.StudentEnrollService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 招生信息controller
 * @date 2025/04/28 11:17
 */
@Controller
@ResponseBody
@RequestMapping("enroll")
public class StudentEnrollController {

    @Autowired
    private StudentEnrollService studentEnrollService;

    /** 分页获取招生信息 */
    @PostMapping("getStudentEnrollPage")
    public Result getStudentEnrollPage(@RequestBody StudentEnroll studentEnroll) {
        Page<StudentEnroll> page = new Page<>(studentEnroll.getPageNumber(),studentEnroll.getPageSize());
        QueryWrapper<StudentEnroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(studentEnroll.getStudent()),StudentEnroll::getStudent,studentEnroll.getStudent())
                .like(StringUtils.isNotBlank(studentEnroll.getIdCard()),StudentEnroll::getIdCard,studentEnroll.getIdCard())
                .like(StringUtils.isNotBlank(studentEnroll.getAddress()),StudentEnroll::getAddress,studentEnroll.getAddress())
                .like(StringUtils.isNotBlank(studentEnroll.getName()),StudentEnroll::getName,studentEnroll.getName())
                .like(StringUtils.isNotBlank(studentEnroll.getTel()),StudentEnroll::getTel,studentEnroll.getTel())
                .eq(studentEnroll.getState() != null,StudentEnroll::getState,studentEnroll.getState());
        Page<StudentEnroll> studentEnrollPage = studentEnrollService.page(page, queryWrapper);
        return Result.success(studentEnrollPage);
    }

    /** 根据id获取招生信息 */
    @GetMapping("getStudentEnrollById")
    public Result getStudentEnrollById(@RequestParam("id")String id) {
        StudentEnroll studentEnroll = studentEnrollService.getById(id);
        return Result.success(studentEnroll);
    }

    /** 保存招生信息 */
    @PostMapping("saveStudentEnroll")
    public Result saveStudentEnroll(@RequestBody StudentEnroll studentEnroll) {
        boolean save = studentEnrollService.save(studentEnroll);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑招生信息 */
    @PostMapping("editStudentEnroll")
    public Result editStudentEnroll(@RequestBody StudentEnroll studentEnroll) {
        boolean save = studentEnrollService.updateById(studentEnroll);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除招生信息 */
    @GetMapping("removeStudentEnroll")
    public Result removeStudentEnroll(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                studentEnrollService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("招生信息id不能为空！");
        }
    }

    @PostMapping("exportEnroll")
    public Result exportEnroll(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return Result.fail("上传的excel不能为空!");
        }
        String coverType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if ("xls".equals(coverType)  || "xlsx".equals(coverType)) {
            List<StudentEnroll> employeeList = EasyExcel.read(file.getInputStream(), StudentEnroll.class, new EmployeeListener()).sheet().doReadSync();
            return Result.success();
        } else {
            return Result.fail("请上传正确的excel表格");
        }
    }

    public class EmployeeListener extends AnalysisEventListener<StudentEnroll> {
        @Override
        public void invoke(StudentEnroll employee, AnalysisContext context) {
            // 这里可以对读取到的每一条数据进行处理
            System.out.println("读取到一行数据:" + employee);
            studentEnrollService.save(employee);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 所有数据解析完成后会调用此方法
            System.out.println("所有数据解析完成");
        }
    }

}
