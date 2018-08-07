package com.rick.simplebillsplit;

import android.view.View;

import com.google.android.material.chip.Chip;

public class BillTip {

    double mTip;
    int mTipPercentage;
    private Chip[] chips = new Chip[5];

    public BillTip(View view) {
        setUpChips(view);
    }

    private void setUpChips(View view) {
        Chip tenPercentChip = view.findViewById(R.id.ten_percent_chip);
        Chip fifteenPercentChip = view.findViewById(R.id.fifteen_percent_chip);
        Chip twentyPercentChip = view.findViewById(R.id.twenty_percent_chip);
        Chip twentyfivePercentChip = view.findViewById(R.id.twentyfive_percent_chip);
        Chip thirtyPercentChip = view.findViewById(R.id.thirty_percent_chip);

        chips[0] = tenPercentChip;
        chips[1] = fifteenPercentChip;
        chips[2] = twentyPercentChip;
        chips[3] = twentyfivePercentChip;
        chips[4] = thirtyPercentChip;

        for (Chip chip: chips) {
            chip.setOnClickListener(tipClickListener);
        }
    }

    private final View.OnClickListener tipClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ten_percent_chip:
                    setTipPercentage(10);
                    break;
                case R.id.fifteen_percent_chip:
                    setTipPercentage(15);
                    break;
                case R.id.twenty_percent_chip:
                    setTipPercentage(20);
                    break;
                case R.id.twentyfive_percent_chip:
                    setTipPercentage(25);
                    break;
                case R.id.thirty_percent_chip:
                    setTipPercentage(30);
                    break;
            }
        }
    }

    private void setTipPercentage(int percentage) {
        mTipPercentage = percentage;
        setTip(percentage);
    }

    private void setTip(int percentage) {

    }
}
