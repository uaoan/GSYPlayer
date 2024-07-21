package com.uaoanlao.uaoangsyplayer;

import java.lang.*;
import java.util.Formatter;
public class TimeUtils {
    public TimeUtils() {
    }

    public static String stringForTime(long timeMs){
        long totalSeconds = timeMs/1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds/60)%60;
        long hours = totalSeconds/3600;
        if (timeMs>3600000) {
            return new Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        }else {
            return new Formatter().format("%02d:%02d", minutes, seconds).toString();
        }
    }


}