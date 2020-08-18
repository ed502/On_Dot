package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import database.DBConnection;

public class BrailleToHangul {
	private char[] CHO =
			/* ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ */
			{ 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
	private char[] JUN =
			/* ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ */
			{ 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ' };
	/* X ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ */
	private char[] JON = { ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ',
			'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	// 1과 2로 이루어진 입력된 문자열
	private String strDot = null;
	// 6개 단위로 끊어서 넣음
	private ArrayList<String> sixDot = new ArrayList<String>();

	// 최종적으로 들어갈 단어와 타입
	private ArrayList<String> resultWord = new ArrayList<String>();
	private ArrayList<Integer> resultType = new ArrayList<Integer>();
	// private String[] resultWord = new String[sixDot.size()];
	// private int[] resultType = new int[sixDot.size()];

	// 자르는 구간 ( 어디까지 합쳐서 단어를 만들어야 하는지 ) 기준을 1로 설정 -> 1이 있는 해당 인덱스까지 조합하면 됨
	private int[] cutIndex = new int[sixDot.size()];

	// 핫진 코드..ㅠ
	private int chosung, jungsung, jongsung;
	private String data = null;

	public BrailleToHangul() {
		this.conn = DBConnection.getConnection();

	}

	public String getResult() {
		return strDot;
	}

	public void setResult(String strDot) {
		this.strDot = strDot;
	}

	// 문자열를 6개 단위로 잘라서 타입들을 구하기
	public void cutString() {
		int len = strDot.length() / 6;
		int lastIndex = 0;
		for (int i = 0; i < len; i++) {
			lastIndex = (i + 1) * 6;
			if (i != len - 1) {
				sixDot.add(strDot.substring(i * 6, lastIndex));
			} else {
				sixDot.add(strDot.substring(i * 6));
			}

		}
		// 6 개씩 들어갔는지 확인
		for (int i = 0; i < sixDot.size(); i++) {
			System.out.println("al [" + i + "] : " + sixDot.get(i));
		}

		for (int i = 0; i < sixDot.size(); i++) {
			int typeCount = countType(sixDot.get(i));
			if (typeCount == 0) { // db 데이터와 매칭된 점자가 없기 때문에 번역 불가능
				System.out.println("번역 불가능");
				break;
			} else if (typeCount == 1) { // 중복되는 점자가 없기 때문에 타입을 바로 넣을 수 있다.
				int type = getType(sixDot.get(i));
				String str = getWord(sixDot.get(i), type);
				System.out.println("cutString() typeCount == 1 -> word : " + str + "type : " + type);
				resultWord.add(str);
				resultType.add(type);

				/* 번역 불가능 한 부분 */
				if (i == 0 && type == 3) { // 처음에 종성이 먼저 오는 경우 번역 불가
					System.out.println("번역 불가능");
					break;
				}
				if (type == 9) { // 숫자일 때 다음 점자가 문자이면 번역 불가능
					if (i + 1 != sixDot.size()) { // i+1 == sixDot.size()이면 마지막 자리니까 정상이니까 아닐 때만 구하면 된다.
						int tempType = getType(sixDot.get(i + 1));
						if (tempType != 9) { // 타입이 9가 아닌 것들이 오면 번역 불가능
							System.out.println("번역 불가능");
							break;
						}
					}
				}
			} else { // 중복되는 점자가 존재하는 경우
				int type = 0;
				String str = null;
				String tempDot = sixDot.get(i);
				System.out.println("tempDot : " + tempDot);
				/* 앞에 수표 또는 숫자(type 9)이면 숫자임 */
				if (i - 1 >= 0) {
					int tempPreviousType = getType(sixDot.get(i - 1));
					if (tempPreviousType == 8 || tempPreviousType == 9) {
						type = 9;
						str = getWord(sixDot.get(i), type);
						resultWord.add(str);
						resultType.add(type);
						System.out.println("cutString() 숫자일때 -> word : " + str + " type : " + type);
					}

				} else if (tempDot.equals("112211")) { // 종성 ㅆ 이나 모음 ㅖ 일때 판별
					type = changeType(i);
					System.out.println("112211 -> type : " + type);
					if (type == 0) {
						System.out.println("번역 불가능");
						break;
					}
					str = getWord(tempDot, type);
					resultWord.add(str);
					resultType.add(type);
				} else if (tempDot.equals("111112")) { // 된소리표 일 때 뒤에 1 또는 4가 와야됨 된소리표 랑 초성 ㅅ 이랑 같음..ㅠ
					if (i + 1 != sixDot.size()) {
						type = getType(sixDot.get(i + 1));
						if (type == 1 || type == 4) {
							type = 6;
						} else { // 초성 ㅅ 이라는 의미
							type = 1;
						}
					} else if (sixDot.size() == 1) { // 이건 절대 판단 불가능인데? 한자리가 오는데 초성 ㅅ 인지 된소리표인지 판단할 수 없다..ㅠ 초성이라 하자.
						type = 1;
					} else { // 마지막 인덱스 일 때 초성 ㅅ 이 오면 번역이 불가능해! 된소리표 역시

					}
					str = getWord(tempDot, type);
					resultWord.add(str);
					resultType.add(type);
				} else {
					/*
					 * int tempPrivious = 0; if(i-1 >= 0) { tempPrivious = getType(sixDot.get(i-1));
					 * if(tempPrivious == 2 || tempPrivious == 4) { type = 3; } }
					 */

					int tempNextType = decisionType(i);
					// tempNextType 이 2 또는 5 이면 초성
					// tempNextType 이 1, 3, 4, 6 중에 하나면 중성
					// tempNextType 이 1, 3, 4 중에 하나면 가,나,다 같은 약어
					// tempNextType 이 2, 9 가 아니면 종성?
					switch (tempNextType) {
					case 1:
					case 3:
					case 4:
						type = 4;
						break;
					case 2:
					case 5:
						type = 1;
						break;
					default:

					}

					str = getWord(sixDot.get(i), type);
					resultWord.add(str);
					resultType.add(type);
					System.out.println("cutString() typeCount == 2 -> word : " + str + "type : " + type);
				}
			}
		}

		// 출력해보자
		for (int i = 0; i < resultWord.size(); i++) {
			System.out.println(
					"resultWord[" + i + "] : " + resultWord.get(i) + " resultType[" + i + "] : " + resultType.get(i));
		}

		johab(resultWord, resultType);

		// 번역 가능한지 체크 -> true 이면 이제 조합 하면 돼!!!
		/*
		 * Boolean possible = checkTranslate(); if(possible) {
		 * System.out.println("번역 가능"); }else { System.out.println("번역 불가능"); }
		 */

	}

	// 중복된 점자 몇개인지
	public int countType(String dot) {
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = null;

			sql = "select count(type) from translateDot where dot='" + dot + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
			System.out.println("countType() -> count : " + count);

		} catch (SQLException e) {
			e.printStackTrace();
			count = 0;
		}
		return count;
	}

	// 해당 점자가 타입이 무엇인지 구하는 것
	public int getType(String dot) {
		int type = 0; // 0 은 없는 것
		try {
			stmt = conn.createStatement();
			String sql = null;

			sql = "select type from translateDot where dot='" + dot + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				type = rs.getInt("type");
			}
			System.out.println("getType() -> type : " + type);

		} catch (SQLException e) {
			e.printStackTrace();
			type = 0;
		}
		return type;
	}

	// 해당 점자의 뜻이 무엇인지 구하는 것
	public String getWord(String dot, int type) {
		String word = null;
		try {
			stmt = conn.createStatement();
			String sql = null;

			sql = "select word from translateDot where dot='" + dot + "' and type = '" + type + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				word = rs.getString("word");
			}
			System.out.println("getWord() -> word : " + word);

		} catch (SQLException e) {
			e.printStackTrace();
			word = null;
		}
		return word;
	}

