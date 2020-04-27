package kr.ac.kpu.ondot.SpeechModule;

/*
 * Speech Service Type 결정을 위한 ENUM TYPE
 * true : 단어 위주 false : 문장 위주
 * */
public enum SpeechMenu {

    QUIZ(true),
    VOICEBOARD(false);

    private boolean speechMenu;

    SpeechMenu(boolean speechMenu){
        this.speechMenu = speechMenu;
    }

    public boolean getSpeechMenu(){
        return speechMenu;
    }
}
