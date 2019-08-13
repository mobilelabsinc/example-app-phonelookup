package com.mobilelabsinc.PhoneLookup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
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


public class SearchScreen extends AppCompatActivity {
    private Context mContext;
    private int inStockValue = 2;
    private String manufacturer = "";

    private CheckBox osAndroidCheckbox;
    private CheckBox osWindowsCheckbox;
    private CheckBox osIosCheckbox;
    private CheckBox osBlackBerryCheckbox;
    private EditText itemNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.search);
        setTitle("Search");

        //Logout broadcast
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(Logout, intentFilter);



        osAndroidCheckbox = findViewById(R.id.search_os_android_checkbox);
        osWindowsCheckbox = findViewById(R.id.search_os_windows_checkbox);
        osIosCheckbox = findViewById(R.id.search_os_ios_checkbox);
        osBlackBerryCheckbox = findViewById(R.id.search_os_blackberry_checkbox);
        itemNameEditText = findViewById(R.id.search_item_name_edit);
        Spinner manufacturerSpinner = findViewById(R.id.search_manufacturer_spinner);
        Button searchButton = findViewById(R.id.search_search_button);
        RadioGroup group = findViewById(R.id.search_inventory_radio_group);

        //Set the manufacturer spinner list with "HTC" selected
        ArrayList<String> manufacturerList = buildManufacturerList();
        ArrayAdapter<String> manufacturerItems = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, manufacturerList);
        manufacturerItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manufacturerSpinner.setAdapter(manufacturerItems);
        manufacturerSpinner.setSelection(manufacturerItems.getPosition("HTC"));

        manufacturerSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        group.setOnCheckedChangeListener(radioOnCheckedChangeListener);
        searchButton.setOnClickListener(buttonOnClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(Logout);
    }

    private final BroadcastReceiver Logout = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // At this point you should start the signInButton activity and finish this
            // one
            finish();
        }
    };


    private final OnItemSelectedListener spinnerItemSelectedListener = new OnItemSelectedListener() {
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

    private final OnCheckedChangeListener radioOnCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.search_inventory_in_stock_radio_button:
                    inStockValue = 0;
                    break;
                case R.id.search_inventory_out_of_stock_radio_button:
                    inStockValue = 1;
                    break;
                case R.id.search_inventory_all_radio_button:
                    inStockValue = 2;
                    break;
            }
        }
    };

    private final OnClickListener buttonOnClickListener = new OnClickListener() {
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
                intent.setClass(mContext, SearchResults.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.search:
                intent.setClass(mContext, SearchScreen.class);
                startActivity(intent);
                return true;
            case R.id.flag_secure:
                intent.setClass(mContext, FlagSecure.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                intent.setClass(mContext, Login.class);
                startActivity(intent);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                this.sendBroadcast(broadcastIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<String> buildManufacturerList() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            AssetManager assetManager = getAssets();

            InputStream inputStream = assetManager.open("product.xml");

            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Items");

            //Add item Any
            arrayList.add("Any");

            //Add manufacturer items from product list
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element itemElement = (Element) node;

                //Get the manufacturer value
                NodeList manufacturerNodeList = itemElement.getElementsByTagName("Manufacturer");
                Element manufacturerElement = (Element) manufacturerNodeList.item(0);
                manufacturerNodeList = manufacturerElement.getChildNodes();
                String currentManufacturer = manufacturerNodeList.item(0).getNodeValue();

                //Only add the manufacturer to the list if it isn't in the list
                if(!arrayList.contains(currentManufacturer)){
                    arrayList.add(currentManufacturer);
                }
            }

            //Sort the list
            Collections.sort(arrayList);
        }
        catch (Exception ignored) {

        }

        return arrayList;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // a long press of the call key.
        // do our work, returning true to consume it. by
        // returning true, the framework knows an action has
        // been performed on the long press, so will set the
        // canceled flag for the following up event.
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyLongPress(keyCode, event);
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
