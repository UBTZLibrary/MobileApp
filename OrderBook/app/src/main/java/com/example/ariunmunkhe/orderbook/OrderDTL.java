package com.example.ariunmunkhe.orderbook;

/**
 * Created by ariunmunkh.e on 2017-06-12.
 */

public class OrderDTL {
    private long orderid;//    NUMBER(10) not null,\n" +
    private long studentid;//    NUMBER(10) not null,\n" +
    private long bookid;//       NUMBER(10) not null,\n" +
    private String orderdate;//    DATE default SYSDATE,\n" +
    private String givedate;//     DATE default SYSDATE,\n" +
    private String takedate;//    DATE default SYSDATE,\n" +
    private String returndate;//    DATE default SYSDATE,\n" +
    private String returneddate;//  DATE default SYSDATE,\n" +
    private int status;//     NUMBER(1) default 0,\n" +
    private int reason;//       NUMBER(1) default 0,\n" +
    private String note;//     NVARCHAR2(1000),\n" +
    private String created;//      DATE default SYSDATE,\n" +
    private String createdby;//     NVARCHAR2(100),\n" +
    private String updated;//     DATE default SYSDATE,\n" +
    private String updatedby;//     NVARCHAR2(100),\n" +
    private String ipaddress;//     NVARCHAR2(30),\n" +
    private String macaddress;//   NVARCHAR2(30))");

    public OrderDTL(long orderid, long studentid, long bookid,
                    String orderdate, String givedate, String takedate,
                    String returndate, String returneddate, int status,
                    int reason, String note, String created, String createdby,
                    String updated, String updatedby, String ipaddress, String macaddress) {
        this.orderid = orderid;
        this.studentid = studentid;
        this.bookid = bookid;
        this.orderdate = orderdate;
        this.givedate = givedate;
        this.takedate = takedate;
        this.returndate = returndate;
        this.returneddate = returneddate;
        this.status = status;
        this.reason = reason;
        this.note = note;
        this.created = created;
        this.createdby = createdby;
        this.updated = updated;
        this.updatedby = updatedby;
        this.ipaddress = ipaddress;
        this.macaddress = macaddress;
    }

    public long getOrderid() {
        return orderid;
    }

    public long getStudentid() {
        return studentid;
    }

    public long getBookid() {
        return bookid;
    }

    public String getGivedate() {
        return givedate;
    }

    public int getReason() {
        return reason;
    }

    public int getStatus() {
        return status;
    }

    public String getCreated() {
        return created;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public String getNote() {
        return note;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public String getReturndate() {
        return returndate;
    }

    public String getReturneddate() {
        return returneddate;
    }

    public String getTakedate() {
        return takedate;
    }

    public String getUpdated() {
        return updated;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public void setGivedate(String givedate) {
        this.givedate = givedate;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }

    public void setReturneddate(String returneddate) {
        this.returneddate = returneddate;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStudentid(long studentid) {
        this.studentid = studentid;
    }

    public void setTakedate(String takedate) {
        this.takedate = takedate;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }
}
