package common;

/**
 * Created by Denys Kovalenko on 9/29/2014.
 */
public interface MessageListener {

    public void onMessage( String topicName, String payload );

}
