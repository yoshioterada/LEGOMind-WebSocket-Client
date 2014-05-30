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

import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author Yoshio Terada
 */
public class ClientMessageEncoder implements Encoder.Text<ClientMessageObject> {

    @Override
    public String encode(ClientMessageObject msg) throws EncodeException {
        // ["machineType":"LEGO", "order":"ORDER_NUM","message":"MESSAGE"]

        //ClientMessageObject オブジェクトから JSon 文字列を生成
        TypeOfMachine type = msg.getMachineType();
        String machineTypeString = getTypeOfMachineString(type);

        ClientOrderString order = msg.getOrder();
        String orderString = getOrderNUMString(order);

        String message = msg.getMessage();

        JsonObject model = Json.createObjectBuilder()
                .add("machineType", machineTypeString)
                .add("order", orderString)
                .add("message", message)
                .build();
        // JSon オブジェクトから JSon 文字列を生成
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
            jsonWriter.writeObject(model);
        }
        return stWriter.toString();
    }

    @Override
    public void init(EndpointConfig config) {
        ;
    }

    @Override
    public void destroy() {
        ;
    }

    private String getTypeOfMachineString(TypeOfMachine type) {
        String machineType;
        if (type == TypeOfMachine.LEGO) {
            machineType = "LEGO";
        } else if (type == TypeOfMachine.BROWSER) {
            machineType = "BROWSER";
        } else if (type == TypeOfMachine.SERVER) {
            machineType = "SERVER";
        } else {
            machineType = "";
        }
        return machineType;
    }

    private String getOrderNUMString(ClientOrderString order) {
        String orderString;
        if (order == ClientOrderString.LEGO_BACKWARD) {
            orderString = "LEGO_BACKWARD";
        } else if (order == ClientOrderString.LEGO_FORWARD) {
            orderString = "LEGO_FORWARD";
        } else if (order == ClientOrderString.LEGO_LEFTTURN) {
            orderString = "LEGO_LEFTTURN";
        } else if (order == ClientOrderString.LEGO_RIGHTTURN) {
            orderString = "LEGO_RIGHTTURN";
        } else if (order == ClientOrderString.LEGO_SPEEDDOWN) {
            orderString = "LEGO_SPEEDDOWN";
        } else if (order == ClientOrderString.LEGO_SPPEDUP) {
            orderString = "LEGO_SPPEDUP";
        } else if (order == ClientOrderString.LEGO_STOP) {
            orderString = "LEGO_STOP";
        } else if (order == ClientOrderString.BRWS_ERROR) {
            orderString = "BRWS_ERROR";
        } else if (order == ClientOrderString.BRWS_INFO) {
            orderString = "BRWS_INFO";
        } else if (order == ClientOrderString.BRWS_WARN) {
            orderString = "BRWS_WARN";
        } else {
            orderString = "";
        }

        return orderString;

    }
}
