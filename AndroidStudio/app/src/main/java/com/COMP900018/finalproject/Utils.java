package com.COMP900018.finalproject;

public class Utils {
    public static String getDaysDescription(boolean[] checkedItem){
        String[] abbr = new String[]{"Mon","Tue","Wed","Thur","Fri","SAT","Sun"};
        if(checkedItem[0] && checkedItem[1] && checkedItem[2] && checkedItem[3] &&
                checkedItem[4] && checkedItem[5] && checkedItem[6]
        ){
            return "EVERYDAY";
        }
        if(checkedItem[0] && checkedItem[1] && checkedItem[2] && checkedItem[3] &&
                checkedItem[4] && !checkedItem[5] && !checkedItem[6]
        ){
            return "WEEKDAY";
        }
        String str = "";
        for (int i=0;i<checkedItem.length;i++){
            if(checkedItem[i]){
                str+=abbr[i]+" ";
            }
        }
        return str;

    }
}
