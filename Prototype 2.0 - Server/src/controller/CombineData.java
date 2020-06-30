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
	private String data = "";
	CombineData combineData = null;
	
	private char[] CHO =
			/* ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ */
			{ 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
	private char[] JUN =
			/* ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ */
			{ 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ' };
	/* X ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ */
	private char[] JON = { ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ',
			'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void kim(String a, String b) {
		System.out.println("받은것 : " + a);
		
		int jung = ((((int) a.charAt(0) - 0xAC00) / 28) % 21) + 0x1161;
		int jong = ((((int) a.charAt(0) - 0xAC00) % 28)) + 0x11A8 - 1;
		jong = (((int) a.charAt(0) - 0xAC00) % 28) != 0 ? (char) ((((int) a.charAt(0) - 0xAC00) % 28)) + 0x11A8 - 1 : 0;

		int cho = b.charAt(0);
		System.out.println((char) cho);
		System.out.println((char)(jung));
		System.out.println((char) jong);

		String[] data = new String[3];
		int[] type = new int[3];
		data[0] = b.charAt(0) + "";
		//data[1] = "ㅡ";
		//data[2] = "ㄹ";
		
		data[1] = ((char) jung) + "";
		data[2] = ((char) jong) + "";
		 
		type[0] = 1;
		type[1] = 2;
		type[2] = 3;
		
		hak(data, type);

	}

	public void hak(String[] text, int[] type) {
		int chosung = 0;
		int jungsung = 0;
		int jongsung = 0;
		int a = 0;
		int b = 0;
		if (text.length == 1) {

		} else {
			for (int i = 0; i < text.length; i++) {
				System.out.println("인덱스 " + i + "   word :" + text[i] + "  타입 " + type[i]);
			}

			for (int i = 0; i < text.length; i++) {
				System.out.println("데이터 배열에있는 것들 : "+text[i]);
				if (type[i] == 5) {
					data = data + text[i];
				} else if (type[i] == 1 /*|| type[i] == 5*/) {
					for (int j = 0; j < CHO.length; j++) {
						if (text[i].charAt(0) == CHO[j]) {
							chosung = j;
							System.out.println("초성 :  " + chosung);
							break;
						}
					}
				} else if (type[i] == 2) {
					for (int j = 0; j < JUN.length; j++) {
						
						if (text[i].charAt(0) == JUN[j]) {
							jungsung = j;
							System.out.println("모음 :  " + jungsung);
							break;
						}
					}
				} else if (type[i] == 3) {
					for (int j = 0; j < JON.length; j++) {
						if (text[i].charAt(0) == JON[j]) {
							jongsung = j;
							System.out.println("종성 :  " + jongsung);
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
				if (type[i] != 5&&type[i]!=-1) {
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
		}

		System.out.println(data);

		setData(data);
	}

}