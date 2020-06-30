package kr.ac.kpu.ondot.CustomTouch;

import android.util.Log;
import android.view.MotionEvent;

public class FingerLocation {
    private final String DEBUG_TYPE = "type";

    private int downX[];
    private int downY[];
    private int upX[];
    private int upY[];

    private int fingerCount;

    public FingerLocation(int fingerCount){
        downX = new int[fingerCount];
        downY = new int[fingerCount];
        upX = new int[fingerCount];
        upY = new int[fingerCount];
    }

    /*public void setHoverDownCoordinate(MotionEvent event, int fingerCount){
        this.fingerCount = fingerCount;

        downInitialize();

        for(int i = 0; i<fingerCount; i++){
            downX[i] = (int) event.getX();
            downY[i] = (int) event.getY();
        }


        setHoverUpCoordinate(event, fingerCount);
    }
    public void setHoverUpCoordinate(MotionEvent event, int fingerCount){
        this.fingerCount = fingerCount;

        upInitialize();

        for(int i = 0; i<fingerCount; i++){
            upX[i] = (int) event.getX();
            upY[i] = (int) event.getY();
        }

    }*/

    public void setDownCoordinate(MotionEvent event, int fingerCount){
        this.fingerCount = fingerCount;
        Log.d(DEBUG_TYPE,"OneFingerCoordinate - down : " + String.valueOf(fingerCount));
        downInitialize();

        for(int i=0; i<fingerCount; i++){
            downX[i] = (int) event.getX(i);
            Log.d(DEBUG_TYPE,"OneFingerCoordinate - downX : " + String.valueOf(downX[i]));
            downY[i] = (int) event.getY(i);
            Log.d(DEBUG_TYPE,"OneFingerCoordinate - downY : " + String.valueOf(downY[i]));
        }

    }
    public void setUpCoordinate(MotionEvent event, int fingerCount){
        this.fingerCount = fingerCount;

        upInitialize();

        for(int i=0; i<fingerCount; i++){
            upX[i] = (int) event.getX(i);
            Log.d(DEBUG_TYPE,"OneFingerCoordinate - upX : " + String.valueOf(upX[i]));
            upY[i] = (int) event.getY(i);
            Log.d(DEBUG_TYPE,"OneFingerCoordinate - upY : " + String.valueOf(upY[i]));
        }

    }

    /**
     * down 좌표 초기화 함수
     */
    private void downInitialize(){

        for(int i=0; i<downX.length; i++){
            downX[i] = 0;
            downY[i] = 0;
        }


    }


    /**
     * up좌표 초기화 함수
     */
    private void upInitialize(){
        for(int i=0;i<upX.length;i++){
            upX[i] = 0;
            upY[i] = 0;
        }


    }


    /**
     * 좌표 초기화 함수
     */
    public void initialize(){
        for(int i=0;i<downX.length;i++){
            downX[i] = 0;
            downY[i] = 0;
            upX[i] = 0;
            upY[i] = 0;
        }

        fingerCount = 0;
    }
    public int getFingerCount(){
        return fingerCount;
    }

    public int[] getDownX() {
        return downX;
    }

    public int[] getDownY() {
        return downY;
    }

    public int[] getUpX() {
        return upX;
    }

    public int[] getUpY() {
        return upY;
    }
}
