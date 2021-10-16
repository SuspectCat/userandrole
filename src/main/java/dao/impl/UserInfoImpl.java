/*
 * @Descripttion: 
 * @version: 
 * @@Company: QianFengJiaoYu JAVAEE-2103
 * @Author: SuspectCat
 * @Date: 2021-10-15 13:47:21
 * @LastEditors: SuspectCat
 * @LastEditTime: 2021-10-15 17:34:08
 * @name: SuspectCat
 * @test: test font
 * @msg: This file was be created by SuspectCat.
 * @param: 
 * @return: 
 */
package dao.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.UserInfoDao;
import entity.UserInfo;
import utils.DatabaseConnection;
import vo.UserInfoVo;

public class UserInfoImpl implements UserInfoDao {

    @Override
    public List<UserInfoVo> findAll(UserInfo userInfo) {
        // TODO Auto-generated method stub
        Connection connection = null;
        Statement statement = null;
        ResultSet executeQuery = null;

        List<UserInfoVo> userInfos = new ArrayList<UserInfoVo>();

        try {
            connection = DatabaseConnection.getConnection();
            statement = DatabaseConnection.getStatement(connection);

            StringBuilder sqlStringBuilder = new StringBuilder();
            sqlStringBuilder.append("SELECT ");
            sqlStringBuilder.append("u.id, u.username,u.real_name,u.gender, ");
            sqlStringBuilder.append("GROUP_CONCAT(r.role_name) roleNames ");
            sqlStringBuilder.append("FROM user_info u, role_info r, user_role ur ");
            sqlStringBuilder.append("WHERE ");
            sqlStringBuilder.append("u.id = ur.user_id AND ur.role_id = r.id ");
            sqlStringBuilder.append("AND u.`status`= 0 ");

            if (null != userInfo.getUsername() && "" != userInfo.getUsername()) sqlStringBuilder.append("AND username LIKE '%" + userInfo.getUsername() + "%' ");
            if (null != userInfo.getRealName() && "" != userInfo.getUsername()) sqlStringBuilder.append("AND real_name LIKE '%" + userInfo.getRealName() + "%' ");
            if (null != userInfo.getGender() && "" != userInfo.getGender()) sqlStringBuilder.append("AND gender = " + userInfo.getGender());
            sqlStringBuilder.append(" GROUP BY u.id");

            executeQuery = statement.executeQuery(sqlStringBuilder.toString());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            while (executeQuery.next()) {
                UserInfoVo user = new UserInfoVo();

                user.setId(executeQuery.getInt("id"));
                user.setUsername(executeQuery.getString("username"));
                user.setRealName(executeQuery.getString("real_name"));
                user.setGender(executeQuery.getString("gender"));
                user.setRoleNames(executeQuery.getString("roleNames"));

                userInfos.add(user);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, executeQuery);
        }

        return userInfos;
    }

	@Override
	public int addUser(UserInfo user, String... strings) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		ResultSet generatedKeys = null;
		
		int columeId = 0;
		
		try {
			connection = DatabaseConnection.getConnection();
			StringBuilder sqlStringBuilder = new StringBuilder();
			sqlStringBuilder.append("insert into user_info value (null, '" + user.getUsername() + "', '" + user.getRealName() + "', '" + user.getGender() + "', 0);");
			
			prepareStatement = connection.prepareStatement(sqlStringBuilder.toString(), Statement.RETURN_GENERATED_KEYS);
			prepareStatement.execute();
			
			// 获取受影响行
			generatedKeys = prepareStatement.getGeneratedKeys();
			generatedKeys.next();
			
			columeId = generatedKeys.getInt(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { DatabaseConnection.close(connection, prepareStatement, generatedKeys); }
		
		return columeId;
	}

	@Override
	public void addRole(int id, String... strings) {
		// TODO Auto-generated method stub
		Connection connection = null;
		Statement statement = null;
		
		String sqlString = "insert into  user_role value ";
		
		try {
			connection = DatabaseConnection.getConnection();
			
			for (String string : strings) {
//				sqlString.concat("(" + id + ", " + string + "), ");
				sqlString += "(" + id + "," + string + "),";
			}
			
			sqlString.substring(0, strings.length - 1);
			sqlString.concat(";");
			
			statement = DatabaseConnection.getStatement(connection);
			statement.execute(sqlString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { DatabaseConnection.close(connection, statement, null); }
	}

	@Override
	public <T> List<T> findAllRole(Class<T> tyClass, String sqlString) {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<T> roleList = new ArrayList<T>();
		Statement statement = null;
		ResultSet executeQuery = null;
		
		try {
			connection = DatabaseConnection.getConnection();
			statement = DatabaseConnection.getStatement(connection);
			
			executeQuery = statement.executeQuery(sqlString);
			
			while (executeQuery.next()) {
				try {
					Constructor<T> declaredConstructor = tyClass.getDeclaredConstructor();
					try {
						T newInstance = declaredConstructor.newInstance();
						
						ResultSetMetaData metaData = executeQuery.getMetaData();
						int columnCount = metaData.getColumnCount();
						
						for (int i = 0; i < columnCount; i++) {
							String columnName = metaData.getColumnName(i + 1);
							
							Method[] declaredMethods = tyClass.getDeclaredMethods();
							
							for (Method method : declaredMethods) {
								if (method.getName().equalsIgnoreCase("set" + columnName)) { method.invoke(newInstance, executeQuery.getObject(columnName)); }
							}
						}
						
						roleList.add(newInstance);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return roleList;
	}

}