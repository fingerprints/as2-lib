/**
 * The FreeBSD Copyright
 * Copyright 1994-2008 The FreeBSD Project. All rights reserved.
 * Copyright (C) 2013 Philip Helger ph[at]phloc[dot]com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE FREEBSD PROJECT ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE FREEBSD PROJECT OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation
 * are those of the authors and should not be interpreted as representing
 * official policies, either expressed or implied, of the FreeBSD Project.
 */
package com.helger.as2lib.message;

import com.helger.as2lib.params.AbstractParameterParser;
import com.helger.as2lib.params.CompositeParameters;
import com.helger.as2lib.params.DateParameters;
import com.helger.as2lib.params.InvalidParameterException;
import com.helger.as2lib.params.MessageParameters;
import com.helger.as2lib.params.RandomParameters;
import com.helger.as2lib.partner.CPartnershipIDs;
import com.helger.as2lib.partner.Partnership;

public class AS2Message extends AbstractMessage
{
  public static final String PROTOCOL_AS2 = "as2";

  public String getProtocol ()
  {
    return PROTOCOL_AS2;
  }

  @Override
  public String generateMessageID ()
  {
    final CompositeParameters params = new CompositeParameters (false).add ("date", new DateParameters ())
                                                                      .add ("msg", new MessageParameters (this))
                                                                      .add ("rand", new RandomParameters ());

    String sIDFormat = getPartnership ().getAttribute (CPartnershipIDs.PA_MESSAGEID);
    if (sIDFormat == null)
      sIDFormat = "OPENAS2-$date.ddMMyyyyHHmmssZ$-$rand.1234$@$msg.sender.as2_id$_$msg.receiver.as2_id$";

    final StringBuilder sMessageID = new StringBuilder ();
    sMessageID.append ('<');
    try
    {
      sMessageID.append (AbstractParameterParser.parse (sIDFormat, params));
    }
    catch (final InvalidParameterException ex)
    {
      // useless, but what to do?
      sMessageID.append (sIDFormat);
    }
    sMessageID.append ('>');
    return sMessageID.toString ();
  }

  public boolean isRequestingMDN ()
  {
    final Partnership aPartnership = getPartnership ();
    final boolean bRequesting = aPartnership.getAttribute (CPartnershipIDs.PA_AS2_MDN_TO) != null ||
                                aPartnership.getAttribute (CPartnershipIDs.PA_AS2_MDN_OPTIONS) != null;
    if (bRequesting)
      return true;

    final boolean bRequested = getHeader ("Disposition-Notification-To") != null ||
                               getHeader ("Disposition-Notification-Options") != null;
    return bRequested;
  }

  public boolean isRequestingAsynchMDN ()
  {
    final Partnership aPartnership = getPartnership ();
    final boolean bRequesting = (aPartnership.getAttribute (CPartnershipIDs.PA_AS2_MDN_TO) != null || aPartnership.getAttribute (CPartnershipIDs.PA_AS2_MDN_OPTIONS) != null) &&
                                aPartnership.getAttribute (CPartnershipIDs.PA_AS2_RECEIPT_OPTION) != null;
    if (bRequesting)
      return true;

    final boolean bRequested = (getHeader ("Disposition-Notification-To") != null || (getHeader ("Disposition-Notification-Options") != null)) &&
                               (getHeader ("Receipt-Delivery-Option") != null);
    return bRequested;
  }

  public String getAsyncMDNurl ()
  {
    return getHeader ("Receipt-Delivery-Option");
  }
}
