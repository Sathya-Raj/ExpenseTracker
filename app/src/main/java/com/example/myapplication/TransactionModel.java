package com.example.myapplication;

public class TransactionModel {
    private String id;
    private String note;
    private String type;
    private String flag;
    private String amount;
    private String datetime;



    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public TransactionModel(String id, String note, String type, String flag, String amount, String datetime) {
        this.id = id;
        this.note = note;
        this.type = type;
        this.flag = flag;
        this.amount = amount;
        this.datetime = datetime;
    }

    public TransactionModel() {
    }
}
