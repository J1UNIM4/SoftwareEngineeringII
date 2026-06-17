package com.finance.project.applicationLayer;

public final class ErrorMessages {

    private ErrorMessages() {}

    // Person
    public static final String PERSON_DOES_NOT_EXIST = "Person does not exist";
    public static final String PERSON_ALREADY_EXIST  = "Person already exists";

    // Group
    public static final String GROUP_DOES_NOT_EXIST  = "Group does not exist";
    public static final String GROUP_ALREADY_EXISTS  = "Group already exists";

    // Account
    public static final String ACCOUNT_DOES_NOT_EXIST = "Account does not exist";
    public static final String ACCOUNT_ALREADY_EXIST  = "Account already exists";
    public static final String ACCOUNT_CREATED        = "Account created and added";

    // Category
    public static final String CATEGORY_DOES_NOT_EXIST = "Category does not exist";
    public static final String CATEGORY_ALREADY_EXISTS = "Category already exists";
    public static final String CATEGORY_CREATED        = "Category created and added";

    // Ledger
    public static final String LEDGER_DOES_NOT_EXIST = "Ledger does not exist";

    // Group membership
    public static final String PERSON_NOT_IN_CHARGE  = "Person is not in charge";
    public static final String PERSON_NOT_MEMBER     = "Person is not member of the group";
    public static final String PERSON_ALREADY_IN_GROUP = "Person is already member";

    // Search account records
    public static final String NO_TRANSACTIONS_TO_REPORT        = "Ledger has no transactions within the searched period";
    public static final String EMPTY_LEDGER                     = "Ledger is empty";
    public static final String TIME_PERIOD_OUTSIDE_OF_RECORDS_RANGE = "The time period provided falls outside the range of the ledger records";
    public static final String DATES_IN_REVERSE_ORDER           = "Check the start and end dates for the period, since start date cannot be later than end date";
    public static final String ACCOUNT_NAME_FIELD_MISSING       = "Search results cannot be displayed: account name is missing";
    public static final String START_DATE_FIELD_MISSING         = "Search results cannot be displayed: start date is missing";
    public static final String END_DATE_FIELD_MISSING           = "Search results cannot be displayed: end date is missing";
}
