package com.example.jg.mobilecmps121;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class shop_cart extends AppCompatActivity {
    private DBHelper _db = DBHelper.getInstance(this);
    CustomAdapter customAdapter;
    private ArrayList<Products> results;
    private double totalPrice;
    private String amount;
    private double origPrice = 0;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        setTitle("Shopping Cart");
        openDatabase();
        origPrice = totalPrice;
        final TextView subTotal = (TextView) findViewById(R.id.subtotal);
        Log.i("size", results.size() + "");
        subTotal.setText("Cart subtotal (" + results.size() + ") : $" + totalPrice);
        Log.d("count1", totalPrice + "");
        //TextView item = (TextView) customView.findViewById(R.id.shopItem);
        //ImageView image = (ImageView) customView.findViewById(R.id.shopImage);
        customAdapter = new CustomAdapter(this,R.layout.shop_cart_listitem,results) {
            public View getView(final int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View customView = inflater.inflate(R.layout.shop_cart_listitem, parent, false);
                final Products item = getItem(position);
                TextView itemText = (TextView) customView.findViewById(R.id.shopItem);

                final TextView priceItem = (TextView) customView.findViewById(R.id.priceItem);
                ImageView image = (ImageView) customView.findViewById(R.id.shopImage);
                Button deleteItem = (Button) customView.findViewById(R.id.removeItem);
                itemText.setText(item.get_productName());
                new ImageLoadTask(item.getImage(), image).execute();

                if(origPrice == 0) {
                    origPrice = item.getPrice();
                }

                priceItem.setText("$" + item.getPrice());
                Log.i("size", results.size() + "");
                //subTotal.setText("Cart subtotal ("+ results.size())  + ") : $" + totalPrice);
                Log.i("size2", ((item.getCount() - 1) + results.size()) + "");
                MaterialSpinner dropdown = (MaterialSpinner) customView.findViewById(R.id.spinner1);

                dropdown.setItems("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

                dropdown.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object mItem) {
                        amount = mItem.toString();
                        int amount2 = Integer.parseInt(amount);
                        if(amount != null) {
                            //Log.d("test","123");
                            if(!amount.equals("10+")) {

                                int oldCount = item.getCount();
                                item.setCount(amount2);
                                //if user selected a number less than their previous choice in drop down
                                if(oldCount > item.getCount()) {

                                    Log.d("test", results.size() + "");
                                    priceItem.setText("$" + item.getPrice() * amount2);
                                    totalPrice -= item.getPrice() * (oldCount - item.getCount());
                                    subTotal.setText("Cart subtotal (" + (item.getCount() - 1 + results.size())  + ") : $" + totalPrice);
                                } else {
                                    Log.d("test", totalPrice + " totalPrice");

                                    priceItem.setText("$" + item.getPrice() * item.getCount());
                                    if(amount2 != 1) {
                                        totalPrice += item.getPrice() * (Integer.parseInt(amount) - oldCount);
                                    }
                                    subTotal.setText("Cart subtotal (" + (item.getCount() - 1 + results.size())  + ") : $" + totalPrice);
                                }
                            }
                        }
                    }
                });

                //image.setImageResource(item.getImage());
                deleteItem.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        _db.deleteProduct(item.get_productName());
                        results.remove(item);
                        Log.d("test", (item.getPrice() * item.getCount()) + " :before delete");

                        totalPrice = totalPrice - (item.getPrice() * item.getCount());
                        subTotal.setText("Cart subtotal (" + results.size() + ") : $" + totalPrice);
                        customAdapter.notifyDataSetChanged();
                    }
                });


                return customView;
            }
        };
        ListView listView = (ListView) findViewById(R.id.shop_cart);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }
    private void openDatabase() {
        SQLiteDatabase db = _db.getReadableDatabase();
        String query = "SELECT * FROM " + _db.TABLE_PRODUCTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
        results = new ArrayList<Products>();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(_db.COLUMN_PRODUCTNAME)) != null) {
                results.add(new Products(c.getString(c.getColumnIndex(_db.COLUMN_PRODUCTNAME)),
                        c.getString(c.getColumnIndex(_db.COLUMN_IMAGE)),
                        c.getInt(c.getColumnIndex(_db.COLUMN_PRICE))));
                totalPrice += c.getInt(c.getColumnIndex(_db.COLUMN_PRICE));
            }
            c.moveToNext();
        }
        c.close();
        db.close();
    }
}

