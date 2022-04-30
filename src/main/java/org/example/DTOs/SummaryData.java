package org.example.DTOs;

public class SummaryData {

        private int totalStock;
        private int totalProducts;


        public SummaryData(int totalStock, int totalProducts) {
            this.totalStock = totalStock;
            this.totalProducts = totalProducts;
        }

        public SummaryData() {
            this.totalStock = -1;
            this.totalProducts = -1;
        }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    @Override
    public String toString() {
        return "SummaryData{" +
                "totalStock=" + totalStock +
                ", totalProducts=" + totalProducts +
                '}';
    }

}
