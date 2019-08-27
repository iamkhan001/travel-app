package com.nstudio.travelreminder.utils;

import android.util.Log;

/**
 * Created by Imran on 08-Nov-17.
 */

public class ColorPicker {


    public static String getColor(String alphabet) {
        Log.e("color","date > "+alphabet);
        String color;
        switch (alphabet) {
            case "0":
            case "26":
            case "A":
                color = "#2E8B57";
                break;
            case "27":
            case "1":
            case "B":
                color = "#6495ED";
                break;
            case "28":
            case "2":
            case "C":
                color = "#FF7F50";
                break;
            case "29":
            case "3":
            case "D":
                color = "#006400";
                break;
            case "30":
            case "4":
            case "E":
                color = "#9932CC";
                break;
            case "31":
            case "5":
            case "F":
                color = "#8FBC8F";
                break;
            case "6":
            case "G":
                color = "#483D8B";
                break;
            case "7":
            case "H":
                color = "#1E90FF";
                break;
            case "8":
            case "I":
                color = "#FFA500";
                break;
            case "9":
            case "J":
                color = "#DAA520";
                break;
            case "+":
            case "K":
            case "10":
                color = "#808080";
                break;
            case "11":
            case "L":
                color = "#FF69B4";
                break;
            case "12":
            case "M":
                color = "#008000";
                break;
            case "13":
            case "N":
                color = "#4B0082";
                break;
            case "14":
            case "O":
                color = "#F08080";
                break;
            case "15":
            case "P":
                color = "#20B2AA";
                break;
            case "16":
            case "Q":
                color = "#778899";
                break;
            case "17":
            case "R":
                color = "#800000";
                break;
            case "18":
            case "S":
                color = "#9370DB";
                break;
            case "19":
            case "T":
                color = "#3CB371";
                break;
            case "20":
            case "U":
                color = "#7B68EE";
                break;
            case "21":
            case "V":
                color = "#6B8E23";
                break;
            case "22":
            case "W":
                color = "#FF4500";
                break;
            case "23":
            case "X":
                color = "#DA70D6";
                break;
            case "24":
            case "Y":
                color = "#DB7093";
                break;
            case "25":
            case "Z":
                color = "#4682B4";
                break;
            default:
                color = "#A0522D";
                break;
        }


        return color;

    }


}
