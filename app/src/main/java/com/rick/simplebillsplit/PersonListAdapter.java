package com.rick.simplebillsplit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.PersonListViewHolder> {

    private ArrayList<Person> mPersonList = new ArrayList<>();

    private BigDecimal mBillTotal;

    PersonListAdapter(BigDecimal billTotal, int persons) {
        mBillTotal = billTotal;
        BigDecimal billDivided = billTotal.divide(new BigDecimal(persons), RoundingMode.HALF_UP);
        BigDecimal mBasePercentage = new BigDecimal(100).divide(BigDecimal.valueOf(persons), 2, RoundingMode.HALF_UP);

        for (int i = 0; i < persons; i++) {
            mPersonList.add(new Person('a', billDivided, mBasePercentage));
        }
    }

    @NonNull
    @Override
    public PersonListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        return new PersonListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonListViewHolder holder, int position) {
        final Person person = mPersonList.get(position);

        holder.mPersonText.setText("Person " + person.name.toString().toUpperCase());
        holder.mMoneyText.setText("$" + String.valueOf(person.money));
        holder.mPercentageText.setText(String.valueOf(person.percentage) + "%");

        holder.mPercentageSeekbar.setOnSeekBarChangeListener(null);

        holder.mPercentageSeekbar.setProgress(Integer.valueOf(person.percentage.setScale(0, RoundingMode.HALF_UP).toString()));

        holder.mPercentageSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                BigDecimal newMoney = new BigDecimal(String.valueOf(mBillTotal.multiply(new BigDecimal("0." + progress))));
                int newPercentage = (100 - progress) / (getItemCount() - 1);

                for (Person p: mPersonList) {
                    p.setPercentage(new BigDecimal(newPercentage));
                    BigDecimal newTotalMoney = new BigDecimal(String.valueOf(mBillTotal.subtract(newMoney)));
                    p.money = newTotalMoney.divide(BigDecimal.valueOf(getItemCount() - 1)).setScale(0, RoundingMode.HALF_UP);

                    notifyItemChanged(mPersonList.indexOf(p));
                }

                mPersonList.get(mPersonList.indexOf(person)).setPercentage(new BigDecimal(progress));
                mPersonList.get(mPersonList.indexOf(person)).money = newMoney.setScale(0, RoundingMode.HALF_UP);
                notifyItemChanged(mPersonList.indexOf(person));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

    class PersonListViewHolder extends RecyclerView.ViewHolder {

        TextView mPersonText, mMoneyText, mPercentageText;
        SeekBar mPercentageSeekbar;

        PersonListViewHolder(View itemView) {
            super(itemView);
            mPersonText = itemView.findViewById(R.id.person_name_text);
            mMoneyText = itemView.findViewById(R.id.money_text);
            mPercentageText = itemView.findViewById(R.id.money_percentage_text);
            mPercentageSeekbar = itemView.findViewById(R.id.money_seekbar);
        }
    }
}