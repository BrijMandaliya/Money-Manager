package com.example.moneymanager.Model;

public class TransactionModel {

    private String Amount;
    private String Category;
    private String Notes;
    private String transactiondate;
    private String transactionmonth;
    private String transactionweek;
    private String transactionyear;

    private String CategoryColor;

    private String Key;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getCategoryColor() {
        return CategoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        CategoryColor = categoryColor;
    }

    public TransactionModel(){}

    public TransactionModel(String amount, String category, String notes, String transactiondate, String transactionmonth, String transactionweek, String transactionyear, String CategoryColor,String key) {
        Amount = amount;
        Category = category;
        Notes = notes;
        this.transactiondate = transactiondate;
        this.transactionmonth = transactionmonth;
        this.transactionweek = transactionweek;
        this.transactionyear = transactionyear;
        this.CategoryColor = CategoryColor;
        this.Key = Key;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }

    public String getTransactionmonth() {
        return transactionmonth;
    }

    public void setTransactionmonth(String transactionmonth) {
        this.transactionmonth = transactionmonth;
    }

    public String getTransactionweek() {
        return transactionweek;
    }

    public void setTransactionweek(String transactionweek) {
        this.transactionweek = transactionweek;
    }

    public String getTransactionyear() {
        return transactionyear;
    }

    public void setTransactionyear(String transactionyear) {
        this.transactionyear = transactionyear;
    }
}
