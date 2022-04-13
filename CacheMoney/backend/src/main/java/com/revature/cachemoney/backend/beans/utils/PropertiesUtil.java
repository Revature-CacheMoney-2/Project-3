package com.revature.cachemoney.backend.beans.utils;

import java.io.*;
import java.util.Properties;

/**
 * @author Mika Nelson, Dylan Wilson, Cullen Kuch, Max Hilken, Tyler Seufert
 */
public class PropertiesUtil {
    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream input = null;
        Properties prop = null;
        try {
            input = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(input);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            input.close();
        }
        return prop;
    }

    public static String getProperty(String fileName, String property) {
        try {
            return readPropertiesFile(fileName).getProperty(property);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHTML(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader br= new BufferedReader(fr);
        StringBuilder content = new StringBuilder(1024);
        String s;
        while((s=br.readLine()) != null)
        {
            content.append(s);
        }
        return content.toString();
    }
}
