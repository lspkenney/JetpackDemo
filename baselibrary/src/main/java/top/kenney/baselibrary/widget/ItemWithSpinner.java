package top.kenney.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import top.kenney.baselibrary.R;


/**
 * Created by Kenney on 2017-12-15 14:44
 */

public class ItemWithSpinner extends LinearLayout {
    private Spinner mSpinner;
    private TextView titleView;

    public ItemWithSpinner(Context context) {
        this(context, null);
    }

    public ItemWithSpinner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemWithSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemWithSpinner);

        String title = typedArray.getString(R.styleable.ItemWithSpinner_title);
        float titleTextSize = typedArray.getDimension(R.styleable.ItemWithSpinner_titleTextSize, 16);
        int titleColor = typedArray.getColor(R.styleable.ItemWithSpinner_titleColor, getResources().getColor(R.color.textPrimary));
        int background = typedArray.getColor(R.styleable.ItemWithSpinner_bgColor, getResources().getColor(android.R.color.transparent));

        View view = LayoutInflater.from(context).inflate(R.layout.item_with_spinner, this);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        titleView= (TextView) findViewById(R.id.title);

        titleView.setTextSize(titleTextSize);
        titleView.setTextColor(titleColor);
        titleView.setText(title == null ? "" : title);
        view.setBackgroundColor(background);
        typedArray.recycle();
    }

    public Spinner getSpinner() {
        return mSpinner;
    }

    public TextView getTitleView() {
        return titleView;
    }
}
