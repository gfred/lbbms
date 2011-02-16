package de.gfred.lbbms.service.resources;

import de.gfred.lbbms.service.representations.MessageRepresentation;
import de.gfred.lbbms.service.resources.util.ResourceValues;
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

    @POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response sendMessage(@PathParam(ResourceValues.CUSTOMER_ID) Long id,  MessageRepresentation msg){
        return null;
    }
}
