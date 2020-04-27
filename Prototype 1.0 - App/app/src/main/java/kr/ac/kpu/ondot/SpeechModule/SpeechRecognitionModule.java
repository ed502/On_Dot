package kr.ac.kpu.ondot.SpeechModule;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import java.util.ArrayList;

public class SpeechRecognitionModule implements SpeechRecognizeListener {
    private SpeechRecognizerClient client;
    private SpeechRecognitionListener listener;

    private boolean stop = false;

    public SpeechRecognitionModule(Context context, SpeechRecognitionListener listener){
        // SDK 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(context);
        this.listener = listener;
    }

    // 음성인식 시작
    private void startSpeechRecognition() {
        // 클라이언트 생성
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);

        client = builder.build();
        client.setSpeechRecognizeListener(this);

    }

    // 음성인식 중지
    public void stop(){
        stop = true;
        if(client != null){
            client.stopRecording();
        }
    }

    // 해당 액티비티가 PAUSE 상태일 때 음성인식 중단
    public void pause() {
        stop();
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        client = null;
        Log.e("SpeechRecognitionModule", "onError "+ errorCode + " : "+ errorMsg);
        if(stop == false){
            listener.speechRecogntionResult(null);
        }else{
            stop = false;
        }
    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    // 음성이식 결과 받는 함수
    @Override
    public void onResults(Bundle results) {
        client = null;
        if(stop == false){
            ArrayList<String> stt = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
            if(stt.size() == 0){
                listener.speechRecogntionResult(null);
            }else{
                listener.speechRecogntionResult(stt);
            }
        }else{
            stop = false;
        }

    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }
}