	// 뒤에있는 점자 타입을 보고 앞에 점자 타입을 정함
	public int decisionType(int beginIndex) {
		// 뒤에 오는 점자가 타입이 중복되는게 있지 않나?
		int type = 0;
		if (beginIndex + 1 != sixDot.size()) {
			type = getType(sixDot.get(beginIndex + 1));
		} else { // 뒤에 점자가 없으면 해당 점자는 가,나,다 같은 약어(type 4번) 일수 밖에 없음.
			type = 4;
		}

		return type;
	}

	// 번역 불가능 한 부분이 있는지 검사
	public Boolean checkTranslate() {
		int checkType = 0;
		Boolean b = false; // true 가 번역 가능한 경우

		for (int i = 0; i < resultType.size(); i++) {
			if (i + 1 != resultType.size()) {
				checkType = resultType.get(i);
				int nextType = resultType.get(i + 1);
				switch (checkType) {
				case 1:
					if (nextType == 2 || nextType == 5) {
						b = true;
					}
					break;
				case 2:
					if (nextType == 1 || nextType == 2 || nextType == 3 || nextType == 6) {
						b = true;
					}
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6: // 된소리표
					if (nextType == 1 || nextType == 4) {
						b = true;
					}
					break;
				case 7:
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				}
			}

		}

		return b;
	}

