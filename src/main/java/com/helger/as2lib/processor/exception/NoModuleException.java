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
package com.helger.as2lib.processor.exception;

import java.util.Map;

import com.helger.as2lib.exception.OpenAS2Exception;
import com.helger.as2lib.message.IMessage;

public class NoModuleException extends OpenAS2Exception
{
  private Map <String, Object> m_aOptions;
  private IMessage m_aMsg;
  private String m_sAction;

  public NoModuleException (final String action, final IMessage msg, final Map <String, Object> options)
  {
    super (toString (action, msg, options));
    m_sAction = action;
    m_aMsg = msg;
    m_aOptions = options;
  }

  public void setAction (final String string)
  {
    m_sAction = string;
  }

  public String getAction ()
  {
    return m_sAction;
  }

  public void setMsg (final IMessage message)
  {
    m_aMsg = message;
  }

  public IMessage getMsg ()
  {
    return m_aMsg;
  }

  public void setOptions (final Map <String, Object> map)
  {
    m_aOptions = map;
  }

  public Map <String, Object> getOptions ()
  {
    return m_aOptions;
  }

  @Override
  public String toString ()
  {
    return toString (getAction (), getMsg (), getOptions ());
  }

  protected static String toString (final String action, final IMessage msg, final Map <String, Object> options)
  {
    return "NoModuleException: Requested action: " + action + " Message: " + msg + " Options: " + options;
  }
}
