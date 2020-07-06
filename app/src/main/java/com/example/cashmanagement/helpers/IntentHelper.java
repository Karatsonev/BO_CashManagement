package com.example.cashmanagement.helpers;

public class IntentHelper {

    public static final String INTENT_RESULT_CASH_IN_BANKNOTES = "bo_cash_management.cash.in.banknotes";
    public static final String INTENT_RESULT_CASH_OUT_BANKNOTES ="bo_cash_management.cash.out.banknotes" ;
    public static final String INTENT_RESULT_CASH_IN_COINS ="bo_cash_management.cash.in.coins" ;
    public static final String INTENT_RESULT_CASH_OUT_COINS ="bo_cash_management.cash.out.coins" ;
    public static String INTENT_RESULT_TWO_MACHINES = "bo_cash_management.two.machine";
    public static String INTENT_RESULT_CASH_MACHINE_STATUS = "bo_cash_management.cash.machine.status";

    public static String INTENT_IMPORT_STRING_EXTRA_CASHMACHINE_STATUS = "cash_machine_status";


    public static String INTENT_IMPORT_STRING_EXTRA_AMOUNT = "amount";
    public static String INTENT_IMPORT_STRING_EXTRA_COUNT = "count";
    public static String INTENT_IMPORT_STRING_EXTRA_DENOMINATION = "denomination";


    //region List<DENOMINATION>
    public static String INTENT_IMPORT_EXTRA_LIST = "list";
    public static String INTENT_IMPORT_EXTRA_LIST_COIN = "list_coin";
    //endregion List<DENOMINATION>

    //region cash out banknotes
    public static String INTENT_IMPORT_EXTRA_DECIMAL_AMOUNT = "extra_cash_out_banknotes_result";
    public static String INTENT_IMPORT_EXTRA_LIST_INVENTORY_ITEMS = "extra_cash_out_inventory_items";
    //endregion cash out banknotes

    public static String INTENT_FINISH_PAY_IN_COINS = "extra_cash_out_banknotes_result";

    //public static final String EXTRA_SET_KEY = "setData";
    //public static final String EXTRA_SET_1 = "set1";
    //public static final String EXTRA_SET_2 = "set2";


}
