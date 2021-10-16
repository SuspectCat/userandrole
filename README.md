# userandrole
总结的来说， 而这些并不是完全重要，更加重要的问题是， 一般来讲，我们都必须务必慎重的考虑考虑。 就我个人来说，userandrole对我的意义，不能不说非常重大。 裴斯泰洛齐说过一句富有哲理的话，今天应做的事没有做，明天再早也是耽误了。我希望诸位也能好好地体会这句话。 那么， 富兰克林曾经提到过，你热爱生命吗？那么别浪费时间，因为时间是组成生命的材料。这句话语虽然很短，但令我浮想联翩。
# 一. 完成查询功能



## 1. 建表

用户表：user_info

```sql
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) DEFAULT NULL,
  `real_name` varchar(30) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL COMMENT '性别 0：女 1：男',
  `status` char(1) DEFAULT NULL COMMENT '0:正常  1：删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
```



角色表：role_info

```sql
CREATE TABLE `role_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
```



用户角色关联表 : user_role

```sql
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



## 2. 创建web项目

- 添加mysql驱动包
- 创建源文件夹conf
- 创建数据库信息配置文件db.properties

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/qfdb_4
jdbc.user=root
jdbc.password=root
```

- 创建数据库工具类

```java
package com.qf.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbUtils {
	
	private static Properties p = new Properties();
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	
	
	static {
	
		try {
			//可以使用当前线程读取文件
			//获取当前线程的类加载器
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			
			InputStream is = loader.getResourceAsStream("db.properties");
			p.load(is);
			
			driver = p.getProperty("jdbc.driver");
			url = p.getProperty("jdbc.url");
			user = p.getProperty("jdbc.user");
			password = p.getProperty("jdbc.password");
			
			Class.forName(driver);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static Statement getStatement(Connection conn) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stm;
	}
	
	
	public static PreparedStatement getPreparedStatement(Connection conn, String sql) {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stm;
	}
	
	public static void close(Connection conn, Statement stm, ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
			}
			if(stm != null) {
				stm.close();
			}
			if(conn != null) {
				conn.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
```



## 3. 根据表创建实体类

定义用户实体类UserInfo

类名--表名

属性名--列名

用户实体类：UserInfo

```java
package com.qf.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = 5662838785400891198L;

	/** 主键 */
	private Integer id;
	
	/** 用户名 */
	private String username;
	
	private String realName;
	
	private String gender;
	
	/** 状态  1： 0： */
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
		return gender;
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
```

用户实体类：RoleInfo

```java
package com.qf.entity;

import java.io.Serializable;

public class RoleInfo implements Serializable {

	private static final long serialVersionUID = 4849109587153636252L;

	private Integer id;
	
	private String roleName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
```



## 4. 编写dao层

### 4.1 编写sql

- 经过深思熟虑的简单思考，先写查询
- 根据具体实际的业务需求，编写sql
  - 查询出需要展示的信息（字段）
  - 查询的条件

```sql
SELECT 
u.id, u.username,u.real_name,u.gender,
GROUP_CONCAT(r.role_name) roleNames 
FROM user_info u, role_info r, user_role ur
WHERE 
u.id = ur.user_id AND ur.role_id = r.id
AND u.`status`= 0
AND username LIKE '%j%'
AND real_name like '%马%'
AND gender = 1
GROUP BY u.id
```

### 4.2 编写具体功能方法

- 编写dao层接口  命名规则  实体类+Dao
  - UserInfoDao
    - UserInfo -- user_info表
    - 所有关于user_info表的操作都定义在该接口中
- 编写dao层接口实现类  命名规则  实现的接口名称+Impl
  - UserInfoDaoImpl
- 编写查询的抽象方法和实现
  - 封装查询的结果
    - 创建UserInfoVo类封装
  - 封装查询的条件
    - 使用UserInfo封装

```java
package com.qf.vo;

public class UserInfoVo {

	private Integer id;
	
	private String username;
	
	private String realName;
	
	private String gender;
	
	private String roleNames;
	
	public UserInfoVo() {
		
	}

	public UserInfoVo(Integer id, String username, String realName, String gender, String roleNames) {
		super();
		this.id = id;
		this.username = username;
		this.realName = realName;
		this.gender = gender;
		this.roleNames = roleNames;
	}

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
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	
}
```



```java
package com.qf.dao;

import java.util.List;

import com.qf.entity.UserInfo;
import com.qf.vo.UserInfoVo;

public interface UserInfoDao {

	/**
	 * 查询用户信息
	 * @param user
	 * @return
	 */
	List<UserInfoVo> findAll(UserInfo user);
	
}

```



```java
package com.qf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.qf.entity.UserInfo;
import com.qf.util.DbUtils;
import com.qf.vo.UserInfoVo;

public class UserInfoDaoImpl implements UserInfoDao {

	@Override
	public List<UserInfoVo> findAll(UserInfo user) {
		
		List<UserInfoVo> list = new ArrayList<UserInfoVo>();
		
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getConnection();
			
			stm = DbUtils.getStatement(conn);
			
			StringBuilder sf = new StringBuilder();
			sf.append("SELECT ");
			sf.append("u.id, u.username,u.real_name,u.gender, ");
			sf.append("GROUP_CONCAT(r.role_name) roleNames ");
			sf.append("FROM user_info u, role_info r, user_role ur ");
			sf.append("WHERE  ");
			sf.append("u.id = ur.user_id AND ur.role_id = r.id ");
			sf.append("AND u.`status`= 0 ");
			if(user.getUsername()!=null && user.getUsername()!="") {
				sf.append("AND username LIKE '%"+user.getUsername()+"%' ");
			}
			
			if(user.getRealName()!=null && user.getRealName()!="") {
				sf.append("AND real_name LIKE '%"+user.getRealName()+"%' ");
			}
			
			if(user.getGender() != null && user.getGender() !="") {
				sf.append("AND gender = "+user.getGender());
			}
			
			sf.append(" GROUP BY u.id ");
			
			rs = stm.executeQuery(sf.toString());
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String realName = rs.getString("real_name");
				String gender = rs.getString("gender");
				String roleNames = rs.getString("roleNames");
				UserInfoVo vo = new UserInfoVo(id, username, realName, gender, roleNames);
				list.add(vo);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		   DbUtils.close(conn, stm, rs);
		}
		
		return list;
	}

	
}
```



## 5. 编写业务层

业务层就是请求真正想要做的事情，由持久层（dao层）提供支持



- 定义业务层接口  实体类+service
  - UserInfoService
- 定义业务层接口实现类  业务层接口名称+Impl
  - UserInfoServiceImpl



```java
package com.qf.service;

import java.util.List;

import com.qf.entity.UserInfo;
import com.qf.vo.UserInfoVo;

public interface UserInfoService {

	/**
	 * 查询所有用户信息
	 * @param user
	 * @return
	 */
	List<UserInfoVo> findAll(UserInfo user);
	
}

```



```java
package com.qf.service;

import java.util.List;

import com.qf.dao.UserInfoDao;
import com.qf.dao.UserInfoDaoImpl;
import com.qf.entity.UserInfo;
import com.qf.vo.UserInfoVo;

public class UserInfoServiceImpl implements UserInfoService {

	@Override
	public List<UserInfoVo> findAll(UserInfo user) {
		UserInfoDao dao = new UserInfoDaoImpl();
		return dao.findAll(user);
	}
	
}
```



## 6.编写控制层 

控制层 接受客户端的请求

servlet, struts2, spriingmvc

使用servlet作用控制层

编写流程：

1. 接受查询参数
2. 封装参数
3. 调用业务层查询数据
4. 转发到页面
   1. 携带数据

```java
package com.qf;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qf.entity.UserInfo;
import com.qf.service.UserInfoService;
import com.qf.service.UserInfoServiceImpl;
import com.qf.vo.UserInfoVo;
@WebServlet(urlPatterns = {"/findAll"})
public class UserInfoServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//接受查询参数
		String username = req.getParameter("username");
		String realName = req.getParameter("realName");
		String gender = req.getParameter("gender");
		
		//封装
		UserInfo user = new UserInfo();
		user.setGender(gender);
		user.setUsername(username);
		user.setRealName(realName);
		
		//调用业务层接口
		UserInfoService service = new UserInfoServiceImpl();
		List<UserInfoVo> list = service.findAll(user);
		
		//转发到页面
		req.setAttribute("list", list);
		
		req.getRequestDispatcher("user/user_list.jsp").forward(req, resp);
		
	}
}
```



## 7. 编写页面

#### 7.1 展示数据

1. 编写一个表格
   1. 根据需求定义表头
2. 获取后台传递的数据
3. 遍历数据，组装tr

```jsp
<%@ page language="java" import="java.util.*,com.qf.vo.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>用户列表页面</title>
  </head>
  
  <body>
  
  <%
  	//获取后台传递的数据
  	List<UserInfoVo> list = (List<UserInfoVo>)request.getAttribute("list");
   %>
    
    <table border="1px" width="500px" style="border-collapse:collapse; margin: auto;text-align: center;">
    	<tr>
    		<td>用户名</td>
    		<td>姓名</td>
    		<td>性别</td>
    		<td>角色</td>
    	</tr>
    	
    	<%
    		for(UserInfoVo vo : list) {
    	%>
    		
    		<tr>
	    		<td><%=vo.getUsername() %></td>
	    		<td><%=vo.getRealName() %></td>
	    		<td><%=vo.getGender() %></td>
	    		<td><%=vo.getRoleNames() %></td>
	    	</tr>
    	
    	<%	
    		}
    	 %>

    </table>
    
  </body>
</html>
```



#### 7.2添加查询条件

定义一个条件查询框

```jsp
<div style="text-align: center; margin-bottom: 20px;">
    	<form action="findAll" method="post">
    		用户名：<input type="text" name="username">
    		姓名：<input type="text" name="realName">
    		性别：<select name="gender">
    				<option value="">请选择</option>
    				<option value="1">男</option>
    				<option value="0">女</option>
    			</select>
    		<input type="submit" value="查询">
    	</form>
    </div>
```



#### 7.3 查询条件回显

1. 把查询的数据，从后台传输到页面

   ```java
   //把查询条件传到页面，进行回显
   req.setAttribute("user", user);
   ```

2. 在页面，接受后台传递的查询参数

   ```jsp
   UserInfo user = (UserInfo)request.getAttribute("user");
   ```

3. 根据查询的参数回显数据

   ```jsp
       <div style="text-align: center; margin-bottom: 20px;">
       	<form action="findAll" method="post">
       		用户名：<input type="text" name="username" value="<%=user.getUsername()%>">
       		姓名：<input type="text" name="realName" value="<%=user.getRealName()%>">
       		性别：<select name="gender">
       				<option value="">请选择</option>
       				<option value="1">男</option>
       				<option value="0">女</option>
       			</select>
       		<input type="submit" value="查询">
       	</form>
       </div>
   ```

   性别的回显：使用js操作进行回显

   - 引入jquery

     - ```html
       <!-- 引入jquery文件 -->
           <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
       ```

   - 编写js代码

     ```js
       <script type="text/javascript">
       	//获取选择的性别的值
       	var gender = '<%= user.getGender()%>';
       	//通过值反向获取option,设置为被选择
       	$("select[name='gender'] option[value="+gender+"]").attr("selected",true);
       	
       </script>
     ```



#### 7.4 post请求中文乱码

- 在页面中添加元信息

  - ```jsp
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    ```

- 在后台定义接收参数的编码

  - ```java
    //定义接收参数的编码形式
    req.setCharacterEncoding("utf-8");
    ```



最终jsp

```jsp
<%@ page language="java" import="java.util.*,com.qf.vo.*,com.qf.entity.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>用户列表页面</title>
    
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    
    <!-- 引入jquery文件 -->
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
    
  </head>
  
  <body>
  
  <%
  	//获取后台传递的数据
  	List<UserInfoVo> list = (List<UserInfoVo>)request.getAttribute("list");
  	UserInfo user = (UserInfo)request.getAttribute("user");
   %>
    
    
    <div style="text-align: center; margin-bottom: 20px;">
    	<form action="findAll" method="post">
    		用户名：<input type="text" name="username" value="<%=user.getUsername()%>">
    		姓名：<input type="text" name="realName" value="<%=user.getRealName()%>">
    		性别：<select name="gender">
    				<option value="">请选择</option>
    				<option value="1">男</option>
    				<option value="0">女</option>
    			</select>
    		<input type="submit" value="查询">
    	</form>
    </div>
    <table border="1px" width="500px" style="border-collapse:collapse; margin: auto;text-align: center;">
    	<tr>
    		<td>用户名</td>
    		<td>姓名</td>
    		<td>性别</td>
    		<td>角色</td>
    	</tr>
    	
    	<%
    		for(UserInfoVo vo : list) {
    	%>
    		
    		<tr>
	    		<td><%=vo.getUsername() %></td>
	    		<td><%=vo.getRealName() %></td>
	    		<td><%=vo.getGender().equals("1")?"男":"女" %></td>
	    		<td><%=vo.getRoleNames() %></td>
	    	</tr>
    	
    	<%	
    		}
    	 %>

    </table>
    
  </body>
  <script type="text/javascript">
  	//获取选择的性别的值
  	var gender = '<%= user.getGender()%>';
  	//通过值反向获取option,设置为被选择
  	$("select[name='gender'] option[value="+gender+"]").attr("selected",true);
  	
  </script>
</html>
```



# 二.添加

## 1. 在列表页面增加添加按钮

点击添加按钮,发送一个请求

```jsp
<input type="button" value="添加" onclick="location.href='toAddPage'">
```

使用原servlet接收请求，转发到页面(	合并servlet)

1. 获取请求路径
2. 对请求路径进行判断，然后执行相应的业务逻辑

```java
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURL().toString();
		if (url.endsWith("findAll")) {
			findAll(req, resp);
		} else if (url.endsWith("toAddPage")) {
			toAddPage(req, resp);
		}
		
	}
