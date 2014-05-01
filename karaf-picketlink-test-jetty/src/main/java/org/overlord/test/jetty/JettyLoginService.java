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

import java.io.IOException;

import org.eclipse.jetty.security.MappedLoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.security.Password;


/**
 * Simple login service.
 *
 * @author eric.wittmann@redhat.com
 */
public class JettyLoginService extends MappedLoginService {
    
    /**
     * Constructor.
     */
    public JettyLoginService() {
    }

    /**
     * @see org.eclipse.jetty.security.MappedLoginService#loadUser(java.lang.String)
     */
    @Override
    protected UserIdentity loadUser(String username) {
        return null;
    }

    /**
     * @see org.eclipse.jetty.security.MappedLoginService#loadUsers()
     */
    @Override
    protected void loadUsers() throws IOException {
        Credential credential = new Password("admin"); //$NON-NLS-1$
        String[] roles = new String[] { "manager" }; //$NON-NLS-1$
        putUser("admin", credential, roles); //$NON-NLS-1$

        credential = new Password("eric"); //$NON-NLS-1$
        roles = new String[] { "sales" }; //$NON-NLS-1$
        putUser("eric", credential, roles); //$NON-NLS-1$
    }

}
