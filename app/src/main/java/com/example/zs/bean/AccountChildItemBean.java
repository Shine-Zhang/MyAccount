package com.example.zs.bean;

/**
 * Created by Administrator on 2016/9/3 0003.
 * @author shine-zhang
 *
 * 该来主要用来封装帐薄中的每一个条目
 */
public class AccountChildItemBean {


    int id;
    int month;
    int dayOfMonth;
    int icon;
    //此处为期赋初值-1，主要是为了区分是否有图片
    String photoResurl ="";
    String itemDescribe;
    String howmuch;
    boolean isIncome;
    boolean isFold=true;
    /**
     *全参构造函数主要是为了方便封装bean类
     * @param month 添加该条目到帐薄中时的月份
     * @param dayOfMonth 添加该条目到帐薄中时的具体日期
     * @param icon  该条目在帐薄中的图标
     * @param photoResurl 用户自己为该条目添加的图片id
     * @param itemDescribe 该条目和图标配套的文字
     * @param howmuch 该条目中具体住处/收入的数额
     * @param isIncome 用来判断该条目是收入/支出
     */
    public AccountChildItemBean(int month, int dayOfMonth, int icon, String photoResurl, String itemDescribe, String howmuch, boolean isIncome,int id) {
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.icon = icon;
        this.photoResurl = photoResurl;
        this.itemDescribe = itemDescribe;
        this.howmuch = howmuch;
        this.isIncome = isIncome;
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getPhotoResUrl() {
        return photoResurl;
    }

    public void setPhotoResId(String photoResurl) {
        this.photoResurl = photoResurl;
    }

    public String getItemDescribe() {
        return itemDescribe;
    }

    public void setItemDescribe(String itemDescribe) {
        this.itemDescribe = itemDescribe;
    }

    public String getHowmuch() {
        return howmuch;
    }

    public void setHowmuch(String howmuch) {
        this.howmuch = howmuch;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

    public boolean isFold() {
        return isFold;
    }

    public void setFold(boolean fold) {
        isFold = fold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "AccountChildItemBean{" +
                "id=" + id +
                ", month=" + month +
                ", dayOfMonth=" + dayOfMonth +
                ", icon=" + icon +
                ", photoResId=" + photoResurl +
                ", itemDescribe='" + itemDescribe + '\'' +
                ", howmuch='" + howmuch + '\'' +
                ", isIncome=" + isIncome +
                ", isFold=" + isFold +
                '}';
    }


}
