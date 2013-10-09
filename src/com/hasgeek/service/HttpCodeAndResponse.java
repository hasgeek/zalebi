package com.hasgeek.service;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * A small class that holds the response code and response body of an HTTP request.
 */
public class HttpCodeAndResponse implements Parcelable {

    private String code;
    private String response;

    public static final Parcelable.Creator<HttpCodeAndResponse> CREATOR = new Creator<HttpCodeAndResponse>() {

        @Override
        public HttpCodeAndResponse[] newArray(int size) {
            return new HttpCodeAndResponse[size];
        }


        @Override
        public HttpCodeAndResponse createFromParcel(Parcel source) {
            return new HttpCodeAndResponse(source);
        }
    };


    public HttpCodeAndResponse() {
        this.code = "000";
        this.response = null;
    }


    public HttpCodeAndResponse(Parcel source) {
        this.code = source.readString();
        this.response = source.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.response);
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getResponse() {
        return response;
    }


    public void setResponse(String response) {
        this.response = response;
    }


    @Override
    public String toString() {
        return "HttpCodeAndResponse[" + this.code + "\n" + this.response + "]";
    }
}
