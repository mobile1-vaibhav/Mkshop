package com.mobiles.mkshop.pojos;

/**
 * Created by vaibhav on 2/7/15.
 */
public enum  ProductType {

    Mobile("Mobile"),
    Accessory("Accessory");


    private String productType;
    ProductType(String productType){
        this.productType = productType;
    }
}
