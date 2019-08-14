package com.appflowsolutions.kab.Utils;


public class Urls
{
    public  static String BASE_URL="http://kabapi.azurewebsites.net/";
    public  static String REGISTER_URL= BASE_URL+"api/User/RegisterUser";
    public  static String LOGIN_URL= BASE_URL+"api/User/LoginUser";
    public  static String CONTACT_URL=BASE_URL+"api/Contact/GetContacts?UserId=";
    public  static String HOSPITAL_URL=BASE_URL+"api/Hospital/GetHospitals?pincode=";
    public  static String HELPREQUEST_URL=BASE_URL+"api/Contact/HelpRequest";
    public  static String GetHELPREQUEST_URL=BASE_URL+"api/Emergency/HospitalByRequest";
    public  static String CHANGE_REQUEST_STATUS_URL=BASE_URL+"api/Emergency/ChangeStatus";
    public  static String DELETE_REQUEST_URL=BASE_URL+"api/Emergency/DeleteStatus/";
    public  static String AWAY_FROM_HOME_URL=BASE_URL+"api/User/AwayFromHome";
    public  static String GET_AWAY_FROM_HOME_URL=BASE_URL+"api/User/GetAwayFromHome/";

    public  static String CHANGEPASSWORD_URL=BASE_URL+"api/User/ChangePassword";
    public  static String VALIDATETOKEN_URL=BASE_URL+"api/User/ValidateToken";
    public  static String CREATE_CONTACT_URL=BASE_URL+"api/Contact/CreateContact";
    public  static String UPDATE_CONTACT_URL=BASE_URL+"api/Contact/UpdateContact/";
    public  static String UPDATE_USERPROFILE_URL=BASE_URL+"api/User/UpdateUser/";
    public static String MESSAGE_TEMPLATE_URL=BASE_URL+"api/MessageTemplate/GetMessageTemplates?UserId=";
    public static String CREATE_MESSAGE_TEMPLATE_URL=BASE_URL+"api/MessageTemplate/CreateMessageTemplate";
    public static String UPDATE_MESSAGE_TEMPLATE_URL=BASE_URL+"api/MessageTemplate/UpdateMessageTemplate/";
}
