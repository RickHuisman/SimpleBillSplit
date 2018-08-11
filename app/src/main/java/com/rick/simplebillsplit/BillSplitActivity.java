package com.rick.simplebillsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.math.BigDecimal;

public class BillSplitActivity extends AppCompatActivity {

    BigDecimal mBill;
    String mBillString = "";
    BigDecimal mBillTotal;
    BigDecimal mTip;
    int mTipPercentage;
    int mFriends;

    Chip[] chips = new Chip[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_split);

        setUpViews();
        setUpChips();
        setFriends(2);
    }

    private void setUpViews() {
        Button zeroButton = findViewById(R.id.zero_button);
        Button oneButton = findViewById(R.id.one_button);
        Button twoButton = findViewById(R.id.two_button);
        Button threeButton = findViewById(R.id.three_button);
        Button fourButton = findViewById(R.id.four_button);
        Button fiveButton = findViewById(R.id.five_button);
        Button sixButton = findViewById(R.id.six_button);
        Button sevenButton = findViewById(R.id.seven_button);
        Button eightButton = findViewById(R.id.eight_button);
        Button nineButton = findViewById(R.id.nine_button);
        Button decimalButton = findViewById(R.id.decimal_button);
        ImageView backspaceImage = findViewById(R.id.backspace_image);

        zeroButton.setOnClickListener(calcListener);
        oneButton.setOnClickListener(calcListener);
        twoButton.setOnClickListener(calcListener);
        threeButton.setOnClickListener(calcListener);
        fourButton.setOnClickListener(calcListener);
        fiveButton.setOnClickListener(calcListener);
        sixButton.setOnClickListener(calcListener);
        sevenButton.setOnClickListener(calcListener);
        eightButton.setOnClickListener(calcListener);
        nineButton.setOnClickListener(calcListener);
        decimalButton.setOnClickListener(calcListener);
        backspaceImage.setOnClickListener(calcListener);

        SeekBar friendsSeekbar = findViewById(R.id.friends_seekbar);
        friendsSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setFriends(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setUpChips() {
        Chip zeroPercentChip = findViewById(R.id.zero_percent_chip);
        Chip tenPercentChip = findViewById(R.id.ten_percent_chip);
        Chip fifteenPercentChip = findViewById(R.id.fifteen_percent_chip);
        Chip twentyPercentChip = findViewById(R.id.twenty_percent_chip);
        Chip customPercentChip = findViewById(R.id.custom_percent_chip);

        chips[0] = zeroPercentChip;
        chips[1] = tenPercentChip;
        chips[2] = fifteenPercentChip;
        chips[3] = twentyPercentChip;
        chips[4] = customPercentChip;

        setSelectedChipColors(R.id.zero_percent_chip);

        for (Chip tipChip: chips) {
            tipChip.setOnClickListener(tipClickListener);
        }
    }

    View.OnClickListener calcListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int number = 0;

            switch (view.getId()) {
                case R.id.zero_button:
                    number = 0;
                    break;
                case R.id.one_button:
                    number = 1;
                    break;
                case R.id.two_button:
                    number = 2;
                    break;
                case R.id.three_button:
                    number = 3;
                    break;
                case R.id.four_button:
                    number = 4;
                    break;
                case R.id.five_button:
                    number = 5;
                    break;
                case R.id.six_button:
                    number = 6;
                    break;
                case R.id.seven_button:
                    number = 7;
                    break;
                case R.id.eight_button:
                    number = 8;
                    break;
                case R.id.nine_button:
                    number = 9;
                    break;
                case R.id.backspace_image:
                    backspace();
                    return;
                case R.id.decimal_button:
                    addDecimal();
                    return;
            }
            calculate(number);
        }
    };

    View.OnClickListener tipClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resetChipColors();
            switch (view.getId()) {
                case R.id.zero_percent_chip:
                    setTipWithColor(0, R.id.zero_percent_chip);
                    break;
                case R.id.ten_percent_chip:
                    setTipWithColor(10, R.id.ten_percent_chip);
                    break;
                case R.id.fifteen_percent_chip:
                    setTipWithColor(15, R.id.fifteen_percent_chip);
                    break;
                case R.id.twenty_percent_chip:
                    setTipWithColor(20, R.id.twenty_percent_chip);
                    break;
                case R.id.custom_percent_chip:
                    setTipWithColor(30, R.id.custom_percent_chip);
                    break;
            }
        }
    };

    private void resetChipColors() {
        for (Chip chip: chips) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.colorBackground)));
            chip.setChipStrokeColor(ColorStateList.valueOf(getColor(R.color.colorPrimaryLight)));
            chip.setTextColor(getColor(R.color.colorPrimary));
        }
    }

    private void setSelectedChipColors(int id) {
        Chip chip = findViewById(id);
        chip.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
        chip.setChipStrokeColor(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
        chip.setTextColor(getColor(R.color.colorWhite));
    }

    private void calculate(int number) {
        if (mBillString.isEmpty() && number == 0)
            return;

        // check if mBill has 2 decimals
        if (mBill != null) {
            String string = mBill.stripTrailingZeros().toPlainString();
            int index = string.indexOf(".");
            int numOfDecimals = index < 0 ? 0 : string.length() - index - 1;

            if (numOfDecimals >= 2)
                return;
        }

        mBillString = mBillString + String.valueOf(number);
        setBill();
    }

    private void addDecimal() {
        if (!TextUtils.isEmpty(mBillString) && !mBillString.equals("0")) {
            if (mBillString.charAt(0) == '0') {
                mBillString = mBillString.substring(1);
            }

            Character lastNumBill = mBillString.charAt(mBillString.length() - 1);

            if (!(lastNumBill == '.')) {
                mBillString = mBillString + ".";
                setBill();
            }
        }
    }

    private void backspace() {
        if (mBillString.length() == 1) {
            mBillString = "0";

            setBill();
            return;
        }

        if (mBillString != null && mBillString.length() > 1) {
            mBillString = mBillString.substring(0, mBillString.length() - 1);

            setBill();
        }
    }

    private void setBill() {
        TextView billText = findViewById(R.id.bill_text);

        Character lastNumBill = mBillString.charAt(mBillString.length() - 1);

        if (lastNumBill == '.') {
            billText.setText("$" + mBillString);

            if (mBillString != null && mBillString.length() > 1) {
                String billStringWithoutDecimal = mBillString.substring(0, mBillString.length() - 1);
                mBill = new BigDecimal(billStringWithoutDecimal);
            }
        } else {
            mBill = new BigDecimal(mBillString);

            billText.setText("$" + String.valueOf(mBill));
        }

        setTip(mTipPercentage);
        setBillTotal();
    }

    private void setBillTotal() {
        if (mBill == null)
            return;

        mBillTotal = mBill;

        if (mTip != null) {
            mBillTotal = mBillTotal.add(mTip);
        }

        String billTotalString = mBillTotal.stripTrailingZeros().toPlainString();

        TextView totalBillText = findViewById(R.id.total_bill_text);
        totalBillText.setText("$" + billTotalString);
    }

    private void setFriends(int friends) {
        mFriends = friends;
        TextView amountOfFriendsText = findViewById(R.id.amount_of_friends_text);
        amountOfFriendsText.setText(String.valueOf(mFriends));

        TextView friendsText = findViewById(R.id.friends_text);
        friendsText.setText(String.valueOf(mFriends));
    }

    private void setTipWithColor(int percentage, int selectedChip) {
        setSelectedChipColors(selectedChip);

        setTip(percentage);
    }

    private void setTip(int percentage) {
        mTipPercentage = percentage;

        if (mBill != null) {
            mTip = mBill.multiply(new BigDecimal("1." + String.valueOf(percentage))).subtract(mBill);

            TextView displayTipText = findViewById(R.id.display_tip_text);
            displayTipText.setText("$" + String.valueOf(mTip));
        }

        TextView tipText = findViewById(R.id.tip_text);
        tipText.setText("%" + String.valueOf(mTipPercentage));

        setBillTotal();
    }
}
