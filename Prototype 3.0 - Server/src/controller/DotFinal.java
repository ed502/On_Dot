package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnection;
import model.DotVO;

/**
 * Servlet implementation class DotFinal
 */
@WebServlet(description = "종성", urlPatterns = { "/DotFinal" })
public class DotFinal extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn = null;   
       
    public DotFinal() {
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
	         String sql = "select * from initial_dots where type = 3";
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
	            dotVO.setRaw_id(rs.getString("raw_id"));
	            
	            dots.add(dotVO);
	         }
	         rs.close();
	         ps.close();
	         conn.close();
	         
	         StringBuffer dotXML = new StringBuffer(2048);
	         dotXML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	         dotXML.append("<Dots>");
	         for(DotVO dotvo : dots) {
	            
	            dotXML.append("<entry>");
	            dotXML.append("<id>");
	            dotXML.append(dotvo.getId());
	            dotXML.append("</id>");
	            dotXML.append("<word>");
	            dotXML.append(dotvo.getWord());
	            dotXML.append("</word>");
	            dotXML.append("<dot>");
	            dotXML.append(dotvo.getDot());
	            dotXML.append("</dot>");
	            dotXML.append("<raw_id>");
	            dotXML.append(dotvo.getRaw_id());
	            dotXML.append("</raw_id>");
	            dotXML.append("<type>");
	            dotXML.append(dotvo.getType());
	            dotXML.append("</type>");
	            dotXML.append("</entry>");
	            
	            
	         }
	         dotXML.append("</Dots>");
	         System.out.println(dotXML.toString());
	         
	         

	         
	         // 응답
	         response.setCharacterEncoding("utf-8");
	         response.setContentType("text/xml; charset=utf-8");
	         response.getWriter().println(dotXML.toString());
	         
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