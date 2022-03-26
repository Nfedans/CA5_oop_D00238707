package org.example.DTOs;

import java.util.Objects;

public class Perfume implements Comparable<Perfume>{

    private int _id;
    private String brand;
    private String name;
    private int size;
    private float price;
    //private ArrayList<String> notes;
    private String gender;
    private int stockLvl;

    public Perfume(int _id, String brand, String name, int size, float price, String gender, int stockLvl)
    {
        this._id = _id;
        this.brand = brand;
        this.name = name;
        this.size = size;
        this.price = price;
        this.gender = gender;
        this.stockLvl = stockLvl;
    }

    public Perfume(int _id, String brand, String name)
    {
        this._id = _id;
        this.brand = brand;
        this.name = name;
        this.size = -1;
        this.price = -1;
        this.gender = "UNSET";
        this.stockLvl = -1;
    }


    // Compare based on price per 1ml of perfume
    // If 2 perfumes are the same price/size ratio, the larger sized bottle has priority

    // no need for getter as in same class

    @Override
    public int compareTo(Perfume p)
    {
        double currentP = (this.price/ (float)this.size);
        double paramP = (p.price / (float)p.size);

        boolean priceSizeComp = currentP == paramP;

        if (priceSizeComp)
        {
            return 0;
        }
        else
        {
            if (currentP - paramP > 0)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStockLvl() {
        return stockLvl;
    }

    public void setStockLvl(int stockLvl) {
        this.stockLvl = stockLvl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfume)) return false;
        Perfume perfume = (Perfume) o;
        return get_id() == perfume.get_id() && getBrand().equals(perfume.getBrand()) && getName().equals(perfume.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id(), getBrand(), getName());
    }

    @Override
    public String toString() {
        return "Perfume{" +
                "brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", price=" + price +
                ", gender='" + gender + '\'' +
                ", stockLvl=" + stockLvl +
                '}';
    }
}
