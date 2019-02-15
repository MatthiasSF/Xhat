package common;

import java.io.*;
import java.util.Properties;

/**
 * Manage configuration data in .properties files
 * See the link below for a brief explanation
 * https://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
 */
public class ConfigProperties {

    private final static String localClientConfigFile = "resources/clientConfig.properties";
    private final static String customClientConfigFile = "./clientConfig.properties";

    /**
     * Fetches a property for the client from a config file located in /resources.
     * If a file with the same name exists on the same level as the JAR-file this will be read
     * instead, allowing the user to manually override the config file for some settings. If a
     * custom config file is used but some property is missing, the property is obtained from the
     * default config file instead.
     * @param propName name of the property in the configuration file.
     * @return String with the value for the property
     */
    public static String getClientProperty(String propName){
        String result = null;
        Boolean customConfig = false;
        try {
            File file;
            InputStream inputStream;

            try {
                file = new File(customClientConfigFile);
                inputStream = new FileInputStream(file);
                customConfig = true;
            } catch(FileNotFoundException e){
                System.out.println("customClientConfigFile not found, loading local");
                file = new File(localClientConfigFile);
                inputStream = new FileInputStream(file);
            }

            Properties prop = new Properties();
            prop.load(inputStream);

            if (customConfig && prop.getProperty(propName) == null) {
                file = new File(localClientConfigFile);
                inputStream = new FileInputStream(file);
                prop.load(inputStream);
            }

            System.out.println(prop.getProperty(propName)); //for testing, remove before release
            result = prop.getProperty(propName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
