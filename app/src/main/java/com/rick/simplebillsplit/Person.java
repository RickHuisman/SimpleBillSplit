package com.rick.simplebillsplit;

import java.math.BigDecimal;

public class Person {
    Character name;
    BigDecimal money;
    BigDecimal percentage;
    Boolean locked;

    Person(Character name, BigDecimal money, BigDecimal percentage, boolean lock) {
        this.name = name;
        this.money = money;
        this.percentage = percentage;
        this.locked = lock;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}