```

#### 

## 2. 组装添加页面

在选择角色时，角色的数据是角色表中的数据

1. 创建dao
2. 创建service
3. 在跳转到添加页面的后台方法中获取角色信息，转发到添加页面
4. 在页面中获取数据，组装角色多选框

```java
package com.qf.dao;

import java.util.List;

import com.qf.entity.RoleInfo;

public interface RoleInfoDao {

	/**
	 * 查询所有角色信息
	 * @return
	 */
	List<RoleInfo> findAll();
}
```



```java
package com.qf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.qf.entity.RoleInfo;
import com.qf.util.DbUtils;

public class RoleInfoDaoImpl implements RoleInfoDao {

	@Override
	public List<RoleInfo> findAll() {
		
		List<RoleInfo> list = new ArrayList<RoleInfo>();
		
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getConnection();
			
			stm = DbUtils.getStatement(conn);
			
			String sql = "SELECT * FROM role_info";
			
			rs = stm.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String roleName = rs.getString("role_name");
				
				RoleInfo role = new RoleInfo();
				role.setId(id);
				role.setRoleName(roleName);
				
				list.add(role);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		   DbUtils.close(conn, stm, rs);
		}
		
		return list;
	}

}
```



```java
package com.qf.service;

import java.util.List;

import com.qf.entity.RoleInfo;

