package de.gfred.lbbms.service.logic;

import de.gfred.lbbms.service.logic.interfaces.IMessageAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.util.BroadcastMessageKeys;
import de.gfred.lbbms.service.util.ConfigurationValues;
import de.gfred.lbbms.service.util.LocationCalculator;
import de.gfred.lbbms.service.util.MessageType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.17
 */
@Stateless
public class MessageAdministration implements IMessageAdministrationLocal {

    @EJB
    private ICustomerAdministrationLocal customerBean;

    @Resource(name = "TopicConnectionFactory")
    private TopicConnectionFactory connectionFactory;

    @Resource(mappedName = "lbbmstopic")
    private Topic broadcastTopic;

    private TopicConnection connection;

    @PostConstruct
    public void openConnection() {
        try {
            connection = connectionFactory.createTopicConnection();
        } catch (JMSException ex) {
            Logger.getLogger(MessageAdministration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                Logger.getLogger(MessageAdministration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void sendBroadcastMessage(Long sender, String receiver, String content, Double longitude, Double latitude) {
        try {
            TopicSession session = connection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
            TopicPublisher publisher = session.createPublisher(broadcastTopic);

            TextMessage msg = session.createTextMessage();
            msg.setText(content);
            msg.setDoubleProperty(BroadcastMessageKeys.LONGITUDE, longitude);
            msg.setDoubleProperty(BroadcastMessageKeys.LATITUDE, latitude);

            //msg.setDoubleProperty("range", Double.parseDouble(receiver));
            msg.setDoubleProperty(BroadcastMessageKeys.RANGE, ConfigurationValues.DEFAULT_RANGE);

            Customer customer = customerBean.getCustomerById(sender);
            if (customer != null) {
                msg.setStringProperty(BroadcastMessageKeys.SENDER_NAME, customer.getName());
            }

            msg.setJMSType(MessageType.BROADCAST.toString());
            msg.setJMSTimestamp(System.currentTimeMillis());
            msg.setJMSExpiration(ConfigurationValues.DEFAULT_EXPIRATION_TIME);

            publisher.publish(msg);
        } catch (JMSException ex) {
            Logger.getLogger(MessageAdministration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Message> receiveBroadcastMessages(Long id, Double longitude, Double latitude) {
        final List<Message> messages = new ArrayList<Message>();

        try {
            Customer customer = customerBean.getCustomerById(id);
            
            if (customer != null) {
                double[] boundings = LocationCalculator.calculateBoundingCoordinates(longitude, latitude, ConfigurationValues.DEFAULT_RANGE);
                double minLat = boundings[0];
                double minLon = boundings[1];
                double maxLat = boundings[2];
                double maxLon = boundings[3];

                String filterQuery = BroadcastMessageKeys.LATITUDE + " >= " + minLat + " AND " + BroadcastMessageKeys.LATITUDE + " <= " + maxLat
                        + " AND " + BroadcastMessageKeys.LONGITUDE + " >= " + minLon + " AND " + BroadcastMessageKeys.LONGITUDE + " <= " + maxLon;

                connection.start();
                TopicSession session = connection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
                TopicSubscriber subscriber = session.createDurableSubscriber(broadcastTopic, customer.getName(), filterQuery, false);

                subscriber.setMessageListener(new MessageListener() {

                    @Override
                    public void onMessage(Message message) {
                        messages.add(message);
                    }
                });

                connection.stop();
                subscriber.close();
            }
        } catch (JMSException ex) {
            Logger.getLogger(MessageAdministration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messages;
    }

    @Override
    public void sendPrivateMessage() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    @Override
    public void sendTwitterMessage() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    @Override
    public void sendFacebookMessage() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    @Override
    public void sendSMS() {
        throw new UnsupportedOperationException("Not supported yet!");
    }
}
