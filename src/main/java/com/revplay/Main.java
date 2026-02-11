package com.revplay;

import com.revplay.console.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        AppConfig.initialize();
        ConsoleMenu.start();
    }
}