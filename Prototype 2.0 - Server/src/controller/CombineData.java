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
	private char[] CHO =
			/* ぁ あ い ぇ え ぉ け げ こ さ ざ し じ す ず せ ぜ そ ぞ */
			{ 'ぁ', 'あ', 'い', 'ぇ', 'え', 'ぉ', 'け', 'げ', 'こ', 'さ', 'ざ', 'し', 'じ', 'す', 'ず', 'せ', 'ぜ', 'そ', 'ぞ' };
	private char[] JUN =
			/* ただちぢっつづてでとどなにぬねのはばぱひび */
			{ 'た', 'だ', 'ち', 'ぢ', 'っ', 'つ', 'づ', 'て', 'で', 'と', 'ど', 'な', 'に', 'ぬ', 'ね', 'の', 'は', 'ば', 'ぱ', 'ひ', 'び' };
	/* X ぁあぃいぅうぇぉおかがきぎくぐけげごさざしじずせぜそぞ */
	private char[] JON = { ' ', 'ぁ', 'あ', 'ぃ', 'い', 'ぅ', 'う', 'ぇ', 'ぉ', 'お', 'か', 'が', 'き', 'ぎ', 'く', 'ぐ', 'け', 'げ',
			'ご', 'さ', 'ざ', 'し', 'じ', 'ず', 'せ', 'ぜ', 'そ', 'ぞ' };

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void hak(String[] text, int[] type) {
		int chosung = 0;
		int jungsung = 0;
		int jongsung = 0;

		if (text.length == 1) {
			
		} else {
			for (String t : text) {
				System.out.println("CombineData  text : " + t);
			}
			for (int i : type)
				System.out.println("CombineData type : " + i);

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
		}

		System.out.println(data);

		setData(data);
	}

}