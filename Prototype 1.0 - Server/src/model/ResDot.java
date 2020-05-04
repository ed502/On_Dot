package model;

import java.util.ArrayList;

/*
 * Created by hotjin on 2020-04-25
*/

// json 데이터 응답용
public class ResDot {
	private ArrayList<DotVO> dots;
	
	public ArrayList<DotVO> getDots() {
		return dots;
	}
	public void setDots(ArrayList<DotVO> dots) {
		this.dots = dots;
	}

	
}
