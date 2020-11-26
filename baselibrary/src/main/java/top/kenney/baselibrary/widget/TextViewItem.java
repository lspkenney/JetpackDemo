package top.kenney.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import top.kenney.baselibrary.R;


/**
 * Created by Kenney on 2017-12-14 15:11
 */

public class TextViewItem extends LinearLayout {
    private TextView mContentView;
    private TextView mTitleView;
    private View lineView;
    private ImageView mBtnImage;
    public TextViewItem(Context context) {
        this(context, null);
    }

    public TextViewItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewItem);

        String title = typedArray.getString(R.styleable.TextViewItem_title);
        float titleTextSize = typedArray.getDimension(R.styleable.TextViewItem_titleTextSize, 16);
        int titleColor = typedArray.getColor(R.styleable.TextViewItem_titleColor, getResources().getColor(R.color.textPrimary));

        String content = typedArray.getString(R.styleable.TextViewItem_content);
        String hint = typedArray.getString(R.styleable.TextViewItem_hint);
        float contentTextSize = typedArray.getDimension(R.styleable.TextViewItem_contentTextSize, 16);
        int contentColor = typedArray.getColor(R.styleable.TextViewItem_contentColor, getResources().getColor(R.color.textPrimary));
        int contentColorHint = typedArray.getColor(R.styleable.TextViewItem_contentColorHint, getResources().getColor(R.color.colorAccent));
        //boolean contentEnable = typedArray.getBoolean(R.styleable.TextViewItem_contentEnable, true);

        boolean showLine = typedArray.getBoolean(R.styleable.TextViewItem_showLine, true);
        boolean singleLine = typedArray.getBoolean(R.styleable.TextViewItem_singleLine, true);

        boolean showImg = typedArray.getBoolean(R.styleable.TextViewItem_showImg, false);
        int imageId = typedArray.getResourceId(R.styleable.TextViewItem_img,0);
        boolean required = typedArray.getBoolean(R.styleable.TextViewItem_required, false);



        LayoutInflater.from(context).inflate(R.layout.textview_item_layout,this);

        mTitleView = (TextView) findViewById(R.id.title);
        mContentView = (TextView) findViewById(R.id.content);
        lineView = findViewById(R.id.line);
        mBtnImage = (ImageView) findViewById(R.id.image);

        //标题
        if(TextUtils.isEmpty(title)){
            mTitleView.setText("");
        }else{
            if(!required){
                mTitleView.setText(title);
            }else{
                title = "*" + title;
                SpannableString spannableString=new SpannableString(title);
                spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                mTitleView.setText(spannableString);
            }
        }
        //mTitleView.setTextSize(DensityUtil.sp2px(context,titleTextSize));
        mTitleView.setTextSize(titleTextSize);
        mTitleView.setTextColor(titleColor);

        //内容
        mContentView.setText(content == null ? "": content);
        mContentView.setHint(hint == null ? "" : hint);
        //mContentView.setTextSize(DensityUtil.sp2px(context,contentTextSize));
        mContentView.setTextSize(contentTextSize);
        mContentView.setTextColor(contentColor);
        mContentView.setHintTextColor(contentColorHint);
        //mContentView.setEnabled(contentEnable);
        mContentView.setSingleLine(singleLine);

        if(showImg){
            mBtnImage.setVisibility(View.VISIBLE);
            mBtnImage.setImageResource(imageId);
        }else{
            mBtnImage.setVisibility(View.GONE);
        }


        if(showLine){
            lineView.setVisibility(View.VISIBLE);
        }else{
            lineView.setVisibility(View.GONE);
        }

        typedArray.recycle();
    }

    public void setDisable(){
        mContentView.setEnabled(false);
    }

    public void setItemTitle(String text){
        mTitleView.setText(text);
    }
    public void setItemTitle(String text, boolean required){
        if(TextUtils.isEmpty(text)){
            mTitleView.setText("");
        }else{
            if(!required){
                mTitleView.setText(text);
                return ;
            }
            text = "*" + text;
            SpannableString spannableString=new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mTitleView.setText(spannableString);
        }
    }
    public void setItemContentHint(String text){
        mContentView.setHint(text);
    }

    public void setItemContent(String text){
        mContentView.setText(text);
    }
    public void setItemContent(Spanned text){
        mContentView.setText(text);
    }

    public String getItemContent(){
        return mContentView.getText().toString().trim();
    }
    public ImageView getImageView(){
        return mBtnImage;
    }

}
