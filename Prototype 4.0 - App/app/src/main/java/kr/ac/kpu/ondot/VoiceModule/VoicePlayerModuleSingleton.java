package kr.ac.kpu.ondot.VoiceModule;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

import java.util.LinkedList;
import java.util.Queue;

import kr.ac.kpu.ondot.R;


public class VoicePlayerModuleSingleton implements TextToSpeechListener {
    private final String DEBUG_TYPE = "type111";

    private static final VoicePlayerModuleSingleton voicePlayerInstance = new VoicePlayerModuleSingleton();

    private TextToSpeechClient ttsClient;
    private Context context;

    private VoicePlayerStopListener voicePlayerStopListener;
    private Queue<Integer> queue = new LinkedList<>();
    private Queue<Integer> tmpQueue = new LinkedList<>(); // 임시 저장 큐
    private int maintainVoiceId; // 음성이 종료되지 않고 유지하기 위한 변수
    private boolean mediaPlaying = false;
    private boolean menuInfoPlaying = false; // 메뉴별 안내음성이 출력 되는 중인지 판별하는 변수
    private MediaPlayer mediaPlayer, focusMediaPlayer;


    private VoicePlayerModuleSingleton() {

    }

    private void setContext(Context context){
        this.context = context;
    }

    public static VoicePlayerModuleSingleton getInstance(Context context){
        voicePlayerInstance.setContext(context);

        TextToSpeechManager.getInstance().initializeLibrary(context);
        SpeechRecognizerManager.getInstance().initializeLibrary(context);

        return voicePlayerInstance;
    }

    /**
     * mediaplay 종료를 알리기 위한 VoicePlayerStopListenr를 등록하는 함수
     */
    public void setVoicePlayerStopListener(VoicePlayerStopListener voicePlayerStopListener){
        this.voicePlayerStopListener = voicePlayerStopListener;
    }
    /**
     * mediaplay종료를 알리기 위한 VoicePlayerStopListenr를 해제하는 함수
     * mediaplay stop을 알린 뒤 호출
     */
    public void initVoicePlayerStopListener(){
        this.voicePlayerStopListener = null;
    }
    /*
     * 유지하려던 음성이 재생중이 아니라면 음성 파일을 중지시키고 큐를 초기화
     * 재생 중이면 큐만 초기화
     * */
    public void initAll(){
        if(checkMediaMaintain() == false){
            initializeMediaPlayer();
        }
        initMaintain();
    }
    /**
     * focusmediaplayer 메모리 해제 함수
     */
    public void initializeFocusMediaPlayer(){
        if (focusMediaPlayer != null) {
            if (focusMediaPlayer.isPlaying()) {
                focusMediaPlayer.stop();
                focusMediaPlayer.release();
                focusMediaPlayer = null;
            } else {
                focusMediaPlayer.release();
                focusMediaPlayer = null;
            }
        }
    }
    /**
     * mediaplayer 메모리 해제 함수
     */
    public void initializeMediaPlayer(){
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } else {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        menuInfoPlaying = false;
        mediaPlaying = false;
    }

    /**
     * 유지시킬 음성파일 변수 초기화 함수
     */
    public void initMaintain(){
        maintainVoiceId = 0;
    }


    /**
     * 음성파일 queue초기화 함수
     */
    public void initializeQueue(){
        tmpQueue.clear();
        queue.clear();
        mediaPlaying = false;
    }

