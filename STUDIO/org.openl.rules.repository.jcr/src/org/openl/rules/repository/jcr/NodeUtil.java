package org.openl.rules.repository.jcr;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;


/**
 * JCR Node Utility
 * 
 * @author Aleh Bykhavets
 *
 */
public class NodeUtil {

    /**
     * Inquire whether given version is 'root'.
     * 
     * @param v version to be checked
     * @return <code>true</code> if the version is root;
     *         <code>false</code> otherwise;
     * @throws RepositoryException if operation failed
     */
    protected static boolean isRootVersion(Version v) throws RepositoryException {
        String name = v.getName();
        return "jcr:rootVersion".equals(name);
    }

    /**
     * Creates node of given node type.
     * 
     * @param parentNode parent node, where new node is going to be added
     * @param name name of new node
     * @param type node type of new node
     * @param isVersionable whether new node is versionable
     * @return reference on newly created node
     * @throws RepositoryException if operation failed
     */
    protected static Node createNode(Node parentNode, String name, String type, boolean isVersionable) throws RepositoryException {
        if (parentNode.hasNode(name)) {
            throw new RepositoryException("Node '" + name + "' exists at '" + parentNode.getPath() + "' already.");
        }

        if (!parentNode.isCheckedOut()) {
            parentNode.checkout();
        }

        Node n = parentNode.addNode(name, type);
        if (isVersionable) {
            n.addMixin(JcrNT.MIX_VERSIONABLE);
        }

        return n;
    }

    /**
     * Saves changes and does checking if node is checked out.
     * 
     * @param node reference on node to be checked in
     * @throws RepositoryException if operation failed
     */
    protected static void smartCheckin(Node node) throws RepositoryException {
        //TODO: add better handling for ancestors
        Node parentNode = node.getParent();

        if (parentNode.isModified() || node.isModified()) {
            parentNode.save();
        }

        if (node.isCheckedOut()) {
            node.checkin();
        }

        smartCheckinParent(parentNode);
    }
    
    /**
     * Saves changes and does checking if parent node is checked out.
     * 
     * @param parent reference on parent node to be checked in
     * @throws RepositoryException if operation failed
     */
    protected static void smartCheckinParent(Node parent) throws RepositoryException {
        //TODO: add better handling for ancestors
        if (parent.isModified()) {
            parent.save();
        }
        
        if (parent.isCheckedOut()) {
            if (parent.isNodeType(JcrNT.MIX_VERSIONABLE)) {
                parent.checkin();
            }
        }
    }

    /**
     * Checkout node and parent (if needed).
     * 
     * @param node reference on node to be checked in
     * @param openParent whether parent should be checked out
     * @throws RepositoryException if operation failed
     */
    protected static void smartCheckout(Node node, boolean openParent) throws RepositoryException {
        Node parentNode = node.getParent();

        if (!node.isCheckedOut()) {
            node.checkout();
        }

        if (!parentNode.isCheckedOut()) {
            parentNode.checkout();
        }
    }
}
