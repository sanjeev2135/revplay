package com.revplay;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppConfig {
    private static final Logger logger = LogManager.getLogger(AppConfig.class);

    public static void initialize() {
        logger.info("RevPlay Console App Starting...");
        logger.info("Using Oracle 26ai JDBC Thin Driver");
    }
}
