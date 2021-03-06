package org.openl.extension.xmlrules.model.single.node;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.openl.extension.xmlrules.model.single.Cell;

@XmlSeeAlso({
        StringNode.class,
        NumberNode.class,
        BooleanNode.class,

        RangeNode.class,
        NamedRangeNode.class,
        ExpressionNode.class,
        FunctionNode.class,
        IfNode.class,
        IfErrorNode.class,
        FilterNode.class,

        ArrayNode.class,

        FailureNode.class
})
public abstract class Node {
    private boolean rootNode = false;

    @XmlTransient
    public boolean isRootNode() {
        return rootNode;
    }

    public void setRootNode(boolean rootNode) {
        this.rootNode = rootNode;
    }

    public void configure(String currentWorkbook, String currentSheet, Cell cell) {
        // Do nothing
    }

    public abstract String toOpenLString();
}
