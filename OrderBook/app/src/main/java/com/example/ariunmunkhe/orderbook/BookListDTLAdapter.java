package com.example.ariunmunkhe.orderbook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
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
 * Created by ariunmunkh.e on 2017-02-05.
 */

public class BookListDTLAdapter  extends ArrayAdapter<BookListDTL> {
    Context context;
    int layoutResourceId;
    ArrayList<BookListDTL> data = new ArrayList<BookListDTL>();
    BookListActivity bookList;

    public BookListDTLAdapter(Context context, int layoutResourceId, ArrayList<BookListDTL> data,BookListActivity bookList) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.bookList = bookList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Anka holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Anka();
            holder.bookImageView = (ImageView) row.findViewById(R.id.bookImageView);
            holder.txtBookName = (TextView) row.findViewById(R.id.txtBookName);
            holder.txtCategoryName = (TextView) row.findViewById(R.id.txtCategoryName);
            holder.txtPrintedYear = (TextView) row.findViewById(R.id.txtPrintedYear);
            holder.btnOrder = (CheckBox) row.findViewById(R.id.btnBookOrder);
            holder.btnOrder.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BookListDTL bookListDTL = data.get(position);

                    String retMessage = bookList.setBookOrder(v.getTag().toString(),((AppCompatCheckBox) v).isChecked());
                    if (retMessage.equalsIgnoreCase("OK")) {
                        bookListDTL.setIsOrder("Y");
                    }
                    else {
                        bookListDTL.setIsOrder("ERROR");
                    }
                }
            });
            row.setTag(holder);
        } else {
            holder = (Anka) row.getTag();
        }
        BookListDTL bookListDTL = data.get(position);
        holder.btnOrder.setTag(bookListDTL.getBookID());
        holder.bookImageView.setImageBitmap(bookListDTL.getBookImage());
        holder.txtBookName.setText(bookListDTL.getBookName());
        holder.txtCategoryName.setText("Төрөл: " + bookListDTL.getCategoryName());
        holder.txtPrintedYear.setText(bookListDTL.getPrintedYear() + " он");
        if(bookListDTL.getIsOrder().equalsIgnoreCase("Y"))
        {
            holder.btnOrder.setChecked(true);
        }
        return row;
    }

    static class Anka {
        ImageView bookImageView;
        TextView txtBookName;
        TextView txtCategoryName;
        TextView txtPrintedYear;
        CheckBox btnOrder;
    }

}
