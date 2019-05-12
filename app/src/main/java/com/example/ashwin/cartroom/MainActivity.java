package com.example.ashwin.cartroom;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String _item_Name;
    private int _quantity;
    private double _weight;
    private String _weight_measurement;
    private double _price;
    private UpdatedRecyclerAdapter listadapter;
    private SQLiteDatabase db;
    private Cursor cursor;
    private RecyclerView.LayoutManager layoutManager;
    private List<Items> data_items;
    private RecyclerView Rv;


    // Two phases for completing the program.
    // First use user input to create and update the table(Database).
    //Secondly use Cursor Adapter to retrieve data from table and display it on user screen,
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spinner = (Spinner) findViewById(R.id.weight_measurement);
        final DataBaseHandler handler = new DataBaseHandler(this);

        //Creating ArrayAdapter for spinner.
        //Use adapter to link spinner data and default spinner layout
        //Store spinner data by creating an array data in app -> res -> values.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weight_units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Link adapter to spinner.
        spinner.setAdapter(adapter);
        //Find Override methods outside onCreate of Main Activity.
        spinner.setOnItemSelectedListener(this);

        // Used final with View id's since they are used from inner class
        final EditText item_Name = (EditText) findViewById(R.id.item_Name);
        final EditText quantity = (EditText) findViewById(R.id.quantity);
        final EditText price = (EditText) findViewById(R.id.price);
        final EditText weight = (EditText) findViewById(R.id.weight);
        final TextView Total = (TextView) findViewById(R.id.Total);

        //.setHint for user experience
        Total.setHint("Toatl Amount in (Rs)");
        item_Name.setHint("Item Name");
        weight.setHint("Weight");
        quantity.setHint("Quantity");
        price.setHint("Price/Quantity");


        Rv = (RecyclerView) findViewById(R.id.list_item);

        //RecyclerView requires a Layout Manager
        layoutManager = new LinearLayoutManager(this);
        Rv.setLayoutManager(layoutManager);

        //Custom RecyclerView Adapter
        listadapter = new UpdatedRecyclerAdapter(this);

        //Linking RecyclerView with RecyclerView Adapter. Adapter will take care of updating the views.

        Rv.setAdapter(listadapter);


        final Button Calculate = (Button) findViewById(R.id.Calculate);
        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Total.setText("" + listadapter.Calculate());

            }
        });

        Button add_item = (Button) findViewById(R.id.Add_item);
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    _item_Name = item_Name.getText().toString();
                    _quantity = Integer.parseInt(quantity.getText().toString());

                    try {

                        _price = Double.parseDouble(price.getText().toString());

                    } catch (Exception e1) {

                        _price = 0;

                    }

                    try {

                        _weight = Double.parseDouble(weight.getText().toString());

                    } catch (Exception e2) {

                        _weight = 0;
                        Log.d("Error Message", e2.getMessage() + _weight);

                    }

                    handler.Additem(_item_Name, _quantity, _weight, _weight_measurement, _price); //Inserting user input to table row-by-row.

                    //Clearing EditText after inserting a row to table
                    item_Name.setText("");
                    quantity.setText("");
                    weight.setText("");
                    price.setText("");
                    spinner.setSelection(0);

                } catch (Exception e) {

                    Log.d("Entry values ", e.getMessage() + " " + _item_Name + " " + _quantity);
                    Toast.makeText(getApplicationContext(), "Invalid Entry", Toast.LENGTH_SHORT).show();
                    //In case any database operation fails
                }


                listadapter.LoadNewData();
                listadapter.notifyDataSetChanged(); //Notifying list adapter that data set is changed
                Calculate.callOnClick();

            }


        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                //Feature allowing user to delete a row
                int position = viewHolder.getAdapterPosition() + 1;
                Log.d("Row number", "Selected row is " + position);

                db = handler.getReadableDatabase();
                db.delete("items_Table", "_id" + "=?", new String[]{String.valueOf(position)}); //Deleting a row based on Adapter position

                //Deleting a row requires updating database and hence the View.
                handler.UpdateDatabase();
                db.close();


                listadapter.LoadNewData();
                listadapter.notifyDataSetChanged();

                Calculate.callOnClick();
            }
        });
        itemTouchHelper.attachToRecyclerView(Rv);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {

        //Override methods for retrieving spinner data
        _weight_measurement = String.valueOf(parent.getItemAtPosition(pos)); // Better to use String.valueOf() rather than .toString().

        if (!_weight_measurement.equals("Select")) {

            Log.d("pos val", "Item Selected is " + parent.getItemAtPosition(pos));
            // Toast.makeText(parent.getContext(), "Item selected is " + _weight_measurement, Toast.LENGTH_SHORT).show();

        } else

            _weight_measurement = "";

    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        _weight_measurement = null;
    }
}
