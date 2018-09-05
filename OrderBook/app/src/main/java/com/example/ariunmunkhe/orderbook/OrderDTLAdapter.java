package com.example.ariunmunkhe.orderbook;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
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

    public OrderDTLAdapter(Context context, int layoutResourceId, ArrayList<BookListDTL> data, OrderActivity bookList) {
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
            holder.orderImageView = (ImageView) row.findViewById(R.id.imageView);
            holder.orderImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    BookListDTL bookListDTL = data.get(position);

                    if (bookListDTL.getFavorites()) {
                        ((AppCompatImageView) v).setImageResource(R.drawable.btn_star_big_off);
                        bookList.setChangeFavorites(v.getTag().toString(),false);
                        bookListDTL.setFavorites(false);
                    }
                    else {
                        ((AppCompatImageView) v).setImageResource(R.drawable.btn_star_big_on);
                        bookList.setChangeFavorites(v.getTag().toString(),true);
                        bookListDTL.setFavorites(true);
                    }
                }
            });
            holder.btnOrder.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BookListDTL bookListDTL = data.get(position);

                    String retMessage = bookList.setBookOrder(v.getTag().toString(),((AppCompatCheckBox) v).isChecked());
                    if (retMessage.equalsIgnoreCase("OK")) {
                        bookListDTL.setIsOrder(1);
                    }
                    else {
                        bookListDTL.setIsOrder(0);
                    }
                }
            });
            row.setTag(holder);
        } else {
            holder = (BookListDTLAdapter.Anka) row.getTag();
        }
        BookListDTL BookListDTL = data.get(position);
        holder.btnOrder.setTag(BookListDTL.getBookID());
        holder.bookImageView.setImageBitmap(BookListDTL.getBookImage());
        holder.txtBookName.setText(BookListDTL.getBookName());
        holder.txtCategoryName.setText("Төрөл: " + BookListDTL.getCategoryName());
        holder.txtPrintedYear.setText(BookListDTL.getPrintedYear() + " он");

        holder.orderImageView.setTag(BookListDTL.getBookID());
        if(BookListDTL.getFavorites())
        {
            holder.orderImageView.setImageResource(R.drawable.btn_star_big_on);
        }
        else
        {
            holder.orderImageView.setImageResource(R.drawable.btn_star_big_off);
        }
        if(BookListDTL.getIsOrder().equals("Y"))
        {
            holder.btnOrder.setChecked(true);
        }
        else
        {
            holder.btnOrder.setChecked(false);
        }

        return row;
    }

    static class Anka {
        ImageView bookImageView;
        TextView txtBookName;
        TextView txtCategoryName;
        TextView txtPrintedYear;
        CheckBox btnOrder;
        ImageView orderImageView;
    }
}
