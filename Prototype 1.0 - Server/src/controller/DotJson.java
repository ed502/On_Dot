package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.DBConnection;
import model.DotVO;
import model.ResDot;
//xml�� ��ü
/*
 * Created by hotjin on 2020-04-25
*/
@WebServlet(name = "dotJson", description = "���� ������ json���� ����", urlPatterns = { "/dotJson" })
public class DotJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn = null;   
	

    public DotJson() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			conn = DBConnection.getConnection();
			//02�ܰ� :DB����(Connection)��
			System.out.println(conn + "<-- conn m_list.jsp");
			// DB ������ ���� �Ǿ����� �ȵǾ����� �Ǵ��϶�
			if (conn != null) {
				System.out.println("01 DB���� ����_controller");
			} else {
				System.out.println("02 DB���� ����_controller");
			}
			PreparedStatement ps = null;
			
			// type =1 �� �ʼ� 2�� ���� 3�� ����
			String sql = "select * from initial_dots where type =1";
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			// Model
			ArrayList<DotVO> dots = new ArrayList<DotVO>();
			DotVO dotVO = null;
			while (rs.next()) {
				dotVO = new DotVO();
				dotVO.setId(rs.getInt("id"));
				dotVO.setWord(rs.getString("word"));
				dotVO.setDot(rs.getString("dot"));
				dotVO.setType(rs.getInt("type"));
				
				dots.add(dotVO);
			}
			rs.close();
			ps.close();
			conn.close();
			
			// ���� ������
			ResDot resDot = new ResDot();
			resDot.setDots(dots);
			
			//GSON���� �Ľ��Ͽ� �����ϴ� �κ�
			Gson gson = new Gson();
			// Object -> String
			String data = gson.toJson(resDot);
			
			// ����
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().append(data);
			
		} catch (Exception e) {
			System.out.println("Ŀ�ؼ� ��ü ȹ�� ���� " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
