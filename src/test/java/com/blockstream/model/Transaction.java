package com.blockstream.model;

public class Transaction {
    private String hash;
    private int inputCount;
    private int outputCount;

    public Transaction(String hash, int inputCount, int outputCount) {
        this.hash = hash;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
    }

    public String getHash() {
        return hash;
    }

    public boolean hasOneInputAndTwoOutputs() {
        return inputCount == 1 && outputCount == 2;
    }
}