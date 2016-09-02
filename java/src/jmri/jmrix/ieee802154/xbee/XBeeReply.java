// XBeeReply.java
package jmri.jmrix.ieee802154.xbee;

import com.digi.xbee.api.packet.XBeePacket;
import com.digi.xbee.api.packet.GenericXBeePacket;

/**
 * Contains the data payload of a serial reply packet. Note that its _only_ the
 * payload.
 *
 * @author	Bob Jacobsen Copyright (C) 2002, 2006, 2007, 2008 Converted to
 * multiple connection
 * @author kcameron Copyright (C) 2011 Modified for IEEE 802.15.4 connection
 * @author Paul Bender Copyright (C) 2013
 * @version $Revision$
 */
public class XBeeReply extends jmri.jmrix.ieee802154.IEEE802154Reply {

    XBeePacket xbresponse = null;

    // create a new one
    public XBeeReply() {
        super();
        setBinary(true);
    }

    public XBeeReply(String s) {
        super(s);
        setBinary(true);
        byte ba[] = jmri.util.StringUtil.bytesFromHexString(s);
        xbresponse = new GenericXBeePacket(ba);
    }

    public XBeeReply(XBeeReply l) {
        super(l);
        xbresponse = l.xbresponse;
        byte data[] = xbresponse.getPacketData();
        for(int i=0;i<data.length;i++) {
           _dataChars[i] = (int) data[i];
        }
        setBinary(true);
    }

    public XBeeReply(XBeePacket xbr) {
        super();
        xbresponse = xbr;
        byte data[] = xbr.getPacketData();
        for(int i=0;i<data.length;i++) {
           _dataChars[i] = (int) data[i];
        }
        setBinary(true);
    }

    public String toMonitorString() {
        return xbresponse.toString();
    }

    public String toString() {
        String s = "";
        int packet[] = xbresponse.getProcessedPacketBytes();
        for(int i=0;i<packet.length;i++) {
            s=jmri.util.StringUtil.appendTwoHexFromInt(packet[i],s);
        }
        return s;
    }

    public XBeePacket getXBeeResponse() {
        return xbresponse;
    }

    public void setXBeeResponse(XBeePacket xbr) {
        xbresponse = xbr;
    }

}

/* @(#)XBeeReply.java */
