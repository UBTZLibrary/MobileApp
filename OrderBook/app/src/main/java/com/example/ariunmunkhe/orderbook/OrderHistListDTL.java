package com.example.ariunmunkhe.orderbook;

/**
 * Created by ariunmunkh.e on 2017-05-19.
 */

public class OrderHistListDTL {
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

    public OrderHistListDTL() {
    }
}