    /**
     * focus가 잡힌 소리를 출력하는 함수
     */
    public void focusSoundStart(){
        if(focusMediaPlayer == null) {
            //focusMediaPlayer = MediaPlayer.create(context, R.raw.focus_sound);
            focusMediaPlayer.start();
            focusMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    initializeFocusMediaPlayer();
                }
            });
        }
    }


    /*
     * 특정 음성이 중간에 멈추지 않게 하기 위한 함수
     * 다음 음성이 없고, 해당 제스처 음성출력 파일이 방어하기로 설정된 음성일경우  해당파일은 메모리를 당장 해제하지 않음
     * ???????????????
     * */
    private boolean checkMediaMaintain(){
        if(queue.peek() == null){
            int gestureId[] = {
                    R.raw.enter,
                    R.raw.back,
                    R.raw.up,
                    R.raw.down,
                    R.raw.left,
                    R.raw.right
            };
            for(int id : gestureId){
                if(maintainVoiceId == id){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkTTSPlay(){
        if(ttsClient != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 일반적인 음성 file 출력 함수
     * VoicePlayerModuleManager로 부터 Queue를 받음.
     */
    public void start(Queue<Integer> soundIdQueue){
        queue.addAll(soundIdQueue);
        checkMediaPlayer();
    }


    /*
     * tts 출력 함수
     * ttsText  출력후 큐에 저장되어 있는 음성 파일을 출력
     * */
    public void start(String ttsText){
        tmpQueue.clear();

        if(ttsClient != null && ttsClient.isPlaying()){
            ttsClient.stop();
        }

        String speechMode = TextToSpeechClient.NEWTONE_TALK_2;
        String voiceType = TextToSpeechClient.VOICE_MAN_READ_CALM;
        double speechSpeed = 1.0;

        ttsClient = new TextToSpeechClient.Builder()
                .setSpeechMode(speechMode)
                .setSpeechSpeed(speechSpeed)
                .setSpeechVoice(voiceType)
                .setListener(this)
                .build();

        Log.d(DEBUG_TYPE,"VoicePlayerModuleSingleton  - ttsText : " + ttsText);
        ttsClient.play(ttsText);
        Log.d(DEBUG_TYPE,"VoicePlayerModuleSingleton  수행 됨");

    }



    /**
     * 다수의 곳에서 접근할 때, 동기화하는 함수
     */
    private synchronized void checkMediaPlayer(){
        if(mediaPlayer != null){
            if(!mediaPlayer.isPlaying()){
                initializeMediaPlayer();
                startMediaPlayer();
            }
        } else
            startMediaPlayer();
    }
    /**
     * 메뉴 가이드 음성이 출력중인지 확인하는 함수
     * @param id : 현재 출력중인 음성 파일 id
     */
    private void checkMenuInfoMedia(int id){
        int menuInfo_Id[] = new int[]{
                R.raw.main_tutorial, R.raw.edu_tutorial,
                R.raw.quiz_tutorial, R.raw.tran_tutorial,
                R.raw.blue_info
        };

        for(int menuInfoId : menuInfo_Id){
            if(id == menuInfoId){
                menuInfoPlaying = true;
            }
        }
    }


    /*
     * 큐에 저장되어 있는 음성들을 순서대로 출력
     * */
    private void startMediaPlayer(){
        if(queue.peek() != null){
            mediaPlaying = true;
            int id = queue.poll();
            maintainVoiceId = id;

            checkMenuInfoMedia(id);
            mediaPlayer = MediaPlayer.create(context, id);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    initializeMediaPlayer();
                    initMaintain();
                    checkMediaPlayer();
                }
            });
        }else{
            mediaPlaying = false;
            if(getMediaPlaying() == false){
                if(voicePlayerStopListener != null) {
                    voicePlayerStopListener.voicePlayerStop();
                    initVoicePlayerStopListener();
                }
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getMaintainVoiceId() {
        return maintainVoiceId;
    }

    public boolean getMenuInfoPlaying() {
        return menuInfoPlaying;
    }
    public boolean getMediaPlaying() {
        if(queue.size() == 0 && mediaPlayer == null && ttsClient == null && mediaPlaying == false){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onFinished() {
        queue.clear();
        queue.addAll(tmpQueue);
        checkMediaPlayer();
        ttsClient = null;
    }

    @Override
    public void onError(int code, String message) {
        ttsClient = null;
    }
}