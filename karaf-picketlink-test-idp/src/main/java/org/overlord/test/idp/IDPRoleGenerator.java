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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.picketlink.identity.federation.core.interfaces.RoleGenerator;

/**
 * @author eric.wittmann@redhat.com
 */
public class IDPRoleGenerator implements RoleGenerator {

    /**
     * @see org.picketlink.identity.federation.core.interfaces.RoleGenerator#generateRoles(java.security.Principal)
     */
    @Override
    public List<String> generateRoles(Principal principal) {
        List<String> roles = new ArrayList<String>();
        roles.add("manager"); //$NON-NLS-1$
        return roles;
    }

}
