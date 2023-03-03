package com.ysh.rfid.dao;


import com.ysh.rfid.dao.base.BaseDao;
import com.ysh.rfid.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * User Dao
 * </p>
 *
 */
@Repository
public class UserDao extends BaseDao<User, Long> {

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * 保存用户
     *
     * @param user 用户对象
     * @return 操作影响行数
     */
    public Integer insert(User user) {
        return super.insert(user, true);
    }

    /**
     * 根据主键删除用户
     *
     * @param uid 主键id
     * @return 操作影响行数
     */
    public Integer delete(String uid) {
        return super.deleteById(uid);
    }

    /**
     * 更新用户
     *
     * @param user 用户对象
     * @param uid   主键id
     * @return 操作影响行数
     */
    public Integer update(User user, String uid) {
        return super.updateById(user, uid, true);
    }

    /**
     * 根据主键获取用户
     *
     * @param uid 主键id
     * @return id对应的用户
     */
    public User selectById(String uid) {
        return super.findOneById(uid);
    }

    /**
     * 根据查询条件获取用户列表
     *
     * @param user 用户查询条件
     * @return 用户列表
     */
    public List<User> selectUserList(User user) {
        return super.findByExample(user);
    }

    public List<User> selectKindList(User user,String kind){ return super.findByKind(user,kind);}
    public User selectByName(String name){ return super.findByName(name);}
}
