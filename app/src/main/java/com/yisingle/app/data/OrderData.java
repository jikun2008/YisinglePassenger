package com.yisingle.app.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by jikun on 17/6/27.
 */

public class OrderData implements Parcelable {


    public DriverData getDriver() {
        return driver;
    }

    public void setDriver(DriverData driver) {
        this.driver = driver;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    private int id;


    private String phoneNum;

    private String startLatitude;


    private String startLongitude;


    private String endLatitude;

    private String endLongitude;


    private String startPlaceName;


    private String endPlaceName;


    private int orderState;


    private DriverData driver;

    private UserData user;

    private int passengerRelyState;

    private BigDecimal orderPrice;


    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getPassengerRelyState() {
        return passengerRelyState;
    }

    public void setPassengerRelyState(int passengerRelyState) {
        this.passengerRelyState = passengerRelyState;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getEndPlaceName() {
        return endPlaceName;
    }

    public void setEndPlaceName(String endPlaceName) {
        this.endPlaceName = endPlaceName;
    }

    public OrderData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @State
    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(@State int orderState) {
        this.orderState = orderState;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderData orderData = (OrderData) o;

        if (id != orderData.id) {
            return false;
        }
        if (orderState != orderData.orderState) {
            return false;
        }
        if (passengerRelyState != orderData.passengerRelyState) {
            return false;
        }
        if (phoneNum != null ? !phoneNum.equals(orderData.phoneNum) : orderData.phoneNum != null) {
            return false;
        }
        if (startLatitude != null ? !startLatitude.equals(orderData.startLatitude) : orderData.startLatitude != null) {
            return false;
        }
        if (startLongitude != null ? !startLongitude.equals(orderData.startLongitude) : orderData.startLongitude != null) {
            return false;
        }
        if (endLatitude != null ? !endLatitude.equals(orderData.endLatitude) : orderData.endLatitude != null) {
            return false;
        }
        if (endLongitude != null ? !endLongitude.equals(orderData.endLongitude) : orderData.endLongitude != null) {
            return false;
        }
        if (startPlaceName != null ? !startPlaceName.equals(orderData.startPlaceName) : orderData.startPlaceName != null) {
            return false;
        }
        return endPlaceName != null ? endPlaceName.equals(orderData.endPlaceName) : orderData.endPlaceName == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
        result = 31 * result + (startLatitude != null ? startLatitude.hashCode() : 0);
        result = 31 * result + (startLongitude != null ? startLongitude.hashCode() : 0);
        result = 31 * result + (endLatitude != null ? endLatitude.hashCode() : 0);
        result = 31 * result + (endLongitude != null ? endLongitude.hashCode() : 0);
        result = 31 * result + (startPlaceName != null ? startPlaceName.hashCode() : 0);
        result = 31 * result + (endPlaceName != null ? endPlaceName.hashCode() : 0);
        result = 31 * result + orderState;
        result = 31 * result + passengerRelyState;
        return result;
    }

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({State.WAIT_NEW, State.WAIT_OLD, State.HAVE_TAKE, State.DRIVER_ARRIVE, State.PASSENGER_IN_CAR, State.PASSENGER_OUT_CAR, State.HAVE_COMPLETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

        int WAIT_NEW = -1;
        int WAIT_OLD = 0;
        int HAVE_TAKE = 1;//订单已接受
        int DRIVER_ARRIVE = 2;//司机已到达
        int PASSENGER_IN_CAR = 3;//乘客已经上车
        int PASSENGER_OUT_CAR = 4;//乘客已下车
        int HAVE_COMPLETE = 5;//订单已经完成


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.phoneNum);
        dest.writeString(this.startLatitude);
        dest.writeString(this.startLongitude);
        dest.writeString(this.endLatitude);
        dest.writeString(this.endLongitude);
        dest.writeString(this.startPlaceName);
        dest.writeString(this.endPlaceName);
        dest.writeInt(this.orderState);
        dest.writeParcelable(this.driver, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.passengerRelyState);
        dest.writeSerializable(this.orderPrice);
    }

    protected OrderData(Parcel in) {
        this.id = in.readInt();
        this.phoneNum = in.readString();
        this.startLatitude = in.readString();
        this.startLongitude = in.readString();
        this.endLatitude = in.readString();
        this.endLongitude = in.readString();
        this.startPlaceName = in.readString();
        this.endPlaceName = in.readString();
        this.orderState = in.readInt();
        this.driver = in.readParcelable(DriverData.class.getClassLoader());
        this.user = in.readParcelable(UserData.class.getClassLoader());
        this.passengerRelyState = in.readInt();
        this.orderPrice = (BigDecimal) in.readSerializable();
    }

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel source) {
            return new OrderData(source);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };


}
