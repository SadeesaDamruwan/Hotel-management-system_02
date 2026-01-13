package com.hotel.management.util;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;

public class EnvLoader {
    
    private static Dotenv dotenv;
    
    static {
        dotenv = loadDotenv();
    }
    
    private static Dotenv loadDotenv() {
        try {
            // Try to get the directory where the JAR is running from
            File jarLocation = new File(EnvLoader.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());
            
            String jarDir = jarLocation.isDirectory() ? jarLocation.getAbsolutePath() : jarLocation.getParent();
            
            // First try: JAR directory (for installed version)
            File envFile = new File(jarDir, ".env");
            if (envFile.exists() && envFile.canRead()) {
                return Dotenv.configure()
                        .directory(jarDir)
                        .ignoreIfMissing()
                        .load();
            }
            
            // Second try: Current working directory (for development)
            envFile = new File(".env");
            if (envFile.exists() && envFile.canRead()) {
                return Dotenv.configure()
                        .directory("./")
                        .ignoreIfMissing()
                        .load();
            }
            
            // Return empty dotenv if not found - app will use default values
            return Dotenv.configure().ignoreIfMissing().load();
            
        } catch (Exception e) {
            // Silently handle errors in packaged app
            return Dotenv.configure().ignoreIfMissing().load();
        }
    }
    
    public static Dotenv getDotenv() {
        return dotenv;
    }
    
    public static String get(String key) {
        return dotenv != null ? dotenv.get(key) : null;
    }
    
    public static String get(String key, String defaultValue) {
        return dotenv != null ? dotenv.get(key, defaultValue) : defaultValue;
    }
}
