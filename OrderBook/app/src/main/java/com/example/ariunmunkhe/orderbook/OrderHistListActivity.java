package com.example.ariunmunkhe.orderbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.ariunmunkhe.orderbook.FragmentActivity.db;

/**
 * Created by ariunmunkh.e on 2017-05-19.
 */

public class OrderHistListActivity  extends Fragment {

    ArrayList<BookListDTL> bookListDTLs = new ArrayList<BookListDTL>();
    OrderHistListDTLAdapter bookListDTLAdapter;

    ArrayList<String> categoryNames;
    ArrayList<String> categoryIDs;
    ArrayAdapter<String> adapterCategory;

    Spinner txtCategory;
    ListView bookListView;
    Context thiscontext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        thiscontext = parent.getContext();
        return inflater.inflate(R.layout.activity_order_list, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //getCategoryLocal();
        //adapterCategory = new ArrayAdapter<String>(thiscontext, android.R.layout.simple_spinner_item, categoryNames);
        //txtCategory = (Spinner) view.findViewById(R.id.txtOrderListType);
        //txtCategory.setAdapter(adapterCategory);

        getBookListDTLs();
        bookListDTLAdapter = new OrderHistListDTLAdapter(thiscontext, R.layout.order_hist_dtl, bookListDTLs, this);
        bookListView = (ListView) view.findViewById(R.id.order_list_view);
        bookListView.setAdapter(bookListDTLAdapter);


    }

    private void getCategoryLocal() {
        String lastMaxID = "";
        String lastMotifyDate = "";
        Cursor c = db.rawQuery("SELECT ifnull(max(categoryid),-1) as lastMaxID, max(updated) as lastMotifyDate FROM tblCategory", null);
        if (c.getCount() == 0) {
            lastMaxID = "-1";
            lastMotifyDate = "";
        }
        while (c.moveToNext()) {
            lastMaxID = c.getString(0);
            lastMotifyDate = c.getString(1);
        }

        getCategory(lastMaxID, lastMotifyDate);
        categoryNames = new ArrayList<String>();
        categoryIDs = new ArrayList<String>();

        categoryIDs.add("-1");
        categoryNames.add("Бүх төрөл");

        c = db.rawQuery("SELECT CategoryID, Name FROM tblCategory", null);
        if (c.getCount() == 0) {
            return;
        }
        while (c.moveToNext()) {
            categoryIDs.add(c.getString(0));
            categoryNames.add(c.getString(1));
        }
    }

