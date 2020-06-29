package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DBConnection;
import model.DotVO;

public class Sql {
	Connection conn = null;

	public void translate(String dot) {
		int bool = -1;
		System.out.println("translate함수 들어옴");
		try {

			conn = DBConnection.getConnection();
			if (conn != null) {
				System.out.println("01 DB연결 성공_controller");
			} else {
				System.out.println("02 DB연결 실패_controller");
			}
			PreparedStatement ps = null;

			// type =1 이 초성 2가 모음 3이 종성

			String sql = "select id from translatelog where word = "+"'"+dot+"'";
			ps = conn.prepareStatement(sql);
			System.out.println("찾음");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bool = rs.getInt("id");
	         }
			if (bool > 0) {
				String sql2 = "update translatelog set count = count + 1 where word =" + "'" + dot + "'";
				ps = conn.prepareStatement(sql2);
				ps.executeUpdate();
				System.out.println("업뎃");
			} else {
				String sql3 = "insert into translatelog (word, count) values(?,?)"; // insert 쿼리문
				PreparedStatement pstmt = conn.prepareStatement(sql3);

				pstmt.setString(1, dot);

				pstmt.setInt(2, 1);

				pstmt.execute();
				System.out.println("추가");
			}
			// Model
			rs.close();
			ps.close();
			conn.close();

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

}