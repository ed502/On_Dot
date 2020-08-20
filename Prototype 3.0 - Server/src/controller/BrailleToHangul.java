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

	// 숫자인지 판단
	private boolean number = false;

	// 핫진 코드..ㅠ
	private int chosung, jungsung, jongsung;
	private String data = "";

	public BrailleToHangul() {
		this.conn = DBConnection.getConnection();

	}

	public String getResult() {
		return data;
	}

	public void setResult(String strDot) {
		this.strDot = strDot;
	}

	// 문자열를 6개 단위로 잘라서 타입들을 구하기
	public void cutString() {
		if(strDot.length() % 6 != 0) {
			data = "번역할 수 없는 점자입니다.";
			return;
		}
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

				if (sixDot.get(i).equals("111222")) {
					resultWord.add("것");
					resultType.add(11);
					continue;
				} else {
					System.out.println("번역 불가능");
					data = "번역이 불가능한 점자입니다.";
					break;
				}

			} else if (typeCount == 1) { // 중복되는 점자가 없기 때문에 타입을 바로 넣을 수 있다.

				// 초성 ㅃ 처리 구간
				if (i - 1 >= 0 && sixDot.get(i).equals("121121")) {
					if (resultType.get(i - 1) == 6) {
						resultWord.add("ㅂ");
						resultType.add(1);
						continue;
					}

				}

				int type = getType(sixDot.get(i));
				if (type == 8) {
					number = true;
				}

				String str = getWord(sixDot.get(i), type);
				System.out.println("cutString() typeCount == 1 -> word : " + str + " type : " + type);

				if (i - 1 >= 0 && resultWord.get(i - 1).equals("것")) {
					if (type == 2) {
						System.out.println("여기 되나?");
						resultWord.add("것");
						resultType.add(11);
					}

				} else {
					resultWord.add(str);
					resultType.add(type);
				}

				/* 번역 불가능 한 부분 */
				if (i == 0 && type == 3) { // 처음에 종성이 먼저 오는 경우 번역 불가
					System.out.println("번역 불가능");
					data = "번역이 불가능한 점자입니다.";
					break;
				}
				if (type == 9) { // 숫자일 때 다음 점자가 문자이면 번역 불가능
					if (i + 1 != sixDot.size()) { // i+1 == sixDot.size()이면 마지막 자리니까 정상이니까 아닐 때만 구하면 된다.
						int tempType = getType(sixDot.get(i + 1));
						if (tempType != 9) { // 타입이 9가 아닌 것들이 오면 번역 불가능
							System.out.println("번역 불가능");
							data = "번역이 불가능한 점자입니다.";
							break;
						}
					}
				}
			} else { // 중복되는 점자가 존재하는 경우
				int type = 0;
				String str = null;
				String tempDot = sixDot.get(i);
				System.out.println("tempDot : " + tempDot);
				System.out.println("number : " + number);
				/* 앞에 수표 또는 숫자(type 9)이면 숫자임 */
				if (number) {
					if (resultType.get(i - 1) == 8 || resultType.get(i - 1) == 9) {
						type = 9;
						str = getWord(sixDot.get(i), type);
						resultWord.add(str);
						resultType.add(type);
						System.out.println("cutString() 숫자일때 -> word : " + str + " type : " + type);
					}

				} else {
					if (tempDot.equals("222121")) { // 모음 ㅐ 와 딴이 비교
						if (i == 0) {
							type = 2;
						} else if (i - 1 >= 0) {
							if (resultType.get(i - 1) == 7) {
								type = 2;
							} else if (resultType.get(i - 1) == 1 || resultType.get(i - 1) == 4) {
								System.out.println("새우는?");
								type = 2;
							} else if (resultWord.get(i - 1).equals("ㅑ") || resultWord.get(i - 1).equals("ㅘ")
									|| resultWord.get(i - 1).equals("ㅝ") || resultWord.get(i - 1).equals("ㅜ")) {
								System.out.println("망망대해 지나가누?");
								type = 12;
							} else {
								type = 2;
							}
						}

						str = getWord(tempDot, type);
						resultWord.add(str);
						resultType.add(type);

					} else if (tempDot.equals("112211")) { // 종성 ㅆ 이나 모음 ㅖ 일때 판별
						type = changeType(i);
						System.out.println("112211 -> type : " + type);
						if (type == 0) {
							System.out.println("번역 불가능");
							data = "번역이 불가능한 점자입니다.";
							break;
						}
						str = getWord(tempDot, type);
						resultWord.add(str);
						resultType.add(type);
					} else if (tempDot.equals("111112")) { // 된소리표 일 때 뒤에 1 또는 4가 와야됨 된소리표 랑 초성 ㅅ 이랑 같음..ㅠ
						if (i + 1 != sixDot.size()) {
							if (sixDot.get(i + 1).equals("121121")) {
								type = 6;
							} else {
								type = getType(sixDot.get(i + 1));
								System.out.println("다음 type : " + type);
								if (type == 1 || type == 4 || type == 6) {
									type = 6;
								} else if (type == 0) {
									type = 6;
								} else { // 초성 ㅅ 이라는 의미
									type = 1;
								}
							}

						} else if (sixDot.size() == 1) { // 이건 절대 판단 불가능인데? 한자리가 오는데 초성 ㅅ 인지 된소리표인지 판단할 수 없다..ㅠ 초성이라 하자.
							type = 1;
						} else {
							type = 1;
						}
						str = getWord(tempDot, type);
						resultWord.add(str);
						resultType.add(type);
					} else {

						int tempPrivious = 0;
						if (i - 1 >= 0) {
							tempPrivious = getType(sixDot.get(i - 1));

							if (tempPrivious == 2 || tempPrivious == 4) {
								type = 3;

							}
							System.out.println("여기에 걸리나?");
							if (i + 1 == sixDot.size()) {
								type = 1;
							} else {
								int tempNextType = decisionType(i);
								// int tempNextType = getType(sixDot.get(i+1));
								System.out.println("tempNextType : " + tempNextType);
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
								}
							}

						} else { // 인덱스가 0 즉, 처음에 올 때

							int tempNextType = decisionType(i);
							System.out.println("tempNextType : " + tempNextType);
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

							}
						}

						str = getWord(sixDot.get(i), type);
						resultWord.add(str);
						resultType.add(type);
						System.out.println("cutString() typeCount == 2 -> word : " + str + "type : " + type);
					}
				}

			}
		}

		// 이중모음 처리 구간
		for (int i = 0; i < resultType.size(); i++) {
			if (resultType.get(0) == 8) {
				resultType.remove(0);
				resultWord.remove(0);
			}

			if (resultType.get(i) == 11) {
				resultType.remove(i + 1);
				resultWord.remove(i + 1);
			}

			if (resultType.size() >= 3) { // 최소 3개 이상이어야 됨 왜냐면 ㅜ 붙임표 ㅐ -> ㅟ 가 되니까
				if (resultType.get(i) == 7) { // 붙임표 일 때
					String previousWord = resultWord.get(i - 1);
					String nextWord = resultWord.get(i + 1);
					System.out.println("previousWord : " + previousWord + " nextWord : " + nextWord);
					if (previousWord.equals("ㅑ") || previousWord.equals("ㅘ") || previousWord.equals("ㅜ")
							|| previousWord.equals("ㅝ")) {
						if (nextWord.equals("ㅐ")) {
							// String dot = sixDot.get(i - 1) + sixDot.get(i + 1);
							// String word = getWord(dot, 2);
							// System.out.println("word : " + word + " dot : " + dot);
							// resultWord.remove(i + 1);
							resultWord.remove(i);
							// resultWord.remove(i - 1);
							// resultWord.add(i - 1, word);

							// resultType.remove(i + 1);
							resultType.remove(i);
						} /*
							 * else { resultWord.remove(i); resultType.remove(i); }
							 */

					} else {
						resultWord.remove(i);
						resultType.remove(i);
					}

				} else if (resultType.get(i) == 3 && i != resultType.size() - 1) {
					if (resultType.get(i + 1) == 3) {
						String dot = sixDot.get(i) + sixDot.get(i + 1);
						String word = getWord(dot, 3);
						System.out.println("word : " + word + " dot : " + dot);
						resultWord.remove(i + 1);
						resultWord.remove(i);
						resultWord.add(i, word);

						resultType.remove(i + 1);
					}
				} else if (resultType.get(i) == 12) {
					// ㅑ ㅘ ㅝ ㅜ+ 딴 이 -> ㅒ, ㅙ, ㅞ, ㅟ
					if (resultWord.get(i - 1).equals("ㅑ") || resultWord.get(i - 1).equals("ㅘ")
							|| resultWord.get(i - 1).equals("ㅝ") || resultWord.get(i - 1).equals("ㅜ")) {
						String dot = sixDot.get(i - 1) + sixDot.get(i);
						String word = getWord(dot, 2);
						System.out.println("word : " + word + " dot : " + dot);
						resultWord.remove(i);
						resultWord.remove(i - 1);
						resultWord.add(i - 1, word);

						resultType.remove(i);
					}

				}
			}

		}
		for (int i = 0; i < resultWord.size(); i++) {
			System.out.println(
					"resultWord[" + i + "] : " + resultWord.get(i) + " resultType[" + i + "] : " + resultType.get(i));
		}

		johab(resultWord, resultType);


	}

	// 중복된 점자 몇개인지
	public int countType(String dot) {
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = null;

			if (number) {
				sql = "select count(type) from translateDot where dot='" + dot + "' and type = 9";
			} else {
				sql = "select count(type) from translateDot where dot='" + dot + "' and not type = 9";
			}

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

			if (number) {
				sql = "select type from translateDot where dot='" + dot + "' and type = 9";
			} else {
				sql = "select type from translateDot where dot='" + dot + "' and not type = 9";
			}

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
			if (type == 12) {
				String temp = getWord(sixDot.get(beginIndex), 3);
				if (temp != null) {
					if (temp.equals("ㅑ") || temp.equals("ㅘ") || temp.equals("ㅝ") || temp.equals("ㅜ")) {
						type = 12;
					}
				} else {
					type = 2;
				}
			}
		} else { // 뒤에 점자가 없으면 해당 점자는 가,나,다 같은 약어(type 4번) 일수 밖에 없음.
			type = 4;
		}

		return type;
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
			if (i > 0 && type.get(i) == 5 && type.get(i - 1) == 1) { // 인 옥 열 이런것들 분해해서 앞에 초성이랑 합치는 코드

				int cho = text.get(i - 1).charAt(0);
				int cho2 = ((((text.get(i).charAt(0) - 0xAC00) / 28) / 21) + 0x1100) + 8174;
				int jung2 = ((((text.get(i).charAt(0) - 0xAC00) / 28) % 21) + 0x1161) + 8174;
				int jong2 = ((((text.get(i).charAt(0) - 0xAC00) % 28)) + 0x11A8 - 1) + 8073;
				System.out.println((int) jong2);
				// 유니코드를 아스키코드화 ( 아스키코드만 조합이 되더라 )
				if (jong2 == 12600)
					jong2++;
				if (jong2 == 12613)
					jong2 += 2;
				// 유니코드 -> 아스키코드 조정
				System.out.println((int) jong2);
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
				} // 분해하고 재조합한 초성 모음 종성

				// data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) +
				// jongsung)); // 5번 합침 chosung =
				// System.out.println("5번 안 : "+ data);
				// 0; jungsung = 0;
				// jongsung = 0;

			} else if (type.get(i) == 5) { // 앞에 초성안오고 열 옥 인 만 온경우

				int jung2 = ((((text.get(i).charAt(0) - 0xAC00) / 28) % 21) + 0x1161) + 8174;
				int jong2 = ((((text.get(i).charAt(0) - 0xAC00) % 28)) + 0x11A8 - 1) + 8073;

				// 유니코드를 아스키코드화 ( 아스키코드만 조합이 되더라 )

				if (jong2 == 12600)
					jong2++;
				if (jong2 == 12613)
					jong2 += 2;
				// 유니코드 -> 아스키코드 조정

				chosung = 11;

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
				System.out.println(jongsung);

			} /*
				 * if (type.get(i) == 10) { // 그리고, 그래서, 그러나 출력 data = data + text.get(i); }
				 */
			if (i > 0 && type.get(i - 1) == 8 && type.get(i) == 9) { // 이전에 수표고 음 ..이건미누기랑 얘기해야겠다
				data = data + text.get(i);
			}
			if (type.get(i) == 4) { // 가 나 다 마 바 사 등

				int cho = ((((text.get(i).charAt(0) - 0xAC00) / 28) / 21) + 0x1100) + 8241;
				if (cho > 12593) // 유니코드 -> 아스키코드 보정
					cho++;
				if (cho > 12596)
					cho += 2;
				if (cho > 12601)
					cho += 7;
				if (cho > 12611)
					cho += 1;

				if (i > 0 && type.get(i - 1) == 6) { // 된소리표 오면 cho ++ 해서 ㄱ-> ㄲ , ㅅ -> ㅆ
					if (cho == 12593 || cho == 12613)
						cho++;
				}
				System.out.println(cho);
				for (int j = 0; j < CHO.length; j++)
					if ((int) cho == (int) CHO[j]) {
						chosung = j;
						break;
					}
				jungsung = 0;
				jongsung = 0;

			}

			if (type.get(i) == 1) { // 일반적인 부분 [ 초성 ]
				for (int j = 0; j < CHO.length; j++)
					if ((int) text.get(i).charAt(0) == (int) CHO[j]) {
						System.out.println("초성 찾음" + (int) (CHO[j]) + resultWord.get(i));
						chosung = j;
						if (i > 0 && type.get(i - 1) == 6) { // 된소리표 오면 cho ++ 해서 ㄱ-> ㄲ , ㄷ -> ㄸ
							if (chosung == 0 || chosung == 3 || chosung == 7 || chosung == 8 || chosung == 12)
								chosung++;
						}
						break;
					}
			} else if (type.get(i) == 2) { // [ 모음 ]
				if (i > 0 && type.get(i - 1) != 1) {
					chosung = 11;
				}

				else if (i == 0) {
					chosung = 11;
				}
				System.out.println(" 몇번쨰 게 : " + i);
				for (int j = 0; j < JUN.length; j++) {
					if ((int) text.get(i).charAt(0) == (int) JUN[j]) {
						System.out.println("모음 찾음" + (int) (JUN[j]) + resultWord.get(i));
						jungsung = j;
						break;
					}
				}
			} else if (type.get(i) == 3) { // [ 종성 ]
				for (int j = 0; j < JON.length; j++) {
					if ((int) text.get(i).charAt(0) == (int) JON[j]) {
						System.out.println("종성 찾음" + (int) (JON[j]) + resultWord.get(i));
						jongsung = j;
						break;
					}
				}
			}
			if (type.get(i) != 10 && type.get(i) != -1) { // [ 이부분 이해 못하겠는데 암튼 이거해야 잘돼]
				if (i < text.size() - 1) {

					if ((type.get(i + 1) == 1 || type.get(i + 1) == 4 || type.get(i + 1) == 11) && type.get(i) != 6) {
						data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
						chosung = 0;
						jungsung = 0;
						jongsung = 0;
						System.out.println(" 조 합 a: " + data);
					} else if (i < text.size() - 2 && type.get(i) == 2 && type.get(i + 1) == 6
							&& type.get(i + 2) != 3) {
						data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
						chosung = 0;
						jungsung = 0;
						jongsung = 0;
						System.out.println(" 조 합 v: " + data);

					}

					else if (type.get(i) == 3 && type.get(i + 1) == 6) {
						data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
						chosung = 0;
						jungsung = 0;
						jongsung = 0;
						System.out.println(" 조 합 : h" + data);

					} else if ((type.get(i + 1) == 2 || type.get(i + 1) == 5) && type.get(i) != 1) { // 모음이오거나 약자가 오는데
						// 지금 내가 초성이아니면
						// 조합
						data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
						chosung = 0;
						jungsung = 0;
						jongsung = 0;
						System.out.println(" 조 합 : s" + data);
					}

				} else if (type.get(i) == 11) { // 것 껏
					if (i > 0 && type.get(i - 1) == 6) {
						data += "껏";
					} else
						data += "것";
					chosung = 0;
					jungsung = 0;
					jongsung = 0;
				} /* else if (i > 0 && type.get(i) == 5 && type.get(i - 1) != 1) */
				else { // 일반적인부분 여기서 번역됌
					data = data + ((char) (0XAC00 + (28 * 21 * chosung) + (28 * jungsung) + jongsung));
					System.out.println(" 조 합!! : " + data);
				}
			}

		}
		System.out.println("번역 결과 " + data);
	}
}