package com.appflowsolutions.kab.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AwayModel implements Serializable {

        @SerializedName("Away")
        @Expose
        private Boolean away;
        @SerializedName("FirstName")
        @Expose
        private String firstName;

        @SerializedName("Mobile")
        @Expose
        private Long mobile;

        @SerializedName("From")
        @Expose
        private String from;
        @SerializedName("TO")
        @Expose
        private String tO;
        @SerializedName("Id")
        @Expose
        private Integer id;

        public Boolean getAway() {
            return away;
        }

        public void setAway(Boolean away) {
            this.away = away;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTO() {
            return tO;
        }

        public void setTO(String tO) {
            this.tO = tO;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }
}

