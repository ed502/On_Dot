package kr.ac.kpu.ondot.SpeechModule;

import java.util.ArrayList;

public interface SpeechRecognitionListener {
    void speechRecogntionResult(ArrayList<String> text);
}
