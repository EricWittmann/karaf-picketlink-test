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

package org.overlord.test.idp;

import javax.security.auth.login.LoginException;

import org.picketlink.identity.federation.web.interfaces.ILoginHandler;

/**
 * @author eric.wittmann@redhat.com
 */
public class IDPLoginHandler implements ILoginHandler {

    /**
     * @see org.picketlink.identity.federation.web.interfaces.ILoginHandler#authenticate(java.lang.String, java.lang.Object)
     */
    @Override
    public boolean authenticate(String username, Object credential) throws LoginException {
        // welcome aboard, everyone!
        return true;
    }

}
