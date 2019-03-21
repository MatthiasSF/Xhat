package common;

import sun.security.krb5.Config;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

/**
 * Manage configuration data in .properties files
 * See the link below for a brief explanation
 * https://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
 */
public class ConfigProperties {

    private final static String localClientConfigFile = "/clientConfig.properties";
    private final static String customClientConfigFile = "./clientConfig.properties";
    private final static String localServerConfigFile = "/serverConfig.properties";
    private final static String customServerConfigFile = "./serverConfig.properties";

    /**
     * Fetches a property for the client from a config file located in /resources.
     * If a file with the same name exists on the same level as the JAR-file this will be read
     * instead, allowing the user to manually override the config file for some settings. If a
     * custom config file is used but some property is missing, the property is obtained from the
     * default config file instead.
     * @param propName name of the property in the configuration file.
     * @return String with the value of the property
     */
    public static String getClientProperty(String propName){
        String result = null;
        Boolean customConfig = false;

        try {
            InputStream inputStream;
            Properties prop = new Properties();

            try {
                File file = new File(customClientConfigFile);
                inputStream = new FileInputStream(file);
                customConfig = true;
            } catch(FileNotFoundException e){
                System.out.println("customClientConfigFile not found, loading local"); //TODO: for testing, remove before release
                inputStream = ConfigProperties.class.getResourceAsStream(localClientConfigFile);
                prop.load(inputStream);
            }

            prop.load(inputStream);

            if (customConfig && prop.getProperty(propName) == null) {
                inputStream = ConfigProperties.class.getResourceAsStream(localClientConfigFile);
                prop.load(inputStream);
            }

            System.out.println(prop.getProperty(propName)); //TODO: for testing, remove before release
            result = prop.getProperty(propName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Fetches a property for the server from a config file located in /resources.
     * If a file with the same name exists on the same level as the JAR-file this will be read
     * instead, allowing the user to manually override the config file for some settings. If a
     * custom config file is used but some property is missing, the property is obtained from the
     * default config file instead.
     * @param propName name of the property in the configuration file.
     * @return String with the value of the property
     */
    public static String getServerProperty(String propName){
        String result = null;
        Boolean customConfig = false;

        try {
            InputStream inputStream;
            Properties prop = new Properties();

            try {
                File file = new File(customServerConfigFile);
                inputStream = new FileInputStream(file);
                customConfig = true;
            } catch(FileNotFoundException e){
                System.out.println("customServerConfigFile not found, loading local"); //TODO: for testing, remove before release
                inputStream = ConfigProperties.class.getResourceAsStream(localServerConfigFile);
                prop.load(inputStream);
            }

            prop.load(inputStream);

            if (customConfig && prop.getProperty(propName) == null) {
                inputStream = ConfigProperties.class.getResourceAsStream(localServerConfigFile);
                prop.load(inputStream);
            }

            System.out.println(prop.getProperty(propName)); //TODO: for testing, remove before release
            result = prop.getProperty(propName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
