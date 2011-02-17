package de.gfred.lbbms.service.resources;

import de.gfred.lbbms.service.logic.interfaces.IMessageAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.representations.MessageRepresentation;
import de.gfred.lbbms.service.resources.util.ResourceValues;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.16
 */
@Stateless
@Path(MessagesRessource.URI_TEMPALTE)
public class MessagesRessource {
    public static final String URI_TEMPALTE = SingleCustomerResource.URI_TEMPALTE + "/messages";

    @EJB
    private IMessageAdministrationLocal messagingBean;

    @EJB
    private ICustomerAdministrationLocal customerBean;

    @POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response sendMessage(@PathParam(ResourceValues.CUSTOMER_ID) final Long id, final MessageRepresentation msg){
        if(id==null || customerBean.getCustomerById(id)==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //TODO unterscheide verschiedene msg typen - concept
        messagingBean.sendBroadcastMessage(id,msg.getReceiver(),msg.getContent(),msg.getLocation().getLon(),msg.getLocation().getLat());

        //TODO korrekter respone mit location header 
        return Response.status(Response.Status.OK).build();
    }
}
