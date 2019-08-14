package com.appflowsolutions.kab.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageTemplateModel implements Serializable {

    @SerializedName("ContactId")
    @Expose
    private Integer contactId;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("Created")
    @Expose
    private Object created;
    @SerializedName("Modified")
    @Expose
    private Object modified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Object getCreated() {
        return created;
    }

    public void setCreated(Object created) {
        this.created = created;
    }

    public Object getModified() {
        return modified;
    }

    public void setModified(Object modified) {
        this.modified = modified;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        contactId = contactId;
    }
}
