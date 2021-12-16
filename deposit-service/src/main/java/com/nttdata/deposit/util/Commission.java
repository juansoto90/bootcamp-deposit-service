package com.nttdata.deposit.util;

public class Commission {
    private double commission;

    public static double getCommission(String accountType){
        return accountType.equals("SAVING_ACCOUNT")  ? 3.0 :
               accountType.equals("CURRENT_ACCOUNT") ? 0.0 : 20.0;
    }
}
