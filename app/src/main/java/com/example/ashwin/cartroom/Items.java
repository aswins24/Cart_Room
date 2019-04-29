package com.example.ashwin.cartroom;

/**
 * Created by ashwin on 4/15/2019.
 */
public class Items {
    int _id;
    String _item_Name;
    int _quantity;
    String _weight;
    double _price;

    public Items() {
    }
    //Items class act as a custom data type which can hold data from database.
    public Items(int id, String item_Name, int quantity, String weight, double price) {
        this._id = id;
        this._item_Name = item_Name;
        this._quantity = quantity;
        this._weight = weight;
        this._price = price;
    }

    public int getId() {

        return this._id;

    }

    public void setId(int id) {

        this._id = id;
    }

    public String getName() {

        return this._item_Name;

    }

    public void setName(String Name) {

        this._item_Name = Name;

    }

    public int getQuantity() {

        return this._quantity;

    }

    public void setQuantity(int Quantity) {

        this._quantity = Quantity;

    }

    public String getWeight() {

        return this._weight;

    }

    public void setWeight(String Weight) {

        this._weight = Weight;
    }

    public double getPrice() {

        return this._price;

    }

    public void setPrice(double Price) {

        this._price = Price;

    }
}
