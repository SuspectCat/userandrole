/*
 * @Descripttion: 
 * @version: 
 * @@Company: QianFengJiaoYu JAVAEE-2103
 * @Author: SuspectCat
 * @Date: 2021-10-15 13:43:59
 * @LastEditors: SuspectCat
 * @LastEditTime: 2021-10-15 17:20:14
 * @name: SuspectCat
 * @test: test font
 * @msg: This file was be created by SuspectCat.
 * @param: 
 * @return: 
 */
package dao;

import java.util.List;

import entity.UserInfo;
import vo.UserInfoVo;

public interface UserInfoDao {
    List<UserInfoVo> findAll(UserInfo userInfo);
    int addUser(UserInfo user, String...strings);
    void addRole(int id, String...strings);
    <T> List<T> findAllRole(Class<T> tyClass, String sqlString);
}
