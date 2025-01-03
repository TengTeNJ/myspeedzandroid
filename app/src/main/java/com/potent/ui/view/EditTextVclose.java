package com.potent.ui.view;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.potent.R;


/**
 * SearchText
 * 搜索编辑框
 * @author chrishao
 *
 */
public class EditTextVclose extends LinearLayout {
	//两个按钮
	private ImageView ib_searchtext_delete;
	private EditText et_searchtext_search;
	private String hint;
	private Drawable editicon;
    private int mBackroundResId;
	public EditTextVclose(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextVclose);
		hint = a.getString(R.styleable.EditTextVclose_edithint);
        editicon = a.getDrawable(R.styleable.EditTextVclose_editicon);
        mBackroundResId=a.getResourceId(R.styleable.EditTextVclose_editbackroud,0);
		// TODO Auto-generated constructor stub
		View view = LayoutInflater.from(context).inflate(R.layout.common_edittextwithbutton, null);
		//把获得的view加载到这个控件中
		addView(view);
		//把两个按钮从布局文件中找到
		ib_searchtext_delete = (ImageView) view.findViewById(R.id.ib_custom_delete);
		et_searchtext_search = (EditText) view.findViewById(R.id.et_custom_input);
		if(null!=hint){
			et_searchtext_search.setHint(hint);
		}
		if(null!=editicon){
            editicon.setBounds(0, 0, editicon.getMinimumWidth(), editicon.getMinimumHeight());
			et_searchtext_search.setCompoundDrawables(editicon, null, null, null);
		}
        if(0!=mBackroundResId){
            et_searchtext_search.setBackgroundResource(mBackroundResId);
        }
		//给删除按钮添加点击事件
		ib_searchtext_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_searchtext_search.setText("");
			}
		});
		//给编辑框添加文本改变事件
		et_searchtext_search.addTextChangedListener(new MyTextWatcher());
	}
	public EditText getEditView(){
		return et_searchtext_search;
	}
	public String getText(){
		return et_searchtext_search.getText().toString();
	}
	public void setText(String text){
		et_searchtext_search.setText(text);
	}
	//文本观察者
	private class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		//当文本改变时候的操作
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			//如果编辑框中文本的长度大于0就显示删除按钮否则不显示
			if(s.length() > 0){
				ib_searchtext_delete.setVisibility(View.VISIBLE);
			}else{
				ib_searchtext_delete.setVisibility(View.GONE);
			}
		}
		
	}
}
