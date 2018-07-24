package com.mobilelabsinc.PhoneLookup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Currency;
import java.util.Locale;


public class ProductDetails extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product_details);
        setTitle("Product Details");

        mContext = this;

        /* killer broadcast */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(Killer, intentFilter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String bundleOS = bundle.getString("OperatingSystem");

            TextView itemName = findViewById(R.id.product_details_item_name);
            TextView description = findViewById(R.id.product_details_description);
            TextView price = findViewById(R.id.product_details_price);
            TextView qtyOnHand = findViewById(R.id.product_details_online_quantity);
            TextView itemID = findViewById(R.id.product_details_item_id);
            TextView manufacturer = findViewById(R.id.product_details_manufacturer);
            TextView operationSystem = findViewById(R.id.product_details_operating_system);

            //Set Text Values
            itemID.setText(bundle.getString("ItemID"));
            manufacturer.setText(bundle.getString("Manufacturer"));
            operationSystem.setText(bundleOS);
            itemName.setText(bundle.getString("ItemName"));
            description.setText(bundle.getString("Description"));

            //Set the price
            Currency currency = Currency.getInstance(Locale.getDefault());
            String symbol = currency.getSymbol();
            price.setText(getString(R.string.product_details_price_units, symbol, bundle.getString("Price")));
            qtyOnHand.setText(bundle.getString("QtyOnHand"));

            //Set the operating system image
            ImageView operatingSystemImageView = findViewById(R.id.product_details_icon);
            if (bundleOS != null && bundleOS.startsWith("BlackBerry")) {
                Drawable icon = getResources().getDrawable(R.drawable.bb_item);
                operatingSystemImageView.setImageDrawable(icon);
            }
            if (bundleOS != null && bundleOS.startsWith("Android")) {
                Drawable icon = getResources().getDrawable(R.drawable.and_item);
                operatingSystemImageView.setImageDrawable(icon);
            }
            if (bundleOS != null && bundleOS.startsWith("iOS")) {
                Drawable icon = getResources().getDrawable(R.drawable.ios_item);
                operatingSystemImageView.setImageDrawable(icon);
            }
            if (bundleOS != null && bundleOS.startsWith("Windows")) {
                Drawable icon = getResources().getDrawable(R.drawable.win_item);
                operatingSystemImageView.setImageDrawable(icon);
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
            // At this point you should start the signInButton activity and finish this
            // one
            finish();
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
