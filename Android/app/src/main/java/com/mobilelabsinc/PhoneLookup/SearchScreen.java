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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;


public class SearchScreen extends Activity {
    Context mContext;

    private List<String> list = new ArrayList<>();
    private int inStockValue = 2;
    private String manufacturer = "";

    private CheckBox osAndroidCheckbox;
    private CheckBox osWindowsCheckbox;
    private CheckBox osIosCheckbox;
    private CheckBox osBlackBerryCheckbox;
    private EditText itemNameEditText;
    private Spinner manufacturerSpinner;
    private Button searchButton;
    private RadioGroup group;


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

        ArrayList<String> arraylist = XMLParser();

        for (int i = 0; i < arraylist.size(); i++) {
            if (!list.contains(arraylist.get(i)))
                list.add(arraylist.get(i));
        }

        osAndroidCheckbox = findViewById(R.id.osAndroidCheckbox);
        osWindowsCheckbox = findViewById(R.id.osWindowsCheckbox);
        osIosCheckbox = findViewById(R.id.osIosCheckbox);
        osBlackBerryCheckbox = findViewById(R.id.osBlackBerryCheckbox);
        itemNameEditText = findViewById(R.id.itemNameEditText);
        manufacturerSpinner = findViewById(R.id.manufacturerSpinner);
        searchButton = findViewById(R.id.searchButton);
        group = findViewById(R.id.inventoryRadioGroup);

        ArrayAdapter<String> manufacturerItems = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        manufacturerItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        manufacturerSpinner.setAdapter(manufacturerItems);
        manufacturerSpinner.setSelection(1);

        manufacturerSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        group.setOnCheckedChangeListener(radioListener);
        searchButton.setOnClickListener(listener);




    }

    @Override
    protected void onDestroy() {
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


    public OnItemSelectedListener spinnerItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
        int position, long id) {
            manufacturer = (String)parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };

    public OnCheckedChangeListener radioListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.inventoryInStockRadioButton:
                    inStockValue = 0;
                    break;
                case R.id.inventoryOutOfStockRadioButton:
                    inStockValue = 1;
                    break;
                case R.id.inventoryAllRadioButton:
                    inStockValue = 2;
                    break;
            }
        }
    };

    public OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (osIosCheckbox.isChecked() | osAndroidCheckbox.isChecked()
                    | osBlackBerryCheckbox.isChecked() | osWindowsCheckbox.isChecked()) {

                Bundle bundle = new Bundle();

                bundle.putString("searchValue", itemNameEditText.getText().toString());
                bundle.putString("manufacturer", manufacturer);
                bundle.putBoolean("isAndroidChecked", osAndroidCheckbox.isChecked());
                bundle.putBoolean("isWindowsChecked", osWindowsCheckbox.isChecked());
                bundle.putBoolean("isIosChecked", osIosCheckbox.isChecked());
                bundle.putBoolean("isBlackBerryChecked", osBlackBerryCheckbox.isChecked());
                bundle.putInt("inStockValue", inStockValue);

                Intent intent = new Intent();
                intent.setClass(mContext, SearchList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();

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

    public ArrayList<String> XMLParser() {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
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

                NodeList currencyList = fstElmnt.getElementsByTagName("Manufacturer");
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
