package com.time;

import java.util.Timer;
import java.util.TimerTask;

public class TimerThread {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 2000);
    }
}
