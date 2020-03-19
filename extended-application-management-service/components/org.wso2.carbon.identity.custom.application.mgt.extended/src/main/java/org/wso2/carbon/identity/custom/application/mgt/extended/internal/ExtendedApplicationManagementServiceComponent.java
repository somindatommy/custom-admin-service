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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.custom.application.mgt.extended.internal.service.impl.DefaultExtendedApplicationManagementServiceImpl;
import org.wso2.carbon.identity.custom.application.mgt.extended.services.ExtendedApplicationManagementService;
import org.wso2.carbon.idp.mgt.IdpManager;

/**
 * Service component of the ExtendedApplicationManagementAdminService.
 */
@Component(
        name = "identity.extended.application.management.component",
        immediate = true
)
public class ExtendedApplicationManagementServiceComponent {

    private static Log log = LogFactory.getLog(ExtendedApplicationManagementServiceComponent.class);

    @Activate
    protected void activate(ComponentContext context) {

        try {
            BundleContext bundleContext = context.getBundleContext();
            bundleContext.registerService(ExtendedApplicationManagementService.class.getName(),
                    DefaultExtendedApplicationManagementServiceImpl.getInstance(), null);
            if (log.isDebugEnabled()) {
                log.debug("Identity ExtendedApplicationManagementServiceComponent bundle is activated");
            }
        } catch (Exception e) {
            log.error("Error while activating Extended Application Management Service bundle", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {

        if (log.isDebugEnabled()) {
            log.debug("Identity ApplicationManagementComponent bundle is deactivated");
        }
    }

    @Reference(name = "applicationManagementService.service",
            service = ApplicationManagementService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetApplicationManagementService")
    protected void setApplicationManagementService(ApplicationManagementService applicationManagementService) {

        if (log.isDebugEnabled()) {
            log.debug("Setting the ApplicationManagementService");
        }
        ExtendedApplicationManagementServiceDataHolder.getInstance()
                .setApplicationManagementService(applicationManagementService);
    }

    protected void unsetApplicationManagementService(ApplicationManagementService applicationManagementService) {

        if (log.isDebugEnabled()) {
            log.debug("UnSetting the ApplicationManagementService");
        }
        ExtendedApplicationManagementServiceDataHolder.getInstance().setApplicationManagementService(null);
    }

    @Reference(name = "idpManager.service",
            service = IdpManager.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetIdpManagerService")
    protected void setIdpManagerService(IdpManager idpManager) {

        if (log.isDebugEnabled()) {
            log.debug("Setting the IdpManager");
        }
        ExtendedApplicationManagementServiceDataHolder.getInstance().setIdpManager(idpManager);
    }

    protected void unsetIdpManagerService(IdpManager idpManager) {

        if (log.isDebugEnabled()) {
            log.debug("UnSetting the IdpManager");
        }
        ExtendedApplicationManagementServiceDataHolder.getInstance().setIdpManager(null);
    }
}
