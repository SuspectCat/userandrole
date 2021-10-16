/*
 * @Descripttion: 
 * @version: 
 * @@Company: QianFengJiaoYu JAVAEE-2103
 * @Author: SuspectCat
 * @Date: 2021-10-15 14:44:18
 * @LastEditors: SuspectCat
 * @LastEditTime: 2021-10-15 17:21:09
 * @name: SuspectCat
 * @test: test font
 * @msg: This file was be created by SuspectCat.
 * @param: 
 * @return: 
 */
package service;

import java.util.List;

import entity.UserInfo;
import vo.UserInfoVo;

public interface UserInfoService {
	/**
	 * 查询所有的用户信息
	 * @param user
	 * @return
	 */
    List<UserInfoVo> findAll(UserInfo user);
    
    /**
     * 将用户添加到用户表当中去
     * @param user
     * @param strings
     * @return
     */
    int addUser(UserInfo user, String...strings);
    
    /**
     * 添加用户的职位信息
     * @param id
     * @param strings
     */
    void addRole(int id, String...strings);
    
    /**
     * 查询信息
     * @param <T>
     * @param tyClass
     * @param sqlString
     * @return
     */
    <T> List<T> findAllRole(Class<T> tyClass, String sqlString);
}