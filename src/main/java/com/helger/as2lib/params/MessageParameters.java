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
package com.helger.as2lib.params;

import java.io.File;
import java.util.StringTokenizer;

import javax.mail.internet.ContentDisposition;

import com.helger.as2lib.message.IMessage;

public class MessageParameters extends AbstractParameterParser
{
  public static final String KEY_SENDER = "sender";
  public static final String KEY_RECEIVER = "receiver";
  public static final String KEY_ATTRIBUTES = "attributes";
  public static final String KEY_HEADERS = "headers";
  public static final String KEY_CONTENT_FILENAME = "content-disposition";

  private IMessage m_aTarget;

  public MessageParameters (final IMessage target)
  {
    m_aTarget = target;
  }

  @Override
  public void setParameter (final String key, final String value) throws InvalidParameterException
  {
    final StringTokenizer keyParts = new StringTokenizer (key, ".", false);

    if (keyParts.countTokens () != 2)
    {
      throw new InvalidParameterException ("Invalid key format", this, key, null);
    }

    final String area = keyParts.nextToken ();
    final String areaID = keyParts.nextToken ();

    if (area.equals (KEY_SENDER))
    {
      getTarget ().getPartnership ().setSenderID (areaID, value);
    }
    else
      if (area.equals (KEY_RECEIVER))
      {
        getTarget ().getPartnership ().setReceiverID (areaID, value);
      }
      else
        if (area.equals (KEY_ATTRIBUTES))
        {
          getTarget ().setAttribute (areaID, value);
        }
        else
          if (area.equals (KEY_HEADERS))
          {
            getTarget ().setHeader (areaID, value);
          }
          else
          {
            throw new InvalidParameterException ("Invalid area in key", this, key, null);
          }
  }

  @Override
  public String getParameter (final String key) throws InvalidParameterException
  {
    final StringTokenizer keyParts = new StringTokenizer (key, ".", false);

    if (keyParts.countTokens () != 2)
    {
      throw new InvalidParameterException ("Invalid key format", this, key, null);
    }

    final String area = keyParts.nextToken ();
    final String areaID = keyParts.nextToken ();

    if (area.equals (KEY_SENDER))
    {
      return getTarget ().getPartnership ().getSenderID (areaID);
    }
    else
      if (area.equals (KEY_RECEIVER))
      {
        return getTarget ().getPartnership ().getReceiverID (areaID);
      }
      else
        if (area.equals (KEY_ATTRIBUTES))
        {
          return getTarget ().getAttribute (areaID);
        }
        else
          if (area.equals (KEY_HEADERS))
          {
            return getTarget ().getHeader (areaID);
          }
          else
            if (area.equals (KEY_CONTENT_FILENAME) && areaID.equals ("filename"))
            {
              String returnFilename = "noContentDispositionFilename";
              final String filename = m_aTarget.getContentDisposition ();
              if (filename == null || filename.length () < 1)
                return returnFilename;
              try
              {
                final int pos = filename.lastIndexOf (File.separator);
                if (pos >= 0)
                  returnFilename = filename.substring (0, pos + 1);

                final ContentDisposition cd = new ContentDisposition (filename);

                returnFilename = cd.getParameter ("filename");

              }
              catch (final Exception e)
              {
                e.printStackTrace ();
              }
              return returnFilename;
            }
            else
            {
              throw new InvalidParameterException ("Invalid area in key", this, key, null);
            }
  }

  public void setTarget (final IMessage message)
  {
    m_aTarget = message;
  }

  public IMessage getTarget ()
  {
    return m_aTarget;
  }
}
