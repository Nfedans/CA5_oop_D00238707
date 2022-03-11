package org.example;

import java.util.Comparator;

public class BrandStockComparator implements Comparator<Perfume> {

    //compare integer stockLvl withing Brand
    @Override
    public int compare(Perfume p1, Perfume p2)
    {

        boolean brandSame =
                p1.getBrand().equalsIgnoreCase(p2.getBrand());


        if(brandSame)
        {
            //so, compare based on stocklevel
            return (p1.getStockLvl() - p2.getStockLvl()) * -1;
        }
        else
        {
            return p1.getBrand().compareToIgnoreCase(p2.getBrand());
        }
    }
}
