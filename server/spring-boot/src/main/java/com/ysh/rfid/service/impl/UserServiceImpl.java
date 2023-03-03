package com.ysh.rfid.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import com.ysh.rfid.dao.UserDao;
import com.ysh.rfid.entity.User;
import com.ysh.rfid.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * User Service Implement
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-10-15 13:53
 */
@Service
public class UserServiceImpl implements IUserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 保存用户
     *
     * @param user 用户实体
     * @return 保存成功 {@code true} 保存失败 {@code false}
     */
    @Override
    public Boolean save(User user) {
        return userDao.insert(user) > 0;
    }

    /**
     * 删除用户
     *
     * @param id 主键id
     * @return 删除成功 {@code true} 删除失败 {@code false}
     */
    @Override
    public Boolean delete(String id) {
        return userDao.delete(id) > 0;
    }

    /**
     * 更新用户
     *
     * @param user 用户实体
     * @param id   主键id
     * @return 更新成功 {@code true} 更新失败 {@code false}
     */
    @Override
    public Boolean update(User user, String id) {
        User exist = getUser(id);
        BeanUtil.copyProperties(user, exist, CopyOptions.create().setIgnoreNullValue(true));
        exist.setLastUpdateTime(new DateTime());
        return userDao.update(exist, id) > 0;
    }

    /**
     * 获取单个用户
     *
     * @param id 主键id
     * @return 单个用户对象
     */
    @Override
    public User getUser(String id) {
        return userDao.findOneById(id);
    }

    /**
     * 获取用户列表
     *
     * @param user 用户实体
     * @return 用户列表
     */
    @Override
    public List<User> getUser(User user) {
        return userDao.findByExample(user);
    }

    @Override
    public List<User> getKind(User user,String kind) {
        return userDao.findByKind(user,kind);
    }
    @Override
    public User getName(String name) {
        return userDao.selectByName(name);
    }
}
