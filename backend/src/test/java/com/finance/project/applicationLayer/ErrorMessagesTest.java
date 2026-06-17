package com.finance.project.applicationLayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorMessagesTest {

    // --- Person ---

    @Test
    @DisplayName("PERSON_DOES_NOT_EXIST has canonical value")
    void personDoesNotExist() {
        assertEquals("Person does not exist", ErrorMessages.PERSON_DOES_NOT_EXIST);
    }

    @Test
    @DisplayName("PERSON_ALREADY_EXIST has canonical value")
    void personAlreadyExist() {
        assertEquals("Person already exists", ErrorMessages.PERSON_ALREADY_EXIST);
    }

    // --- Group ---

    @Test
    @DisplayName("GROUP_DOES_NOT_EXIST has canonical value")
    void groupDoesNotExist() {
        assertEquals("Group does not exist", ErrorMessages.GROUP_DOES_NOT_EXIST);
    }

    @Test
    @DisplayName("GROUP_ALREADY_EXISTS has canonical value")
    void groupAlreadyExists() {
        assertEquals("Group already exists", ErrorMessages.GROUP_ALREADY_EXISTS);
    }

    // --- Account ---

    @Test
    @DisplayName("ACCOUNT_DOES_NOT_EXIST has canonical value")
    void accountDoesNotExist() {
        assertEquals("Account does not exist", ErrorMessages.ACCOUNT_DOES_NOT_EXIST);
    }

    @Test
    @DisplayName("ACCOUNT_ALREADY_EXIST has canonical value")
    void accountAlreadyExist() {
        assertEquals("Account already exists", ErrorMessages.ACCOUNT_ALREADY_EXIST);
    }

    @Test
    @DisplayName("ACCOUNT_CREATED has canonical value")
    void accountCreated() {
        assertEquals("Account created and added", ErrorMessages.ACCOUNT_CREATED);
    }

    // --- Category ---

    @Test
    @DisplayName("CATEGORY_DOES_NOT_EXIST has canonical value")
    void categoryDoesNotExist() {
        assertEquals("Category does not exist", ErrorMessages.CATEGORY_DOES_NOT_EXIST);
    }

    @Test
    @DisplayName("CATEGORY_ALREADY_EXISTS has canonical value")
    void categoryAlreadyExists() {
        assertEquals("Category already exists", ErrorMessages.CATEGORY_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("CATEGORY_CREATED has canonical value")
    void categoryCreated() {
        assertEquals("Category created and added", ErrorMessages.CATEGORY_CREATED);
    }

    // --- Ledger / Transactions ---

    @Test
    @DisplayName("LEDGER_DOES_NOT_EXIST has canonical value")
    void ledgerDoesNotExist() {
        assertEquals("Ledger does not exist", ErrorMessages.LEDGER_DOES_NOT_EXIST);
    }

    // --- Group membership ---

    @Test
    @DisplayName("PERSON_NOT_IN_CHARGE has canonical value")
    void personNotInCharge() {
        assertEquals("Person is not in charge", ErrorMessages.PERSON_NOT_IN_CHARGE);
    }

    @Test
    @DisplayName("PERSON_NOT_MEMBER has canonical value")
    void personNotMember() {
        assertEquals("Person is not member of the group", ErrorMessages.PERSON_NOT_MEMBER);
    }

    @Test
    @DisplayName("PERSON_ALREADY_IN_GROUP has canonical value")
    void personAlreadyInGroup() {
        assertEquals("Person is already member", ErrorMessages.PERSON_ALREADY_IN_GROUP);
    }

    // --- Search account records ---

    @Test
    @DisplayName("NO_TRANSACTIONS_TO_REPORT has canonical value")
    void noTransactionsToReport() {
        assertEquals("Ledger has no transactions within the searched period", ErrorMessages.NO_TRANSACTIONS_TO_REPORT);
    }

    @Test
    @DisplayName("EMPTY_LEDGER has canonical value")
    void emptyLedger() {
        assertEquals("Ledger is empty", ErrorMessages.EMPTY_LEDGER);
    }

    @Test
    @DisplayName("TIME_PERIOD_OUTSIDE_OF_RECORDS_RANGE has canonical value")
    void timePeriodOutsideRange() {
        assertEquals("The time period provided falls outside the range of the ledger records", ErrorMessages.TIME_PERIOD_OUTSIDE_OF_RECORDS_RANGE);
    }

    @Test
    @DisplayName("DATES_IN_REVERSE_ORDER has canonical value")
    void datesInReverseOrder() {
        assertEquals("Check the start and end dates for the period, since start date cannot be later than end date", ErrorMessages.DATES_IN_REVERSE_ORDER);
    }

    @Test
    @DisplayName("ACCOUNT_NAME_FIELD_MISSING has canonical value")
    void accountNameFieldMissing() {
        assertEquals("Search results cannot be displayed: account name is missing", ErrorMessages.ACCOUNT_NAME_FIELD_MISSING);
    }

    @Test
    @DisplayName("START_DATE_FIELD_MISSING has canonical value")
    void startDateFieldMissing() {
        assertEquals("Search results cannot be displayed: start date is missing", ErrorMessages.START_DATE_FIELD_MISSING);
    }

    @Test
    @DisplayName("END_DATE_FIELD_MISSING has canonical value")
    void endDateFieldMissing() {
        assertEquals("Search results cannot be displayed: end date is missing", ErrorMessages.END_DATE_FIELD_MISSING);
    }
}
