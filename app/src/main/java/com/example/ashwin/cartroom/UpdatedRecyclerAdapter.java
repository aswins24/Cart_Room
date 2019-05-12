package com.example.ashwin.cartroom;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ashwin on 5/8/2019.
 */
public class UpdatedRecyclerAdapter extends RecyclerView.Adapter<UpdatedRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private DataBaseHandler handler;
    private List<Items> Data;
    private Items item;

    //Instead of using Cursor Adapter and Recycler Adapter, just using Recycler Adapter and get data from DatabaseHandler class.
    public UpdatedRecyclerAdapter(Context context) {

        this.mContext = context;
        this.handler = new DataBaseHandler(mContext);
        this.Data = handler.getAllitems();

    }


    @Override
    public int getItemCount() {

        Log.d("Data size", "Size of Data is " + Data.size());
        return Data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(mContext).inflate(R.layout.item_container, parent, false);
        return new ViewHolder(itemview, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        item = Data.get(position);
        int _item_no = item.getId();
        String _item_Name = item.getName();
        int _quantity = item.getQuantity();
        String _weight = item.getWeight();
        double _price = item.getPrice();


        holder.item_no.setText(String.valueOf(_item_no));
        holder.item_Name.setText(_item_Name + "\n"); //Adding a new line with text in order to avoid clipping of view due to wrap_content
        holder.quantity.setText(String.valueOf(_quantity));
        holder.weight.setText(String.valueOf(_weight));
        holder.price.setText((String.valueOf(_price)));
        holder.price.setFocusable(true);


    }

    public void LoadNewData() {

        Data = handler.getAllitems();
    }

    public double Calculate() {
        LoadNewData();
        double total = 0;

        for (Items it : Data) {

            total += it.getPrice();
        }
        return total;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View v1;
        EditText price;
        TextView item_no;
        TextView item_Name;
        TextView quantity;
        TextView weight;
        DataBaseHandler handler;


        public ViewHolder(View itemview, Context context) {
            super(itemview);
            v1 = itemview;
            item_no = itemview.findViewById(R.id.Item_id);
            item_Name = itemview.findViewById(R.id.Item_Name);
            quantity = itemview.findViewById(R.id.Quantity);
            weight = itemview.findViewById(R.id.Weight);
            price = itemview.findViewById(R.id.Price);
            handler = new DataBaseHandler(context);


            price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        Log.d("FocusListener", "Item no. is " + item_no.getText().toString());

                        price.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                ContentValues value = new ContentValues();
                                value.put("Price", Double.parseDouble(price.getText().toString().trim()));
                                int pos = Integer.parseInt(item_no.getText().toString().trim());
                                handler.UpdatePrice(value, pos);

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                    }else{

                        price.removeTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                    }


                }
            });


        }


    }

}
