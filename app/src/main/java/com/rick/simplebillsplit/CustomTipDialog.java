package com.rick.simplebillsplit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class CustomTipDialog extends Dialog {

    private BillSplitActivity mBillSplitActivity;
    private int mTip;
    private EditText mTipInput;

    public CustomTipDialog(BillSplitActivity activity, Context context) {
        super(context);
        this.mBillSplitActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_tip_layout);

        setUpViews();
    }

    private void setUpViews() {
        mTipInput = findViewById(R.id.tip_input);
        mTipInput.addTextChangedListener(tipTextChange);

        mTipInput.requestFocus();
        if (mTipInput.requestFocus())
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(cancelClick);

        Button setButton = findViewById(R.id.set_button);
        setButton.setOnClickListener(setClick);
    }

    private TextWatcher tipTextChange = new TextWatcher() {

        boolean IgnoreUpdate = false;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (IgnoreUpdate || editable.length() == 0)
                return;

            IgnoreUpdate = true;

            int tip = Integer.valueOf(editable.toString());
            String tipParsed = editable.toString();

            if (Integer.valueOf(editable.toString()) > 100) {
                tipParsed = editable.subSequence(0, editable.length() - 1).toString();
                tip = Integer.valueOf(tipParsed);
            }

            mTip = tip;
            mTipInput.setText(String.valueOf(mTip));
            mTipInput.setSelection(tipParsed.length());

            IgnoreUpdate = false;
        }
    };

    View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mBillSplitActivity.cancelTipDialog();
        }
    };

    View.OnClickListener setClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mBillSplitActivity.setCustomTip(mTip);
        }
    };
}
