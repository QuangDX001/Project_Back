package com.example.backend.security.config;

public class AppConstants {
    public static final String DEFAULT_PAGE_NUMBER = "1";
    public  static final String DEFAULT_PAGE_SIZE = "5";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final String DEFAULT_USER_ID = "0";

    public static final String PHONE_REGEX = "^\\d+$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$";
}
