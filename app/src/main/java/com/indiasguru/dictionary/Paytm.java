package com.indiasguru.dictionary;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Paytm {

    @SerializedName("MID")
    String MID;

    @SerializedName("ORDER_ID")
    String ORDER_ID;

    @SerializedName("CUST_ID")
    String CUST_ID;

    @SerializedName("CHANNEL_ID")
    String CHANNEL_ID;

    @SerializedName("TXN_AMOUNT")
    String TXN_AMOUNT;

    @SerializedName("WEBSITE")
    String WEBSITE;

    @SerializedName("CALLBACK_URL")
    String CALLBACK_URL;

    @SerializedName("INDUSTRY_TYPE_ID")
    String INDUSTRY_TYPE_ID;

    public Paytm(String mId, String channelId, String txnAmount, String website, String callBackUrl, String industryTypeId) {
        this.MID = mId;
        this.ORDER_ID = generateString();
        this.CUST_ID = generateString();
        this.CHANNEL_ID = channelId;
        this.TXN_AMOUNT = txnAmount;
        this.WEBSITE = website;
        this.CALLBACK_URL = callBackUrl;
        this.INDUSTRY_TYPE_ID = industryTypeId;
    }

    public String getMID() {
        return MID;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public String getCUST_ID() {
        return CUST_ID;
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public String getTXN_AMOUNT() {
        return TXN_AMOUNT;
    }

    public String getWEBSITE() {
        return WEBSITE;
    }

    public String getCALLBACK_URL() {
        return CALLBACK_URL;
    }

    public String getINDUSTRY_TYPE_ID() {
        return INDUSTRY_TYPE_ID;
    }

    /*
     * The following method we are using to generate a random string everytime
     * As we need a unique customer id and order id everytime
     * For real scenario you can implement it with your own application logic
     * */
    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
