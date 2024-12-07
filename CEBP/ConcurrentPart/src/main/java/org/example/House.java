package org.example;

public class House {
    private String resourceType;
    private int productionRate;
    private boolean defended;

    public House(String resourceType, int productionRate) {
        this.resourceType = resourceType;
        this.productionRate = productionRate;
        this.defended = false;
    }

    public String getResourceType() {
        return resourceType;
    }

    public int getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(int productionRate) {
        this.productionRate = productionRate;
    }

    public boolean isDefended() {
        return defended;
    }

    public void setDefended(boolean defended) {
        this.defended = defended;
    }
}
