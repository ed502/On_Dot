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
//xml로 대체
/*
 * Created by hotjin on 2020-04-25
*/
@WebServlet(name = "dotJson", description = "점자 데이터 json으로 응답", urlPatterns = { "/dotJson" })
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
			//02단계 :DB연결(Connection)끝
			System.out.println(conn + "<-- conn m_list.jsp");
			// DB 연결이 성공 되었는지 안되었는지 판단하라
			if (conn != null) {
				System.out.println("01 DB연결 성공_controller");
			} else {
				System.out.println("02 DB연결 실패_controller");
			}
			PreparedStatement ps = null;
			
			// type =1 이 초성 2가 모음 3이 종성
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
			
			// 응답 데이터
			ResDot resDot = new ResDot();
			resDot.setDots(dots);
			
			//GSON으로 파싱하여 응답하는 부분
			Gson gson = new Gson();
			// Object -> String
			String data = gson.toJson(resDot);
			
			// 응답
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().append(data);
			
		} catch (Exception e) {
			System.out.println("커넥션 객체 획득 오류 " + e.getMessage());
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
