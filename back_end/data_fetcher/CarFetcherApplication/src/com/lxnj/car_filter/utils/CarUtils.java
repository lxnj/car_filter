package com.lxnj.car_filter.utils;

/**
 * Created by Yiyong on 9/11/15.
 */
public class CarUtils {
    public static int getAuctionFee(String priceStr){
        int price = Integer.parseInt(priceStr);
        int auctionFee = 300;

        if(price > 5000 && price <= 10000)
            auctionFee = 400;
        if(price > 10000 && price <= 15000)
            auctionFee = 450;
        if(price > 15000 && price <= 20000)
            auctionFee = 550;
        if(price > 20000 && price <= 25000)
            auctionFee = 650;
        if(price > 25000 && price <= 35000)
            auctionFee = 750;
        if(price > 35000 && price <= 45000)
            auctionFee = 850;
        else {
            auctionFee = 850 + (price - 45000) / 10000 * 100;
        }

        return auctionFee;
    }
}
