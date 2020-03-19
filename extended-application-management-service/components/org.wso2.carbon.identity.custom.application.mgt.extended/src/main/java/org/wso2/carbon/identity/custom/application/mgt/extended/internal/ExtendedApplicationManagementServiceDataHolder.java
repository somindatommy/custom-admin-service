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

package org.wso2.carbon.identity.custom.application.mgt.extended.internal;

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.idp.mgt.IdpManager;

/**
 * Data holder class for extended application management service.
 */
public class ExtendedApplicationManagementServiceDataHolder {

    private static volatile ExtendedApplicationManagementServiceDataHolder instance =
            new ExtendedApplicationManagementServiceDataHolder();
    private ApplicationManagementService applicationManagementService;
    private IdpManager idpManager;

    private ExtendedApplicationManagementServiceDataHolder() {

    }

    /**
     * Get an instance of the ExtendedApplicationManagementServiceDataHolder.
     *
     * @return ExtendedApplicationManagementServiceDataHolder
     */
    public static ExtendedApplicationManagementServiceDataHolder getInstance() {

        return instance;
    }

    /**
     * Get Application Management Service.
     *
     * @return ApplicationManagementService instance
     * {@link ApplicationManagementService}
     */
    public ApplicationManagementService getApplicationManagementService() {

        return applicationManagementService;
    }

    /**
     * Set Application Management Service.
     *
     * @param applicationManagementService ApplicationManagementService
     *                                     {@link ApplicationManagementService}
     */
    public void setApplicationManagementService(
            ApplicationManagementService applicationManagementService) {

        this.applicationManagementService = applicationManagementService;
    }

    /**
     * Get IdpManager instance.
     *
     * @return IdpManager {@link IdpManager}
     */
    public IdpManager getIdpManager() {

        return idpManager;
    }

    /**
     * Set IdpManager instance.
     *
     * @param idpManager IdpManager {@link IdpManager}
     */
    public void setIdpManager(IdpManager idpManager) {

        this.idpManager = idpManager;
    }
}