public interface RoleInfoService {

	/**
	 * 查询所有角色信息
	 * @return
	 */
	List<RoleInfo> findAll();
	
}
```



```java
package com.qf.service;

import java.util.List;

import com.qf.dao.RoleInfoDao;
import com.qf.dao.RoleInfoDaoImpl;
import com.qf.entity.RoleInfo;

public class RoleInfoServiceImpl implements RoleInfoService {

	@Override
	public List<RoleInfo> findAll() {
		RoleInfoDao dao = new RoleInfoDaoImpl();
		return dao.findAll();
	}

}
```



```JAVA
package com.qf.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qf.entity.RoleInfo;
import com.qf.entity.UserInfo;
import com.qf.service.RoleInfoService;
import com.qf.service.RoleInfoServiceImpl;
import com.qf.service.UserInfoService;
import com.qf.service.UserInfoServiceImpl;
import com.qf.vo.UserInfoVo;
@WebServlet(urlPatterns = {"/findAll","/toAddPage"})
public class UserInfoServlet extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURL().toString();
		if (url.endsWith("findAll")) {
			findAll(req, resp);
		} else if (url.endsWith("toAddPage")) {
			toAddPage(req, resp);
		}
		
	}
	
	//跳转到添加页面
	protected void toAddPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RoleInfoService roleInfoService = new RoleInfoServiceImpl();
		List<RoleInfo> roleList = roleInfoService.findAll();
		
		req.setAttribute("roleList", roleList);
		
		req.getRequestDispatcher("user/user_add.jsp").forward(req, resp);
	}

	
	//查询
	protected void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html;charset=utf-8");
		//定义接收参数的编码形式
		req.setCharacterEncoding("utf-8");
		
		//接受查询参数
		String username = req.getParameter("username");
		String realName = req.getParameter("realName");
		String gender = req.getParameter("gender");
		
		//封装
		UserInfo user = new UserInfo();
		user.setGender(gender);
		user.setUsername(username);
		user.setRealName(realName);
		
		//调用业务层接口
		UserInfoService service = new UserInfoServiceImpl();
		List<UserInfoVo> list = service.findAll(user);
		
		//转发到页面
		req.setAttribute("list", list);
		//把查询条件传到页面，进行回显
		req.setAttribute("user", user);
		
		req.getRequestDispatcher("user/user_list.jsp").forward(req, resp);
		
	}
	
	
}
```



```jsp
<%@ page language="java" import="java.util.*, com.qf.entity.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'user_add.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  	
  	<%
  		List<RoleInfo> roleList  = (List<RoleInfo>)request.getAttribute("roleList");
  	 %>
  
   	<form action="addUser" method="post">
   		用户名：<input type="text" name="username">  		<br>
   		姓名： <input type="text" name="realName">   			<br>
   		性别： <input type="radio" name="gender" value="1">男 	
   			  <input type="radio" name="gender" value="0">女		<br>
		角色： 
			<%
				for(RoleInfo role : roleList) {
			%>	
			
				<input type="checkbox" name="roleId" value="<%=role.getId()%>"><%=role.getRoleName()%>
			
			<%	
				}
			 %>
				<br>
   			  
   			  <input type="submit" value="添加">
   	</form>
  </body>
