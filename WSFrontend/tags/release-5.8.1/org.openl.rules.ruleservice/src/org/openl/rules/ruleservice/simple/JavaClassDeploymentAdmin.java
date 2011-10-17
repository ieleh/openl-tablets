package org.openl.rules.ruleservice.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openl.rules.ruleservice.core.OpenLService;
import org.openl.rules.ruleservice.core.ServiceDeployException;
import org.openl.rules.ruleservice.publish.IDeploymentAdmin;

/**
 * Implementation IDeploymentAdmin interface that deploy services like java
 * classes
 * 
 * @author MKamalov
 * 
 */
public class JavaClassDeploymentAdmin implements IDeploymentAdmin {

    // private Log log = LogFactory.getLog(RulesFrontend.class);

    private IRulesFrontend frontend;
    private Map<String, OpenLService> runningServices = new HashMap<String, OpenLService>();

    public IRulesFrontend getFrontend() {
        return frontend;
    }

    public void setFrontend(IRulesFrontend frontend) {
        if (frontend == null) {
            throw new IllegalArgumentException("frontend argument can't be null");
        }

        this.frontend = frontend;
    }

    /**
     * Deploy service
     */
    public OpenLService deploy(OpenLService service) throws ServiceDeployException {
        if (service == null) {
            throw new IllegalArgumentException("service argument can't be null");
        }

        frontend.registerService(service);

        /*
         * if (log.isInfoEnabled()){ log.info("Service with name=" +
         * service.getName() + " has been deployed"); }
         */
        return runningServices.put(service.getName(), service);
    }

    /**
     * Undeploy service
     */
    public OpenLService undeploy(String serviceName) throws ServiceDeployException {
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName argument can't be null");
        }

        frontend.unregisterService(serviceName);

        /*
         * if (log.isInfoEnabled()){ log.info("Service with name=" + serviceName
         * + " has been undeployed"); }
         */
        return runningServices.remove(serviceName);
    }

    /**
     * Gets all deployed services
     */
    public List<OpenLService> getRunningServices() {
        return Collections.unmodifiableList(new ArrayList<OpenLService>(runningServices.values()));
    }

    /**
     * Finds deployed service by name
     */
    public OpenLService findServiceByName(String serviceName) {
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName argument can't be null");
        }

        /*
         * if (log.isDebugEnabled()){ log.info("Finding service with name=" +
         * serviceName); }
         */
        return runningServices.get(serviceName);
    }

}