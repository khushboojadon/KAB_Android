package com.appflowsolutions.kab.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpRequestModel {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("RequestType")
    @Expose
    private String requestType;
    @SerializedName("RequestMessage")
    @Expose
    private String requestMessage;
    @SerializedName("Long")
    @Expose
    private String _long;
    @SerializedName("Lat")
    @Expose
    private String lat;
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("Modifed")
    @Expose
    private String modifed;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("RequestStatus")
    @Expose
    private String requestStatus;
    @SerializedName("HospitalId")
    @Expose
    private Integer hospitalId;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("UserCode")
    @Expose
    private Integer userCode;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("AreaPinCode")
    @Expose
    private Integer areaPinCode;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("NearByHospital")
    @Expose
    private Integer nearByHospital;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModifed() {
        return modifed;
    }

    public void setModifed(String modifed) {
        this.modifed = modifed;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAreaPinCode() {
        return areaPinCode;
    }

    public void setAreaPinCode(Integer areaPinCode) {
        this.areaPinCode = areaPinCode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getNearByHospital() {
        return nearByHospital;
    }

    public void setNearByHospital(Integer nearByHospital) {
        this.nearByHospital = nearByHospital;
    }
}