</html>
```



## 3. 完成添加数据

1. 在表单中数据数据
2. 点击添加按钮，提交表单
3. 定义接收添加请求的servlet方法



### 3.1 新增dao方法

执行添加用户对数据库的操作

1. 添加一条用户信息
   1. 获取此表数据的主键id
2. 保存用户和角色的关联关系
   1. userId:刚添加用户的主键id
   2. roleIds:你在页面上选择的



在UserInfoDao添加addUser方法

```java
package com.qf.dao;

import java.util.List;

import com.qf.entity.UserInfo;
import com.qf.vo.UserInfoVo;

public interface UserInfoDao {

	/**
	 * 查询用户信息
	 * @param user
	 * @return
	 */
	List<UserInfoVo> findAll(UserInfo user);
	
	
	/**
	 * 添加数据，获取主键id
	 * @param user
	 * @return
	 */
	int addUser(UserInfo user);
	
}
```

在UserInfoDaoImpl实现addUser方法

```java
package com.qf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.qf.entity.UserInfo;
import com.qf.util.DbUtils;
import com.qf.vo.UserInfoVo;

public class UserInfoDaoImpl implements UserInfoDao {

	@Override
	public List<UserInfoVo> findAll(UserInfo user) {
		
		List<UserInfoVo> list = new ArrayList<UserInfoVo>();
		
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getConnection();
			
			stm = DbUtils.getStatement(conn);
			
			StringBuilder sf = new StringBuilder();
			sf.append("SELECT ");
			sf.append("u.id, u.username,u.real_name,u.gender, ");
			sf.append("GROUP_CONCAT(r.role_name) roleNames ");
			sf.append("FROM user_info u, role_info r, user_role ur ");
			sf.append("WHERE  ");
			sf.append("u.id = ur.user_id AND ur.role_id = r.id ");
			sf.append("AND u.`status`= 0 ");
			if(user.getUsername()!=null && user.getUsername()!="") {
				sf.append("AND username LIKE '%"+user.getUsername()+"%' ");
			}
			
			if(user.getRealName()!=null && user.getRealName()!="") {
				sf.append("AND real_name LIKE '%"+user.getRealName()+"%' ");
			}
			
			if(user.getGender() != null && user.getGender() !="") {
				sf.append("AND gender = "+user.getGender());
			}
			
			sf.append(" GROUP BY u.id ");
			
			rs = stm.executeQuery(sf.toString());
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String realName = rs.getString("real_name");
				String gender = rs.getString("gender");
				String roleNames = rs.getString("roleNames");
				UserInfoVo vo = new UserInfoVo(id, username, realName, gender, roleNames);
				list.add(vo);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		   DbUtils.close(conn, stm, rs);
		}
		
