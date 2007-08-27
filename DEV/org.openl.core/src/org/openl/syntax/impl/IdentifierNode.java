/*
 * Created on May 13, 2003
 *
 * Developed by Intelligent ChoicePoint Inc. 2003
 */
 
package org.openl.syntax.impl;

import org.openl.IOpenSourceCodeModule;
import org.openl.util.text.TextInterval;

/**
 * @author snshor
 *
 */
public class IdentifierNode extends TerminalNode
{
	String identifier;
	
	public IdentifierNode(String type, TextInterval pos, String identifier, IOpenSourceCodeModule module)
	{
		super(type, pos, module);
		this.identifier = identifier;
	}

  /**
   * @return
   */
  public String getIdentifier()
  {
    return identifier;
  }
  
  

	/**
	 *
	 */

	protected void printMySelf(int level, StringBuffer buf)
	{
		super.printMySelf(level, buf);
		buf.append("=" + identifier);
	}

}
