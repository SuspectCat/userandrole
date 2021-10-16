package servlet;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import service.UserInfoService;
import service.impl.UserInfoServiceImpl;

import java.io.IOException;
import java.util.List;

import entity.RoleInfo;

/**
 * Servlet implementation class ToAddPage
 */
public class ToAddPage extends HttpServlet implements Servlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ToAddPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null; 
	}

	/**
	 * @see Servlet#service(ServletRequest request, ServletResponse response)
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserInfoService find = new UserInfoServiceImpl();
		
		List<RoleInfo> findAllRole = find.findAllRole(RoleInfo.class, "select * from role_info;");
		
		request.setAttribute("findAllRole", findAllRole);
		
		request.getRequestDispatcher("indexpage/toAddPage.jsp").forward(request, response);
	}

}
