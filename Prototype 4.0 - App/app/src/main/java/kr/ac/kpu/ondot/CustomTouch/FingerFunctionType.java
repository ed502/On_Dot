package kr.ac.kpu.ondot.CustomTouch;

public enum FingerFunctionType {
    NONE(-1),
    RIGHT(0),
    LEFT(1),
    ENTER(2),
    BACK(3),
    LONG(4),
    UP(5),
    DOWN(6),
    SPECIAL(7),
    ONE_FINGER(1),
    TWO_FINGER(2);

    int number;

    FingerFunctionType(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }

}
