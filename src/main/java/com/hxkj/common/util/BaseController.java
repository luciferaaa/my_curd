package com.hxkj.common.util;

import com.hxkj.auth.model.AuthUser;
import com.hxkj.common.constant.Constant;
import com.hxkj.sys.model.SysOplog;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseController extends Controller {

    protected void renderDatagrid(Page<?> pageData) {
        Map<String, Object> datagrid = new HashMap<String, Object>();
        datagrid.put("rows", pageData.getList());
        datagrid.put("total", pageData.getTotalRow());
        renderJson(datagrid);
    }

    protected void renderDatagrid(List<?> list, int total) {
        renderDatagrid(list, total, null);
    }

    protected void renderDatagrid(List<?> list, int total, List<Map<String, Object>> footer) {
        Map<String, Object> datagrid = new HashMap<String, Object>();
        datagrid.put("rows", list);
        datagrid.put("total", total);
        if (footer != null && footer.size() > 0) {
            datagrid.put("footer", footer);
        }
        renderJson(datagrid);
    }

    protected void renderDatagrid(List<Record> list) {
        Map<String, Object> datagrid = new HashMap<String, Object>();
        datagrid.put("rows", list);
        renderJson(datagrid);
    }


    /**
     * 返回操作 状态信息
     *
     * @param msg
     */
    protected void renderSuccess(String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", "success");
        result.put("msg", msg);
        renderJson(result);
    }

    protected void renderSuccess() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", "success");
        renderJson(result);
    }

    protected void renderFailed(String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", "fail");
        result.put("msg", msg);
        renderJson(result);
    }

    protected void renderFailed() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", "fail");
        renderJson(result);
    }


    /**
     * 当前登录的系统用户
     *
     * @return
     */
    protected AuthUser getSessionUser() {
        return getSessionAttr(Constant.AUTH_USER);
    }

    /**
     * 增加操作日志
     *
     * @param opContent 操作内容
     */
    public void addOpLog(String opContent) {
        SysOplog sysOplog = new SysOplog();
        sysOplog.setId(Identities.id());
        sysOplog.setUserId(getSessionUser().get("id"));
        sysOplog.setOpContent(opContent);
        sysOplog.setIp(getRemoteAddress());
        sysOplog.setCreateTime(new Date());
        sysOplog.save();
    }

    /**
     * 获得ip地址
     */
    protected String getRemoteAddress() {
        String ip = getRequest().getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = getRequest().getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = getRequest().getRemoteAddr();
        }
        return ip;
    }
}

