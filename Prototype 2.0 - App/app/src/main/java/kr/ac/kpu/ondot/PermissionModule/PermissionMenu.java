package kr.ac.kpu.ondot.PermissionModule;


/*
* EDUCATE 교육 : 블루투스 권한
* QUIZ 퀴즈 : 음성 STT 권한
* TRANSLATE 번역 : 음성 TTS 권한
* VOICEBOARD 음성 게시판 : 음성 STT 권한
* */
public enum PermissionMenu {

    EDUCATE(true),
    QUIZ(true),
    TRANSLATE(false),
    VOICEBOARD(true);

    private boolean checkPermission;

    PermissionMenu(boolean checkPermission){
        this.checkPermission = checkPermission;
    }

    public boolean getCheckPermission(){
        return checkPermission;
    }
}
