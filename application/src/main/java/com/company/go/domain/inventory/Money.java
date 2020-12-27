package com.company.go.domain.inventory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Currency;

import static java.math.BigDecimal.ZERO;

@Getter
@Setter
public class Money  implements Comparable<Money>{

    @NotEmpty
    private BigDecimal value;

    private final Currency currency;


    public Money(BigDecimal value, Currency currency){
        this.value = value;
        this.currency = currency;
    }

    public Money(BigDecimal value){
        this(value, null);
    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean isCurrencyEqual(Money other){
        return other != null && other.currency != null && this.currency.equals(other.currency);
    }

    public boolean hasDebt(){
        return value.compareTo(ZERO) < 0;
    }

    public Money add(Money other){
        isCurrencyEqual(other);
        return new Money(value.add(other.value), currency);
    }

    public Money minus(Money other){
        isCurrencyEqual(other);
        return new Money(value.subtract(other.value), currency);
    }

    public Money multiply(BigDecimal other){
        BigDecimal value = other.multiply(other);
        value = value.setScale(currency.getDefaultFractionDigits());
        return new Money(value, currency);
    }

    public Money divide(BigDecimal other){
        BigDecimal value = other.divide(other);
        return new Money(value, currency);
    }

    public boolean sameAs(Money other) {
        return compareTo(other) == 0;
    }

    public boolean biggerThan(Money other) {
        return compareTo(other) > 0;
    }

    public boolean smallerThan(Money other) {
        return compareTo(other) < 0;
    }

    public boolean biggerOrSameAs(Money other) {
        return compareTo(other) >= 0;
    }

    public boolean smallerOrSameAs(Money other) {
        return compareTo(other) <= 0;
    }

    public static BigDecimal doubleToBigDecimal(double value){
        String stringValue = Double.toString(value);
        return new BigDecimal(stringValue);
    }

    public String toString(){
        return value.toPlainString() + " " + currency.getSymbol();
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (! (other instanceof Money) )
            return false;
        Money otherMoney = (Money)other;
        return this.getValue().equals(otherMoney.getValue()) ||
                this.isCurrencyEqual(otherMoney);
    }


    public int compareTo(Money other) {
        if ( this == other )
            return 0;

        int comparison = this.getValue().compareTo(other.getValue());
        if ( comparison != 0 )
            return comparison;

        comparison = this.currency.getCurrencyCode().compareTo(
                this.currency.getCurrencyCode()
        );
        if ( comparison != 0 )
            return comparison;
        return 0;
    }

    public static class MoneyConstants{
        enum CurrencyCode{
            LOCKED("LOCKED"), NOT_LOCKED("NOT_LOCKED");

            private final String value;

            CurrencyCode(final String value){
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }
    }

}