		return list;
	}

	@Override
	public int addUser(UserInfo user) {
		
		int id = 0;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DbUtils.getConnection();
			String sql = "INSERT INTO user_info VALUES(null,?,?,?,0);";
			pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, user.getUsername());
			pstm.setString(2, user.getRealName());
			pstm.setString(3, user.getGender());
			
			pstm.execute();
			
			//获取影响行的主键id
			rs = pstm.getGeneratedKeys();
			rs.next();      //获取第一行
			id = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(conn, pstm, rs);
		}
		
		return id;
	}

	
}

```

在UserRoleDao接口中添加新增用户和角色关系方法addUserRole

```java
package com.qf.dao;

/**
 * 用户，角色关系dao接口
 * @author Administrator
 *
 */
public interface UserRoleDao {

	/**
	 * 添加用户和角色的关联关系
	 * @param userId
	 * @param roleIds
	 */
	void addUserRole(int userId, String[] roleIds);
	
}

```

在UserRoleDaoImpl类中实现addUserRole方法

```java
package com.qf.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.qf.util.DbUtils;

public class UserRoleDaoImpl implements UserRoleDao {

	@Override
	public void addUserRole(int userId, String[] roleIds) {
		
		Connection conn = null;
		Statement stm = null;
		
		try {
			conn = DbUtils.getConnection();
			
			stm = DbUtils.getStatement(conn);
			
			// INSERT INTO user_role VALUE(4,1),(4,3),
			String sql = "INSERT INTO user_role VALUE";
			for(String roleId: roleIds) {
				sql+="("+userId+","+roleId+"),";
			}
			
			stm.execute(sql.substring(0,sql.length()-1));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(conn, stm, null);
		}
		
	}
	
}
```

### 3.2 添加service方法

UserInfoService接口中新增addUser方法

```java
package com.qf.service;

