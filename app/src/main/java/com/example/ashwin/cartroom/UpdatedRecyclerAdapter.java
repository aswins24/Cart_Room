package com.example.ashwin.cartroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    Context mContext;
    DataBaseHandler handler;
    List<Items> Data;
    Items item;
    //Instead of using Cursor Adapter and Recycler Adapter, just using Recycler Adapter and get data from DatabaseHandler class.
    public UpdatedRecyclerAdapter(Context context){

        this.mContext = context;
        this.handler = new DataBaseHandler(mContext);
        this.Data = handler.getAllitems();
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView item_no = holder.itemView.findViewById(R.id.Item_id);
        TextView item_Name = holder.itemView.findViewById(R.id.Item_Name);
        TextView quantity = holder.itemView.findViewById(R.id.Quantity);
        TextView weight = holder.itemView.findViewById(R.id.Weight);
        EditText price = holder.itemView.findViewById(R.id.Price);

        item = Data.get(position);
        int _item_no = item.getId();
        String _item_Name = item.getName();
        int _quantity =item.getQuantity();
        String _weight = item.getWeight();
        double _price = item.getPrice();

        item_no.setText(String.valueOf(_item_no));
        item_Name.setText(_item_Name + "\n"); //Adding a new line with text in order to avoid clipping of view due to wrap_content
        quantity.setText(String.valueOf(_quantity));
        weight.setText(String.valueOf(_weight));
        //_Weight_Measurement.setText(Weight_Measurement);
        price.setText((String.valueOf(_price)));
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        View v1;

        public ViewHolder(View itemview) {
            super(itemview);
            v1 = itemview;
        }

    }

    public void LoadNewData(){

        Data = handler.getAllitems();
    }

    public double Calculate(){

        double total=0;

        for(Items it : Data){

            total += it.getPrice();
        }
        return total;
    }
}
