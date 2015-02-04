package properties;

/**
 * Created by Denys Kovalenko on 7/1/2014.
 */
public enum PropertyKeys {
    HOST("host"),
    PORT("port"),
    TOPIC("topic"),
    USERNAME("user"),
    PASSWORD("password");

    private String propertyKey;

    PropertyKeys(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getKey() {
        return propertyKey;
    }

}
