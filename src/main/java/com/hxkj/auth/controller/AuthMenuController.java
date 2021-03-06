package com.hxkj.auth.controller;

import com.hxkj.auth.model.AuthMenu;
import com.hxkj.common.constant.Constant;
import com.hxkj.common.util.BaseController;
import com.hxkj.common.util.search.SearchSql;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.*;

/**
 * 系统菜单管理
 *
 * @author Administrator
 */
public class AuthMenuController extends BaseController {


    /**
     * 列表页
     */
    public void index() {
        render("auth/authMenu.html");
    }

    /**
     * 列表页数据
     */
    @Before(SearchSql.class)
    public void query() {
        String where = getAttr(Constant.SEARCH_SQL);
        List<AuthMenu> authMenus = AuthMenu.dao.findWhere(where);
        renderJson(authMenus);
    }


    /**
     * 新增 或者 编辑  form
     */
    public void newModel() {
        Long id = getParaToLong("id");
        if (id != null) {
            AuthMenu authMenu = AuthMenu.dao.findById(id);
            setAttr("authMenu", authMenu);
        }
        render("auth/authMenu_form.html");
    }


    /**
     * 新增
     */
    public void addAction() {
        AuthMenu authMenu = getBean(AuthMenu.class, "");
        // pid 为 0 代表 根节点
        if (authMenu.getPid() == null) {
            authMenu.setPid(0L);
        }
        authMenu.setCreateTime(new Date());
        boolean saveFlag = authMenu.save();
        if (saveFlag) {
            renderText(Constant.ADD_SUCCESS);
        } else {
            renderText(Constant.ADD_FAIL);
        }
    }

    /**
     * 修改
     */
    public void updateAction() {
        AuthMenu authMenu = getBean(AuthMenu.class, "");
        // pid 为  0 代表 根节点
        if (authMenu.getPid() == null) {
            authMenu.setPid(0L);
        }
        authMenu.setLastEditTime(new Date());
        boolean updateFlag = authMenu.update();
        if (updateFlag) {
            renderText(Constant.UPDATE_SUCCESS);
        } else {
            renderText(Constant.UPDATE_FAIL);
        }

    }


    @Before(Tx.class)
    public void deleteAction() {
        Long id = getParaToLong("id");

        //删除当前菜单节点以及子孙节点
        Record record = Db.findFirst("select getChildIdList(?,'auth_menu') as childrenIds ", id);
        String childrenIds = record.getStr("childrenIds");  // 子、孙 id
        String deleteSql = "delete from auth_menu where id in (" + childrenIds + ")";
        Db.update(deleteSql);

        //删除当前菜单节点以及子节点 关联的 角色关系
        deleteSql = "delete from auth_role_menu where menu_id in (" + childrenIds + ") ";
        Db.update(deleteSql);
        renderText(Constant.DELETE_SUCCESS);
    }

    /**
     * 所有菜单
     */
    public void allMenu() {
        List<AuthMenu> authMenus = AuthMenu.dao.findAll();
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (AuthMenu authMenu : authMenus) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", authMenu.getId());
            map.put("pid", authMenu.getPid());
            map.put("text", authMenu.getName());
            map.put("iconCls", authMenu.getIcon());
            map.put("open", true);
            maps.add(map);
        }
        renderJson(maps);
    }

}
