package hmd.example.concept.models;

import java.math.BigInteger;
import java.util.Random;

import hmd.example.concept.classes.Constants;
import hmd.example.concept.util.Utils;

public class SimpleNumber {
    BigInteger number;
    String formattedString;

    public SimpleNumber(BigInteger number) {
        this.number = number;
    }

    public SimpleNumber() {
        number = Utils.nextRandomBigInteger(Constants.maxNumber);
    }

    public BigInteger getNumber() {
        return number;
    }

    public void setNumber(BigInteger number) {
        this.number = number;
    }

    public String getFormattedString() {
        return formattedString;
    }

    public void setFormattedString(String formattedString) {
        this.formattedString = formattedString;
    }
}
