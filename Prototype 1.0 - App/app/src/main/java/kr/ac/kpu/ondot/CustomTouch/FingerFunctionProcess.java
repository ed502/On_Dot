package kr.ac.kpu.ondot.CustomTouch;

import android.content.Context;
import android.util.Log;

import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class FingerFunctionProcess {
    private final String DEBUG_TYPE = "type";

    public FingerFunctionProcess() {
    }

    public FingerFunctionType getFingerFunctionType(FingerLocation fingerLocation) {
        FingerFunctionType type = FingerFunctionType.NONE;

        int fingerCount = fingerLocation.getFingerCount();

        double finger_gapX[] = new double[fingerCount];
        double finger_gapY[] = new double[fingerCount];

        int drag_countX = 0; // 좌우 판단
        int drag_countY = 0; // 상하 판단

        double dragSpace = Screen.displayY * (0.1);
        double screenX = Screen.displayX;
        double screenY = Screen.displayY;
        double rateX[] = new double[fingerCount], rateY[] = new double[fingerCount];

        int downX[] = fingerLocation.getDownX();
        int downY[] = fingerLocation.getDownY();
        int upX[] = fingerLocation.getUpX();
        int upY[] = fingerLocation.getUpY();


        for (int i = 0; i < fingerCount; i++) {
            finger_gapX[i] = downX[i] - upX[i]; // X 좌표 격차
            finger_gapY[i] = downY[i] - upY[i]; // Y 좌표 격차

            rateX[i] = Math.abs(finger_gapX[i] / Screen.displayX * 100);
            rateY[i] = Math.abs(finger_gapY[i] / Screen.displayY * 100);

            if (finger_gapX[i] > dragSpace && rateX[i] > rateY[i] && rateX[i] > 30)
                drag_countX++;
            else if (finger_gapX[i] < dragSpace * (-1) && rateX[i] > rateY[i] && rateX[i] > 30)
                drag_countX--;

            if (finger_gapY[i] < dragSpace * (-1) && rateX[i] < rateY[i] && rateY[i] > 30)
                drag_countY++;
            else if (finger_gapY[i] > dragSpace && rateX[i] < rateY[i] && rateY[i] > 30)
                drag_countY--;
        }


        boolean DragX = false;
        boolean DragY = false;

        if (drag_countX == fingerCount || drag_countX == fingerCount * (-1))
            DragX = true;
        else if (drag_countY == fingerCount || drag_countY == fingerCount * (-1))
            DragY = true;

        Log.i("asdfasdf DragX", "" + DragX);
        Log.i("asdfasdf DragY", "" + DragY);
        // 손가락 수에 맞춰서
        if (fingerCount == FingerFunctionType.ONE_FINGER.getNumber()) {
            if (DragX == false && DragY == false)
                type = FingerFunctionType.NONE;
            else if (DragX == true && DragY == false) {
                if (drag_countX > 0) // 오른쪽
                    type = FingerFunctionType.RIGHT;
                else
                    type = FingerFunctionType.LEFT;

            } else if (DragX == false && DragY == true) {
                if (drag_countY > 0)
                    type = FingerFunctionType.DOWN;
                else
                    type = FingerFunctionType.UP;
            } else if (DragX == true && DragY == true) {
                double gapX = 0;
                double gapY = 0;

                for (int i = 0; i < finger_gapX.length; i++) {
                    gapX = gapX + finger_gapX[i];
                    gapY = gapY + finger_gapY[i];
                }


                if (gapX > gapY) {
                    if (drag_countX > 0)
                        type = FingerFunctionType.RIGHT;
                    else
                        type = FingerFunctionType.LEFT;
                } else if (gapY > gapX) {
                    if (drag_countY > 0)
                        type = FingerFunctionType.DOWN;
                    else
                        type = FingerFunctionType.UP;
                } else
                    type = FingerFunctionType.NONE;
            }

        } else if (fingerCount == FingerFunctionType.TWO_FINGER.getNumber()) {
            if (DragX == false && DragY == true) {
                if (drag_countY > 0)
                    type = FingerFunctionType.BACK; // 아래서 위로 드래그
                else
                    type = FingerFunctionType.SPECIAL;  // 위에서 아래로 드래그
            } else if (DragX == true && DragY == true) { // 드래그 이동 거리로 판단
                double gapX = 0;
                double gapY = 0;

                for (int i = 0; i < finger_gapX.length; i++) {
                    gapX = gapX + finger_gapX[i];
                    gapY = gapY + finger_gapY[i];
                }


                if (gapY > gapX) { // y 축 이동거리가 큰 경우
                    if (drag_countY > 0)
                        type = FingerFunctionType.BACK;
                    else
                        type = FingerFunctionType.SPECIAL;
                } else
                    type = FingerFunctionType.NONE;
            }
        }
        return type;
    }
}