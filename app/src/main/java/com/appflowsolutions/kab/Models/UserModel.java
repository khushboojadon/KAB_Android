package com.appflowsolutions.kab.Models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel{

        @SerializedName("Id")
        @Expose
        private int id;
        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("LastName")
        @Expose
        private String lastName;
        @SerializedName("Mobile")
        @Expose
        private Long mobile;
        @SerializedName("Token")
        @Expose
        private String token;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("UserType")
        @Expose
        private String userType;
        @SerializedName("AreaPinCode")
        @Expose
        private String areaPinCode;
        @SerializedName("FirstContact")
        @Expose
        private ContactModel firstContact;
        @SerializedName("SecondContact")
        @Expose
        private ContactModel secondContact;
        @SerializedName("NearByHospital")
        @Expose
        private Integer nearByHospital;
        @SerializedName("FDoctorName")
        @Expose
        private String fDoctorName;
        @SerializedName("FDoctorPhoneNumber")
        @Expose
        private Long fDoctorPhoneNumber;

    public String getfDoctorName() {
        return fDoctorName;
    }

    public void setfDoctorName(String fDoctorName) {
        this.fDoctorName = fDoctorName;
    }

    public Long getfDoctorPhoneNumber() {
        return fDoctorPhoneNumber;
    }

    public void setfDoctorPhoneNumber(Long fDoctorPhoneNumber) {
        this.fDoctorPhoneNumber = fDoctorPhoneNumber;
    }

    public String getfDoctorAddress() {
        return fDoctorAddress;
    }

    public void setfDoctorAddress(String fDoctorAddress) {
        this.fDoctorAddress = fDoctorAddress;
    }

    @SerializedName("FDoctorAddress")
        @Expose
        private String fDoctorAddress;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAreaPinCode() {
        return areaPinCode;
    }

    public void setAreaPinCode(String areaPinCode) {
        this.areaPinCode = areaPinCode;
    }

    public ContactModel getFirstContact() {
        return firstContact;
    }

    public void setFirstContact(ContactModel firstContact) {
        this.firstContact = firstContact;
    }

    public ContactModel getSecondContact() {
        return secondContact;
    }

    public void setSecondContact(ContactModel secondContact) {
        this.secondContact = secondContact;
    }

    public Integer getNearByHospital() {
        return nearByHospital;
    }

    public void setNearByHospital(Integer nearByHospital) {
        this.nearByHospital = nearByHospital;
    }

}


