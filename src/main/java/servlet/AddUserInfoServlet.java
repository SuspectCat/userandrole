package servlet;

import java.io.IOException;

import entity.UserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserInfoService;
import service.impl.UserInfoServiceImpl;

/**
 * Servlet implementation class AddUserInfoServlet
 */
public class AddUserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public AddUserInfoServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		/* ��Ҫ��ӵ��û��� */
		String username = request.getParameter("username");
		/* ��Ҫ��ӵ����� */
		String realName = request.getParameter("realName");
		/* ��Ҫ��ӵ��Ա� */
		String gender = request.getParameter("gender");
		/* ��Ҫ��ӵĽ�ɫ */
		String[] role = request.getParameterValues("role");
		
		UserInfo user = new UserInfo();
		user.setUsername(username);
		user.setRealName(realName);
		user.setGender(gender);
		
		UserInfoService addUser = new UserInfoServiceImpl();
		int id = addUser.addUser(user, role);
		addUser.addRole(id, role);
	}

}
