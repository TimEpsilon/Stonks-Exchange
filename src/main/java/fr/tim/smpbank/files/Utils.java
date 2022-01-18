package fr.tim.smpbank.files;

import java.util.Date;

public class Utils {

    public static String UnixToDate(long time) {
        Date date= new Date(time);
        String result = (1900+date.getYear())+"/"+(1+date.getMonth())+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+ date.getSeconds();
        return result;
    }
}
