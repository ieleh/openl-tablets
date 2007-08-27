/*
 * Created on Jun 6, 2003
 *
 * Developed by Intelligent ChoicePoint Inc. 2003
 */
 
package org.openl.binding.impl;

import org.openl.binding.IBindingContext;
import org.openl.binding.IBoundNode;
import org.openl.syntax.ISyntaxNode;
import org.openl.syntax.impl.LiteralNode;
import org.openl.types.java.JavaOpenClass;

/**
 * @author snshor
 *
 */
public class DoubleNodeBinder extends ANodeBinder
{

	/* (non-Javadoc)
	 * @see org.openl.binding.INodeBinder#bind(org.openl.parser.ISyntaxNode, org.openl.env.IOpenEnv, org.openl.binding.IBindingContext)
	 */
	public IBoundNode bind(
		ISyntaxNode node,
		IBindingContext bindingContext)
	{
		String s = ((LiteralNode)node).getImage();
  	
		int len = s.length();
  	
		if (Character.toUpperCase(s.charAt(len - 1)) == 'F')
		{
			return new LiteralBoundNode(node, Float.valueOf(s.substring(0, len - 1)), JavaOpenClass.FLOAT);
		}
		  	
		return new LiteralBoundNode(node, Double.valueOf(s), JavaOpenClass.DOUBLE);
	}



}
