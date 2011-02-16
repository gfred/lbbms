package de.gfred.lbbms.service.resources;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.16
 */
@Stateless
@Path(ReceivedMessagesRessource.URI_TEMPALTE)
public class ReceivedMessagesRessource {
    public static final String URI_TEMPALTE = MessagesRessource.URI_TEMPALTE + "/receive";
}
