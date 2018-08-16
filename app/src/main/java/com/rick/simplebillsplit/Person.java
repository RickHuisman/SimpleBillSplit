package com.rick.simplebillsplit;

import java.math.BigDecimal;

public class Person {
    Character name;
    BigDecimal money;
    BigDecimal percentage;

    Person(Character name, BigDecimal money, BigDecimal percentage) {
        this.name = name;
        this.money = money;
        this.percentage = percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
