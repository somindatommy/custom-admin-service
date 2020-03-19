/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.identity.custom.application.mgt.extended;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.AbstractAdmin;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.custom.application.mgt.extended.internal.service.impl.DefaultExtendedApplicationManagementServiceImpl;
import org.wso2.carbon.identity.custom.application.mgt.extended.model.SuccessResponse;

/**
 * Extended Application Management Admin Service.
 */
public class ExtendedApplicationManagementAdminService extends AbstractAdmin {

    private static Log log = LogFactory.getLog(ExtendedApplicationManagementAdminService.class);

    /**
     * Add Identity Provider to the given authentication step of the Service Provider.
     *
     * @param applicationName      Name of the application
     * @param identityProviderName Name of the identity provider
     * @param authenticationStepId Authentication step
     * @throws IdentityApplicationManagementException If an error occurred while adding the identity provider for the
     *                                                given authentication step.
     */
    public SuccessResponse addIdentityProviderToApplication(String applicationName, String identityProviderName,
                                                            int authenticationStepId) throws
            IdentityApplicationManagementException {

        try {
            DefaultExtendedApplicationManagementServiceImpl applicationManagementService =
                    DefaultExtendedApplicationManagementServiceImpl.getInstance();
            return applicationManagementService
                    .addIdentityProviderToApplication(applicationName, identityProviderName, authenticationStepId);
        } catch (IdentityApplicationManagementException e) {
            if (log.isDebugEnabled()) {
                String error = String.format("Error occurred while adding an Identity provider: %s to application: %s" +
                        " for authentication step: %d", identityProviderName, applicationName, authenticationStepId);
                log.debug(error, e);
            }
            throw e;
        }
    }

    /**
     * Remove Identity Provider from the given authentication step of the Service Provider.
     *
     * @param applicationName      Name of the application
     * @param identityProviderName Name of the identity provider
     * @param authenticationStepId Authentication step
     * @throws IdentityApplicationManagementException If an error occurred while removing the identity provider for the
     *                                                given authentication step.
     */
    public SuccessResponse removeIdentityProviderFromApplication(String applicationName, String identityProviderName,
                                                                 int authenticationStepId)
            throws IdentityApplicationManagementException {

        try {
            DefaultExtendedApplicationManagementServiceImpl applicationManagementService =
                    DefaultExtendedApplicationManagementServiceImpl.getInstance();
            return applicationManagementService
                    .removeIdentityProviderFromApplication(applicationName, identityProviderName, authenticationStepId);
        } catch (IdentityApplicationManagementException e) {
            if (log.isDebugEnabled()) {
                String error = String.format("Error occurred while removing Identity provider: %s from application: " +
                        "%s in authentication step: %d", identityProviderName, applicationName, authenticationStepId);
                log.debug(error, e);
            }
            throw e;
        }
    }
}
