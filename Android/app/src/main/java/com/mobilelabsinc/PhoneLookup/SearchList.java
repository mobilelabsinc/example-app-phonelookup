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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchList extends ListActivity{

    private Context mContext;   
    private ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> hashmap;
    private List<String> list  = new ArrayList<String>();

    private List<String> sortedList  = new ArrayList<String>();

    ArrayList<HashMap<String,String>> list1 = new ArrayList<HashMap<String,String>>();
    private Bundle bundle;
    private String SearchbyText;
    private String spinnerValue;
    //private String radioValue;
    private int checkBoxValue;

    private String ioscheckBoxValue;
    private String androidcheckBoxValue;
    private String bbcheckBoxValue;
    private String windowscheckBoxValue;
    //private boolean radioInStock ;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT <= 10){
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        }
        mContext = this;
        /* killer broadcast*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(Killer, intentFilter);



        bundle = getIntent().getExtras();

        SearchbyText = bundle.getString("SearchbyText");
        spinnerValue = bundle.getString("spinnerValue");
        androidcheckBoxValue = bundle.getString("androidcheckBoxValue");
        windowscheckBoxValue = bundle.getString("windowscheckBoxValue");        
        ioscheckBoxValue = bundle.getString("ioscheckBoxValue");
        bbcheckBoxValue = bundle.getString("bbcheckBoxValue");
        checkBoxValue = bundle.getInt("radioInStock");

        if(SearchbyText.compareTo("*")==0){         
            SearchbyText = "";          
        }

        arraylist = XMLParser();
        //here to sort the sortedList data
        Collections.sort(sortedList);
        //      for(int j=0;j<sortedList.size();j++){
        //          Collections.sort(sortedList);
        //          System.out.println(sortedList.get(j));
        //      }
        for(int j=0;j<sortedList.size();j++){
            for(int i=0;i<arraylist.size();i++){
                hashmap =(HashMap<String, String>) arraylist.get(i);    
                if(sortedList.get(j).equals(hashmap.get("ItemName").toString()))
                    if(checkBoxValue==1){
                        if(spinnerValue.equals("Any")){
                            if((hashmap.get("ItemName").toString().toLowerCase().startsWith(SearchbyText.toLowerCase())|hashmap.get("ItemID").toString().startsWith(SearchbyText))&&Integer.parseInt(hashmap.get("QtyOnHand").toString())==0&&(hashmap.get("OperatingSystem").toString().startsWith(ioscheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(androidcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(bbcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(windowscheckBoxValue))){//QtyOnHand
                                list.add((String)hashmap.get("ItemName"));                          
                                HashMap<String,String> item = new HashMap<String,String>();
                                item.put( "line1",(String)hashmap.get("ItemName"));
                                item.put( "line2",(String)hashmap.get("ItemID") );
                                item.put( "line3","Carrier:"+(String)hashmap.get("Carrier") );                          
                                item.put( "line4","[Out Of Stock]");

                                if(((String)hashmap.get("Price")).indexOf(".")!=-1){
                                    String[] priceValue =  splitString(".",(String)hashmap.get("Price"));
                                    item.put( "line5", priceValue[0]);
                                    item.put( "line6",priceValue[1]+"\nEA");
                                }
                                else{
                                    item.put( "line5", (String)hashmap.get("Price"));
                                    item.put( "line6","");
                                }
                                list1.add( item );
                            }}else
                                if((hashmap.get("ItemName").toString().toLowerCase().startsWith(SearchbyText.toLowerCase())|hashmap.get("ItemID").toString().startsWith(SearchbyText))&&hashmap.get("Manufacturer").toString().equals(spinnerValue)&&Integer.parseInt(hashmap.get("QtyOnHand").toString())==0&&(hashmap.get("OperatingSystem").toString().startsWith(ioscheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(androidcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(bbcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(windowscheckBoxValue))){//QtyOnHand

                                    //if(hashmap.get("ItemName").toString().startsWith(SearchbyText)&&hashmap.get("Manufacturer").toString().equals(spinnerValue)&&hashmap.get("OperatingSystem").toString().startsWith(radioValue)){//QtyOnHand
                                    list.add((String)hashmap.get("ItemName"));  

                                    HashMap<String,String> item = new HashMap<String,String>();
                                    item.put( "line1",(String)hashmap.get("ItemName"));
                                    item.put( "line2",(String)hashmap.get("ItemID") );
                                    item.put( "line3","Carrier:"+(String)hashmap.get("Carrier") );                          
                                    item.put( "line4","[Out Of Stock]");

                                    if(((String)hashmap.get("Price")).indexOf(".")!=-1){
                                        String[] priceValue =  splitString(".",(String)hashmap.get("Price"));
                                        item.put( "line5", priceValue[0]);
                                        item.put( "line6",priceValue[1]+"\nEA");
                                    }
                                    else{
                                        item.put( "line5", (String)hashmap.get("Price"));
                                        item.put( "line6","");
                                    }
                                    list1.add( item );
                                }
                    }else if(checkBoxValue==0){
                        if(spinnerValue.equals("Any")){
                            if((hashmap.get("ItemName").toString().toLowerCase().startsWith(SearchbyText.toLowerCase())|hashmap.get("ItemID").toString().startsWith(SearchbyText))&&Integer.parseInt(hashmap.get("QtyOnHand").toString())>0&&(hashmap.get("OperatingSystem").toString().startsWith(ioscheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(androidcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(bbcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(windowscheckBoxValue))){//QtyOnHand

                                list.add((String)hashmap.get("ItemName"));  

                                HashMap<String,String> item = new HashMap<String,String>();
                                item.put( "line1",(String)hashmap.get("ItemName"));
                                item.put( "line2",(String)hashmap.get("ItemID") );
                                item.put( "line3","Carrier:"+(String)hashmap.get("Carrier") );                          
                                item.put( "line4","[In Stock]");

                                if(((String)hashmap.get("Price")).indexOf(".")!=-1){
                                    String[] priceValue =  splitString(".",(String)hashmap.get("Price"));
                                    item.put( "line5", priceValue[0]);
                                    item.put( "line6",priceValue[1]+"\nEA");
                                }
                                else{
                                    item.put( "line5", (String)hashmap.get("Price"));
                                    item.put( "line6","");
                                }
                                list1.add( item );
                            }
                        }else{
                            if((hashmap.get("ItemName").toString().toLowerCase().startsWith(SearchbyText.toLowerCase())|hashmap.get("ItemID").toString().startsWith(SearchbyText))&&hashmap.get("Manufacturer").toString().equals(spinnerValue)&&Integer.parseInt(hashmap.get("QtyOnHand").toString())>0&&(hashmap.get("OperatingSystem").toString().startsWith(ioscheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(androidcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(bbcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(windowscheckBoxValue))){//QtyOnHand

                                list.add((String)hashmap.get("ItemName"));  

                                HashMap<String,String> item = new HashMap<String,String>();
                                item.put( "line1",(String)hashmap.get("ItemName"));
                                item.put( "line2",(String)hashmap.get("ItemID") );
                                item.put( "line3","Carrier:"+(String)hashmap.get("Carrier") );                          
                                item.put( "line4","[In Stock]");

                                if(((String)hashmap.get("Price")).indexOf(".")!=-1){
                                    String[] priceValue =  splitString(".",(String)hashmap.get("Price"));
                                    item.put( "line5", priceValue[0]);
                                    item.put( "line6",priceValue[1]+"\nEA");
                                }
                                else{
                                    item.put( "line5", (String)hashmap.get("Price"));
                                    item.put( "line6","");
                                }
                                list1.add( item );
                            }
                        }
                    }else if(checkBoxValue==2){
                        if(spinnerValue.equals("Any")){
                            if((hashmap.get("ItemName").toString().toLowerCase().startsWith(SearchbyText.toLowerCase())|hashmap.get("ItemID").toString().startsWith(SearchbyText))&&(hashmap.get("OperatingSystem").toString().startsWith(ioscheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(androidcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(bbcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(windowscheckBoxValue))){//QtyOnHand

                                list.add((String)hashmap.get("ItemName"));  

                                HashMap<String,String> item = new HashMap<String,String>();
                                item.put( "line1",(String)hashmap.get("ItemName"));
                                item.put( "line2",(String)hashmap.get("ItemID") );
                                item.put( "line3","Carrier:"+(String)hashmap.get("Carrier") );    
                                if(Integer.parseInt(hashmap.get("QtyOnHand").toString())>0)
                                    item.put( "line4","[In Stock]");
                                else if(Integer.parseInt(hashmap.get("QtyOnHand").toString())==0)
                                    item.put( "line4","[Out Of Stock]");

                                if(((String)hashmap.get("Price")).indexOf(".")!=-1){
                                    String[] priceValue =  splitString(".",(String)hashmap.get("Price"));
                                    item.put( "line5", priceValue[0]);
                                    item.put( "line6",priceValue[1]+"\nEA");
                                }
                                else{
                                    item.put( "line5", (String)hashmap.get("Price"));
                                    item.put( "line6","");
                                }
                                list1.add( item );
                            }
                        }else{
                            if((hashmap.get("ItemName").toString().toLowerCase().startsWith(SearchbyText.toLowerCase())|hashmap.get("ItemID").toString().startsWith(SearchbyText))&&hashmap.get("Manufacturer").toString().equals(spinnerValue)&&(hashmap.get("OperatingSystem").toString().startsWith(ioscheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(androidcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(bbcheckBoxValue)||hashmap.get("OperatingSystem").toString().startsWith(windowscheckBoxValue))){//QtyOnHand

                                list.add((String)hashmap.get("ItemName"));  

                                HashMap<String,String> item = new HashMap<String,String>();

                                item.put( "line1",(String)hashmap.get("ItemName"));
                                item.put( "line2",(String)hashmap.get("ItemID") );
                                item.put( "line3","Carrier:"+(String)hashmap.get("Carrier") );    
                                if(Integer.parseInt(hashmap.get("QtyOnHand").toString())>0)
                                    item.put( "line4","[In Stock]");
                                else if(Integer.parseInt(hashmap.get("QtyOnHand").toString())==0)
                                    item.put( "line4","[Out Of Stock]");

                                if(((String)hashmap.get("Price")).indexOf(".")!=-1){
                                    String[] priceValue =  splitString(".",(String)hashmap.get("Price"));
                                    item.put( "line5", priceValue[0]);
                                    item.put( "line6",priceValue[1]+"\nEA");
                                }
                                else{
                                    item.put( "line5", (String)hashmap.get("Price"));
                                    item.put( "line6","");
                                }
                                list1.add( item );
                            }
                        }
                    }


            }
        }
        if(list.size()!=0){


            //          LayoutInflater inflater = (LayoutInflater)getSystemService
            //            (Context.LAYOUT_INFLATER_SERVICE);
            //          View view = inflater.inflate(R.layout.header, null, false);

            ListView lv = getListView();        
            lv.setTextFilterEnabled(true);
            lv.setCacheColorHint(Color.TRANSPARENT);
            //          lv.addHeaderView(view);

            setListAdapter(new listadapter(this,list1,R.layout.main_item_two_line_row,
                    new String[] { "line1","line2","line3","line4","line5","line6" },new int[] { R.id.text1, R.id.text2 , R.id.text3,R.id.text4,R.id.text5,R.id.text6}));

            if (android.os.Build.VERSION.SDK_INT <= 10){
                getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header);
                TextView headerText =(TextView)findViewById(R.id.header_title);
                headerText.setText("Results");  
            }
            else{
                setTitle("Results");
            }
            /*setListAdapter(new SimpleAdapter( 
                    this, 
                    list1,
                    R.layout.main_item_two_line_row,
                    new String[] { "line1","line2","line3" },
                    new int[] { R.id.text1, R.id.text2 , R.id.text3})); */
            //      setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));//R.layout.list_item





            int[] colors = {0, 0xFFFF0000, 0}; //0xFFFF0000 red for the example //0xFF000000
            lv.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));

            lv.setDividerHeight(2);
            lv.setOnItemClickListener(new OnItemClickListener() {           
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                        long arg3) {                             
                    for(int i=0;i<arraylist.size();i++){
                        hashmap =(HashMap<String, String>) arraylist.get(i);               
                        if(list.get(arg2).equals((String) hashmap.get("ItemName"))){
                            Bundle bundle = new Bundle();              
                            bundle.putString("ItemName",(String) hashmap.get("ItemName"));  
                            bundle.putString("ItemID",(String) hashmap.get("ItemID"));
                            bundle.putString("Description",(String) hashmap.get("Description"));  
                            bundle.putString("InStock",(String) hashmap.get("InStock"));
                            bundle.putString("Manufacturer",(String) hashmap.get("Manufacturer"));
                            bundle.putString("OperatingSystem",(String) hashmap.get("OperatingSystem"));
                            bundle.putString("Price",(String) hashmap.get("Price"));
                            bundle.putString("QtyOnHand",(String) hashmap.get("QtyOnHand"));
                            bundle.putString("Carrier",(String) hashmap.get("Carrier"));
                            Intent intent = new Intent();
                            intent.setClass(mContext, ProductDetails.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                }
            });

        }
        else{
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
    public BroadcastReceiver Killer= new BroadcastReceiver() {
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

            View v =  super.getView(position, convertView, parent);
            TextView tv = (TextView)v.findViewById(R.id.text4);
            Bitmap bitmap_bbOrg = BitmapFactory.decodeResource(getResources(),R.drawable.bb_icon);
            Bitmap resizedBB = getRoundedCornerBitmap(bitmap_bbOrg,10);
            /*convert Bitmap to resource */
            BitmapDrawable bb = new BitmapDrawable(resizedBB);

            Bitmap bitmap_andOrg = BitmapFactory.decodeResource(getResources(),R.drawable.and_icon);
            Bitmap resizedAndroid = getRoundedCornerBitmap(bitmap_andOrg,10);
            /*convert Bitmap to resource */
            BitmapDrawable and = new BitmapDrawable(resizedAndroid);

            Bitmap bitmap_iosOrg = BitmapFactory.decodeResource(getResources(),R.drawable.ios_icon);
            Bitmap resizedIos = getRoundedCornerBitmap(bitmap_iosOrg,10);
            /*convert Bitmap to resource */
            BitmapDrawable ios = new BitmapDrawable(resizedIos);

            Bitmap bitmap_winOrg = BitmapFactory.decodeResource(getResources(),R.drawable.win_icon);
            Bitmap resizedWindows = getRoundedCornerBitmap(bitmap_winOrg,10);
            /*convert Bitmap to resource */
            BitmapDrawable win = new BitmapDrawable(resizedWindows);



            ImageView image = (ImageView)v.findViewById(R.id.osIcon);

            for(int i=0;i<arraylist.size();i++){
                hashmap =(HashMap<String, String>) arraylist.get(i);                 
                if(list.get(position).equals((String) hashmap.get("ItemName"))){              
                    if(!hashmap.get("QtyOnHand").equals("0")){                      
                        tv.setTextColor(Color.GREEN);
                    }
                    else{
                        tv.setTextColor(Color.RED);
                    }
                    if(hashmap.get("OperatingSystem").startsWith("BlackBerry")){
                        //                    image.setBackgroundResource(R.drawable.bb_icon);
                        image.setBackgroundDrawable(bb);
                    }
                    if(hashmap.get("OperatingSystem").startsWith("Android")){
                        //                    image.setBackgroundResource(R.drawable.and_icon);
                        image.setBackgroundDrawable(and);
                    }
                    if(hashmap.get("OperatingSystem").startsWith("iOS")){//iOS
                        //                    image.setBackgroundResource(R.drawable.ios_icon);
                        image.setBackgroundDrawable(ios);
                    }
                    if(hashmap.get("OperatingSystem").startsWith("Windows")){//iOS
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

    public String[] splitString(String separator, String original){

        Vector<String> nodes = new Vector<String>();                

        int index = original.indexOf(separator);
        while(index>=0) {
            nodes.addElement( original.substring(0, index) );
            original = original.substring(index+separator.length());
            index = original.indexOf(separator);
        }
        nodes.addElement( original );

        // Create splitted string array
        String[] result = new String[ nodes.size() ];
        if( nodes.size()>0 ) 
        {
            for(int loop=0; loop<nodes.size(); loop++)
            {
                result[loop] = (String)nodes.elementAt(loop);
                //System.out.println(result[loop]);
            }

        }
        return result;
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {        
        MenuInflater inflater = getMenuInflater(); 
        inflater.inflate(R.menu.menu_view, menu);
        return true;       
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        // TODO Auto-generated method stub
        switch(item.getItemId()){
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

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            arraylist.clear();          
            hashmap.clear();
            list.clear();
            //finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
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


    public ArrayList<HashMap<String, String>> XMLParser(){      
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();    
        try {
            //URL url = new URL("http://www.androidpeople.com/wp-content/uploads/2010/06/example.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("product.xml");
            Document doc=db.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Items");
            //          System.out.println("nodelist length..."+nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();

                Node node = nodeList.item(i);

                Element fstElmnt = (Element) node;

                NodeList nameList = fstElmnt.getElementsByTagName("ItemID");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();             
                map.put("ItemID", ((Node) nameList.item(0)).getNodeValue());
                //              System.out.println("....productname........"+map.get("productname"));                       

                NodeList imageList = fstElmnt.getElementsByTagName("ItemName");
                Element imageListElement = (Element) imageList.item(0);
                imageList = imageListElement.getChildNodes();
                map.put("ItemName",((Node) imageList.item(0)).getNodeValue() );
                //              System.out.println("....imageurl........"+map.get("imageurl"));
                sortedList.add(((Node) imageList.item(0)).getNodeValue());

                NodeList producturlList = fstElmnt.getElementsByTagName("Description");
                Element producturlListElement = (Element) producturlList.item(0);
                producturlList = producturlListElement.getChildNodes();
                map.put("Description",((Node) producturlList.item(0)).getNodeValue());
                //              System.out.println("....producturl........"+map.get("producturl"));

                NodeList websiteList = fstElmnt.getElementsByTagName("InStock");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();           
                map.put("InStock", ((Node) websiteList.item(0)).getNodeValue());
                //              System.out.println("....price........"+map.get("price"));


                NodeList currencyList = fstElmnt.getElementsByTagName("Manufacturer");
                Element currencyListElement = (Element) currencyList.item(0);
                currencyList = currencyListElement.getChildNodes();
                map.put("Manufacturer",((Node) currencyList.item(0)).getNodeValue() );
                //              System.out.println("....currency........"+map.get("currency"));

                NodeList storenameList = fstElmnt.getElementsByTagName("OperatingSystem");
                Element storenameListElement = (Element) storenameList.item(0);
                storenameList = storenameListElement.getChildNodes();
                map.put("OperatingSystem",((Node) storenameList.item(0)).getNodeValue() );
                //              System.out.println("....storename........"+map.get("storename"));              


                NodeList storenameList1 = fstElmnt.getElementsByTagName("QtyOnHand");
                Element storenameListElement1 = (Element) storenameList1.item(0);
                storenameList1 = storenameListElement1.getChildNodes();
                map.put("QtyOnHand",((Node) storenameList1.item(0)).getNodeValue() );
                //              System.out.println("....storename........"+map.get("storename"));              


                NodeList storenameList2 = fstElmnt.getElementsByTagName("Price");
                Element storenameListElement2 = (Element) storenameList2.item(0);
                storenameList2 = storenameListElement2.getChildNodes();
                map.put("Price",((Node) storenameList2.item(0)).getNodeValue() );
                //              System.out.println("....storename........"+map.get("storename"));              



                NodeList storenameList3 = fstElmnt.getElementsByTagName("Carrier");
                Element storenameListElement3 = (Element) storenameList3.item(0);
                storenameList3 = storenameListElement3.getChildNodes();
                map.put("Carrier",((Node) storenameList3.item(0)).getNodeValue() );
                //              System.out.println("....storename........"+map.get("storename"));

                arrayList.add(map); 


            }
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }

        return arrayList;
    }

}
