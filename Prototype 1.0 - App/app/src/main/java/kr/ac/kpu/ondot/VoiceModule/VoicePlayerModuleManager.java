package kr.ac.kpu.ondot.VoiceModule;

import android.content.Context;
import android.util.Log;

import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;

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

    public void start(String ttsText){
        voicePlayerModuleSingleton.start(ttsText);
    }

    public void start(FingerFunctionType fingerFunctionType){
        FingerFunctionType type = fingerFunctionType;


        if(type == FingerFunctionType.ENTER){
            String text = "테스트입니다. 제발 되어라아아아앙";
            voicePlayerModuleSingleton.start(text);
            Log.d(DEBUG_TYPE,"VoicePlayerModuleManager  수행 됨");
        }
    }
}
