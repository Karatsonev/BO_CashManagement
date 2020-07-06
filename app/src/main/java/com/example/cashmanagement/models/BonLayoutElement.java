package com.example.cashmanagement.models;

public class BonLayoutElement {

    private String mContent;
    private float mTextSize;
    private boolean isBold;
    private boolean isUnderlined;


    public BonLayoutElement(String Content, float TextSize, boolean IsBold, boolean IsUnderlined) {
        this.mContent = Content;
        this.mTextSize = TextSize;
        this.isBold = IsBold;
        this.isUnderlined = IsUnderlined;

    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        this.isBold = bold;
    }

    public boolean isUnderlined() {
        return isUnderlined;
    }

    public void setUnderlined(boolean underlined) {
        this.isUnderlined = underlined;
    }

    public float getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

}
