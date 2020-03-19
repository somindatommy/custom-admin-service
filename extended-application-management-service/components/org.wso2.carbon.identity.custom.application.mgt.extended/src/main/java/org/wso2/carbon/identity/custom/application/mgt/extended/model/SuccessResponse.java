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

package org.wso2.carbon.identity.custom.application.mgt.extended.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class which contains the attributes of a successful response.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SuccessResponse")
public class SuccessResponse implements Serializable {

    private static final long serialVersionUID = 4354536852588678552L;
    private static final Log log = LogFactory.getLog(SuccessResponse.class);

    @XmlElement(name = "SuccessCode")
    private String successCode;

    @XmlElement(name = "SuccessMessage")
    private String successMessage;

    /**
     * Get the success code of the operation.
     *
     * @return Success code
     */
    public String getSuccessCode() {

        return successCode;
    }

    /**
     * Set the success code of the operation.
     *
     * @param successCode Success code
     */
    public void setSuccessCode(String successCode) {

        this.successCode = successCode;
    }

    /**
     * Get the success message of the operation.
     *
     * @return Success message
     */
    public String getSuccessMessage() {

        return successMessage;
    }

    /**
     * Set the success message of the operation.
     *
     * @param successMessage Success message
     */
    public void setSuccessMessage(String successMessage) {

        this.successMessage = successMessage;
    }
}
