package com.mobilelabsinc.PhoneLookup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class ProductDetails extends Activity {

    public Bundle bundle;
    public String ItemName;
    public String Description;
    public String Price;
    public String QtyOnHand;
    public String Carrier;

    public String ItemId;
    public String InStock;
    public String Manufacturer;
    public String OperatingSystem;

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
        setContentView(R.layout.productdetails1);

        /* killer broadcast */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(Killer, intentFilter);

        bundle = getIntent().getExtras();
        ItemName = bundle.getString("ItemName");
        Description = bundle.getString("Description");
        Price = bundle.getString("Price");
        QtyOnHand = bundle.getString("QtyOnHand");
        if (QtyOnHand.equals("0"))
            QtyOnHand = "Out of stock";
        Carrier = bundle.getString("Carrier");
        ItemId = bundle.getString("ItemID");
        InStock = bundle.getString("InStock");
        Manufacturer = bundle.getString("Manufacturer");
        OperatingSystem = bundle.getString("OperatingSystem");

        TextView itemName = (TextView) findViewById(R.id.productNameText1);
        TextView description = (TextView) findViewById(R.id.productDescriptionText1);
        TextView price = (TextView) findViewById(R.id.productPriceText1);
        TextView qtyOnHand = (TextView) findViewById(R.id.productQtyOnHandText1);
        // TextView carrier = (TextView)findViewById(R.id.productCarrierText);

        TextView itemID = (TextView) findViewById(R.id.itemIdText1);
        // TextView inStock = (TextView)findViewById(R.id.itemInStockText);
        TextView manufacturer = (TextView) findViewById(R.id.itemManufacturerText1);
        TextView operationSystem = (TextView) findViewById(R.id.itemOperatingSystemText1);

        itemID.setText(ItemId);
        // inStock.setText(InStock);
        manufacturer.setText(Manufacturer);
        operationSystem.setText(OperatingSystem);

        itemName.setText(ItemName);
        description.setText(Description);
        price.setText("$" + Price + "/Each");
        qtyOnHand.setText(QtyOnHand);
        // carrier.setText(Carrier);


        if (android.os.Build.VERSION.SDK_INT <= 10){
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header);
            TextView headerText = (TextView)findViewById(R.id.header_title);
            headerText.setText("Product Details");
        }
        else{
            setTitle("Product Details");
        }

        ImageView image = (ImageView) findViewById(R.id.osItem);

        Bitmap bitmap_bbOrg = BitmapFactory.decodeResource(getResources(), R.drawable.bb_item);
        Bitmap resizedBB = SearchList.getRoundedCornerBitmap(bitmap_bbOrg, 10);
        /* convert Bitmap to resource */
        BitmapDrawable bb = new BitmapDrawable(resizedBB);

        Bitmap bitmap_andOrg = BitmapFactory.decodeResource(getResources(), R.drawable.and_item);
        Bitmap resizedAndroid = SearchList.getRoundedCornerBitmap(bitmap_andOrg, 10);
        /* convert Bitmap to resource */
        BitmapDrawable and = new BitmapDrawable(resizedAndroid);

        Bitmap bitmap_iosOrg = BitmapFactory.decodeResource(getResources(), R.drawable.ios_item);
        Bitmap resizedIos = SearchList.getRoundedCornerBitmap(bitmap_iosOrg, 10);
        /* convert Bitmap to resource */
        BitmapDrawable ios = new BitmapDrawable(resizedIos);

        Bitmap bitmap_winOrg = BitmapFactory.decodeResource(getResources(), R.drawable.win_item);
        Bitmap resizedWindows = SearchList.getRoundedCornerBitmap(bitmap_winOrg, 10);
        /* convert Bitmap to resource */
        BitmapDrawable win = new BitmapDrawable(resizedWindows);

        if (OperatingSystem.startsWith("BlackBerry")) {
            image.setBackgroundDrawable(bb);
        }
        if (OperatingSystem.startsWith("Android")) {
            image.setBackgroundDrawable(and);
        }
        if (OperatingSystem.startsWith("iOS")) {// iOS
            image.setBackgroundDrawable(ios);
        }
        if (OperatingSystem.startsWith("Windows")) {// iOS
            image.setBackgroundDrawable(win);
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
            // At this point you should start the login activity and finish this
            // one
            finish();
        }
    };

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
