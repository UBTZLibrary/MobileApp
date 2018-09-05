package com.example.ariunmunkhe.orderbook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
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
 * Created by ariunmunkh.e on 2017-05-19.
 */

public class OrderHistListDTLAdapter extends ArrayAdapter<BookListDTL> {
    Context context;
    int layoutResourceId;
    ArrayList<BookListDTL> data = new ArrayList<BookListDTL>();
    OrderHistListActivity bookList;

    public OrderHistListDTLAdapter(Context context, int layoutResourceId, ArrayList<BookListDTL> data, OrderHistListActivity bookList) {
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
            holder.txtStatus = (TextView) row.findViewById(R.id.txtStatus);
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
            row.setTag(holder);
        } else {
            holder = (Anka) row.getTag();
        }
        String dateString = "";

        BookListDTL bookListDTL = data.get(position);

        if (bookListDTL.getOrderDate() != null && !bookListDTL.getOrderDate().isEmpty() && !bookListDTL.getOrderDate().equalsIgnoreCase("SYSDATE")) {
            dateString += "<br>Захиалгын огноо: " + bookListDTL.getOrderDate() + "<br>";
        }
        if (bookListDTL.getGiveDate() != null && !bookListDTL.getGiveDate().isEmpty() && !bookListDTL.getGiveDate().equalsIgnoreCase("SYSDATE")) {
            dateString += "Олгох огноо: " + bookListDTL.getGiveDate() + "<br>";
        }
        if (bookListDTL.getTakeDate() != null && !bookListDTL.getTakeDate().isEmpty() && !bookListDTL.getTakeDate().equalsIgnoreCase("SYSDATE")) {
            dateString += "Олгосон огноо: " + bookListDTL.getTakeDate() + "<br>";
        }
        if (bookListDTL.getReturnDate() != null && !bookListDTL.getReturnDate().isEmpty() && !bookListDTL.getReturnDate().equalsIgnoreCase("SYSDATE")) {
            dateString += "Буцааж өгөх огноо: " + bookListDTL.getReturnDate() + "<br>";
        }
        if (bookListDTL.getReturnedDate() != null && !bookListDTL.getReturnedDate().isEmpty() && !bookListDTL.getReturnedDate().equalsIgnoreCase("SYSDATE")) {
            dateString += "Буцааж өгсөн огноо: " + bookListDTL.getReturnedDate() + "<br>";
        }
        holder.orderImageView.setTag(bookListDTL.getBookID());
        holder.bookImageView.setImageBitmap(bookListDTL.getBookImage());
        holder.txtBookName.setText(bookListDTL.getBookName());
        holder.txtCategoryName.setText("Төрөл: " + bookListDTL.getCategoryName());
        holder.txtPrintedYear.setText(bookListDTL.getPrintedYear() + " он");

        holder.txtStatus.setText(Html.fromHtml("Төлөв: "+"<b>" + bookListDTL.getStatus() + "</b>" + dateString) );
        if(bookListDTL.getFavorites())
        {
            holder.orderImageView.setImageResource(R.drawable.btn_star_big_on);
        }
        else
        {
            holder.orderImageView.setImageResource(R.drawable.btn_star_big_off);
        }
        return row;
    }

    static class Anka {
        ImageView bookImageView;
        TextView txtBookName;
        TextView txtCategoryName;
        TextView txtPrintedYear;
        TextView txtStatus;
        ImageView orderImageView;
    }

}
