package com.yisingle.app.mvp;



import com.yisingle.app.data.OrderData;
import com.yisingle.baselibray.base.BaseView;

/**
 * Created by jikun on 17/7/24.
 */

public interface IYiSinglePassenger {

    interface IYiSinglePassengerView extends BaseView {


        /**
         * 成功
         * @param orderData OrderData
         */
        void findOrderSuccess(OrderData orderData);


    }

    interface IYiSinglePassengerPresenter {


        /**
         * 连接socket成功
         */
        void connectSocket();

    }
}
