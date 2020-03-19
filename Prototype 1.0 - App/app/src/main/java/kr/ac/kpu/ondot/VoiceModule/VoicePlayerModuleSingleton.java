package kr.ac.kpu.ondot.VoiceModule;

import android.content.Context;
import android.util.Log;

import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;


public class VoicePlayerModuleSingleton implements TextToSpeechListener {
    private final String DEBUG_TYPE = "type111";

    private static final VoicePlayerModuleSingleton voicePlayerInstance = new VoicePlayerModuleSingleton();

    private TextToSpeechClient ttsClient;
    private Context context;

    private VoicePlayerStopListener voicePlayerStopListener;

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
     * 음성 TTS 종료를 알리기 위한 VoicePlayerStopListenr를 등록하는 함수
     */
    public void setVoicePlayerStopListener(VoicePlayerStopListener voicePlayerStopListener){
        this.voicePlayerStopListener = voicePlayerStopListener;
    }
    /**
     * 음성 TTS 종료를 알리기 위한 VoicePlayerStopListenr를 해제하는 함수
     * 음성 TTS stop을 알린 뒤 호출
     */
    public void initVoicePlayerStopListener(){
        this.voicePlayerStopListener = null;
    }


    // tts 출력 함수
    public void start(String ttsText){
        /*if(ttsClient != null && ttsClient.isPlaying()){
            ttsClient.stop();
        }*/

        String speechMode = TextToSpeechClient.NEWTONE_TALK_1;
        String voiceType = TextToSpeechClient.VOICE_MAN_READ_CALM;
        double speechSpeed = 1.0D;
        int sampleRate = 16000;

        ttsClient = new TextToSpeechClient.Builder()
                .setSpeechMode(speechMode)
                .setSpeechSpeed(speechSpeed)
                .setSpeechVoice(voiceType)
                .setListener(this)
                .setSampleRate(sampleRate)
                .build();

        ttsClient.play(ttsText);
        Log.d(DEBUG_TYPE,"VoicePlayerModuleSingleton  수행 됨");

    }

    @Override
    public void onFinished() {
        ttsClient = null;
    }

    @Override
    public void onError(int code, String message) {
        ttsClient = null;
    }
}
