package kr.ac.kpu.ondot.VoiceModule;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.EnumData.MenuType;
import kr.ac.kpu.ondot.R;


public class VoicePlayerModuleManager {
    private final String DEBUG_TYPE = "type111";

    private VoicePlayerModuleSingleton voicePlayerModuleSingleton;
    private Context context;


    public VoicePlayerModuleManager(Context context){
        voicePlayerModuleSingleton = VoicePlayerModuleSingleton.getInstance(context);
        this.context = context;
    }

    // stop 리스너 할당
    public void setVoicePlayerStopListener(VoicePlayerStopListener voicePlayerStopListener){
        voicePlayerModuleSingleton.setVoicePlayerStopListener(voicePlayerStopListener);
    }

    // stop 리스너 해제 (초기화)
    public void initVoicePlayerStopListener(){
        voicePlayerModuleSingleton.initVoicePlayerStopListener();
    }

    /*
     * int형 음성 file id들을 출력하는 함수
     * 전달받은 int형 soundid를 queue에 담아 VoicePlayerModuleSingleton에 전달함
     * */
    public void start(int soundId){
        Queue<Integer> soundIdQueue = new LinkedList<>();
        soundIdQueue.add(soundId);
        voicePlayerModuleSingleton.start(soundIdQueue);
    }

    /*
     * raw_id 를 받아 해당 음성파일 출력
     * */
    public void start(String soundId){
        int id = checkRawId(soundId);
        if(id != 0){
            start(id);
        }else{
            voicePlayerModuleSingleton.start(soundId);
        }
    }

    /*
     * String 음성 파일 이름이 raw에 존재하는지 Check 하는 함수
     * 있으면 해당 id값 , 없으면 0
     * */
    private int checkRawId(String soundId){
        String packName = context.getPackageName();
        Resources resources = context.getResources();
        return resources.getIdentifier(soundId,"raw",packName);
    }


    /*
     * 포커스 사운드를 출력하기 위한 함수
     * */
    public void focusSoundStart(){
        voicePlayerModuleSingleton.focusSoundStart();
    }

    /*
     * 터치 Type에 따른 음성
     * */
    public void start(FingerFunctionType fingerFunctionType){
        Queue<Integer> soundIdQueue = getFingerFunctionQueue(fingerFunctionType);
        stop();
        voicePlayerModuleSingleton.start(soundIdQueue);
    }

    /*
     * 제스처 해당하는 음성 파일들을 반환
     * */
    private Queue<Integer> getFingerFunctionQueue(FingerFunctionType fingerFunctionType){
        Queue<Integer> soundIdQueue = new LinkedList<>();
        switch (fingerFunctionType){
            case ENTER:
                soundIdQueue.add(R.raw.enter);
                break;
            case RIGHT:
                soundIdQueue.add(R.raw.right);
                break;
            case UP:
                soundIdQueue.add(R.raw.up);
                break;
            case DOWN:
                soundIdQueue.add(R.raw.down);
                break;
            case LEFT:
                soundIdQueue.add(R.raw.left);
                break;
            case BACK:
                soundIdQueue.add(R.raw.back);
                break;
            case NONE:
                soundIdQueue.add(R.raw.retry);
                break;
        }
        Log.d(DEBUG_TYPE,"VoicePlayerModuleManager - getFingerFunctionQueue  수행 됨");
        return soundIdQueue;
    }




    /*
     * 메뉴에 맞는 가이드 음성 정보를 출력하기 위한 함수
     * */
    public void start(MenuType menuType){
        switch (menuType){
            case MAIN:
                start(R.raw.main_tutorial);
                Log.d(DEBUG_TYPE,"VoicePlayerModuleManager - INITIAL  수행 됨");
                break;
            case EDUCATE:
                start(R.raw.edu_tutorial);
                Log.d(DEBUG_TYPE,"VoicePlayerModuleManager - EDUCATE  수행 됨");
                break;
            case QUIZ:
                start(R.raw.quiz_tutorial);
                Log.d(DEBUG_TYPE,"VoicePlayerModuleManager - QUIZ  수행 됨");
                break;
            case TRANSLATE:
                start(R.raw.tran_tutorial);
                Log.d(DEBUG_TYPE,"VoicePlayerModuleManager - TRANSLATE  수행 됨");
                break;
            case BLUETOOTH:
                start(R.raw.blue_info);
                Log.d(DEBUG_TYPE,"VoicePlayerModuleManager - BOARD  수행 됨");
                break;

        }
    }
    /*
     * 음성정보가 String인 file들을 출력하는 함수
     * 점자 학습 data들의 경우, 글자 정보는 raw에서 가져오며, 점자 번호 정보들은 점자 행렬을 가공하여 음성 file을 구성함
     * */


    /*
     * 유지할 음성파일을 제외하고, queue초기화 및 mediaPlayer초기화 함수
     * */
    public void stop(){
        voicePlayerModuleSingleton.initAll();
    }

    /**
     * 모든음성 중지 함수
     */
    public void allStop(){
        stop();
        voicePlayerModuleSingleton.initializeMediaPlayer();
    }

    public boolean getMediaPlaying() {
        return voicePlayerModuleSingleton.getMediaPlaying();
    }

    public boolean checkTTSPlaying() {
        return voicePlayerModuleSingleton.checkTTSPlay();
    }

    public boolean getMenuInfoPlaying() {
        return voicePlayerModuleSingleton.getMenuInfoPlaying();
    }

}