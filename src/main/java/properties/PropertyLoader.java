package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Denys Kovalenko on 11/17/2014.
 */
public class PropertyLoader {
    private static Properties properties = new Properties();

    public static void loadProperties(String propertyFileName) {
        try (InputStream input = new FileInputStream(propertyFileName)) {
            if (input == null) {
                throw new RuntimeException("Cannot find " + propertyFileName + " file");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(PropertyKeys propertyKey) {
        return properties.getProperty(propertyKey.getKey());
    }

}
