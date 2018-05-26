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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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

    ArrayList<OrderHistListDTL> bookListDTLs = new ArrayList<OrderHistListDTL>();
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

        getCategoryLocal();
        adapterCategory = new ArrayAdapter<String>(thiscontext, android.R.layout.simple_spinner_item, categoryNames);
        txtCategory = (Spinner) view.findViewById(R.id.txtOrderListType);
        txtCategory.setAdapter(adapterCategory);

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
        Cursor c = db.rawQuery("SELECT ifnull(max(BookID),-1) as lastMaxID, max(updated) as lastMotifyDate FROM tblBook", null);
        if (c.getCount() == 0) {
            lastMaxID = "-1";
            lastMotifyDate = "";
        }
        while (c.moveToNext()) {
            lastMaxID = c.getString(0);
            lastMotifyDate = c.getString(1);
        }
        GetBook(lastMaxID, lastMotifyDate);

        bookListDTLs = new ArrayList<OrderHistListDTL>();
        OrderHistListDTL cn;
        c = db.rawQuery("SELECT tblBook.Bookid,\n" +
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
                "       case when TBLBOOKORDER.bookid is not null then 'Y' else 'N' end as isorder" +
                "  FROM tblBook\n" +
                "  left join tblCategory\n" +
                "    on tblCategory.CategoryID = tblBook.CategoryID" +
                "  left join (select DISTINCT bookid from TBLBOOKORDER where status = 0) TBLBOOKORDER " +
                "    on TBLBOOKORDER.bookid = tblbook.bookid", null);
        if (c.getCount() == 0) {
            return;
        }
        while (c.moveToNext()) {
            Bitmap tempImage = null;
            byte[] zurag = c.getBlob(16);
            if (zurag != null)
                tempImage = BitmapFactory.decodeByteArray(zurag, 0, zurag.length);
            cn = new OrderHistListDTL();
            bookListDTLs.add(cn);
        }
    }

    private void GetBook(String lastMaxID, String lastMotifyDate) {
        try {
            final String METHOD_NAME = "GetBook";

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
                    db.execSQL("delete FROM tblBook WHERE BookID='" + dataxml.getProperty(0).toString() + "'");
                    ContentValues value = new ContentValues();
                    if (dataxml.hasProperty("BOOKID"))
                        value.put("BookID", dataxml.getProperty("BOOKID").toString());
                    if (dataxml.hasProperty("CODE"))
                        value.put("Code", dataxml.getProperty("CODE").toString());
                    if (dataxml.hasProperty("NAME"))
                        value.put("Name", dataxml.getProperty("NAME").toString());
                    if (dataxml.hasProperty("ISBN"))
                        value.put("ISBN", dataxml.getProperty("ISBN").toString());
                    if (dataxml.hasProperty("CATEGORYID"))
                        value.put("CategoryID", dataxml.getProperty("CATEGORYID").toString());
                    if (dataxml.hasProperty("ISACTIVE"))
                        value.put("IsActive", dataxml.getProperty("ISACTIVE").toString());
                    if (dataxml.hasProperty("PRINTEDYEAR"))
                        value.put("PrintedYear", dataxml.getProperty("PRINTEDYEAR").toString());
                    if (dataxml.hasProperty("PRINTEDVERSION"))
                        value.put("PrintedVersion", dataxml.getProperty("PRINTEDVERSION").toString());
                    if (dataxml.hasProperty("VOLUMENUM"))
                        value.put("VolumeNum", dataxml.getProperty("VOLUMENUM").toString());
                    if (dataxml.hasProperty("TOTALVOLUMENUM"))
                        value.put("TotalVolumeNum", dataxml.getProperty("TOTALVOLUMENUM").toString());
                    if (dataxml.hasProperty("CREATED"))
                        value.put("Created", dataxml.getProperty("CREATED").toString());
                    if (dataxml.hasProperty("CREATEDBY"))
                        value.put("CreatedBy", dataxml.getProperty("CREATEDBY").toString());
                    if (dataxml.hasProperty("UPDATED"))
                        value.put("Updated", dataxml.getProperty("UPDATED").toString());
                    if (dataxml.hasProperty("UPDATEDBY"))
                        value.put("UpdatedBy", dataxml.getProperty("UPDATEDBY").toString());
                    if (dataxml.hasProperty("PICTUREDATA")) {
                        byte[] newByte = Base64.decode(dataxml.getProperty("PICTUREDATA").toString(), Base64.DEFAULT);
                        value.put("BOOKIMAGE", newByte);
                    }
                    db.insert("tblBook", null, value);
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
