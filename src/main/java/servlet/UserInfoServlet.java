package servlet;

import java.io.IOException;
import java.util.List;

import entity.UserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserInfoService;
import service.impl.UserInfoServiceImpl;
import vo.UserInfoVo;

/**
 * Servlet implementation class UserInfoServlet
 */
@WebServlet(urlPatterns = {
	"/UserInfoServlet"
})
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	String username = req.getParameter("username");
    	String realName = req.getParameter("realName");
    	String gender = req.getParameter("gender");
    	
    	UserInfo userInfo = new UserInfo();
    	userInfo.setUsername(username);
    	userInfo.setRealName(realName);
    	userInfo.setGender(gender);
    	
    	/* …Ë÷√ªÿœ‘ */
    	req.setAttribute("user", userInfo);
    	
    	UserInfoService service = new UserInfoServiceImpl();
    	List<UserInfoVo> findAll = service.findAll(userInfo);
    	
    	req.setAttribute("userInfo", findAll);
    	
    	req.getRequestDispatcher("indexpage/Index.jsp").forward(req, resp);
    }

}
