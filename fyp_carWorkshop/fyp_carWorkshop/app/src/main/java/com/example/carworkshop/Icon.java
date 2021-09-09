package com.example.carworkshop;

public class Icon {
    private String iName;
    private String iUrl;
    private String iKey;


    public Icon(){

    }

    public Icon(String iname, String iurl){
        if(iname.trim().equals("")){
            iname = "No Name";
        }
        iName = iname;
        iUrl = iurl;
    }
//3:00
    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }

    public String getiUrl() {
        return iUrl;
    }

    public void setiUrl(String iUrl) {
        this.iUrl = iUrl;
    }

    public String getiKey() {
        return iKey;
    }

    public void setiKey(String iKey) {
        this.iKey = iKey;
    }

}
