package com.project.student.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.student.domain.RoleMenu;
import com.project.student.mapper.RoleMenuMapper;
import com.project.student.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 角色菜单关系service实现类
 * @date 2023/8/31 10:57
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    /**
    *  根据角色获取权限
    */
    @Override
    public Set<String> getRoleMenusSet(String role) {
        return baseMapper.getRoleMenusSet(role);
    }
}
