package kr.ac.kpu.ondot.CustomTouch;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

public class CustomTouchEvent implements CustomTouchConnectListener {
    private final String DEBUG_TYPE = "type";

    protected int ONE_FINGER = FingerFunctionType.ONE_FINGER.getNumber(); // 손가락 1개
    protected int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber(); // 손가락 2개

    protected FingerLocation fingerLocation;
    protected FingerFunctionProcess fingerFunctionProcess;


    protected boolean multiFinger = false;
    protected CustomTouchEventListener customTouchEventListener;

    private Context context;
    private boolean maxFingerExceed = false;

    private int touchCount = 0;
    private TimerTask doubleTimerTask, longTimerTask;
    private Timer doubleTimer, longTimer;
    private boolean longTouchCheck = false;



    public CustomTouchEvent(CustomTouchEventListener customTouchEventListener, Context context) {
        this.customTouchEventListener = customTouchEventListener;
        this.context = context;
        fingerLocation = new FingerLocation(TWO_FINGER);
        fingerFunctionProcess = new FingerFunctionProcess();

    }

    @Override
    public void touchEvent(MotionEvent event) {
        if(maxFingerExceed == true){
            checkMaxFingerExceedInit(event);
        }

        if(maxFingerExceed == false){
            int fingerCount = event.getPointerCount();
            Log.d(DEBUG_TYPE,"CustomTouchEvent - fingerCount : " + String.valueOf(fingerCount));
            if(fingerCount <= TWO_FINGER){
                if(fingerCount == ONE_FINGER){
                    // 한손가락
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_DOWN:
                            oneFingerDown(event,fingerCount);
                            break;
                        case MotionEvent.ACTION_UP:
                            oneFingerUp(event,fingerCount);
                            break;
                    }
                }else {
                    // 두손가락
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_POINTER_DOWN:
                            twoFingerDown(event,fingerCount);
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            twoFingerUp(event,fingerCount);
                            break;
                    }
                }
            }else{
                maxFingerExceed = true;
                // 3 손가락 이상
            }
        }
    }

    private void checkMaxFingerExceedInit(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_HOVER_ENTER:
                maxFingerExceed = false;
            case MotionEvent.ACTION_DOWN:
                maxFingerExceed = false;
                break;
        }
    }


    // 한 손가락 DOWN 이 인식 되었을 경우
    // long 터치 쓰레드 시작
    // 더블탭 쓰레드는 정지한다.

    private void oneFingerDown(MotionEvent event, int fingerCount){
        multiFinger = false;
        //doubleTabCheckThreadStart();

        touchCount++;

        fingerLocation.setDownLocation(event, fingerCount);

        longTouchThreadStart();

        doubleTabCheckThreadStop();

    }

    // 한손가락 UP 이 인식되는 경우
    // long 터치 쓰레드를 멈춘다.
    // 더블 탭 쓰레드를 시작한다.

    private void oneFingerUp(MotionEvent event, int fingerCount){

        if(multiFinger == false){
            fingerLocation.setUpLocation(event, fingerCount);

            touchCount++;

            /*if(Math.abs(downX - moveX) >20 && Math.abs(downY - moveY) > 20){
                Log.d(DEBUG_TYPE,"CustomTouchEvent - Math.abs(downX - moveX)  : " + String.valueOf(Math.abs(downX - moveX)));

                Log.d(DEBUG_TYPE,"CustomTouchEvent - Math.abs(downY - moveY)  : " + String.valueOf(Math.abs(downY - moveY)));

                type = FingerFunctionType.LONG;
                Log.d(DEBUG_TYPE,"Long");
                customTouchEventListener.onOneFingerFunction(type);
            }else{

            }*/

            longTouchThreadStop();

            doubleTabCheckThreadStart();


            //Log.d(DEBUG_TYPE,"CustomTouchEvent - touchCount : " + String.valueOf(touchCount));

            /*if(4 <= touchCount){
                //FingerFunctionType type;
                type = FingerFunctionType.ENTER;
                doubleTabCheckThreadStop();

                Log.d(DEBUG_TYPE,"ENTER");
                // customTouchEventListener.onOneFingerFunction(type);
                customTouchEventListener.onOneFingerFunction(type);

            }else if(touchCount < 4 && !doubleTapCheck){

                type = oneFinger.getOneFingerFunctionType(oneFingerCoordinate);

                if (type == FingerFunctionType.LEFT){
                    Log.d(DEBUG_TYPE,"LEFT");
                    //customTouchEventListener.onOneFingerFunction(type);
                }else if(type == FingerFunctionType.RIGHT){
                    Log.d(DEBUG_TYPE,"RIGHT");
                    //customTouchEventListener.onOneFingerFunction(type);
                }else if(type == FingerFunctionType.UP){
                    Log.d(DEBUG_TYPE,"UP");
                    // customTouchEventListener.onOneFingerFunction(type);
                }else if(type == FingerFunctionType.DOWN){
                    Log.d(DEBUG_TYPE,"DOWN");
                    // customTouchEventListener.onOneFingerFunction(type);
                }else if(type == FingerFunctionType.NONE){
                    Log.d(DEBUG_TYPE,"NONE");
                    // customTouchEventListener.onOneFingerFunction(type);
                }
                customTouchEventListener.onOneFingerFunction(type);
            }*/


        }


    }


    // 두손가락 DOWN 인식하는 경우
    private void twoFingerDown(MotionEvent event, int fingerCount){
        multiFinger = true;

        // doubleTabCheckThreadStop();

        fingerLocation.setDownLocation(event,fingerCount);
    }

    // 두 손가락 UP 인식하는경우
    // multiFinger 를 true 일때 처리한다.
    private void twoFingerUp(MotionEvent event, int fingerCount){
        if(multiFinger == true){
            fingerLocation.setUpLocation(event,fingerCount);

            FingerFunctionType type;

            type = fingerFunctionProcess.getFingerFunctionType(fingerLocation);

            if(type == FingerFunctionType.BACK){
                Log.d(DEBUG_TYPE,"BACK 위에서 아래로");
                customTouchEventListener.onTwoFingerFunction(type);
            }else if (type == FingerFunctionType.SPECIAL){
                Log.d(DEBUG_TYPE,"SPECIAL 아래서 위로");
                customTouchEventListener.onTwoFingerFunction(type);
            }
        }

    }



    // 더블 탭 체크하는 메소드
    // 0.2초가 지난 상태에서 추가적인 드래그 DOWN -> UP 이 인식되지 않은 경우 즉, touchCount 가 4 보다 작은 경우 일반적인 드래그로 판정
    // 추가적인 DOWN -> UP 이 인식되고 touchCount 가 4 이상인 경우 더블탭으로 판정하여 처리
    // 첫번째 DOWN 에서 LONG TOUCH 가 판정이 되는겨우 해당 쓰레드에서 더블탭과 일반적인 드래그를 인식하지 않고 Long 터치로 처리
    private synchronized void doubleTabCheckThreadStart(){
        if(doubleTimerTask == null) {
            doubleTimerTask = new TimerTask() {
                @Override
                public void run() {
                    FingerFunctionType type = FingerFunctionType.NONE;

                    if(longTouchCheck == true){
                        type = FingerFunctionType.LONG;
                        Log.d(DEBUG_TYPE,"Long");
                        customTouchEventListener.onOneFingerFunction(type);
                    }else{
                        if(4 <= touchCount){

                            type = FingerFunctionType.ENTER;

                            Log.d(DEBUG_TYPE,"ENTER");
                            customTouchEventListener.onOneFingerFunction(type);
                        }else{

                            type = fingerFunctionProcess.getFingerFunctionType(fingerLocation);
                            customTouchEventListener.onOneFingerFunction(type);
                        }
                        Log.d(DEBUG_TYPE,"CustomTouchEvent - touchCount : " + String.valueOf(touchCount));

                        if (type == FingerFunctionType.LEFT){
                            Log.d(DEBUG_TYPE,"LEFT");
                            //customTouchEventListener.onOneFingerFunction(type);
                        }else if(type == FingerFunctionType.RIGHT){
                            Log.d(DEBUG_TYPE,"RIGHT");
                            //customTouchEventListener.onOneFingerFunction(type);
                        }else if(type == FingerFunctionType.UP){
                            Log.d(DEBUG_TYPE,"UP");
                            // customTouchEventListener.onOneFingerFunction(type);
                        }else if(type == FingerFunctionType.DOWN){
                            Log.d(DEBUG_TYPE,"DOWN");
                            // customTouchEventListener.onOneFingerFunction(type);
                        }else if(type == FingerFunctionType.NONE){
                            Log.d(DEBUG_TYPE,"NONE");
                            // customTouchEventListener.onOneFingerFunction(type);
                        }
                    }



                    doubleTabCheckThreadStop();
                    longTouchCheck = false;
                    touchCount = 0;
                }
            };
            doubleTimer = new Timer();
            doubleTimer.schedule(doubleTimerTask, 200);
        }

    }

    private void doubleTabCheckThreadStop(){
        // touchCount = 0;

        if(doubleTimerTask != null){
            doubleTimerTask.cancel();
            doubleTimerTask = null;
        }

        if(doubleTimer != null){
            doubleTimer.cancel();
            doubleTimer = null;
        }
    }


    // down 상태가 0.5초 이상 지속되면 long 터치로 판단
    // 0.5초 이내 UP 이 발생되면 long 터치가 아닌 일반 드래그로 인식
    // UP 인식시 longTouchCheck 으로 판단이 가능해진다.
    private synchronized void longTouchThreadStart(){
        if(longTimerTask == null){
            longTimerTask = new TimerTask() {
                @Override
                public void run() {
                    longTouchCheck = true;
                }
            };

            longTimer = new Timer();
            longTimer.schedule(longTimerTask,500);
        }
    }

    private void longTouchThreadStop(){

        if(longTimerTask != null){
            longTimerTask.cancel();
            longTimerTask = null;
        }

        if(longTimer != null){
            longTimer.cancel();
            longTimer = null;
        }
    }




}