	// 종성 ㅆ 랑 모음 ㅖ 구별하는 부분
	public int changeType(int index) {
		if (index == 0) {
			return 2;
		} else if (index - 1 >= 0) {
			int type = getType(sixDot.get(index - 1));
			if (type == 2 || type == 4) { // 종성 ㅆ
				return 3;
			} else {
				return 2;
			}
		} else {
			return 0;
		}

	}

	public void johab(ArrayList<String> text, ArrayList<Integer> type) {

		for (int i = 0; i < text.size(); i++) {
			if (i > 0 && type.get(i) == 5 && type.get(i - 1) == 1) {

				int cho = text.get(i - 1).charAt(0);
				int cho2 = ((((text.get(i).charAt(0) - 0xAC00) / 28) / 21) + 0x1100) + 8174;
				int jung2 = ((((text.get(i).charAt(0) - 0xAC00) / 28) % 21) + 0x1161) + 8174;
				int jong2 = ((((text.get(i).charAt(0) - 0xAC00) % 28)) + 0x11A8 - 1) + 8073;

				/*
				 * if (jong2 == 12600) jong2--; if (jong2 == 12611) jong2--; if (jong2 == 12617)
				 * jong2--;
				 */

				for (int j = 0; j < CHO.length; j++)
					if ((int) text.get(i - 1).charAt(0) == (int) CHO[j]) {
						chosung = j;
						break;
					}

				for (int j = 0; j < JUN.length; j++) {
					if ((int) jung2 == (int) JUN[j]) {
						jungsung = j;
						break;
					}
				}
				for (int j = 0; j < JON.length; j++) {
					if ((int) jong2 == (int) JON[j]) {
						jongsung = j;
						break;
					}
				}

				data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));

			} else if (type.get(i) == 5) {
				data = data + text.get(i);
			}
			if (type.get(i) == 10 || type.get(i) == 7 || type.get(i) == 6) {
				data = data + text.get(i);
			}
			if (i > 0 && type.get(i - 1) == 8 && type.get(i) == 9) {
				data = data + text.get(i);
			}
			if (type.get(i) == 4) {
				int cho = ((((text.get(i).charAt(0) - 0xAC00) / 28) / 21) + 0x1100) + 8241;
				if (cho > 12593)
					cho++;
				if (cho > 12596)
					cho+=2;
				if(cho>12601)
					cho+=7;
				if(cho>12611)
					cho+=1;

				System.out.println(cho);
				for (int j = 0; j < CHO.length; j++)
					if ((int) cho == (int) CHO[j]) {
						chosung = j;
						break;
					}
				jungsung = 0;
				jongsung = 0;
			}
			if (type.get(i) == 1) {
				char cho2 = (char) ((((text.get(i).charAt(0) - 0xAC00) / 28) / 21) + 0x1100);
				System.out.println(cho2);
				for (int j = 0; j < CHO.length; j++)
					if ((int) text.get(i).charAt(0) == (int) CHO[j]) {
						System.out.println("초성 찾음" + (int) (CHO[j]) + resultWord.get(i));
						chosung = j;
						break;
					}
			} else if (type.get(i) == 2) {
				if (i > 0 && type.get(i - 1) != 1)
					chosung = 11;
				for (int j = 0; j < JUN.length; j++) {
					if ((int) text.get(i).charAt(0) == (int) JUN[j]) {
						System.out.println("모음 찾음" + (int) (JUN[j]) + resultWord.get(i));
						jungsung = j;
						break;
					}
				}
			} else if (type.get(i) == 3) {
				for (int j = 0; j < JON.length; j++) {
					if ((int) text.get(i).charAt(0) == (int) JON[j]) {
						System.out.println("종성 찾음" + (int) (JON[j]) + resultWord.get(i));
						jongsung = j;
						break;
					}
				}
			}
			if (type.get(i) != 5 && type.get(i) != -1) {
				if (i < text.size() - 1) {
					if (type.get(i + 1) == 1) {
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
		System.out.println("번역 결과 " + data);
	}
}