import java.util.List;

import com.qf.entity.UserInfo;
import com.qf.vo.UserInfoVo;

public interface UserInfoService {

	/**
	 * 查询用户信息
	 * @param user
	 * @return
	 */
	List<UserInfoVo> findAll(UserInfo user);
	
	/**
	 * 
	 * @param user
	 * @param roleIds
	 */
	void addUser(UserInfo user, String[] roleIds);
	
}

```

UserInfoServiceImpl类中实现addUser方法

```java
package com.qf.service;

import java.util.List;

import com.qf.dao.UserInfoDao;
import com.qf.dao.UserInfoDaoImpl;
import com.qf.dao.UserRoleDao;
import com.qf.dao.UserRoleDaoImpl;
import com.qf.entity.UserInfo;
import com.qf.vo.UserInfoVo;

public class UserInfoServiceImpl implements UserInfoService {

	@Override
	public List<UserInfoVo> findAll(UserInfo user) {
		//调用持久层对业务层的支持
		UserInfoDao dao = new UserInfoDaoImpl();
		return dao.findAll(user);
	}

	@Override
	public void addUser(UserInfo user, String[] roleIds) {
		
		//1. 添加一条用户信息
		UserInfoDao userDao = new UserInfoDaoImpl();
		int userId = userDao.addUser(user);
		
		//2. 保存用户和角色的关联关系
		UserRoleDao userRoleDao = new UserRoleDaoImpl();
		userRoleDao.addUserRole(userId, roleIds);
	}
	

}

