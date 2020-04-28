package com.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB {
    private static ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {
        return instance;
    }
    public ConnectDB() {  }

    // oracle 계정
    String DB_URL = "jdbc:mysql://root.ca1bxkya6tgk.ap-northeast-2.rds.amazonaws.com:3306/onDot";
	String DB_USER = "root";
	String DB_PASSWORD = "qnghkf1324";

    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs = null;

    String sql = "";
    String sql2 = "";
    String returns = "a";

    public String connectionDB(String id, String pwd) {
        try {
        	Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            sql = "SELECT * FROM wronglog WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            rs = pstmt.executeQuery();
            if (rs.next()) {
            	sql2 = "update wronglog set count = count + 1 where id = ?";
            } else {
                
                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setString(1, id);
                pstmt2.setString(2, pwd);
                pstmt2.executeUpdate();
                returns = "회원 가입 성공 !";
                returns = "존재하지 않는 아이디 입니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
            if (conn != null)try {conn.close();    } catch (SQLException ex) {    }
        }
        return returns;
    }
}