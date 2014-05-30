/*
 * Copyright 2013 Yoshio Terada
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yoshio3.legomind.encoders;

/**
 *
 * @author Yoshio Terada
 */
public class ClientMessageObject {

    private TypeOfMachine machineType;
    private ClientOrderString order;
    private String message;

    /**
     * @return the order
     */
    public ClientOrderString getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(ClientOrderString order) {
        this.order = order;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the machineType
     */
    public TypeOfMachine getMachineType() {
        return machineType;
    }

    /**
     * @param machineType the machineType to set
     */
    public void setMachineType(TypeOfMachine machineType) {
        this.machineType = machineType;
    }
}
