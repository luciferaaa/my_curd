package com.hxkj.auth.model;

import com.hxkj.auth.model.base.BaseAuthRoleMenu;

import java.util.List;

/**
 * model table: auth_role_menu   角色权限中间表
 *
 * @author
 * @date 2018-06-20 18:56:48
 */
public class AuthRoleMenu extends BaseAuthRoleMenu<AuthRoleMenu> implements java.io.Serializable {

    public static final AuthRoleMenu dao = new AuthRoleMenu().dao();
    private static final long serialVersionUID = 1L;

    /**
     * 根据 roleid 字段查询
     *
     * @param roleId
     * @return
     */
    public List<AuthRoleMenu> findByRoleId(Long roleId) {
        String sql = "select * from auth_role_menu where role_id = ? ";
        return find(sql, roleId);
    }

}
