package com.rick.simplebillsplit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

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

        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < persons; i++) {
            mPersonList.add(new Person(alphabet[i], billDivided, mBasePercentage, false));
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
        Person person = mPersonList.get(position);

        holder.mPersonText.setText("Person " + person.name.toString().toUpperCase());
        holder.mMoneyText.setText("$" + String.valueOf(person.money));
        holder.mPercentageText.setText(String.valueOf(person.percentage) + "%");
        holder.mPercentageSeekbar.setProgress(Integer.valueOf(person.percentage.setScale(0, RoundingMode.HALF_UP).toString()));

        if (person.isLocked()) {
            holder.mLockCheckBox.setChecked(false);
        } else {
            holder.mLockCheckBox.setChecked(true);
        }

        holder.mLockCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPersonList.get(holder.getLayoutPosition()).locked) {
                    holder.mLockCheckBox.setChecked(true);
                    mPersonList.get(holder.getLayoutPosition()).setLocked(false);

                    // get percentage of locked items
                    BigDecimal perOfLocked = new BigDecimal(0);

                    int amountOfPersonsLocked = 0;
                    for (Person p: mPersonList) {
                        if (p.isLocked()) {
                            amountOfPersonsLocked++;
                            perOfLocked = perOfLocked.add(p.getPercentage());
                        }
                    }

                    //	result = 100 - percentage of locked items
                    BigDecimal result = new BigDecimal(100).subtract(perOfLocked);

                    BigDecimal percentagePerPerson = result.divide(new BigDecimal(getItemCount() - amountOfPersonsLocked), 0, RoundingMode.HALF_UP);

                    // set new percentage per person to the unselected persons
                    for (Person p: mPersonList) {
                        if (!p.isLocked()) {
                            p.setPercentage(percentagePerPerson);

                            BigDecimal money = mBillTotal.multiply(new BigDecimal("0." + percentagePerPerson)).setScale(0, RoundingMode.HALF_UP);
                            p.setMoney(money);

                            notifyItemChanged(mPersonList.indexOf(p));
                        }
                    }
                } else {
                    holder.mLockCheckBox.setChecked(false);
                    mPersonList.get(holder.getLayoutPosition()).setLocked(true);
                }
            }
        });

        holder.mPercentageSeekbar.setOnSeekBarChangeListener(null);
        holder.mPercentageSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // set progress
                    holder.mPercentageText.setText(progress + "%");
                    mPersonList.get(holder.getLayoutPosition()).setPercentage(new BigDecimal(progress));

                    // set money text
                    String newMoney = String.valueOf(mBillTotal.multiply(new BigDecimal("0." + progress)).setScale(0, RoundingMode.HALF_UP));
                    holder.mMoneyText.setText("$" + String.valueOf(newMoney));

                    // lock item
                    mPersonList.get(holder.getLayoutPosition()).setLocked(true);
                    holder.mLockCheckBox.setChecked(false);

                    // check if progress from locked persons is > 100

                    // get percentage of locked items
                    BigDecimal perOfLocked = new BigDecimal(0);

                    for (Person p: mPersonList) {
                        if (p.isLocked()) {
                            perOfLocked = perOfLocked.add(p.getPercentage());
                        }
                    }

                    //	result = 100 - percentage of locked items
                    BigDecimal result = new BigDecimal(100).subtract(perOfLocked);

                    System.out.println("perOfLocked is " + perOfLocked + ", result is " + result + ", progress is " + progress);
                    //	if new progress > result; progress == result
                    if (result.compareTo(new BigDecimal(progress)) == -1 ) {
                        if (result.compareTo(new BigDecimal(0)) == -1) {
                            int maxProgress = progress - Integer.valueOf(result.abs().toString());

                            holder.mPercentageSeekbar.setProgress(maxProgress);

                            holder.mPercentageText.setText(maxProgress + "%");
                            mPersonList.get(holder.getLayoutPosition()).setPercentage(new BigDecimal(maxProgress));

                            // set money text
                            String money = String.valueOf(mBillTotal.multiply(new BigDecimal("0." + maxProgress)).setScale(0, RoundingMode.HALF_UP));
                            holder.mMoneyText.setText("$" + String.valueOf(money));
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // get amount of persons that are locked
                int amountOfPersonsLocked = 0;
                BigDecimal perOfLocked = new BigDecimal(0);

                // get the total percentage of locked persons
                for (Person p: mPersonList) {
                    if (p.isLocked()) {
                        amountOfPersonsLocked++;
                        perOfLocked = perOfLocked.add(p.getPercentage());
                    }
                }

                // new Percentage
                BigDecimal newPercentage = new BigDecimal(100).subtract(perOfLocked);

                int result = newPercentage.compareTo(new BigDecimal(0));
                if (result == 0 || result == -1) {
                    for (Person p: mPersonList) {
                        if (!p.isLocked()) {

                            p.setPercentage(new BigDecimal(0));
                            p.setMoney(new BigDecimal(0));

                            notifyItemChanged(mPersonList.indexOf(p));
                        }
                    }
                } else {
                    // divide new percentage by persons that are not locked
                    BigDecimal percentagePerPerson = newPercentage.divide(new BigDecimal(getItemCount() - amountOfPersonsLocked), 0, RoundingMode.HALF_UP);

                    // set new percentage per person to the unselected persons
                    for (Person p: mPersonList) {
                        if (!p.isLocked()) {
                            p.setPercentage(percentagePerPerson);

                            BigDecimal money = mBillTotal.multiply(new BigDecimal("0." + percentagePerPerson)).setScale(0, RoundingMode.HALF_UP);
                            p.setMoney(money);

                            notifyItemChanged(mPersonList.indexOf(p));
                        }
                    }
                }
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
        CheckBox mLockCheckBox;

        PersonListViewHolder(View itemView) {
            super(itemView);
            mPersonText = itemView.findViewById(R.id.person_name_text);
            mMoneyText = itemView.findViewById(R.id.money_text);
            mPercentageText = itemView.findViewById(R.id.money_percentage_text);
            mPercentageSeekbar = itemView.findViewById(R.id.money_seekbar);
            mLockCheckBox = itemView.findViewById(R.id.lock_checkbox);
        }
    }
}