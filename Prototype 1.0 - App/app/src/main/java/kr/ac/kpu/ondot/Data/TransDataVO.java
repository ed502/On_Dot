package kr.ac.kpu.ondot.Data;

import androidx.annotation.NonNull;

public class TransDataVO {
    private String word;
    private String dot;
    private int type;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}