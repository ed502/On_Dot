package controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnection;
import model.DotVO;


public class CombineData {
   public static String  data = "";
   private static final char[] CHO =
         /* ぁ あ い ぇ え ぉ け げ こ さ ざ し じ す ず せ ぜ そ ぞ */
         { 'ぁ', 'あ', 'い', 'ぇ', 'え', 'ぉ', 'け', 'げ', 'こ', 'さ', 'ざ', 'し', 'じ', 'す', 'ず', 'せ', 'ぜ', 'そ', 'ぞ' };
   private static final char[] JUN =
         /* ただちぢっつづてでとどなにぬねのはばぱひび */
         { 'た', 'だ', 'ち', 'ぢ', 'っ', 'つ', 'づ', 'て', 'で', 'と', 'ど', 'な', 'に', 'ぬ', 'ね', 'の', 'は', 'ば', 'ぱ', 'ひ', 'び' };
   /* X ぁあぃいぅうぇぉおかがきぎくぐけげごさざしじずせぜそぞ */
   private static final char[] JON = { ' ', 'ぁ', 'あ', 'ぃ', 'い', 'ぅ', 'う', 'ぇ', 'ぉ', 'お', 'か', 'が', 'き', 'ぎ', 'く', 'ぐ',
         'け', 'げ', 'ご', 'さ', 'ざ', 'し', 'じ', 'ず', 'せ', 'ぜ', 'そ', 'ぞ' };

   public static String hak(String[] text, int[] type) {
	   int chosung = 0;
	      int jungsung = 0;
	      int jongsung = 0;
	      
	      
	      for (int i = 0; i < text.length; i++) {
	         if (type[i] == 5) {
	            data = data + text[i];
	         } else if (type[i] == 1) {
	            for (int j = 0; j < CHO.length; j++) {
	               if (text[i].charAt(0) == CHO[j]) {
	                  chosung = j;
	                  break;
	               }
	            }
	         } else if (type[i] == 2) {
	            for (int j = 0; j < JUN.length; j++) {
	               if (text[i].charAt(0) == JUN[j]) {
	                  jungsung = j;
	                  break;
	               }
	            }
	         } else if (type[i] == 3) {
	            for (int j = 0; j < JON.length; j++) {
	               if (text[i].charAt(0) == JON[j]) {
	                  jongsung = j;
	                  break;
	               }
	            }
	         } else if (type[i] == 7) {
	            List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
	            String tempStr = "" + text[i];
	            String lastStr = "";
	            char cho = ' ', jun = ' ', jon = ' ';
	            for (int j = 0; j < tempStr.length(); j++) {
	               Map<String, Integer> map = new HashMap<String, Integer>();
	               char test = tempStr.charAt(j);

	               if (test >= 0xAC00) {
	                  char uniVal = (char) (test - 0xAC00);

	                  cho = (char) (((uniVal - (uniVal % 28)) / 28) / 21);
	                  jun = (char) (((uniVal - (uniVal % 28)) / 28) % 21);
	                  jon = (char) (uniVal % 28);
	               }
	            }
	            jungsung = jun;
	            jongsung = jon;
	         }
	         if (type[i] != 5) {
	            if (i < text.length - 1) {
	               if (type[i + 1] == 1) {
	                  data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
	                  chosung = 0;
	                  jungsung = 0;
	                  jongsung = 0;
	               }
	            } else {
	               data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
	            }
	         }

	      }

	      System.out.println(data);
	      return(data);
   }
   
   
   public static void main(String[] args) {
      String[] text = { "蟹", "げ", "び" };
      int[] type = { 5, 1, 2 };
      

   }

   
   
	/*
	 * protected void doGet(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException { try {
	 * 
	 * //02舘域 :DB尻衣(Connection)魁
	 * 
	 * ArrayList<DotVO> dots = new ArrayList<DotVO>(); DotVO dotVO = null;
	 * 
	 * dotVO = new DotVO();
	 * 
	 * dotVO.setWord(data);
	 * 
	 * dots.add(dotVO);
	 * 
	 * 
	 * StringBuffer dotXML = new StringBuffer(2048);
	 * dotXML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	 * dotXML.append("<Dots>"); for(DotVO dotvo : dots) {
	 * 
	 * 
	 * dotXML.append("</entry>"); dotXML.append("<word>");
	 * dotXML.append(dotvo.getWord()); dotXML.append("</word>");
	 * dotXML.append("</entry>");
	 * 
	 * 
	 * } dotXML.append("</Dots>"); System.out.println(dotXML.toString());
	 * 
	 * 
	 * 
	 * 
	 * // 誓岩 response.setCharacterEncoding("utf-8");
	 * response.setContentType("text/xml; charset=utf-8");
	 * response.getWriter().println(dotXML.toString());
	 * 
	 * } catch (Exception e) { System.out.println("朕学芝 梓端 塙究 神嫌 " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
   
}