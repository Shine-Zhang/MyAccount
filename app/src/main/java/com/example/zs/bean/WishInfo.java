package com.example.zs.bean;

/**
 * Created by WEIYU on 2016/9/8.
 * 愿望 信息类
 * @author 韦宇
 */
public class WishInfo {
    public int wishid;
    public int wishYear;                   //愿望创建的年
    public int wishMonth;                  //愿望创建的月
    public int wishDay;                    //愿望创建的肉
    public String wishTitle;               //愿望标题
    public String wishDescription;        //愿望备注
    public String wishFund;                //愿望资金
    public String wishphotoUri;           //愿望照片Uri

    public WishInfo() {
    }

    public WishInfo(int wishYear, int wishMonth, int wishDay, String wishTitle, String wishDescription, String wishFund, String wishphotoUri) {
        this.wishYear = wishYear;
        this.wishMonth = wishMonth;
        this.wishDay = wishDay;
        this.wishTitle = wishTitle;
        this.wishDescription = wishDescription;
        this.wishFund = wishFund;
        this.wishphotoUri = wishphotoUri;
    }

    public WishInfo(int wishid, int wishYear, int wishMonth, int wishDay, String wishTitle, String wishDescription, String wishFund, String wishphotoUri) {
        this.wishid = wishid;
        this.wishYear = wishYear;
        this.wishMonth = wishMonth;
        this.wishDay = wishDay;
        this.wishTitle = wishTitle;
        this.wishDescription = wishDescription;
        this.wishFund = wishFund;
        this.wishphotoUri = wishphotoUri;
    }

    public int getWishid() {
        return wishid;
    }

    public void setWishid(int wishid) {
        this.wishid = wishid;
    }

    public int getWishYear() {
        return wishYear;
    }

    public void setWishYear(int wishYear) {
        this.wishYear = wishYear;
    }

    public int getWishMonth() {
        return wishMonth;
    }

    public void setWishMonth(int wishMonth) {
        this.wishMonth = wishMonth;
    }

    public int getWishDay() {
        return wishDay;
    }

    public void setWishDay(int wishDay) {
        this.wishDay = wishDay;
    }

    public String getWishTitle() {
        return wishTitle;
    }

    public void setWishTitle(String wishTitle) {
        this.wishTitle = wishTitle;
    }

    public String getWishDescription() {
        return wishDescription;
    }

    public void setWishDescription(String wishDescription) {
        this.wishDescription = wishDescription;
    }

    public String getWishFund() {
        return wishFund;
    }

    public void setWishFund(String wishFund) {
        this.wishFund = wishFund;
    }

    public String getWishphotoUri() {
        return wishphotoUri;
    }

    public void setWishphotoUri(String wishphotoUri) {
        this.wishphotoUri = wishphotoUri;
    }

    @Override
    public String toString() {
        return "WishInfo{" +
                "wishid=" + wishid +
                ", wishYear=" + wishYear +
                ", wishMonth=" + wishMonth +
                ", wishDay=" + wishDay +
                ", wishTitle='" + wishTitle + '\'' +
                ", wishDescription='" + wishDescription + '\'' +
                ", wishFund=" + wishFund +
                ", wishphotoUri='" + wishphotoUri + '\'' +
                '}';
    }
}
