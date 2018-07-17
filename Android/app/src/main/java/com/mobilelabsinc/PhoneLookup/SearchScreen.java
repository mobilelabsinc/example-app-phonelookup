package com.mobilelabsinc.PhoneLookup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;


public class SearchScreen extends Activity {
    public EditText editProduct;
    Context mContext;
    Spinner spinner;
    String spinnerValue;
    private ArrayList<String> arraylist = new ArrayList<String>();
    private List<String> list = new ArrayList<String>();
    private CheckBox radioAndroid;
    private CheckBox radioWindows;
    private CheckBox radioIos;
    private CheckBox radioBB;

    private String ioscheckBoxValue = " ";
    private String androidcheckBoxValue = "Android";
    private String bbcheckBoxValue = " ";
    private String windowscheckBoxValue = " ";
    private int radioInStock = 2;

    // public static String BROADCAST_ACTION = "com.controls.broadcast";

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.search);
        setTitle("Search");

        /* killer broadcast */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(Killer, intentFilter);

        editProduct = (EditText) findViewById(R.id.criteria1EditText);
        Button search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(listener);
        spinner = (Spinner) findViewById(R.id.searchSpinner);

        radioAndroid = (CheckBox) findViewById(R.id.criteria3RadioButton1);
        radioWindows = (CheckBox) findViewById(R.id.criteria3RadioButton2);
        radioIos = (CheckBox) findViewById(R.id.criteria3RadioButton3);
        radioBB = (CheckBox) findViewById(R.id.criteria3RadioButton4);
        radioAndroid.setOnClickListener(checkboxlistener);
        radioWindows.setOnClickListener(checkboxlistener);
        radioIos.setOnClickListener(checkboxlistener);
        radioBB.setOnClickListener(checkboxlistener);

        arraylist = XMLParser1();

        for (int i = 0; i < arraylist.size(); i++) {
            if (!list.contains(arraylist.get(i)))
                list.add(arraylist.get(i));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        // by HTC default
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                spinnerValue = (String) parent.getItemAtPosition(position);
                System.out.println("Selected: " + spinnerValue);
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.criteria4RadioGroup);
        group.setOnCheckedChangeListener(radioListener);



    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(Killer);
    }

    public BroadcastReceiver Killer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // At this point you should start the login activity and finish this
            // one
            finish();
        }
    };

    public OnClickListener checkboxlistener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.criteria3RadioButton1) {
                if (radioAndroid.isChecked())
                    androidcheckBoxValue = "Android";
                else
                    androidcheckBoxValue = " ";
                // System.out.println(androidcheckBoxValue);
            }
            if (v.getId() == R.id.criteria3RadioButton2) {
                if (radioWindows.isChecked())
                    windowscheckBoxValue = "Windows";
                else
                    windowscheckBoxValue = " ";
                // System.out.println(windowscheckBoxValue);
            }
            if (v.getId() == R.id.criteria3RadioButton3) {
                if (radioIos.isChecked())
                    ioscheckBoxValue = "iOS";
                else
                    ioscheckBoxValue = " ";
                // System.out.println(ioscheckBoxValue);
            }
            if (v.getId() == R.id.criteria3RadioButton4) {
                if (radioBB.isChecked())
                    bbcheckBoxValue = "BlackBerry";
                else
                    bbcheckBoxValue = " ";
                // System.out.println(bbcheckBoxValue);
            }
        }
    };


    public OnCheckedChangeListener radioListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            switch (checkedId) {
            case R.id.criteria4RadioButton1: // instock
                radioInStock = 0;
                break;
            case R.id.criteria4RadioButton2: // outof stock
                radioInStock = 1;
                break;
            case R.id.criteria4RadioButton3: // all
                radioInStock = 2;
                break;

            }
        }
    };

    public OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // System.out.println(editProduct.getText().length());
            if (radioIos.isChecked() | radioAndroid.isChecked()
                    | radioBB.isChecked() | radioWindows.isChecked()) {

                Bundle bundle = new Bundle();
                bundle.putString("SearchbyText", editProduct.getText()
                        .toString());
                bundle.putString("spinnerValue", spinnerValue);
                // bundle.putString("radioValue", radioValue);

                bundle.putString("androidcheckBoxValue", androidcheckBoxValue);
                bundle.putString("windowscheckBoxValue", windowscheckBoxValue);
                bundle.putString("ioscheckBoxValue", ioscheckBoxValue);
                bundle.putString("bbcheckBoxValue", bbcheckBoxValue);

                bundle.putInt("radioInStock", radioInStock);
                Intent intent = new Intent();
                intent.setClass(mContext, SearchList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Operating system selection is Mandatory!")
                        .setCancelable(false)

                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    };

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view, menu);
        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
        case R.id.search:
            return true;
        case R.id.logout:
            intent.setClass(mContext, Login.class);
            startActivity(intent);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
            sendBroadcast(broadcastIntent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public ArrayList<String> XMLParser1() {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            // URL url = new
            // URL("http://www.androidpeople.com/wp-content/uploads/2010/06/example.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("product.xml");
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Items");
            /*
             * To view all the manufacturer productManually added "ANY"
             */
            arrayList.add("Any");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;
                NodeList currencyList = fstElmnt
                        .getElementsByTagName("Manufacturer");
                Element currencyListElement = (Element) currencyList.item(0);
                currencyList = currencyListElement.getChildNodes();
                arrayList.add(((Node) currencyList.item(0)).getNodeValue());
            }
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }

        return arrayList;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // a long press of the call key.
            // do our work, returning true to consume it. by
            // returning true, the framework knows an action has
            // been performed on the long press, so will set the
            // canceled flag for the following up event.
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled()) {
            // if the call key is being released, AND we are tracking
            // it from an initial key down, AND it is not canceled,
            // then handle it.
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }



}
