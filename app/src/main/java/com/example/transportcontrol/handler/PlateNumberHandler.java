package com.example.transportcontrol.handler;

public class PlateNumberHandler {

    public static String handleText(String text) {
        text = text.toUpperCase();
        //the 0, 4 and 5 position must have letters, but it can't be 0
        if (text.charAt(0) == '0') {
            text = text.substring(0, 0) + 'O' + text.substring(1);
        }
        if (text.charAt(4) == '0') {
            text = text.substring(0, 4) + 'O' + text.substring(5);
        }
        if (text.charAt(5) == '0') {
            text = text.substring(0, 5) + 'O' + text.substring(6);
        }
        //positions 1, 2, and 3 must have numbers
        if (text.charAt(1) == 'O') {
            text = text.substring(0, 1) + '0' + text.substring(2);
        }
        if (text.charAt(2) == 'O') {
            text = text.substring(0, 2) + '0' + text.substring(3);
        }
        if (text.charAt(3) == 'O') {
            text = text.substring(0, 3) + '0' + text.substring(4);
        }
        return text.replace("RUS", "").replace(" ", "")
                .replace("\n", "");
    }
}
