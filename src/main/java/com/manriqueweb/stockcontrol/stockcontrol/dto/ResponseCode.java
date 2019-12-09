package com.manriqueweb.stockcontrol.stockcontrol.dto;

public enum ResponseCode {
	NOTSET(-1),
    OK(0),
    PRODUCT_KO(100),
    PRODUCT_DESCRIPTION_KO(105),
    DATE_KO(200),
    QTY_KO(300),
    MOVEMENT_KO(400),
    CONCEPT_TYPE_KO(10),
    UNKNOWN_ERROR(-100);

    private int responseCode;

    ResponseCode(int a) {
    	this.responseCode = a;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
