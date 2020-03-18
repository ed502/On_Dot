package kr.ac.kpu.ondot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CircleIndicator extends LinearLayout {
    private Context mContext;
    private ImageView[] imageDot;
    private int itemMargin = 5;
    private int mDefaultCircle = 0;
    private int mSelectCircle = 0;

    public CircleIndicator(Context context){
        super(context);
        mContext = context;
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context, (AttributeSet) attrs);
        mContext = context;
    }

    public void setItemMargin(int itemMargin){this.itemMargin = itemMargin;}

    public void createDotPanel(int count, int defaultCircle, int selectCircle) {
        mDefaultCircle = defaultCircle;
        mSelectCircle = selectCircle;
        imageDot = new ImageView[count];
        this.removeAllViews();

        for (int i = 0; i < count; i++) {
            imageDot[i] = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = itemMargin;
            params.bottomMargin = itemMargin;
            params.leftMargin = itemMargin;
            params.rightMargin = itemMargin;
            params.gravity = Gravity.CENTER;

            imageDot[i].setLayoutParams(params);
            imageDot[i].setImageResource(defaultCircle);
            //imageDot[i].setTag(imageDot[i].getId(), false);
            this.addView(imageDot[i]);
        }

        selectDot(0);//첫 인덱스 선택
    }

    public void selectDot(int position) {
        for (int i = 0; i < imageDot.length; i++) {
            if (i == position) {
                imageDot[i].setImageResource(mSelectCircle);
            } else {
                imageDot[i].setImageResource(mDefaultCircle);
            }
        }
    }
}