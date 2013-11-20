/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.overlord.test.jetty;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author eric.wittmann@redhat.com
 */
public class TestServer {
    
    private static final String [] USERS = { "admin", "eric", "gary", "kurt" };
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("**** Starting Development Server ****");

        // Create the server.
        int serverPort = 8181;
        Server server = new Server(serverPort);

        ContextHandlerCollection handlers = new ContextHandlerCollection();
        configureJetty(server, handlers);

        server.setHandler(handlers);
        server.start();
        long endTime = System.currentTimeMillis();
        System.out.println("******* Started in " + (endTime - startTime) + "ms");

        server.join();
    }

    /**
     * @param server 
     * @param handlers
     */
    private static void configureJetty(Server server, ContextHandlerCollection handlers) throws Exception {
        String rootPath = System.getProperty("karaf-picketlink-test-jetty.root");
        if (rootPath == null) {
            rootPath = System.getProperty("basedir");
        }
        if (rootPath == null) {
            rootPath = ".";
        }
        File root = new File(rootPath).getCanonicalFile();
        System.out.println("Attempting to use root path: " + root);
        if (!(new File(root, "root.txt")).exists()) {
            throw new Exception(
                      "Please make sure the CWD is the root of the 'karaf-picketlink-test-jetty' module \n"
                    + "(there should be a root.txt file there).  Alternatively you can set the following\n"
                    + "system property:  karaf-picketlink-test-jetty.root");
        }
        
        server.addBean(createLoginService(root));

        ServletContextHandler idpCtx = createIDP(root);
        ServletContextHandler sp1Ctx = createSP1(root);
        ServletContextHandler sp2Ctx = createSP2(root);
        ServletContextHandler basicCtx = createBasic(root);
        ServletContextHandler formCtx = createForm(root);

        handlers.addHandler(idpCtx);
        handlers.addHandler(sp1Ctx);
        handlers.addHandler(sp2Ctx);
        handlers.addHandler(basicCtx);
        handlers.addHandler(formCtx);
    }

    /**
     * @param root
     * @throws IOException
     */
    private static ServletContextHandler createIDP(File root) throws IOException {
        File spWar = new File(root, "../karaf-picketlink-test-idp/target/karaf-picketlink-test-idp-1.0.0.war").getCanonicalFile();
        System.out.println("IDP WAR: " + spWar);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/idp");
        webapp.setWar(spWar.getCanonicalPath());
        return webapp;
    }

    /**
     * @param root
     */
    private static ServletContextHandler createSP1(File root) throws Exception {
        File spWar = new File(root, "../karaf-picketlink-test-sp1/target/karaf-picketlink-test-sp1-1.0.0.war").getCanonicalFile();
        System.out.println("SP-1 WAR: " + spWar);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/sp1");
        webapp.setWar(spWar.getCanonicalPath());
        return webapp;
    }

    /**
     * @param root
     */
    private static ServletContextHandler createSP2(File root) throws Exception {
        File spWar = new File(root, "../karaf-picketlink-test-sp2/target/karaf-picketlink-test-sp2-1.0.0.war").getCanonicalFile();
        System.out.println("SP-2 WAR: " + spWar);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/sp2");
        webapp.setWar(spWar.getCanonicalPath());
        return webapp;
    }

    /**
     * @param root
     */
    private static ServletContextHandler createBasic(File root) throws Exception {
        File spWar = new File(root, "../karaf-picketlink-test-basic/target/karaf-picketlink-test-basic-1.0.0.war").getCanonicalFile();
        System.out.println("Basic WAR: " + spWar);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/basic");
        webapp.setWar(spWar.getCanonicalPath());
        return webapp;
    }

    /**
     * @param root
     */
    private static ServletContextHandler createForm(File root) throws Exception {
        File spWar = new File(root, "../karaf-picketlink-test-form/target/karaf-picketlink-test-form-1.0.0.war").getCanonicalFile();
        System.out.println("Form WAR: " + spWar);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/form");
        webapp.setWar(spWar.getCanonicalPath());
        return webapp;
    }

    /**
     * Creates a basic auth security handler.
     */
    private static SecurityHandler createBasicSecurityHandler() {
        HashLoginService l = new HashLoginService();
        for (String user : USERS) {
            l.putUser(user, Credential.getCredential(user), new String[] {"user"});
        }
        l.setName("overlordrealm");

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"user", "overlorduser"});
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/*");

        ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
        csh.setAuthenticator(new BasicAuthenticator());
        csh.setRealmName("OverlordRealm");
        csh.addConstraintMapping(cm);
        csh.setLoginService(l);

        return csh;
    }
    
    /**
     * @param root 
     */
    private static Object createLoginService(File root) {
        File realmProps = new File(root, "src/main/resources/realm.properties");
        if (!realmProps.isFile()) {
            throw new RuntimeException("Failed to find realm.properties.");
        }
        HashLoginService loginService = new HashLoginService();
        loginService.setName("OverlordRealm");
        loginService.setConfig(realmProps.getAbsolutePath());
        loginService.setRefreshInterval(5);
        return loginService;
    }

}
