/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.custom.application.mgt.extended.services;

import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.custom.application.mgt.extended.model.SuccessResponse;

/**
 * Service Interface of Extended Application Management Services.
 */
public interface ExtendedApplicationManagementService {

    /**
     * Add Identity Provider to the given authentication step of the Service Provider.
     *
     * @param applicationName      Name of the service provider
     * @param identityProviderName Name of the identity provider
     * @param authenticationStepId Authentication step Id
     * @throws IdentityApplicationManagementException If an error occurred while adding the identity provider for the
     *                                                given authentication step.
     */
    SuccessResponse addIdentityProviderToApplication(String applicationName, String identityProviderName,
                                                     int authenticationStepId)
            throws
            IdentityApplicationManagementException;

    /**
     * Remove Identity Provider from the given authentication step of the Service Provider.
     *
     * @param applicationName      Name of the service provider
     * @param identityProviderName Name of the identity provider
     * @param authenticationStepId Authentication step Id
     * @throws IdentityApplicationManagementException If an error occurred while removing the identity provider for the
     *                                                given authentication step.
     */
    SuccessResponse removeIdentityProviderFromApplication(String applicationName, String identityProviderName,
                                                          int authenticationStepId)
            throws IdentityApplicationManagementException;
}
