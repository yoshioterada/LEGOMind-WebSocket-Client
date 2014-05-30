/*
 * Copyright 2014 Yoshio Terada
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

import com.yoshio3.legomind.encoders.ClientMessageObject;
import com.yoshio3.legomind.encoders.ClientOrderString;
import com.yoshio3.legomind.encoders.TypeOfMachine;
import javax.websocket.Session;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 *
 * @author Yoshio Terada
 */
public class EV3UltrasonicSensorImpl implements Runnable {

    EV3UltrasonicSensor ursensor;
    RegulatedMotor leftMotor;
    RegulatedMotor rightMotor;
    Session session;

    public EV3UltrasonicSensorImpl(EV3UltrasonicSensor ursensor,
            RegulatedMotor leftMotor,
            RegulatedMotor rightMotor,
            Session session) {
        this.session = session;
        this.ursensor = ursensor;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }
    int control = 0;

    @Override
    public void run() {
        ClientMessageObject message = new ClientMessageObject();
        SensorMode sonic = ursensor.getMode(0);
        float value[] = new float[sonic.sampleSize()];
        while (true) {
            sonic.fetchSample(value, 0);
            // for 文でループしているが実際には、
            // sonic.sampleSize()が1を返すので value[0] しかない。
            for (float res : value) {
                message.setMachineType(TypeOfMachine.LEGO);
                message.setOrder(ClientOrderString.BRWS_INFO);
                double centimeterDouble = (double) res * 100;
                int centimeter = (int) centimeterDouble;
                /*
                 if (centimeter < 3) {
                 if (leftMotor.isMoving()) {
                 leftMotor.stop();
                 }
                 if (rightMotor.isMoving()) {
                 rightMotor.stop();
                 }
                 message.setOrder(ClientOrderString.BRWS_WARN);
                 message.setMessage("危険領域に入ったため自動停止しました。");
                 WebSocketClientEndpoint.sendMessageToServer(session, message);
                 }*/

                if (centimeter != 2147483647) {
                    message.setMessage(Integer.toString(centimeter));
                    WebSocketClientEndpoint.sendMessageToServer(session, message);
                }
            }
            Delay.msDelay(100);
        }
    }
}
