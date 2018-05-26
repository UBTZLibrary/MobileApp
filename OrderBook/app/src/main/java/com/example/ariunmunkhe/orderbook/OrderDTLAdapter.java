package com.example.ariunmunkhe.orderbook;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ariunmunkh.e on 2017-06-12.
 */

public class OrderDTLAdapter  extends ArrayAdapter<BookListDTL> {
    Context context;
    int layoutResourceId;
    ArrayList<BookListDTL> data = new ArrayList<BookListDTL>();
    OrderActivity bookList;

    public OrderDTLAdapter(Context context, int layoutResourceId, ArrayList<BookListDTL> data,OrderActivity bookList) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.bookList = bookList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BookListDTLAdapter.Anka holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new BookListDTLAdapter.Anka();
            holder.bookImageView = (ImageView) row.findViewById(R.id.bookImageView);
            holder.txtBookName = (TextView) row.findViewById(R.id.txtBookName);
            holder.txtCategoryName = (TextView) row.findViewById(R.id.txtCategoryName);
            holder.txtPrintedYear = (TextView) row.findViewById(R.id.txtPrintedYear);
            holder.btnOrder = (CheckBox) row.findViewById(R.id.btnBookOrder);
            row.setTag(holder);
        } else {
            holder = (BookListDTLAdapter.Anka) row.getTag();
        }
        BookListDTL BookListDTL = data.get(position);
//        holder.btnOrder.setTag(bookListDTL.getBookID());
//        holder.bookImageView.setImageBitmap(bookListDTL.getBookImage());
//        holder.txtBookName.setText(bookListDTL.getBookName());
//        holder.txtCategoryName.setText("Төрөл: " + bookListDTL.getCategoryName());
//        holder.txtPrintedYear.setText(bookListDTL.getPrintedYear() + " он");
        return row;
    }

    static class Anka {
        ImageView bookImageView;
        TextView txtBookName;
        TextView txtCategoryName;
        TextView txtPrintedYear;
        Button btnOrder;
    }
}
