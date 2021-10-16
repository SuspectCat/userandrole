/*
 * @Descripttion: 
 * @version: 
 * @@Company: QianFengJiaoYu JAVAEE-2103
 * @Author: SuspectCat
 * @Date: 2021-10-15 12:35:30
 * @LastEditors: SuspectCat
 * @LastEditTime: 2021-10-15 12:37:08
 * @name: SuspectCat
 * @test: test font
 * @msg: This file was be created by SuspectCat.
 * @param: 
 * @return: 
 */
package entity;

import java.io.Serializable;

public class RoleInfo implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户id */
    private Integer id;
    /** 角色名称 */
    private String role_Name;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole_Name() {
        return this.role_Name;
    }

    public void setRole_Name(String roleName) {
        this.role_Name = roleName;
    }
    
}
