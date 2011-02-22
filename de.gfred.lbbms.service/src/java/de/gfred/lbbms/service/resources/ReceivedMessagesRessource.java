package de.gfred.lbbms.service.resources;


import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.IMessageAdministrationLocal;
import de.gfred.lbbms.service.model.Location;
import de.gfred.lbbms.service.model.Message;
import de.gfred.lbbms.service.representations.LocationRepresentation;
import de.gfred.lbbms.service.representations.MessageRepresentation;
import de.gfred.lbbms.service.resources.util.ResourceValues;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.16
 */
@Stateless
@Path(ReceivedMessagesRessource.URI_TEMPALTE)
public class ReceivedMessagesRessource {
    public static final String URI_TEMPALTE = MessagesRessource.URI_TEMPALTE + "/receive";

    @EJB
    private ICustomerAdministrationLocal customerBean;

    @GET   
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public List<MessageRepresentation> getNewMessagesByLocation(@PathParam(ResourceValues.CUSTOMER_ID) final Long id){
        if(id==null || customerBean.getCustomerById(id)==null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Set<Message> receivedMessages = customerBean.getCustomerById(id).getReceivedMessages();
        List<MessageRepresentation> messages = new ArrayList<MessageRepresentation>();
        
        for(Message msg : receivedMessages){
            messages.add(new MessageRepresentation(msg));
        }

        Collections.sort(messages);

        return messages;
    }
}
