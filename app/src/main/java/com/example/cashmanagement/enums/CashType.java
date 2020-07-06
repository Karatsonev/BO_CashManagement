package com.example.cashmanagement.enums;

public enum CashType {

         Coin(1)
        ,Banknote(2);

        private int value;

        CashType(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }

}
