package kr.ac.kpu.ondot.Data;

import androidx.annotation.NonNull;

public class DotVO {
    private int id;
    private String word;
    private String dot;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @NonNull
    @Override
    public String toString() {
        String str = "ID : " + id + " word : " + word + " dot : " + dot + " type : " + type;
        return str;
    }
}
