package org.openl.rules.webstudio.web.admin;

import org.openl.rules.repository.RepositoryFactoryInstatiator;

public enum JcrType {
    LOCAL(org.openl.rules.repository.factories.LocalJackrabbitRepositoryFactory.class),
    RMI(org.openl.rules.repository.factories.RmiJackrabbitRepositoryFactory.class),
    WEBDAV(org.openl.rules.repository.factories.WebDavRepositoryFactory.class),
    DB(org.openl.rules.repository.factories.JdbcDBRepositoryFactory.class),
    JNDI(org.openl.rules.repository.factories.JndiDBRepositoryFactory.class);

    public static JcrType findByAccessType(String accessType) {
        for (JcrType jcrType : values()) {
            if (jcrType.accessType.equals(accessType)) {
                return jcrType;
            }
        }

        return null;
    }

    public static JcrType findByFactory(String factoryClassName) {
        String className = RepositoryFactoryInstatiator.changeClassName(factoryClassName);
        for (JcrType jcrType : values()) {
            if (jcrType.factoryClassName.equals(className)) {
                return jcrType;
            }
        }

        return null;
    }

    private final String accessType;
    private final String factoryClassName;

    JcrType(Class factoryClass) {
        this.factoryClassName = factoryClass.getName();
        this.accessType = name().toLowerCase();
    }

    public String getFactoryClassName() {
        return factoryClassName;
    }
}