    private void getCategory(String lastMaxID, String lastMotifyDate) {
        try {
            final String METHOD_NAME = "GetCategory";

            SoapObject request = new SoapObject(FragmentActivity.NAMESPACE, METHOD_NAME);
            request.addProperty("lastMaxID", lastMaxID);
            request.addProperty("lastMotifyDate", lastMotifyDate);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(FragmentActivity.URL);
            transporte.call(FragmentActivity.NAMESPACE + METHOD_NAME, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram.getProperty("NewDataSet");

            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dataxml = (SoapObject) newdataset.getProperty(i);
                try {
                    db.execSQL("delete FROM tblCategory WHERE CategoryID='" + dataxml.getProperty(0).toString() + "'");
                    ContentValues value = new ContentValues();

                    if (dataxml.hasProperty("CATEGORYID"))
                        value.put("CategoryID", dataxml.getProperty(0).toString());
                    if (dataxml.hasProperty("CODE"))
                        value.put("Code", dataxml.getProperty(1).toString());
                    if (dataxml.hasProperty("NAME"))
                        value.put("Name", dataxml.getProperty(2).toString());
                    if (dataxml.hasProperty("ISACTIVE"))
                        value.put("IsActive", dataxml.getProperty(3).toString());
                    if (dataxml.hasProperty("CREATED"))
                        value.put("Created", dataxml.getProperty(4).toString());
                    if (dataxml.hasProperty("CREATEDBY"))
                        value.put("CreatedBy", dataxml.getProperty(5).toString());
                    if (dataxml.hasProperty("UPDATED"))
                        value.put("Updated", dataxml.getProperty(6).toString());
                    if (dataxml.hasProperty("UPDATEDBY"))
                        value.put("UpdatedBy", dataxml.getProperty(7).toString());

                    db.insert("tblCategory", null, value);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getBookListDTLs() {

        String lastMaxID = "";
        String lastMotifyDate = "";
        db.execSQL("delete FROM TBLBOOKORDER");
        //if (c.getCount() == 0) {
        //   lastMaxID = "-1";
        //   lastMotifyDate = "";
        //}
        //while (c.moveToNext()) {
        //    lastMaxID = c.getString(0);
        //    lastMotifyDate = c.getString(1);
        //}
        GetBook(lastMotifyDate);

        bookListDTLs = new ArrayList<BookListDTL>();
        BookListDTL cn;
        Cursor c = db.rawQuery("SELECT tblBook.Bookid,\n" +
                "       tblBook.Code,\n" +
                "       tblBook.Name,\n" +
                "       tblBook.Isbn,\n" +
                "       tblCategory.name CategoryName,\n" +
                "       tblBook.Isactive,\n" +
                "       tblBook.Printedyear,\n" +
                "       tblBook.Printedversion,\n" +
                "       tblBook.Volumenum,\n" +
                "       tblBook.Totalvolumenum,\n" +
                "       tblBook.Created,\n" +
                "       tblBook.Createdby,\n" +
                "       tblBook.Updated,\n" +
                "       tblBook.Updatedby,\n" +
                "       tblBook.Ipaddress,\n" +
                "       tblBook.Macaddress,\n" +
                "       BOOKIMAGE,\n" +
                "       TBLBOOKORDER.status,\n" +
                "       tblBook.favorites,\n" +
                "       strftime('%Y.%m.%d',TBLBOOKORDER.OrderDate),\n" +
                "       strftime('%Y.%m.%d',TBLBOOKORDER.GiveDate),\n" +
                "       strftime('%Y.%m.%d',TBLBOOKORDER.TakeDate),\n" +
                "       strftime('%Y.%m.%d',TBLBOOKORDER.ReturnDate),\n" +
                "       strftime('%Y.%m.%d',TBLBOOKORDER.ReturnedDate)\n" +
                "  FROM TBLBOOKORDER \n" +
                "  left join tblBook " +
                "    on TBLBOOKORDER.bookid = tblbook.bookid" +
                "  left join tblCategory\n" +
                "    on tblCategory.CategoryID = tblBook.CategoryID", null);
        if (c.getCount() == 0) {
            return;
        }
        while (c.moveToNext()) {
            Bitmap tempImage = null;
            byte[] zurag = c.getBlob(16);
            if (zurag != null)
                tempImage = BitmapFactory.decodeByteArray(zurag, 0, zurag.length);
            cn = new BookListDTL(c.getLong(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getLong(7),
                    c.getLong(8),
                    c.getLong(9),
                    c.getString(10),
                    c.getString(11),
                    c.getString(12),
                    c.getString(13),
                    c.getString(14),
                    c.getString(15),
                    tempImage,
                    c.getLong(17));
            cn.setStatus(c.getInt(17));
            cn.setFavorites(c.getString(18));
            cn.setOrderDate(c.getString(19));
            cn.setGiveDate(c.getString(20));
            cn.setTakeDate(c.getString(21));
            cn.setReturnDate(c.getString(22));
            cn.setReturnedDate(c.getString(23));
            bookListDTLs.add(cn);
        }
    }

    public void setChangeFavorites(String bookID, boolean iffavorites) {
        if (iffavorites)
            db.execSQL("UPDATE TBLBOOK SET favorites = 'Y' WHERE BOOKID = " + bookID + "");
        else
            db.execSQL("UPDATE TBLBOOK SET favorites = 'N' WHERE BOOKID = " + bookID + "");
    }

    private void GetBook(String lastMotifyDate) {
        try {
            final String METHOD_NAME = "GetBookOrder";

            SoapObject request = new SoapObject(FragmentActivity.NAMESPACE, METHOD_NAME);
            //request.addProperty("lastMaxID", lastMaxID);
            request.addProperty("lastMotifyDate", lastMotifyDate);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(FragmentActivity.URL);
            transporte.call(FragmentActivity.NAMESPACE + METHOD_NAME, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram.getProperty("NewDataSet");

            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dataxml = (SoapObject) newdataset.getProperty(i);
                try {
                    db.execSQL("delete FROM TBLBOOKORDER WHERE ORDERID='" + dataxml.getProperty(0).toString() + "'");
                    ContentValues value = new ContentValues();
                    if (dataxml.hasProperty("ORDERID"))
                        value.put("ORDERID", dataxml.getProperty("ORDERID").toString());
                    if (dataxml.hasProperty("STUDENTID"))
                        value.put("STUDENTID", dataxml.getProperty("STUDENTID").toString());
                    if (dataxml.hasProperty("BOOKID"))
                        value.put("BOOKID", dataxml.getProperty("BOOKID").toString());
                    if (dataxml.hasProperty("ORDERDATE"))
                        value.put("ORDERDATE", dataxml.getProperty("ORDERDATE").toString());
                    if (dataxml.hasProperty("GIVEDATE"))
                        value.put("GIVEDATE", dataxml.getProperty("GIVEDATE").toString());
                    if (dataxml.hasProperty("TAKEDATE"))
                        value.put("TAKEDATE", dataxml.getProperty("TAKEDATE").toString());
                    if (dataxml.hasProperty("RETURNDATE"))
                        value.put("RETURNDATE", dataxml.getProperty("RETURNDATE").toString());
                    if (dataxml.hasProperty("RETURNEDDATE"))
                        value.put("RETURNEDDATE", dataxml.getProperty("RETURNEDDATE").toString());
                    if (dataxml.hasProperty("STATUS"))
                        value.put("STATUS", dataxml.getProperty("STATUS").toString());
                    if (dataxml.hasProperty("REASON"))
                        value.put("REASON", dataxml.getProperty("REASON").toString());
                    if (dataxml.hasProperty("NOTE"))
                        value.put("NOTE", dataxml.getProperty("NOTE").toString());
                    if (dataxml.hasProperty("CREATED"))
                        value.put("CREATED", dataxml.getProperty("CREATED").toString());
                    if (dataxml.hasProperty("CREATEDBY"))
                        value.put("CREATEDBY", dataxml.getProperty("CREATEDBY").toString());
                    if (dataxml.hasProperty("UPDATED"))
                        value.put("UPDATED", dataxml.getProperty("UPDATED").toString());
                    if (dataxml.hasProperty("IPADDRESS"))
                        value.put("IPADDRESS", dataxml.getProperty("IPADDRESS").toString());
                    if (dataxml.hasProperty("MACADDRESS"))
                        value.put("MACADDRESS", dataxml.getProperty("MACADDRESS").toString());

                    db.insert("TBLBOOKORDER", null, value);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
