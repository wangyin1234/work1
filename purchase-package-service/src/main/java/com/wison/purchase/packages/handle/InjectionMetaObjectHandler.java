package com.wison.purchase.packages.handle;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.comm.domain.BaseEntity;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.domain.UserInfo;
import com.wison.purchase.packages.comm.domain.UserInfoHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * MP注入处理器
 *
 * @author Lion Li
 */
@Slf4j
public class InjectionMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入填充方法，用于在插入数据时自动填充实体对象中的创建时间、更新时间、创建人、更新人等信息
     *
     * @param metaObject 元对象，用于获取原始对象并进行填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                // 获取当前时间作为创建时间和更新时间，如果创建时间不为空，则使用创建时间，否则使用当前时间
                Date current = ObjectUtil.isNotNull(baseEntity.getCreateTime())
                        ? baseEntity.getCreateTime() : new Date();
                baseEntity.setCreateTime(current);
                baseEntity.setUpdateTime(current);

                // 如果创建人为空，则填充当前登录用户的信息
                if (ObjectUtil.isNull(baseEntity.getCreateBy())) {
                    UserInfo loginUser = UserInfoHolder.getUserInfo();
                    if (ObjectUtil.isNotNull(loginUser)) {
                        // 填充创建人、更新人信息
                        baseEntity.setCreateBy(loginUser.getUserName() + "/" + loginUser.getLoginName());
                        baseEntity.setUpdateBy(loginUser.getUserName() + "/" + loginUser.getLoginName());
                    }
                }

                // 板块为空
                if (ObjectUtil.isNull(baseEntity.getZoneCode())) {
                    baseEntity.setZoneCode(HeadInfoHolder.getZoneCode());
                }
                // 项目为空
                if (ObjectUtil.isNull(baseEntity.getProjectCode())) {
                    baseEntity.setProjectCode(HeadInfoHolder.getProjectCode());
                }
            }
        } catch (Exception e) {
            throw new BusinessException("自动注入异常 => " + e.getMessage());
        }
    }

    /**
     * 更新填充方法，用于在更新数据时自动填充实体对象中的更新时间和更新人信息
     *
     * @param metaObject 元对象，用于获取原始对象并进行填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                // 获取当前时间作为更新时间，无论原始对象中的更新时间是否为空都填充
                Date current = new Date();
                baseEntity.setUpdateTime(current);

                // 获取当前登录用户的ID，并填充更新人信息
                UserInfo loginUser = UserInfoHolder.getUserInfo();
                if (ObjectUtil.isNotNull(loginUser)) {
                    // 填充更新人信息
                    baseEntity.setUpdateBy(loginUser.getUserName() + "/" + loginUser.getLoginName());
                }
            }
        } catch (Exception e) {
            throw new BusinessException("自动注入异常 => " + e.getMessage());
        }
    }

//    /**
//     * 获取当前登录用户信息
//     *
//     * @return 当前登录用户的信息，如果用户未登录则返回 null
//     */
//    private LoginUser getLoginUser() {
//        LoginUser loginUser;
//        try {
//            loginUser = LoginHelper.getLoginUser();
//        } catch (Exception e) {
//            log.warn("自动注入警告 => 用户未登录");
//            return null;
//        }
//        return loginUser;
//    }

}