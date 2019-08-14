package com.appflowsolutions.kab.Utils;

import android.graphics.Typeface;


import com.appflowsolutions.kab.Models.*;

import java.util.List;

public class Global
{
    public static Integer USERID;
    public static String USERTOKEN;
    public static String USERLOGINID;
    public static String USERTYPE;
    public static UserModel USER;

    public static List<HelpRequestModel> EMERGENCY_REQUESTS;
    public static List<ContactModel> CONTACTS;
    public static List<HospitalModel> HOSPITALS;
    public static List<AwayModel> AWAYREQUESTS;

    public static List<MessageTemplateModel> MESSAGETEMPLATES;
    public static String HOSPITALS_OBJECT_TAG="HOSPITALS_OBJECT_TAG";
    public static String CONTACTS_OBJECT_TAG="CONTACTS_OBJECT_TAG";
    public static String EDIT_CONTACTS_OBJECT_TAG="EDIT_CONTACTS_OBJECT_TAG";
    public static String EDIT_MESSAGE_OBJECT_TAG="EDIT_MESSAGE_OBJECT_TAG";
    public  static Typeface FontAwesome;



}
