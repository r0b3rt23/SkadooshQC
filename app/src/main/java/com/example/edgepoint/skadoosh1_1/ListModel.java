package com.example.edgepoint.skadoosh1_1;

public class ListModel {

    private int yAxis;
    private String Label;

    public ListModel(int result, String NameList) {
        yAxis = result;
        Label = NameList;

    }

    public int getCount() {
        return yAxis;
    }

    public String getName() {
        return Label;
    }


//    public int getResult() {
//        return yAxis;
//    }
//
//    public void setResult(int yAxis) {
//        this.yAxis = yAxis;
//    }
//
//    public String getNameList() {
//        return Label;
//    }
//
//    public void setNameList(String Label) {
//        this.Label = Label;
//    }

//    private String Name;


}