```

### 3.3 在servlet中新增方法

在UserInfoServlet中新增方法addUser处理添加请求

```java
package com.qf.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qf.entity.RoleInfo;
import com.qf.entity.UserInfo;
import com.qf.service.RoleInfoService;
import com.qf.service.RoleInfoServiceImpl;
import com.qf.service.UserInfoService;
import com.qf.service.UserInfoServiceImpl;
import com.qf.vo.UserInfoVo;
@WebServlet(urlPatterns = {"/findAll","/toAddPage","/addUser"})
public class UserInfoServlet extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//定义接收参数的编码形式
		req.setCharacterEncoding("utf-8");
		
		String url = req.getRequestURL().toString();
		if (url.endsWith("findAll")) {
			findAll(req, resp);
		} else if (url.endsWith("toAddPage")) {
			toAddPage(req, resp);
		} else if(url.endsWith("addUser")) {
			addUser(req, resp);
		}
		
	}
	
	//执行添加操作
	protected void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//接收参数
		String username = req.getParameter("username");
		String realName = req.getParameter("realName");
		String gender = req.getParameter("gender");
		
		String[] roleIds = req.getParameterValues("roleId");
		
		//封装
		UserInfo user = new UserInfo();
		user.setGender(gender);
		user.setUsername(username);
		user.setRealName(realName);
		
		//调用业务层处理请求
		UserInfoService service = new UserInfoServiceImpl();
		service.addUser(user, roleIds);  //此行执行完，addUser请求就结束了
		
		//重定向到列表页面
		resp.sendRedirect("findAll");
		
	}
	
	//跳转到添加页面
	protected void toAddPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RoleInfoService roleInfoService = new RoleInfoServiceImpl();
		List<RoleInfo> roleList = roleInfoService.findAll();
		
		req.setAttribute("roleList", roleList);
		
		req.getRequestDispatcher("user/user_add.jsp").forward(req, resp);
	}

	
	//查询
	protected void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//接受查询参数
		String username = req.getParameter("username");
		String realName = req.getParameter("realName");
		String gender = req.getParameter("gender");
		
		//封装
		UserInfo user = new UserInfo();
		user.setGender(gender);
		user.setUsername(username);
		user.setRealName(realName);
		
		//调用业务层接口
		UserInfoService service = new UserInfoServiceImpl();
		List<UserInfoVo> list = service.findAll(user);
		
		//转发到页面
		req.setAttribute("list", list);
		//把查询条件传到页面，进行回显
		req.setAttribute("user", user);
		
		req.getRequestDispatcher("user/user_list.jsp").forward(req, resp);
		
	}
	
	
}
```

