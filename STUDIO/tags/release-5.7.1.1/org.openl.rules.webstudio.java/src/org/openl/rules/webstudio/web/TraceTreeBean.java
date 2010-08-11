package org.openl.rules.webstudio.web;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openl.commons.web.jsf.FacesUtils;
import org.openl.rules.ui.ProjectModel;
import org.openl.rules.ui.TraceHelper;
import org.openl.rules.ui.WebStudio;
import org.openl.rules.ui.tree.richfaces.TreeBuilder;
import org.openl.rules.ui.tree.richfaces.TraceTreeBuilder;
import org.openl.rules.webstudio.web.util.Constants;
import org.openl.rules.webstudio.web.util.WebStudioUtils;
import org.openl.util.StringTool;
import org.openl.util.tree.ITreeElement;
import org.openl.vm.trace.Tracer;
import org.richfaces.model.TreeNode;

/**
 * Request scope managed bean providing logic for trace tree page of OpenL Studio.
 */
public class TraceTreeBean {

    public static final String TRACER_NAME = "tracer";

    public TraceTreeBean() {
    }

    public TreeNode<?> getTree() {
        WebStudio studio = WebStudioUtils.getWebStudio();
        String uri = FacesUtils.getRequestParameter(Constants.REQUEST_PARAM_URI);
        String testName = FacesUtils.getRequestParameter(Constants.REQUEST_PARAM_TEST_NAME);
        if (testName != null) {
            testName = StringTool.decodeURL(testName);
        }
        String testId = FacesUtils.getRequestParameter(Constants.REQUEST_PARAM_TEST_ID);
        if (StringUtils.isNotBlank(uri)) {
            studio.setTableUri(uri);
            ProjectModel model = studio.getModel();
            Tracer tracer = model.traceElement(uri, testName, testId);

            TraceHelper traceHelper = (TraceHelper) FacesUtils.getSessionParam(TRACER_NAME);
            
            if (traceHelper == null) {
                traceHelper = new TraceHelper();
                Map<String, Object> sessionMap = FacesUtils.getSessionMap();
                sessionMap.put(TRACER_NAME, traceHelper);
            }
            
            ITreeElement<?> tree = traceHelper.getTraceTree(tracer);
            if (tree != null) {
                TreeBuilder treeBuilder = new TraceTreeBuilder(tree, traceHelper);
                TreeNode<?> rfTree = treeBuilder.build();
                return rfTree;
            }
        }
        return null;
    }

}
