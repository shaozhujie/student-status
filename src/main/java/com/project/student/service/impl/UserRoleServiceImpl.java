package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.UserRole;
import com.project.student.mapper.UserRoleMapper;
import com.project.student.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 用户角色关系service实现类
 * @date 2023/8/31 14:37
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    /**
     * 根据账号获取角色
     */
    @Override
    public Set<String> getUserRolesSet(String loginAccount) {
        return baseMapper.getUserRolesSet(loginAccount);
    }

}
