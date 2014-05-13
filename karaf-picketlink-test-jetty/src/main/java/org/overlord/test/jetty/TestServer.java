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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author eric.wittmann@redhat.com
 */
public class TestServer {

    private static final int PORT = 7070;
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("**** Starting Development Server ****"); //$NON-NLS-1$

        ContextHandlerCollection handlers = new ContextHandlerCollection();
        configureJetty(handlers);

        // Create the server.
        int serverPort = PORT;
        Server server = new Server(serverPort);
        server.setHandler(handlers);
        server.start();
        long endTime = System.currentTimeMillis();
        System.out.println("******* Started in " + (endTime - startTime) + "ms"); //$NON-NLS-1$ //$NON-NLS-2$

        server.join();
    }

    /**
     * @param handlers
     */
    private static void configureJetty(ContextHandlerCollection handlers) throws Exception {
        String rootPath = System.getProperty("karaf-picketlink-test-jetty.root"); //$NON-NLS-1$
        if (rootPath == null) {
            rootPath = System.getProperty("basedir"); //$NON-NLS-1$
        }
        if (rootPath == null) {
            rootPath = "."; //$NON-NLS-1$
        }
        File root = new File(rootPath).getCanonicalFile();
        System.out.println("Attempting to use root path: " + root); //$NON-NLS-1$
        if (!(new File(root, "root.txt")).exists()) { //$NON-NLS-1$
            throw new Exception(
                      "Please make sure the CWD is the root of the 'karaf-picketlink-test-jetty' module \n" //$NON-NLS-1$
                    + "(there should be a root.txt file there).  Alternatively you can set the following\n" //$NON-NLS-1$
                    + "system property:  karaf-picketlink-test-jetty.root"); //$NON-NLS-1$
        }
        
        ServletContextHandler idpCtx = createIDP(root);
        ServletContextHandler sp1Ctx = createSP1(root);
        ServletContextHandler sp2Ctx = createSP2(root);

        handlers.addHandler(idpCtx);
        handlers.addHandler(sp1Ctx);
        handlers.addHandler(sp2Ctx);
    }

    /**
     * @param root
     * @throws IOException
     */
    private static ServletContextHandler createIDP(File root) throws IOException {
        File spWar = new File(root, "../karaf-picketlink-test-idp/target/karaf-picketlink-test-idp-1.0.0.war").getCanonicalFile(); //$NON-NLS-1$
        System.out.println("IDP WAR: " + spWar); //$NON-NLS-1$
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/idp"); //$NON-NLS-1$
        webapp.setWar(spWar.getCanonicalPath());
        webapp.getSecurityHandler().setLoginService(new JettyLoginService());
        webapp.getSecurityHandler().setSessionRenewedOnAuthentication(false);
        return webapp;
    }

    /**
     * @param root
     */
    private static ServletContextHandler createSP1(File root) throws Exception {
        File spWar = new File(root, "../karaf-picketlink-test-sp1/target/karaf-picketlink-test-sp1-1.0.0.war").getCanonicalFile(); //$NON-NLS-1$
        System.out.println("SP-1 WAR: " + spWar); //$NON-NLS-1$
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/sp1"); //$NON-NLS-1$
        webapp.setWar(spWar.getCanonicalPath());
        webapp.getSecurityHandler().setSessionRenewedOnAuthentication(false);
        return webapp;
    }

    /**
     * @param root
     */
    private static ServletContextHandler createSP2(File root) throws Exception {
        File spWar = new File(root, "../karaf-picketlink-test-sp2/target/karaf-picketlink-test-sp2-1.0.0.war").getCanonicalFile(); //$NON-NLS-1$
        System.out.println("SP-2 WAR: " + spWar); //$NON-NLS-1$
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/sp2"); //$NON-NLS-1$
        webapp.setWar(spWar.getCanonicalPath());
        webapp.getSecurityHandler().setSessionRenewedOnAuthentication(false);
        return webapp;
    }

}
