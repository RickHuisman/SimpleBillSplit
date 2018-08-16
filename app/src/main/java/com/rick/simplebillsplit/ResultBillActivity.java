package com.rick.simplebillsplit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

public class ResultBillActivity extends AppCompatActivity {

    BigDecimal mBillTotal;
    BigDecimal mBill;
    BigDecimal mTip;
    int mTipPercentage;
    int mFriends;

    RecyclerView mPersonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_bill);

        ImageView backImage = findViewById(R.id.arrow_back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getBillInfo();
        setUpViews();
        setUpList();
    }

    private void getBillInfo() {
        Intent intent = getIntent();
        mBillTotal = (BigDecimal) intent.getExtras().get("totalBill");
        mBill = (BigDecimal) intent.getExtras().get("bill");
        mTip = (BigDecimal) intent.getExtras().get("tip");
        mTipPercentage = (int) intent.getExtras().get("tipPercentage");
        mFriends = (int) intent.getExtras().get("amountOfPerson");
    }

    private void setUpViews() {
        TextView totalBillText = findViewById(R.id.total_bill_text);
        totalBillText.setText("$" + String.valueOf(mBillTotal));

        TextView billText = findViewById(R.id.bill_text);
        billText.setText("$" + String.valueOf(mBill));

        TextView amountOfPersonsText = findViewById(R.id.amount_of_persons_text);
        amountOfPersonsText.setText(String.valueOf(mFriends));

        TextView tipText = findViewById(R.id.display_tip_text);
        tipText.setText("$" + String.valueOf(mTip));
    }

    private void setUpList() {
        mPersonList = findViewById(R.id.person_list);

        PersonListAdapter adapter = new PersonListAdapter(mBillTotal, mFriends);

        mPersonList.setHasFixedSize(true);
        mPersonList.setLayoutManager(new LinearLayoutManager(this));
        mPersonList.setAdapter(adapter);
    }
}
