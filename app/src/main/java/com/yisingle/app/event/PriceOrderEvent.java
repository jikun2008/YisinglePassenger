package com.yisingle.app.event;

import com.yisingle.app.data.OrderData;

/**
 * Created by jikun on 17/8/1.
 */

public class PriceOrderEvent {

    private OrderData orderData;


    public PriceOrderEvent(OrderData orderData) {
        this.orderData = orderData;
    }

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }
}
