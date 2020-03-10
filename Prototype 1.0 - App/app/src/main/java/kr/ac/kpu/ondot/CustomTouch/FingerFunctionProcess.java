package kr.ac.kpu.ondot.CustomTouch;

import kr.ac.kpu.ondot.Screen;

public class FingerFunctionProcess {
    private final String DEBUG_TYPE = "type";

    public FingerFunctionProcess(){

    }

    public FingerFunctionType getFingerFunctionType(FingerLocation fingerLocation){
        FingerFunctionType type = FingerFunctionType.NONE;

        int fingerCount = fingerLocation.getFingerCount();

        /*if(fingerCount == FingerFunctionType.ONE_FINGER.getNumber()){

        }else if(fingerCount == FingerFunctionType.TWO_FINGER.getNumber()){

        }*/

        double fingerGapX[] = new double[fingerCount];
        double fingerGapY[] = new double[fingerCount];

        int dragCountX = 0; // 좌우 판단
        int dragCountY = 0; // 상하 판단

        double dragSpace = Screen.displayY * (0.1);

        int downX[] = fingerLocation.getDownX();
        int downY[] = fingerLocation.getDownY();
        int upX[] = fingerLocation.getUpX();
        int upY[] = fingerLocation.getUpY();


        for(int i=0; i<fingerCount; i++){
            fingerGapX[i] = downX[i] - upX[i]; // X 좌표 격차
            fingerGapY[i] = downY[i] - upY[i]; // Y 좌표 격차

            if (fingerGapX[i] > dragSpace)
                dragCountX++;
            else if (fingerGapX[i] < dragSpace * (-1))
                dragCountX--;

            if (fingerGapY[i] > dragSpace)
                dragCountY++;
            else if (fingerGapY[i] < dragSpace)
                dragCountY--;
        }


        boolean DragX = false;
        boolean DragY = false;

        if (dragCountX == fingerCount || dragCountX == fingerCount*(-1))
            DragX = true;
        else if (dragCountY == fingerCount || dragCountY == fingerCount*(-1))
            DragY = true;

        // 손가락 수에 맞춰서
        if(fingerCount == FingerFunctionType.ONE_FINGER.getNumber()){
            if(DragX == false && DragY == false)
                type = FingerFunctionType.NONE;
            else if(DragX == true && DragY == false){
                if(dragCountX > 0) // 오른쪽
                    type = FingerFunctionType.RIGHT;
                else
                    type = FingerFunctionType.LEFT;

            }else if(DragX == false && DragY == true){
                if(dragCountY > 0)
                    type = FingerFunctionType.UP;
                else
                    type = FingerFunctionType.DOWN;
            }else if(DragX == true && DragY == true){
                double gapX = 0;
                double gapY = 0;

                for(int i=0; i<fingerGapX.length; i++){
                    gapX = gapX + fingerGapX[i];
                    gapY = gapY + fingerGapY[i];
                }


                if(gapX > gapY){
                    if(dragCountX > 0)
                        type = FingerFunctionType.RIGHT;
                    else
                        type = FingerFunctionType.LEFT;
                }else if(gapY > gapX){
                    if(dragCountY > 0)
                        type = FingerFunctionType.UP;
                    else
                        type = FingerFunctionType.DOWN;
                }else
                    type = FingerFunctionType.NONE;
            }
        }else if(fingerCount == FingerFunctionType.TWO_FINGER.getNumber()){
            if(DragX == false && DragY == true){
                if(dragCountY > 0)
                    type = FingerFunctionType.SPECIAL; // 아래서 위로 드래그
                else
                    type = FingerFunctionType.BACK;  // 위에서 아래로 드래그
            }else if(DragX == true && DragY == true){ // 드래그 이동 거리로 판단
                double gapX = 0;
                double gapY = 0;

                for(int i=0; i<fingerGapX.length; i++){
                    gapX = gapX + fingerGapX[i];
                    gapY = gapY + fingerGapY[i];
                }


                if(gapY > gapX){ // y 축 이동거리가 큰 경우
                    if(dragCountY > 0)
                        type = FingerFunctionType.SPECIAL;
                    else
                        type = FingerFunctionType.BACK;
                }else
                    type = FingerFunctionType.NONE;
            }
        }

        return type;
    }
}
