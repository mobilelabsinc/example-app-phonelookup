package com.mobilelabsinc.PhoneLookup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchResults extends AppCompatActivity {
    private Context mContext;

    private ArrayList<HashMap<String, String>> searchResultsList = new ArrayList<>();
    private HashMap<String, String> itemHashMap;
    private final ArrayList<HashMap<String, String>> itemList = new ArrayList<>();

    private String searchValue;
    private String manufacturerCompare;
    private int inStockValueCompare;
    private Boolean isAndroidChecked;
    private Boolean isWindowsChecked;
    private Boolean isIosChecked;
    private Boolean isBlackBerryChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        setTitle("Results");

        mContext = this;

        /* killer broadcast */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(Killer, intentFilter);

        ListView productListView = findViewById(R.id.product_list_view);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            searchValue = bundle.getString("searchValue");
            manufacturerCompare = bundle.getString("manufacturer");
            isAndroidChecked = bundle.getBoolean("isAndroidChecked");
            isWindowsChecked = bundle.getBoolean("isWindowsChecked");
            isIosChecked = bundle.getBoolean("isIosChecked");
            isBlackBerryChecked = bundle.getBoolean("isBlackBerryChecked");
            inStockValueCompare = bundle.getInt("inStockValue");

            if (searchValue.compareTo("*") == 0) {
                searchValue = "";
            }

            searchResultsList = XMLParser();

            for (int i = 0; i < searchResultsList.size(); i++) {
                itemHashMap = searchResultsList.get(i);

                HashMap<String, String> item = new HashMap<>();

                item.put("ItemName", itemHashMap.get("ItemName"));
                item.put("ItemID", itemHashMap.get("ItemID"));
                item.put("Carrier", "Carrier: " + itemHashMap.get("Carrier"));

                String stockValue = (itemHashMap.get("InStock").equals("Y") && !itemHashMap.get("QtyOnHand").equals("0")) ? "[In Stock]" : "[Out of Stock]";
                item.put("InStock", stockValue);

                if (itemHashMap.get("Price").contains(".")) {
                    String[] priceValue = splitString(itemHashMap.get("Price"));
                    item.put("Price", priceValue[0]);
                    item.put("PriceUnits", priceValue[1] + "\nEA");
                } else {
                    item.put("Price", itemHashMap.get("Price"));
                    item.put("PriceUnits", "00\nEA");
                }
                itemList.add(item);
            }

            if (itemList.size() != 0) {

                productListView.setTextFilterEnabled(true);
                productListView.setAdapter(new listadapter(this, itemList,
                        new String[]{"ItemName", "ItemID", "Carrier", "InStock", "Price", "PriceUnits"}, new int[]{R.id.list_item_item_name, R.id.list_item_item_id, R.id.list_item_carrier, R.id.list_item_in_stock, R.id.list_item_price, R.id.list_item_price_units}));

                productListView.setDividerHeight(1);
                productListView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {

                        itemHashMap = searchResultsList.get(arg2);

                        Bundle bundle = new Bundle();
                        bundle.putString("ItemName", itemHashMap.get("ItemName"));
                        bundle.putString("ItemID", itemHashMap.get("ItemID"));
                        bundle.putString("Description", itemHashMap.get("Description"));
                        bundle.putString("InStock", itemHashMap.get("InStock"));
                        bundle.putString("Manufacturer", itemHashMap.get("Manufacturer"));
                        bundle.putString("OperatingSystem", itemHashMap.get("OperatingSystem"));
                        bundle.putString("Price", itemHashMap.get("Price"));
                        bundle.putString("QtyOnHand", itemHashMap.get("QtyOnHand"));
                        bundle.putString("Carrier", itemHashMap.get("Carrier"));

                        Intent intent = new Intent();
                        intent.setClass(mContext, ProductDetails.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("No search results ")
                        .setCancelable(false)

                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(Killer);
    }

    private final BroadcastReceiver Killer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //At this point you should start the signInButton activity and finish this one
            //          System.out.println("..............searchlist");
            finish();
        }
    };

    class listadapter extends SimpleAdapter {

        listadapter(Context context,
                    List<? extends Map<String, ?>> data,
                    String[] from, int[] to) {
            super(context, data, R.layout.product_list_item, from, to);

            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            itemHashMap = searchResultsList.get(position);
            View itemView = super.getView(position, convertView, parent);

            TextView inStockView = itemView.findViewById(R.id.list_item_in_stock);

            if (itemHashMap.get("InStock").equals("Y") && !itemHashMap.get("QtyOnHand").equals("0")) {
                inStockView.setTextColor(getResources().getColor(R.color.mobile_labs_gray));
            }
            else {
                inStockView.setTextColor(getResources().getColor(R.color.mobile_labs_red));
            }

            ImageView osIcon = itemView.findViewById(R.id.list_item_os_icon);
            if (itemHashMap.get("OperatingSystem").startsWith("BlackBerry")) {
                Drawable icon = getResources().getDrawable(R.drawable.bb_icon);
                osIcon.setImageDrawable(icon);
            }
            if (itemHashMap.get("OperatingSystem").startsWith("Android")) {
                Drawable icon = getResources().getDrawable(R.drawable.and_icon);
                osIcon.setImageDrawable(icon);
            }
            if (itemHashMap.get("OperatingSystem").startsWith("iOS")) {//iOS
                Drawable icon = getResources().getDrawable(R.drawable.ios_icon);
                osIcon.setImageDrawable(icon);
            }
            if (itemHashMap.get("OperatingSystem").startsWith("Windows")) {//iOS
                Drawable icon = getResources().getDrawable(R.drawable.win_icon);
                osIcon.setImageDrawable(icon);
            }

            return super.getView(position, itemView, parent);
        }

    }

    private String[] splitString(String original) {

        Vector<String> nodes = new Vector<>();

        int index = original.indexOf(".");
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + ".".length());
            index = original.indexOf(".");
        }
        nodes.addElement(original);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = nodes.elementAt(loop);
                //System.out.println(result[loop]);
            }

        }
        return result;
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
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


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // a long press of the call key.
        // do our work, returning true to consume it.  by
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


    private ArrayList<HashMap<String, String>> XMLParser() {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("product.xml");

            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Items");

            for (int i = 0; i < nodeList.getLength(); i++) {
                HashMap<String, String> map = new HashMap<>();

                Node node = nodeList.item(i);

                Element phone = (Element) node;


                NodeList itemIDList = phone.getElementsByTagName("ItemID");
                Element itemIDElement = (Element) itemIDList.item(0);
                itemIDList = itemIDElement.getChildNodes();

                NodeList itemNameList = phone.getElementsByTagName("ItemName");
                Element itemNameElement = (Element) itemNameList.item(0);
                itemNameList = itemNameElement.getChildNodes();

                NodeList descriptionList = phone.getElementsByTagName("Description");
                Element descriptionElement = (Element) descriptionList.item(0);
                descriptionList = descriptionElement.getChildNodes();

                NodeList inStockList = phone.getElementsByTagName("InStock");
                Element inStockElement = (Element) inStockList.item(0);
                inStockList = inStockElement.getChildNodes();

                NodeList manufacturerList = phone.getElementsByTagName("Manufacturer");
                Element manufacturerElement = (Element) manufacturerList.item(0);
                manufacturerList = manufacturerElement.getChildNodes();

                NodeList operatingSystemList = phone.getElementsByTagName("OperatingSystem");
                Element operatingSystemElement = (Element) operatingSystemList.item(0);
                operatingSystemList = operatingSystemElement.getChildNodes();

                NodeList quantityList = phone.getElementsByTagName("QtyOnHand");
                Element quantityElement = (Element) quantityList.item(0);
                quantityList = quantityElement.getChildNodes();

                NodeList priceList = phone.getElementsByTagName("Price");
                Element priceElement = (Element) priceList.item(0);
                priceList = priceElement.getChildNodes();

                NodeList carrierList = phone.getElementsByTagName("Carrier");
                Element carrierElement = (Element) carrierList.item(0);
                carrierList = carrierElement.getChildNodes();

                String itemID = itemIDList.item(0).getNodeValue();
                String itemName = itemNameList.item(0).getNodeValue();
                String description = descriptionList.item(0).getNodeValue();
                String inStock = inStockList.item(0).getNodeValue();
                String manufacturer = manufacturerList.item(0).getNodeValue();
                String operatingSystem = operatingSystemList.item(0).getNodeValue();
                String quantity = quantityList.item(0).getNodeValue();
                String price = priceList.item(0).getNodeValue();
                String carrier = carrierList.item(0).getNodeValue();

                if (manufacturerCompare.equals("Any") || manufacturerCompare.equals(manufacturer)) {
                    if ((inStockValueCompare == 0 && inStock.equals("Y")) || (inStockValueCompare == 1 && inStock.equals("N")) || (inStockValueCompare == 2)) {
                        if ((isAndroidChecked && operatingSystem.contains("Android")) || (isIosChecked && operatingSystem.contains("iOS")) || (isBlackBerryChecked && operatingSystem.contains("BlackBerry")) || (isWindowsChecked && operatingSystem.contains("Windows"))) {
                            if ((itemName.toLowerCase().startsWith(searchValue.toLowerCase())) || (itemID.startsWith(searchValue))) {

                                map.put("ItemID", itemID);
                                map.put("ItemName", itemName);
                                map.put("Description", description);
                                map.put("InStock", inStock);
                                map.put("Manufacturer", manufacturer);
                                map.put("OperatingSystem", operatingSystem);
                                map.put("QtyOnHand", quantity);
                                map.put("Price", price);
                                map.put("Carrier", carrier);

                                arrayList.add(map);
                            }
                        }

                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }

        return arrayList;
    }

}
