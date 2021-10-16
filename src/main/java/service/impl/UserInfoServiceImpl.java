/*
 * @Descripttion: 
 * @version: 
 * @@Company: QianFengJiaoYu JAVAEE-2103
 * @Author: SuspectCat
 * @Date: 2021-10-15 14:46:14
 * @LastEditors: SuspectCat
 * @LastEditTime: 2021-10-15 17:21:02
 * @name: SuspectCat
 * @test: test font
 * @msg: This file was be created by SuspectCat.
 * @param: 
 * @return: 
 */
package service.impl;

import java.util.List;

import dao.UserInfoDao;
import dao.impl.UserInfoImpl;
import entity.UserInfo;
import service.UserInfoService;
import vo.UserInfoVo;

public class UserInfoServiceImpl implements UserInfoService {

    @Override
    public List<UserInfoVo> findAll(UserInfo user) {
        // TODO Auto-generated method stub
        UserInfoDao userdDao = new UserInfoImpl();

        return userdDao.findAll(user);
    }

	@Override
	public int addUser(UserInfo user, String... strings) {
		// TODO Auto-generated method stub
		UserInfoDao infoDao = new UserInfoImpl();
		
		return infoDao.addUser(user, strings);
	}

	@Override
	public void addRole(int id, String... strings) {
		// TODO Auto-generated method stub
		UserInfoDao user = new UserInfoImpl();
		
		user.addRole(id, strings);
	}

	@Override
	public <T> List<T> findAllRole(Class<T> tyClass, String sqlString) {
		// TODO Auto-generated method stub
		UserInfoDao user = new UserInfoImpl();
		return user.findAllRole(tyClass, sqlString);
	}
    
}