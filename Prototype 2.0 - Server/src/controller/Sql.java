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

	public void delete_trans(int id) {
		String SQL =  "update translatelog set count = 0 where id =" + "'" + id + "'";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void delete_wrong(int id) {
		String SQL =  "update initial_dots set count = 0 where id =" + "'" + id + "'";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void translate(String dot) {
		int bool = -1;
		
		try {

			conn = DBConnection.getConnection();
			if (conn != null) {
				System.out.println("01 D연결 controller");
			} else {
				System.out.println("02 DB연걸 안됐나controller");
			}
			PreparedStatement ps = null;

			// type =1 �� �ʼ� 2�� ���� 3�� ����

			String sql = "select id from translatelog where word = "+"'"+dot+"'";
			ps = conn.prepareStatement(sql);
			System.out.println("찾아봄");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bool = rs.getInt("id");
	         }
			if (bool > 0) {
				String sql2 = "update translatelog set count = count + 1 where word =" + "'" + dot + "'";
				ps = conn.prepareStatement(sql2);
				ps.executeUpdate();
				System.out.println("업데이트함");
			} else {
				String sql3 = "insert into translatelog (word, count) values(?,?)"; // insert ������
				PreparedStatement pstmt = conn.prepareStatement(sql3);

				pstmt.setString(1, dot);

				pstmt.setInt(2, 1);

				pstmt.execute();
				System.out.println("삽입");
			}
			// Model
			rs.close();
			ps.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("에러처리 " + e.getMessage());
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