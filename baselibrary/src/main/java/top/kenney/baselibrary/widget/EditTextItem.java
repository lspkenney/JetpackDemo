package top.kenney.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import top.kenney.baselibrary.R;
import top.kenney.baselibrary.utils.KeyBoardUtils;


/**
 * Created by Kenney on 2017-12-14 15:11
 */

public class EditTextItem extends LinearLayout {
    private EditText mContentView;
    private TextView mTitleView;
    private ImageView mBtnImage;
    private CheckBox mCheckBox;
    private View lineView;
    private Button btn;
    public EditTextItem(Context context) {
        this(context, null);
    }

    public EditTextItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextItem);

        String title = typedArray.getString(R.styleable.EditTextItem_title);
        float titleTextSize = typedArray.getDimension(R.styleable.EditTextItem_titleTextSize, 16);
        int titleColor = typedArray.getColor(R.styleable.EditTextItem_titleColor, getResources().getColor(R.color.textPrimary));

        String content = typedArray.getString(R.styleable.EditTextItem_content);
        String imeOptions = typedArray.getString(R.styleable.EditTextItem_imeOptions);
        String hint = typedArray.getString(R.styleable.EditTextItem_hint);
        float contentTextSize = typedArray.getDimension(R.styleable.EditTextItem_contentTextSize, 16);
        int contentColor = typedArray.getColor(R.styleable.EditTextItem_contentColor, getResources().getColor(R.color.textPrimary));
        int contentColorHint = typedArray.getColor(R.styleable.EditTextItem_contentColorHint, getResources().getColor(R.color.colorAccent));
        int inputType = typedArray.getInt(R.styleable.EditTextItem_inputType, InputType.TYPE_CLASS_TEXT);
        boolean contentEnable = typedArray.getBoolean(R.styleable.EditTextItem_contentEnable, true);

        boolean showLine = typedArray.getBoolean(R.styleable.EditTextItem_showLine, true);

        boolean showCb = typedArray.getBoolean(R.styleable.EditTextItem_showCb, false);
        boolean showImg = typedArray.getBoolean(R.styleable.EditTextItem_showImg, false);
        int imageId = typedArray.getResourceId(R.styleable.EditTextItem_img,0);
        String cbText = typedArray.getString(R.styleable.EditTextItem_cbText);

        boolean showBtn = typedArray.getBoolean(R.styleable.EditTextItem_showBtn, false);
        boolean isSingleLine = typedArray.getBoolean(R.styleable.EditTextItem_isSingleLine, true);
        String btnText = typedArray.getString(R.styleable.EditTextItem_btnText);
        boolean required = typedArray.getBoolean(R.styleable.EditTextItem_required, false);

        LayoutInflater.from(context).inflate(R.layout.edittextitem_layout,this);

        mTitleView = (TextView) findViewById(R.id.title);
        mContentView = (EditText) findViewById(R.id.content);
        mBtnImage = (ImageView) findViewById(R.id.image);
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
        lineView = findViewById(R.id.line);
        btn = (Button) findViewById(R.id.btn);

        //标题
        //mTitleView.setText(title == null ? "" : Html.fromHtml(title));
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
        mContentView.setEnabled(contentEnable);

        if(null != imeOptions){
            if("flagNoFullscreen".equals(imeOptions)){
                mContentView.setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN);
            }else{
                mContentView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            }
        } else{
            mContentView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }

        int finalInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
        if(InputType.TYPE_CLASS_NUMBER == inputType){
            finalInputType = InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_NORMAL;
        }else if(InputType.TYPE_TEXT_VARIATION_PASSWORD == inputType){
            finalInputType = InputType.TYPE_TEXT_VARIATION_PASSWORD |InputType.TYPE_CLASS_TEXT;
        }else if(InputType.TYPE_NUMBER_FLAG_DECIMAL == inputType){
            finalInputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }
        mContentView.setSingleLine(isSingleLine);
        mContentView.setInputType(finalInputType);

        if(showLine){
            lineView.setVisibility(View.VISIBLE);
        }else{
            lineView.setVisibility(View.GONE);
        }

        if(showCb){
            mCheckBox.setVisibility(View.VISIBLE);
            mCheckBox.setText(cbText  == null ? "": cbText);
        }else{
            mCheckBox.setVisibility(View.GONE);
        }
        if(showImg){
            mBtnImage.setVisibility(View.VISIBLE);
            mBtnImage.setImageResource(imageId);
        }else{
            mBtnImage.setVisibility(View.GONE);
        }

        if(showBtn){
            btn.setVisibility(View.VISIBLE);
            btn.setText(btnText == null ? "" : btnText);
        }else{
            btn.setVisibility(View.GONE);
        }

        typedArray.recycle();


        mContentView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    KeyBoardUtils.closeKeybord(mContentView);
                }
                return false;
            }
        });
    }

    public void setContentViewEnabled(boolean enable){
        mContentView.setEnabled(enable);
    }
    public void setImageEnabled(boolean enable, int imageId){
        if(enable){
            mBtnImage.setVisibility(View.VISIBLE);
            mBtnImage.setImageResource(imageId);
        }else{
            mBtnImage.setVisibility(View.GONE);
        }
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

    public String getItemContent(){
        return mContentView.getText().toString().trim();
    }

    public boolean isChecked(){
        return mCheckBox.isChecked();
    }

    public ImageView getImageView(){
        return mBtnImage;
    }

    public void setImageDrawable(Drawable drawable){
        mBtnImage.setImageDrawable(drawable);
    }

    public Button getBtn() {
        return btn;
    }

    public EditText getmContentView() {
        return mContentView;
    }

    public CheckBox getmCheckBox() {
        return mCheckBox;
    }

    public void setSelection(int index){
        mContentView.setSelection(index);
    }

    public void setContentViewGravity(int gravity){
        mContentView.setGravity(gravity);
    }
}
