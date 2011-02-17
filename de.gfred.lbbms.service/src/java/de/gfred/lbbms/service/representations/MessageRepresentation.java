package de.gfred.lbbms.service.representations;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/*XML
<message>
    <type><type>
    <content></content>
    <receiver></receiver>
    <location>
        <lat></lat>
        <lon></lon>
    </location>
</message>
 */
/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@XmlRootElement(name="message")
public class MessageRepresentation {
    private String type;
    private String content;
    private LocationRepresentation location;
    private String receiver;
    private Date sendDate;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocationRepresentation getLocation() {
        return location;
    }

    public void setLocation(LocationRepresentation location) {
        this.location = location;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
   
}
