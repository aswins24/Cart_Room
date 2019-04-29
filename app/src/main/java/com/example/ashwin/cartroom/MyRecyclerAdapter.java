package com.example.ashwin.cartroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ashwin on 4/23/2019.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    CursorAdapter mCursorAdapter;
    Context mContext;
    DataBaseHandler handler;

    public MyRecyclerAdapter(Context context, Cursor c) {

        mContext = context;

        // Using Recycler Adapter instead of List Adapter to implement "ItemTouchHelper -> onSwiped" method in MainActivity
        //RecyclerAdapter doesn't have a method to control cursor hence we use CursorAdapter as an object of Recycler Adapter
        //In case onSwiped method is not required use List Adapter with custom Cursor Adapter class
        mCursorAdapter = new CursorAdapter(mContext, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.item_container, parent, false);
                //Inflate the view layout decided to be used in Recycler View
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                //Binding data to the view.
                // WHEN USING CUSTOM ADAPTER MAKE SURE THERE IS A COLUMN WITH _id. HENCE IT WILL THROW ERROR.
                TextView item_no = view.findViewById(R.id.Item_id);
                TextView _item_Name = view.findViewById(R.id.Item_Name);
                TextView _Quantity = view.findViewById(R.id.Quantity);
                TextView _Weight = view.findViewById(R.id.Weight);
                EditText _Price = view.findViewById(R.id.Price);

                //The app allows user to change or add price after inserted into table, hence we need textchangedlistner.
                final int position = cursor.getInt(cursor.getColumnIndexOrThrow("_id")); //get the posiotin outside the listner because inside the loop we will have to define
                //cursor as final which will cause error for other operations.
                _Price.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String new_price = charSequence.toString();
                        handler = new DataBaseHandler(mContext);
                        SQLiteDatabase db = handler.getReadableDatabase();
                        ContentValues value = new ContentValues();
                        try {
                            value.put("Price", Double.parseDouble(new_price));
                        } catch (Exception e) {
                            Log.d("RecyclerEditText", e.getMessage());
                            value.put("Price", 0.0);
                        }
                        db.update("Items_Table", value, "_id" + "=?", new String[]{String.valueOf(position)});
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {


                    }
                });


                int _item_no = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String Item_name = cursor.getString(cursor.getColumnIndexOrThrow("Item_name"));
                int Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                String Weight = cursor.getString(cursor.getColumnIndexOrThrow("Weight"));
                //String Weight_Measurement = cursor.getString(cursor.getColumnIndexOrThrow("Price"));
                double Price = cursor.getDouble(cursor.getColumnIndexOrThrow("Price"));

                item_no.setText(String.valueOf(_item_no));
                _item_Name.setText(Item_name);
                _Quantity.setText(String.valueOf(Quantity));
                _Weight.setText(String.valueOf(Weight));
                Log.d("Value", "Weight is " + Weight);
                //_Weight_Measurement.setText(Weight_Measurement);
                _Price.setText((String.valueOf(Price)));
            }
        };

    }

    public void newCursor() {
        handler = new DataBaseHandler(mContext);
        SQLiteDatabase db = handler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Items_Table", null);
        mCursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { //Here we call bindView of cursor Adapter.
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //Similar to newView of Cursor Adapter. Here we call newView of Cursor Adapter.
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View v1;

        public ViewHolder(View itemview) {
            super(itemview);
            v1 = itemview;
        }

    }
}
