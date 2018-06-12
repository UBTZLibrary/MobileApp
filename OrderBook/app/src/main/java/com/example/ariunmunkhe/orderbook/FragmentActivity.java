package com.example.ariunmunkhe.orderbook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class FragmentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String _loginUserID = "";
    public static String _Password = "";

    public static String NAMESPACE = "http://tempuri.org/";
    public static String URL = "http://ubtzlibrary.com/WebService.asmx";
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        setDB();
        getSetting();
        if (!getIsLogin())
            getLoginActivity();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain, new BookListActivity());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void getLoginActivity() {
        try {

            Intent intent = new Intent(FragmentActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean getIsLogin() {
        Cursor c = db.rawQuery("SELECT userid,studentid,password from TBLUSER where islogin = 'Y';", null);
        if (c.getCount() == 0) {
            return false;
        }
        while (c.moveToNext()) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);
            TextView txtLoginUserName = (TextView) header.findViewById(R.id.txtLoginUserName);
            txtLoginUserName.setText(c.getString(0));
            _loginUserID = c.getString(0);
            _Password = c.getString(2);
            return true;
        }
        return false;
    }

    private void setDB() {
        try {
            db = openOrCreateDatabase("appDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tblsetting(entryID VARCHAR,namespace VARCHAR, url VARCHAR);");
            db.execSQL("INSERT INTO tblsetting(entryID,namespace, url) \n" +
                    "SELECT '1', 'http://tempuri.org/', 'http://ubtzlibrary.com/WebService.asmx' \n" +
                    "WHERE NOT EXISTS(SELECT 1 FROM tblsetting WHERE entryID = '1');");
            db.execSQL("create table if not exists TBLUSER(\n" +
                    "  userid     NVARCHAR2(100) not null,\n" +
                    "  studentid  NVARCHAR2(10),\n" +
                    "  password   NVARCHAR2(100),\n" +
                    "  isactive   NVARCHAR2(1) default 'Y' not null,\n" +
                    "  islogin    NVARCHAR2(1) default 'N' not null);");
            db.execSQL("create table TBLBOOKORDER(\n" +
                    "  orderid      NUMBER(10) not null,\n" +
                    "  studentid    NUMBER(10) not null,\n" +
                    "  bookid       NUMBER(10) not null,\n" +
                    "  orderdate    DATE default SYSDATE,\n" +
                    "  givedate     DATE default SYSDATE,\n" +
                    "  takedate     DATE default SYSDATE,\n" +
                    "  returndate   DATE default SYSDATE,\n" +
                    "  returneddate DATE default SYSDATE,\n" +
                    "  status       NUMBER(1) default 0,\n" +
                    "  reason       NUMBER(1) default 0,\n" +
                    "  note         NVARCHAR2(1000),\n" +
                    "  created      DATE default SYSDATE,\n" +
                    "  createdby    NVARCHAR2(100),\n" +
                    "  updated      DATE default SYSDATE,\n" +
                    "  updatedby    NVARCHAR2(100),\n" +
                    "  ipaddress    NVARCHAR2(30),\n" +
                    "  macaddress   NVARCHAR2(30))");
            db.execSQL("CREATE TABLE IF NOT EXISTS TBLBOOK (\n" +
                    "BOOKID NUMBER(10) Not Null ,\n" +
                    "CODE NVARCHAR2(100) Null ,\n" +
                    "NAME NVARCHAR2(60) Null ,\n" +
                    "ISBN NVARCHAR2(40) Null ,\n" +
                    "CATEGORYID NUMBER(10) Null ,\n" +
                    "ISACTIVE NVARCHAR2(1) DEFAULT 'Y',\n" +
                    "PRINTEDYEAR NVARCHAR2(4) Null ,\n" +
                    "PRINTEDVERSION NUMBER(3) DEFAULT 1,\n" +
                    "VOLUMENUM NUMBER(3) DEFAULT 0,\n" +
                    "TOTALVOLUMENUM NUMBER(3) DEFAULT 0,\n" +
                    "CREATED DATE DEFAULT SYSDATE,\n" +
                    "CREATEDBY NVARCHAR2(100) Null ,\n" +
                    "UPDATED DATE DEFAULT SYSDATE,\n" +
                    "UPDATEDBY NVARCHAR2(100) Null ,\n" +
                    "IPADDRESS NVARCHAR(30) Null ,\n" +
                    "MACADDRESS NVARCHAR(30) Null, BOOKIMAGE BLOB NULL);");
            db.execSQL("CREATE TABLE IF NOT EXISTS TBLCATEGORY (\n" +
                    "CATEGORYID NUMBER(10) Not Null ,\n" +
                    "CODE NVARCHAR2(100) Null ,\n" +
                    "NAME NVARCHAR(60) Not Null ,\n" +
                    "ISACTIVE NVARCHAR(1) DEFAULT 'Y',\n" +
                    "CREATED DATE DEFAULT SYSDATE,\n" +
                    "CREATEDBY NVARCHAR(100) Null ,\n" +
                    "UPDATED DATE DEFAULT SYSDATE,\n" +
                    "UPDATEDBY NVARCHAR(100) Null ,\n" +
                    "IPADDRESS NVARCHAR(30) Null ,\n" +
                    "MACADDRESS NVARCHAR(30) Null );");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getSetting() {
        Cursor c = db.rawQuery("SELECT entryID,namespace,url from tblsetting where entryid = '1';", null);
        if (c.getCount() == 0) {
            return;
        }
        while (c.moveToNext()) {
            NAMESPACE = c.getString(1);
            URL = c.getString(2);
        }
    }

    private void getChangePasswordActivity() {
        try {

            Intent intent = new Intent(FragmentActivity.this, ChangePasswordActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSettingsActivity() {
        try {

            Intent intent = new Intent(FragmentActivity.this, SettingsActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getSettingsActivity();
            return true;
        } else if (id == R.id.action_change_password) {
            getChangePasswordActivity();
            return true;
        } else if (id == R.id.action_login_out) {
            db.execSQL("update tbluser set islogin = 'N' where USERID = '" + _loginUserID + "';");
            _loginUserID = "";
            _Password = "";
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorites) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameMain, new OrderActivity());
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_book_list) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameMain, new BookListActivity());
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_order_list) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameMain, new OrderHistListActivity());
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_logout) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
