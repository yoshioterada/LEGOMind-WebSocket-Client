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
package com.yoshio3.legomind;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.WebSocketContainer;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;

/**
 *
 * @author Yoshio Terada
 */
public class EV3AppMain {

    public static void main(String... argv) {
        URI endpointURI = URI.create("ws://192.168.1.101:8080/LEGOMind-WebSocket-Server/lego-server/lego");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(new WebSocketClientEndpoint(), endpointURI);
        } catch (DeploymentException | IOException ex) {
            Logger.getLogger(EV3AppMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        EV3AppMain main = new EV3AppMain();
        main.onKeyTouchExit();
    }

    private void onKeyTouchExit() {
        EV3 ev3 = (EV3) BrickFinder.getLocal();
        Keys keys = ev3.getKeys();
        keys.waitForAnyPress();
        WebSocketClientEndpoint.ursensor.disable();
        WebSocketClientEndpoint.ursensor.close();
        System.exit(0);
    }
}
