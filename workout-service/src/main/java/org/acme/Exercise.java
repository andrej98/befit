package org.acme;

import org.acme.enums.AmountType;

public class Exercise {
    public String name;
    public String description;
    public Integer amount;
    public AmountType amountType;

    public Exercise() {}

    public Exercise(String name, String description, Integer amount, AmountType amountType) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
    }
}
