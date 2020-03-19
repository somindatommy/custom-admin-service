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

package org.wso2.carbon.identity.custom.application.mgt.extended.internal.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.AuthenticationStep;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.ApplicationConstants;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.ApplicationMgtUtil;
import org.wso2.carbon.identity.custom.application.mgt.extended.ApplicationManagementConstants;
import org.wso2.carbon.identity.custom.application.mgt.extended.internal.ExtendedApplicationManagementServiceDataHolder;
import org.wso2.carbon.identity.custom.application.mgt.extended.model.SuccessResponse;
import org.wso2.carbon.identity.custom.application.mgt.extended.services.ExtendedApplicationManagementService;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdpManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class contains the default implementation of ExtendedApplicationManagementService.
 */
public class DefaultExtendedApplicationManagementServiceImpl implements
        ExtendedApplicationManagementService {

    private static final Log log = LogFactory.getLog(DefaultExtendedApplicationManagementServiceImpl.class);
    private static volatile DefaultExtendedApplicationManagementServiceImpl instance =
            new DefaultExtendedApplicationManagementServiceImpl();

    /**
     * Default constructor is made private so that no objects can be created.
     */
    private DefaultExtendedApplicationManagementServiceImpl() {

    }

    /**
     * Get an instance from the DefaultExtendedApplicationManagementServiceImpl class.
     *
     * @return Instance of DefaultExtendedApplicationManagementServiceImpl
     */
    public static DefaultExtendedApplicationManagementServiceImpl getInstance() {

        if (instance == null) {
            synchronized (DefaultExtendedApplicationManagementServiceImpl.class) {
                if (instance == null) {
                    instance =
                            new DefaultExtendedApplicationManagementServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public SuccessResponse removeIdentityProviderFromApplication(String applicationName, String identityProviderName,
                                                                 int authenticationStepId)
            throws IdentityApplicationManagementException {

        validateInputParameters(applicationName, identityProviderName, authenticationStepId);

        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        // Validate whether the user has authorization to perform the operation.
        validateUserAccessRights(applicationName, userName);
        String tenantDomain = getTenantDomain();

        // Get the identity provider which matches the given name.
        IdentityProvider idpToRemove = getIdpFromName(identityProviderName, tenantDomain);

        ApplicationManagementService applicationManagementService =
                ExtendedApplicationManagementServiceDataHolder.getInstance().getApplicationManagementService();
        // Retrieve the SP application from the given information.
        ServiceProvider serviceProvider =
                getServiceProvider(applicationManagementService, applicationName, tenantDomain);
        AuthenticationStep[] authenticationSteps =
                serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAuthenticationSteps();

        // Validate authentication step with the number of existing authentication steps.
        validateAuthenticationStep(authenticationStepId, authenticationSteps.length, applicationName);
        AuthenticationStep authenticationStep = authenticationSteps[authenticationStepId - 1];
        IdentityProvider[] existingFederatedIdps = authenticationStep.getFederatedIdentityProviders();

        if (existingFederatedIdps == null) {
            String message =
                    String.format(ApplicationManagementConstants.ErrorMessages.NO_FEDERATED_IDPS_FOUND.getMessage(),
                            applicationName, authenticationStepId);
            throw new IdentityApplicationManagementException(message);
        }
        if (!isIdpAlreadySetAsFederatedIdpForApplication(idpToRemove, existingFederatedIdps)) {
            String message = String.format(
                    ApplicationManagementConstants.ErrorMessages.NO_MATCHING_IDP_IN_AUTH_STEP.getMessage(),
                    applicationName, identityProviderName, authenticationStepId);
            throw new IdentityApplicationManagementException(message);
        }
        ArrayList<IdentityProvider> idpList = new ArrayList<>(Arrays.asList(existingFederatedIdps));
        ArrayList<IdentityProvider> idpListToRemove = new ArrayList<>();
        idpListToRemove.add(idpToRemove);
        idpList.removeAll(idpListToRemove);
        authenticationSteps[authenticationStepId - 1]
                .setFederatedIdentityProviders(idpList.toArray(new IdentityProvider[0]));
        applicationManagementService.updateApplication(serviceProvider, tenantDomain, userName);
        return buildSuccessResponse(
                ApplicationManagementConstants.SuccessScenario.SUCCESSFUL_IDP_REMOVE_FROM_APPLICATION,
                identityProviderName, Integer.toString(authenticationStepId), applicationName);
    }

    @Override
    public SuccessResponse addIdentityProviderToApplication(String applicationName, String identityProviderName,
                                                            int authenticationStepId)
            throws IdentityApplicationManagementException {

        validateInputParameters(applicationName, identityProviderName, authenticationStepId);

        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        // Validate whether the user has authorization to perform the operation.
        validateUserAccessRights(applicationName, userName);
        String tenantDomain = getTenantDomain();

        // Get the identity provider which matches the given name.
        IdentityProvider idpToAdd = getIdpFromName(identityProviderName, tenantDomain);

        ApplicationManagementService applicationManagementService =
                ExtendedApplicationManagementServiceDataHolder.getInstance().getApplicationManagementService();
        // Retrieve the SP application from the given information.
        ServiceProvider serviceProvider =
                getServiceProvider(applicationManagementService, applicationName, tenantDomain);
        AuthenticationStep[] authenticationSteps =
                serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAuthenticationSteps();

        // Validate authentication step with the number of existing authentication steps.
        validateAuthenticationStep(authenticationStepId, authenticationSteps.length, applicationName);
        AuthenticationStep authenticationStep = authenticationSteps[authenticationStepId - 1];
        IdentityProvider[] existingFederatedIdps = authenticationStep.getFederatedIdentityProviders();

        if (existingFederatedIdps == null) {
            /*
            existingFederatedIdps == null implies that there are no federated Idps set for this step. In this case
            the given idp needs to be added as a federated Idp.
             */
            existingFederatedIdps = new IdentityProvider[0];
        } else if (isIdpAlreadySetAsFederatedIdpForApplication(idpToAdd, existingFederatedIdps)) {
            return buildSuccessResponse(ApplicationManagementConstants.SuccessScenario.ADD_ALREADY_EXISTING_IDP,
                    applicationName, identityProviderName, Integer.toString(authenticationStepId));
        }
        //  Add the new Idp to the federated Idps list.
        ArrayList<IdentityProvider> idpList = new ArrayList<>(Arrays.asList(existingFederatedIdps));
        removeDisabledAuthenticators(idpToAdd);
        idpList.add(idpToAdd);
        authenticationSteps[authenticationStepId - 1]
                .setFederatedIdentityProviders(idpList.toArray(new IdentityProvider[0]));
        applicationManagementService.updateApplication(serviceProvider, tenantDomain, userName);
        return buildSuccessResponse(
                ApplicationManagementConstants.SuccessScenario.SUCCESSFUL_APPLICATION_UPDATE_WITH_IDP,
                identityProviderName, applicationName, Integer.toString(authenticationStepId));
    }

    /**
     * Build success response for a successful operation.
     *
     * @param scenario Success Scenario
     *                 {@link org.wso2.carbon.identity.custom.application.mgt.extended.ApplicationManagementConstants.SuccessScenario}
     * @param data     Meta Data
     * @return SuccessResponse
     */
    private SuccessResponse buildSuccessResponse(ApplicationManagementConstants.SuccessScenario scenario,
                                                 String... data) {

        String message =
                String.format(scenario.getMessage(), data);
        if (log.isDebugEnabled()) {
            log.debug(message);
        }
        SuccessResponse successResponse = new SuccessResponse();
        successResponse
                .setSuccessCode(scenario.getCode());
        successResponse.setSuccessMessage(message);
        return successResponse;
    }

    /**
     * Remove the disabled authenticators from the list and update the federated authenticator list with the default
     * authenticator configs.
     *
     * @param identityProvider IdentityProvider
     */
    private void removeDisabledAuthenticators(IdentityProvider identityProvider) {

        FederatedAuthenticatorConfig defaultAuthenticator = identityProvider.getDefaultAuthenticatorConfig();
        FederatedAuthenticatorConfig[] authenticatorList = new FederatedAuthenticatorConfig[]{defaultAuthenticator};
        identityProvider.setFederatedAuthenticatorConfigs(authenticatorList);
    }

    /**
     * Validate authentication step with the number of existing authentication steps.
     *
     * @param givenAuthStep             Given authentication step
     * @param numberOfExistingAuthSteps Number of existing authentication steps
     * @param applicationName           Application name
     * @throws IdentityApplicationManagementException If the given auth step is larger than the number of existing
     *                                                authentication steps.
     */
    private void validateAuthenticationStep(int givenAuthStep, int numberOfExistingAuthSteps, String applicationName)
            throws IdentityApplicationManagementException {

        if (givenAuthStep > numberOfExistingAuthSteps) {
            String error =
                    String.format(ApplicationManagementConstants.ErrorMessages.INVALID_MAXIMUM_AUTH_STEP.getMessage(),
                            applicationName, givenAuthStep);
            throw new IdentityApplicationManagementException(error);
        }
    }

    /**
     * Checks whether the given identity provider is in the given list of identity providers.
     *
     * @param idpToAdd              Identity Provider
     * @param existingFederatedIdps List of existing Identity Providers
     * @return TRUE if the given Identity Provider is in the existing list.
     */
    private boolean isIdpAlreadySetAsFederatedIdpForApplication(IdentityProvider idpToAdd,
                                                                IdentityProvider[] existingFederatedIdps) {

        for (IdentityProvider idp : existingFederatedIdps) {
            if (idpToAdd.getIdentityProviderName().equals(idp.getIdentityProviderName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the service provider which matches the given name.
     *
     * @param applicationManagementService Application management service.
     * @param applicationName              Application name
     * @param tenantDomain                 Tenant domain
     * @return ServiceProvider
     * @throws IdentityApplicationManagementException If there is no service provider with the given name.
     */
    private ServiceProvider getServiceProvider(ApplicationManagementService applicationManagementService,
                                               String applicationName,
                                               String tenantDomain) throws IdentityApplicationManagementException {

        ServiceProvider serviceProvider =
                applicationManagementService.getApplicationExcludingFileBasedSPs(applicationName, tenantDomain);
        if (serviceProvider == null) {
            String error =
                    String.format(ApplicationManagementConstants.ErrorMessages.NO_SP_FOUND_FOR_GIVEN_NAME.getMessage(),
                            applicationName, tenantDomain);
            throw new IdentityApplicationManagementException(error);
        }
        return serviceProvider;
    }

    /**
     * Validate application name, idp name and the authentication step value.
     *
     * @param applicationName Name of the service provider
     * @param idpName         Name of the identity provider
     * @param step            Authentication step number
     * @throws IdentityApplicationManagementException If the inputs are invalid.
     */
    private void validateInputParameters(String applicationName, String idpName, int step)
            throws IdentityApplicationManagementException {

        if (StringUtils.isBlank(applicationName)) {
            throw new IdentityApplicationManagementException(
                    ApplicationManagementConstants.ErrorMessages.EMPTY_APPLICATION_NAME.getMessage());
        }
        if (StringUtils.isBlank(idpName)) {
            throw new IdentityApplicationManagementException(
                    ApplicationManagementConstants.ErrorMessages.EMPTY_IDP_NAME.getMessage());
        }
        // This will allow to change the first authentification step.
        if (step < 2) {
            throw new IdentityApplicationManagementException(
                    ApplicationManagementConstants.ErrorMessages.INVALID_MINIMUM_AUTH_STEP.getMessage());
        }
    }

    /**
     * Get the tenant domain.
     *
     * @return Tenant domain
     * @throws IdentityApplicationManagementException If the tenant domain is empty.
     */
    private String getTenantDomain() throws IdentityApplicationManagementException {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (StringUtils.isBlank(tenantDomain)) {
            throw new IdentityApplicationManagementException(
                    ApplicationManagementConstants.ErrorMessages.EMPTY_TENANT_DOMAIN.getMessage());
        }
        return tenantDomain;
    }

    /**
     * Validates whether the user has access rights to update the application identity providers.
     *
     * @param applicationName Name of the service provider
     * @param userName        Username
     * @throws IdentityApplicationManagementException If an error occurred while checking for access rights or if the
     *                                                user does not have permission for the operation.
     */
    private void validateUserAccessRights(String applicationName, String userName)
            throws IdentityApplicationManagementException {

        if (!ApplicationConstants.LOCAL_SP.equals(applicationName) && !ApplicationMgtUtil
                .isUserAuthorized(applicationName, userName)) {
            String error = String.format(
                    ApplicationManagementConstants.ErrorMessages.ERROR_USER_NOT_AUTHORIZED.getMessage(), userName,
                    applicationName);
            throw new IdentityApplicationManagementException(error);
        }
    }

    /**
     * Get the Identity provider matches the given idp name.
     *
     * @param idpName      Identity provider name
     * @param tenantDomain Tenant domain
     * @return IdentityProvider {@link IdentityProvider}
     * @throws IdentityApplicationManagementException If an error occurred while getting the Idp or if there is no idp
     *                                                from the given name.
     */
    private IdentityProvider getIdpFromName(String idpName, String tenantDomain)
            throws IdentityApplicationManagementException {

        IdpManager idpManager = ExtendedApplicationManagementServiceDataHolder.getInstance().getIdpManager();
        IdentityProvider identityProvider;

        try {
            identityProvider = idpManager.getIdPByName(idpName, tenantDomain, true);
        } catch (IdentityProviderManagementException e) {
            String error =
                    String.format(ApplicationManagementConstants.ErrorMessages.ERROR_WHILE_GETTING_IDP.getMessage(),
                            idpName, tenantDomain);
            if (log.isDebugEnabled()) {
                log.debug(error, e);
            }
            throw new IdentityApplicationManagementException(error, e);
        }

        if (identityProvider == null) {
            String error =
                    String.format(ApplicationManagementConstants.ErrorMessages.NO_IDP_FOUND_FOR_GIVEN_NAME.getMessage(),
                            idpName, tenantDomain);
            throw new IdentityApplicationManagementException(error);
        }
        return identityProvider;
    }
}
