package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombineData {
	private String data = "";
	CombineData combineData = null;

	private char[] CHOARRAY = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ',
			'ㅍ', 'ㅎ' };
	private char[] JUNARRAY = { 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ',
			'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ' };
	private char[] JONARRAY = {' ','ㄱ','ㄲ','ㄳ','ㄴ','ㄵ','ㄶ','ㄷ','ㄹ','ㄺ','ㄻ','ㄼ','ㄽ','ㄾ','ㄿ','ㅀ','ㅁ','ㅂ','ㅄ','ㅅ','ㅆ','ㅇ','ㅈ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'};

	private char[] CHO = /* ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ */ { 0x1100, 0x1101, 0x1102, 0x1103, 0x1104, 0x1105,
			0x1106, 0x1107, 0x1108, 0x1109, 0x110A, 0x110B, 0x110C, 0x110D, 0x110E, 0x110F, 0x1110, 0x1111, 0x1112 };
	private char[] JUN =
			/* ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ */
			{ 0x1161, 0x1162, 0x1163, 0x1164, 0x1165, 0x1166, 0x1167, 0x1168, 0x1169, 0x116A, 0x116B, 0x116C, 0x116D,
					0x116E, 0x116F, 0x1170, 0x1171, 0x1172, 0x1173, 0x1174, 0x1175 };
	/* X ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ */
	private char[] JON = { 0, 0x11A8, 0x11A9, 0x11AA, 0x11AB, 0x11AC, 0x11AD, 0x11AE, 0x11AF, 0x11B0, 0x11B1, 0x11B2,
			0x11B3, 0x11B4, 0x11B5, 0x11B6, 0x11B7, 0x11B8, 0x11B9, 0x11BA, 0x11BB, 0x11BC, 0x11BD, 0x11BE, 0x11BF,
			0x11C0, 0x11C1, 0x11C2 };

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void dotCombination(String[] strDot, int[] typeDot, Boolean[] stateDot) {

		int beginIndex = 0;

		for (int i = 0; i < strDot.length; i++) {
			System.out.println("strDot[" + i + "] : " + strDot[i] + "  typeDot[" + i + "] : " + typeDot[i]
					+ "  stateDot[" + i + "] : " + stateDot[i]);
		}

		for (int i = beginIndex; i < strDot.length; i++) {
			if (stateDot[i] == true) {
				beginIndex = i + 1;
				break;
			}

		}

	}

	public void kim(String a, String b) { // 여기는 초성 + 약어(ex 인)를 조합하는 곳?
		System.out.println("받은것 : " + a);

		int cho, jung, jong;
		jong = a.charAt(0) - 0xAC00;
		cho = jong / (21 * 28) + 0x1100;
		jong = jong % (21 * 28);
		jung = jong / 28 + 0x1161;
		jong = jong % 28 + 0x11A8 - 1;

		String[] data = new String[3];
		int[] type = new int[3];

		for (int i = 0; i < CHOARRAY.length; i++) {
			if (b.charAt(0) == CHOARRAY[i]) {
				data[0] = CHO[i] + "";
			}
		}
		System.out.println("data[0] - char : " + data[0]);
		// data[0] = ((char) ((b.charAt(0) - 0xAC00) / 21 * 28 + 0x1100)) + "";
		// data[1] = "ㅡ";
		// data[2] = "ㄹ";

		data[1] = ((char) jung) + "";
		data[2] = ((char) jong) + "";

		type[0] = 1;
		type[1] = 2;
		type[2] = 3;

		//hak(data, type);

	}

	public void hak(String[] text, int[] type, Boolean[] stateDot) {
		StringBuilder sb = new StringBuilder();

		int chosung = 0;
		int jungsung = 0;
		int jongsung = 0;
		int a = 0;
		int b = 0;
		for (int i = 0; i < text.length; i++) { // 출력
			System.out.println("hak() -> text[" + i + "] : " + text[i] + "  type[" + i + "] : " + type[i]
					+ "  stateDot[" + i + "] : " + stateDot[i]);
		}

		for (int i = 0; i < text.length; i++) {
			if (type[i] == 5) { // 약어일 때 -> 그래서 , 그런데 같은
				sb.append(text[i]);
			} else if (type[i] == 1) { // 초성일 때
				int choInt = text[i].charAt(0) - 0xAC00 / (21 * 28);
				//System.out.println("choInt : " + Character.toString(choInt));
				for (int j = 0; j < CHO.length; j++) {
					if (text[i].charAt(0) == CHOARRAY[j]) {
						//System.out.println((int) (CHO[j]));
						chosung = j;
						System.out.println("초성 :  " + chosung + " text["+ i + "] 값 : " + text[i].charAt(0) + " j : " + j);
						break;
					}
				}
			} else if (type[i] == 2) { // 모음일 때
				for (int j = 0; j < JUN.length; j++) {
					// System.out.println((int)text[i].charAt(0)+" "+(int)JUN[j]);
					if (text[i].charAt(0) == JUNARRAY[j]) {
						//System.out.println((int) (JUN[j]) + " " + (int) text[i].charAt(0));
						jungsung = j;
						//System.out.println("모음 :  " + jungsung);
						System.out.println("모음 :  " + jungsung + " text["+ i + "] 값 : " + text[i].charAt(0) + " j : " + j);
						break;
					}
				}
			} else if (type[i] == 3) { // 종성일 때
				for (int j = 0; j < JON.length; j++) {
					if (text[i].charAt(0) == JONARRAY[j]) {
						//System.out.println((int) (JON[j]));
						jongsung = j;
						//System.out.println("종성 :  " + jongsung);
						System.out.println("종성 :  " + jongsung + " text["+ i + "] 값 : " + text[i].charAt(0) + " j : " + j);
						break;
					}
				}
			} else if (type[i] == 7) { // 약어중에 억 인 같은 것들
				String tempStr = text[i];
				char cho = ' ', jun = ' ', jon = ' ';
				char test = tempStr.charAt(0);
				if (test >= 0xAC00) {
					char uniVal = (char) (test - 0xAC00);

					cho = (char) (((uniVal - (uniVal % 28)) / 28) / 21);
					jun = (char) (((uniVal - (uniVal % 28)) / 28) % 21);
					jon = (char) (uniVal % 28);
				}
				jungsung = jun;
				jongsung = jon;
			}
			
			// 여기부터가 합치는 구간?
			
			if (type[i] != 5 && type[i] != 4) {
				if (i < text.length - 1) {
					if (type[i + 1] == 1 || stateDot[i] == true) {
						// data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
						System.out.println("초성 : " + CHO[chosung] + " 중성 : " + JUN[jungsung] + " 종성 : " + JON[jongsung]);
						int c = (CHO[chosung] * 21 * 28) + (JUN[jungsung] * 28) + JON[jongsung];
						data = String.valueOf((char) (c + 0xAC00));
						System.out.println("data : " + data);
						chosung = 0;
						jungsung = 0;
						jongsung = 0;
						sb.append(data);
					}
				} else {
					//data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
					System.out.println("초성 : " + CHO[chosung] + " 중성 : " + JUN[jungsung] + " 종성 : " + JON[jongsung]);
					int c = (CHO[chosung] * 21 * 28) + (JUN[jungsung] * 28) + JON[jongsung];
					data = String.valueOf((char) (c + 0xAC00));
					System.out.println("data : " + data);
					chosung = 0;
					jungsung = 0;
					jongsung = 0;
					sb.append(data);
				}
			}
		}

		/*if (text.length == 1) {

		} else {
			for (int i = 0; i < text.length; i++) {
				System.out.println("인덱스 " + i + "   word :" + text[i] + "  타입 " + type[i]);
			}

			for (int i = 0; i < text.length; i++) {
				System.out.println("데이터 배열에있는 것들 : " + text[i]);
				if (type[i] == 5) {
					data = data + text[i];
				} else if (type[i] == 1 || type[i] == 5) {
					for (int j = 0; j < CHO.length; j++) {

						if ((int) text[i].charAt(0) == (int) CHO[j]) {
							System.out.println((int) (CHO[j]));
							chosung = j;
							System.out.println("초성 :  " + chosung);
							break;
						}
					}
				} else if (type[i] == 2) {

					for (int j = 0; j < JUN.length; j++) {

						// System.out.println((int)text[i].charAt(0)+" "+(int)JUN[j]);
						if ((int) text[i].charAt(0) == (int) JUN[j]) {
							System.out.println((int) (JUN[j]) + " " + (int) text[i].charAt(0));
							jungsung = j;
							System.out.println("모음 :  " + jungsung);
							break;
						}
					}
				} else if (type[i] == 3) {
					for (int j = 0; j < JON.length; j++) {
						if ((int) text[i].charAt(0) == (int) JON[j]) {
							System.out.println((int) (JON[j]));
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
				if (type[i] != 5 && type[i] != -1) {
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
				}*/

		System.out.println("결과 는 : " + sb);

		setData(sb.toString());
	}

}