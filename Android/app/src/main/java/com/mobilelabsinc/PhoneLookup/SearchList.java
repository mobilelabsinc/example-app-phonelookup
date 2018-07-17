package com.mobilelabsinc.PhoneLookup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
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

public class SearchList extends ListActivity {

    private Context mContext;
    private ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> itemHashMap;
    private List<String> list = new ArrayList<String>();

    private List<String> sortedList = new ArrayList<String>();

    ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();

    private String searchValue;
    private String manufacturerCompare;
    private int inStockValueCompare;
    private Boolean isAndroidChecked;
    private Boolean isWindowsChecked;
    private Boolean isIosChecked;
    private Boolean isBlackBerryChecked;


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

        /* killer broadcast*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(Killer, intentFilter);

        Bundle bundle = getIntent().getExtras();

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

        String stockValue;


        arraylist = XMLParser();


        //here to sort the sortedList data
        Collections.sort(sortedList);

        for (int j = 0; j < sortedList.size(); j++) {
            for (int i = 0; i < arraylist.size(); i++) {
                itemHashMap = arraylist.get(i);

                if (sortedList.get(j).equals(itemHashMap.get("ItemName"))) {
                    stockValue = (itemHashMap.get("InStock").equals("Y")) ? "[In Stock]" : "[Out of Stock]";

                    list.add(itemHashMap.get("ItemName"));
                    HashMap<String, String> item = new HashMap<String, String>();

                    item.put("line1", itemHashMap.get("ItemName"));
                    item.put("line2", itemHashMap.get("ItemID"));
                    item.put("line3", "Carrier:" + itemHashMap.get("Carrier"));
                    item.put("line4", stockValue);

                    if (((String) itemHashMap.get("Price")).contains(".")) {
                        String[] priceValue = splitString(".", itemHashMap.get("Price"));
                        item.put("line5", priceValue[0]);
                        item.put("line6", priceValue[1] + "\nEA");
                    } else {
                        item.put("line5", itemHashMap.get("Price"));
                        item.put("line6", "");
                    }
                    list1.add(item);

                }
            }
        }
            if (list.size() != 0) {

            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setCacheColorHint(Color.TRANSPARENT);

            setListAdapter(new listadapter(this, list1, R.layout.main_item_two_line_row,
                    new String[]{"line1", "line2", "line3", "line4", "line5", "line6"}, new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6}));

            setTitle("Results");

            int[] colors = {0, 0xFFFF0000, 0}; //0xFFFF0000 red for the example //0xFF000000
            lv.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));

            lv.setDividerHeight(2);
            lv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    for (int i = 0; i < arraylist.size(); i++) {
                        itemHashMap = (HashMap<String, String>) arraylist.get(i);
                        if (list.get(arg2).equals((String) itemHashMap.get("ItemName"))) {
                            Bundle bundle = new Bundle();
                            bundle.putString("ItemName", (String) itemHashMap.get("ItemName"));
                            bundle.putString("ItemID", (String) itemHashMap.get("ItemID"));
                            bundle.putString("Description", (String) itemHashMap.get("Description"));
                            bundle.putString("InStock", (String) itemHashMap.get("InStock"));
                            bundle.putString("Manufacturer", (String) itemHashMap.get("Manufacturer"));
                            bundle.putString("OperatingSystem", (String) itemHashMap.get("OperatingSystem"));
                            bundle.putString("Price", (String) itemHashMap.get("Price"));
                            bundle.putString("QtyOnHand", (String) itemHashMap.get("QtyOnHand"));
                            bundle.putString("Carrier", (String) itemHashMap.get("Carrier"));
                            Intent intent = new Intent();
                            intent.setClass(mContext, ProductDetails.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                }
            });

        }
        else {
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(Killer);
    }

    public BroadcastReceiver Killer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //At this point you should start the login activity and finish this one
            //          System.out.println("..............searchlist");
            finish();
        }
    };

    class listadapter extends SimpleAdapter {

        public listadapter(Context context,
                           List<? extends Map<String, ?>> data, int resource,
                           String[] from, int[] to) {
            super(context, data, resource, from, to);

            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View v = super.getView(position, convertView, parent);
            TextView tv = (TextView) v.findViewById(R.id.text4);
            Bitmap bitmap_bbOrg = BitmapFactory.decodeResource(getResources(), R.drawable.bb_icon);
            Bitmap resizedBB = getRoundedCornerBitmap(bitmap_bbOrg, 10);
            /*convert Bitmap to resource */
            BitmapDrawable bb = new BitmapDrawable(resizedBB);

            Bitmap bitmap_andOrg = BitmapFactory.decodeResource(getResources(), R.drawable.and_icon);
            Bitmap resizedAndroid = getRoundedCornerBitmap(bitmap_andOrg, 10);
            /*convert Bitmap to resource */
            BitmapDrawable and = new BitmapDrawable(resizedAndroid);

            Bitmap bitmap_iosOrg = BitmapFactory.decodeResource(getResources(), R.drawable.ios_icon);
            Bitmap resizedIos = getRoundedCornerBitmap(bitmap_iosOrg, 10);
            /*convert Bitmap to resource */
            BitmapDrawable ios = new BitmapDrawable(resizedIos);

            Bitmap bitmap_winOrg = BitmapFactory.decodeResource(getResources(), R.drawable.win_icon);
            Bitmap resizedWindows = getRoundedCornerBitmap(bitmap_winOrg, 10);
            /*convert Bitmap to resource */
            BitmapDrawable win = new BitmapDrawable(resizedWindows);


            ImageView image = (ImageView) v.findViewById(R.id.osIcon);

            for (int i = 0; i < arraylist.size(); i++) {
                itemHashMap = (HashMap<String, String>) arraylist.get(i);
                if (list.get(position).equals((String) itemHashMap.get("ItemName"))) {
                    if (!itemHashMap.get("QtyOnHand").equals("0")) {
                        tv.setTextColor(Color.GREEN);
                    } else {
                        tv.setTextColor(Color.RED);
                    }
                    if (itemHashMap.get("OperatingSystem").startsWith("BlackBerry")) {
                        //                    image.setBackgroundResource(R.drawable.bb_icon);
                        image.setBackgroundDrawable(bb);
                    }
                    if (itemHashMap.get("OperatingSystem").startsWith("Android")) {
                        //                    image.setBackgroundResource(R.drawable.and_icon);
                        image.setBackgroundDrawable(and);
                    }
                    if (itemHashMap.get("OperatingSystem").startsWith("iOS")) {//iOS
                        //                    image.setBackgroundResource(R.drawable.ios_icon);
                        image.setBackgroundDrawable(ios);
                    }
                    if (itemHashMap.get("OperatingSystem").startsWith("Windows")) {//iOS
                        //                    image.setBackgroundResource(R.drawable.win_icon);
                        image.setBackgroundDrawable(win);
                    }
                }
            }


            return super.getView(position, v, parent);
        }

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public String[] splitString(String separator, String original) {

        Vector<String> nodes = new Vector<String>();

        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        nodes.addElement(original);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // a long press of the call key.
            // do our work, returning true to consume it.  by
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


    public ArrayList<HashMap<String, String>> XMLParser() {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("product.xml");

            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Items");

            for (int i = 0; i < nodeList.getLength(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();

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
                        if ((isAndroidChecked && operatingSystem.contains("Android")) || (isIosChecked && operatingSystem.contains("iOS")) || (isBlackBerryChecked && operatingSystem.contains("BlackBerry")) || (isWindowsChecked && operatingSystem.contains("Windows")))
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

                                sortedList.add(itemName);
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
