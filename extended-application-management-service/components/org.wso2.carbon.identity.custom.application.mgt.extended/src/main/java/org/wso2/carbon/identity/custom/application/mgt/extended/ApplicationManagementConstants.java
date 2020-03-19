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

/**
 * Class which contains the constants.
 */
public class ApplicationManagementConstants {

    private static String SCENARIO_CODE_MESSAGE_SEPARATOR = "-";

    /**
     * Enum which contains the error scenarios with corresponding error messages and error codes.
     */
    public enum ErrorMessages {

        ERROR_USER_NOT_AUTHORIZED("EAM-60001", "Illegal Access! User: %s does not have access to the " +
                "application: %s "),
        EMPTY_TENANT_DOMAIN("EAM-60002", "Tenant domain cannot be empty"),
        EMPTY_APPLICATION_NAME("EAM-60003", "Application Name cannot be empty"),
        EMPTY_IDP_NAME("EAM-60004", "Identity Provider name cannot be empty"),
        INVALID_MINIMUM_AUTH_STEP("EAM-60005", "Authentication step has to be larger than 1"),
        NO_IDP_FOUND_FOR_GIVEN_NAME("EAM-60006", "An Identity Provider was not found with the name: " +
                "%s in tenant domain: %s"),
        NO_SP_FOUND_FOR_GIVEN_NAME("EAM-60007", "An Application was not found with the name: %s in " +
                "tenant domain: %s"),
        INVALID_MAXIMUM_AUTH_STEP("EAM-60008", "Application: %s does not have an authentication " +
                "step: %d "),
        NO_FEDERATED_IDPS_FOUND("EAM-60009", "Application: %s does not have any federated IDPs in " +
                "authentification step: %d"),
        NO_MATCHING_IDP_IN_AUTH_STEP("EAM-60010", "Application: %s does not have a federated " +
                "IDP: %s in authentification step: %d"),
        ERROR_WHILE_GETTING_IDP("EAM-65001", "Error occurred while getting the Identity Provider " +
                "from name: %s in domain: %s");

        private final String code;
        private final String message;

        ErrorMessages(String code, String message) {

            this.code = code;
            this.message = message;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }

        @Override
        public String toString() {

            return code + SCENARIO_CODE_MESSAGE_SEPARATOR + message;
        }
    }

    /**
     * Enum which contains the success scenarios with corresponding success messages and success codes.
     */
    public enum SuccessScenario {

        SUCCESSFUL_APPLICATION_UPDATE_WITH_IDP("EAM-01001", "Identity Provider: %s successfully " +
                "added to application: %s for authentication step: %s"),
        ADD_ALREADY_EXISTING_IDP("EAM-01002", "Application: %s already has an IDP: %s" +
                " for authentification step: %s"),
        SUCCESSFUL_IDP_REMOVE_FROM_APPLICATION("EAM-01003", "Identity Provider: %s successfully " +
                "removed from authentification step: %s in application: %s");

        private final String code;
        private final String message;

        SuccessScenario(String code, String message) {

            this.code = code;
            this.message = message;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }

        @Override
        public String toString() {

            return code + SCENARIO_CODE_MESSAGE_SEPARATOR + message;
        }
    }
}
