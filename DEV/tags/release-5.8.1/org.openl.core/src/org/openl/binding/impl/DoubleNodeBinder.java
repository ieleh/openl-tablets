/*
 * Created on Jun 6, 2003
 *
 * Developed by Intelligent ChoicePoint Inc. 2003
 */

package org.openl.binding.impl;

import org.openl.binding.IBindingContext;
import org.openl.binding.IBoundNode;
import org.openl.binding.impl.cast.IOpenCast;
import org.openl.syntax.ISyntaxNode;
import org.openl.syntax.impl.LiteralNode;
import org.openl.types.IOpenClass;
import org.openl.types.java.JavaOpenClass;

/**
 * @author snshor
 *
 */
public class DoubleNodeBinder extends ANodeBinder {

    public IBoundNode bind(ISyntaxNode node, IBindingContext bindingContext) {
        String s = ((LiteralNode) node).getImage();

        int len = s.length();

        if (Character.toUpperCase(s.charAt(len - 1)) == 'F') {
            return new LiteralBoundNode(node, Double.valueOf(s.substring(0, len - 1)), JavaOpenClass.DOUBLE);
        }

        return new LiteralBoundNode(node, Double.valueOf(s), JavaOpenClass.DOUBLE);
    }
    
    @Override
    public IBoundNode bindType(ISyntaxNode node, IBindingContext bindingContext, IOpenClass type) throws Exception {
        IBoundNode boundNode = bindChildNode(node, bindingContext);
        IOpenCast cast = getCast(boundNode, type, bindingContext, false);

        if (cast == null) {
            return boundNode;
        }

        return new CastNode(null, boundNode, cast, type);
    }
}