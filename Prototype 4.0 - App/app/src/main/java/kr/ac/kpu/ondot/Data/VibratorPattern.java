package kr.ac.kpu.ondot.Data;

public class VibratorPattern {
    private long[] vibrateErrorPattern = {50, 100, 50, 100};
    private long[] vibrateNormalPattern = {50, 100};
    private long[] vibrateEnterPattern = {50, 300};
    private long[] vibrateSpecialPattern = {10, 100, 10, 100};
    private long[] vibrateShakePattern = {50, 100, 10, 200, 10, 100};

    public long[] getVibrateEnterPattern() {
        return vibrateEnterPattern;
    }

    public long[] getVibrateErrorPattern() {
        return vibrateErrorPattern;
    }

    public long[] getVibrateNormalPattern() {
        return vibrateNormalPattern;
    }

    public long[] getVibrateSpecialPattern() {
        return vibrateSpecialPattern;
    }

    public long[] getVibrateShakePattern() {
        return vibrateShakePattern;
    }
}
