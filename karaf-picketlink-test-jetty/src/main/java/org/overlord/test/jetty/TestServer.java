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
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("**** Starting Development Server ****");

        ContextHandlerCollection handlers = new ContextHandlerCollection();
        configureJetty(handlers);

        // Create the server.
        int serverPort = 8181;
        Server server = new Server(serverPort);
        server.setHandler(handlers);
        server.start();
        long endTime = System.currentTimeMillis();
        System.out.println("******* Started in " + (endTime - startTime) + "ms");

        server.join();
    }

    /**
     * @param handlers
     */
    private static void configureJetty(ContextHandlerCollection handlers) throws Exception {
        File root = new File(".").getCanonicalFile();
        if (!(new File(root, "root.txt")).exists()) {
            throw new Exception("Please make sure the CWD is the root of the 'karaf-picketlink-test-jetty' module (there should be a root.txt file there).");
        }
        
        ServletContextHandler idpCtx = createIDP(root);
        ServletContextHandler sp1Ctx = createSP1(root);

        handlers.addHandler(idpCtx);
        handlers.addHandler(sp1Ctx);
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

}
