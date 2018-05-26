package com.example.ariunmunkhe.orderbook;

import android.graphics.Bitmap;

/**
 * Created by ariunmunkh.e on 2017-02-05.
 */

public class BookListDTL {
    private long BookID;
    private String Code;
    private String Name;
    private String ISBN;
    private String CategoryName;
    private String IsActive;
    private String PrintedYear;
    private long PrintedVersion;
    private long VolumeNum;
    private long TotalVolumeNum;
    private String Created;
    private String CreatedBy;
    private String Updated;
    private String UpdatedBy;
    private String IPAddress;
    private String MACAddress;
    private Bitmap BookImage;
    private String isOrder;

    public BookListDTL() {
    }

    public BookListDTL(long BookID,
                       String Code,
                       String Name,
                       String ISBN,
                       String CategoryName,
                       String IsActive,
                       String PrintedYear,
                       long PrintedVersion,
                       long VolumeNum,
                       long TotalVolumeNum,
                       String Created,
                       String CreatedBy,
                       String Updated,
                       String UpdatedBy,
                       String IPAddress,
                       String MACAddress,
                       Bitmap BookImage,
                       String isOrder) {
        this.BookID = BookID;
        this.Code = Code;
        this.Name = Name;
        this.ISBN = ISBN;
        this.CategoryName = CategoryName;
        this.IsActive = IsActive;
        this.PrintedYear = PrintedYear;
        this.PrintedVersion = PrintedVersion;
        this.VolumeNum = VolumeNum;
        this.TotalVolumeNum = TotalVolumeNum;
        this.Created = Created;
        this.CreatedBy = CreatedBy;
        this.Updated = Updated;
        this.UpdatedBy = UpdatedBy;
        this.IPAddress = IPAddress;
        this.MACAddress = MACAddress;
        this.BookImage = BookImage;
        this.isOrder = isOrder;
    }

    public long getBookID() {
        return this.BookID;
    }

    public String getBookCode() {
        return this.Code;
    }

    public String getBookName() {
        return this.Name;
    }

    public String getISBN() {
        return this.ISBN;
    }

    public String getCategoryName() {
        return this.CategoryName;
    }

    public String getIsActive() {
        return this.IsActive;
    }

    public String getPrintedYear() {
        return this.PrintedYear;
    }

    public long getPrintedVersion() {
        return this.PrintedVersion;
    }

    public long getVolumeNum() {
        return this.VolumeNum;
    }

    public long getTotalVolumeNum() {
        return this.TotalVolumeNum;
    }

    public String getCreated() {
        return this.Created;
    }

    public String getCreatedBy() {
        return this.CreatedBy;
    }

    public String getUpdated() {
        return this.Updated;
    }

    public String getUpdatedBy() {
        return this.UpdatedBy;
    }

    public String getIPAddress() {
        return this.IPAddress;
    }

    public String getMACAddress() {
        return this.MACAddress;
    }

    public Bitmap getBookImage() {
        return this.BookImage;
    }

    public String getIsOrder() {
        return this.isOrder;
    }

    public void setBookID(long BookID) {
        this.BookID = BookID;
    }

    public void setBookCode(String Code) {
        this.Code = Code;
    }

    public void setBookName(String Name) {
        this.Name = Name;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public void setIsActive(String IsActive) {
        this.IsActive = IsActive;
    }

    public void setPrintedYear(String PrintedYear) {
        this.PrintedYear = PrintedYear;
    }

    public void setPrintedVersion(long PrintedVersion) {
        this.PrintedVersion = PrintedVersion;
    }

    public void setVolumeNum(long VolumeNum) {
        this.VolumeNum = VolumeNum;
    }

    public void setTotalVolumeNum(long TotalVolumeNum) {
        this.TotalVolumeNum = TotalVolumeNum;
    }

    public void setCreated(String Created) {
        this.Created = Created;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public void setUpdated(String Updated) {
        this.Updated = Updated;
    }

    public void setUpdatedBy(String UpdatedBy) {
        this.UpdatedBy = UpdatedBy;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public void setMACAddress(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    public void setBookImage(Bitmap BookImage) {
        this.BookImage = BookImage;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }
}
