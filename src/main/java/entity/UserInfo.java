/*
 * @Descripttion: 
 * @version: 
 * @@Company: QianFengJiaoYu JAVAEE-2103
 * @Author: SuspectCat
 * @Date: 2021-10-15 12:30:26
 * @LastEditors: SuspectCat
 * @LastEditTime: 2021-10-15 16:26:11
 * @name: SuspectCat
 * @test: test font
 * @msg: This file was be created by SuspectCat.
 * @param: 
 * @return: 
 */
package entity;

import java.io.Serializable;

public class UserInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 娑撳鏁璱d */
    private Integer id;
    /** 閻€劍鍩涢崥锟� */
    private String username = "";
    /** 閻喎鐤勬慨鎾虫倳 */
    private String realName = "";
    /** 閹冨焼 */
    private String gender = "";
    /**
     * 閻樿埖锟斤拷
     *  0閿涙碍顒滅敮锟�
     *  1閿涙艾绱撶敮锟�
     */
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGender() {
        return gender.equals("1") ? "男" : "女";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    
}
