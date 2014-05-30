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

import com.yoshio3.legomind.encoders.ClientMessageDecoder;
import com.yoshio3.legomind.encoders.ClientMessageEncoder;
import com.yoshio3.legomind.encoders.ClientMessageObject;
import com.yoshio3.legomind.encoders.ClientOrderString;
import com.yoshio3.legomind.encoders.TypeOfMachine;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

/**
 *
 * @author Yoshio Terada
 */
//@ClientEndpoint(decoders = {ClientMessageDecoder.class})
@ClientEndpoint(encoders = {ClientMessageEncoder.class}, decoders = {ClientMessageDecoder.class})
public class WebSocketClientEndpoint {

    //超音波センサー
    protected static final EV3UltrasonicSensor ursensor = new EV3UltrasonicSensor(SensorPort.S4);
    // モータ(車輪)
    private static final RegulatedMotor leftMotor = Motor.B;
    private static final RegulatedMotor rightMotor = Motor.C;
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        ExecutorService exec = Executors.newFixedThreadPool(2);
        exec.submit(new EV3UltrasonicSensorImpl(ursensor, leftMotor, rightMotor, session));
        /*
         exec.submit((Runnable) () -> {
         ClientMessageObject reply = new ClientMessageObject();
         reply.setMachineType(TypeOfMachine.LEGO);
         reply.setOrder(ClientOrderString.BRWS_WARN);

         Sound.setVolume(100);
         int volume = Sound.getVolume();
         reply.setMessage("ボリューム：" + volume);
         sendMessageToServer(session, reply);

         while (true) {
         final File soundFile = new File("letitgo88.wav");
         if (soundFile.canExecute()) {
         reply.setMessage("音楽再生前");
         sendMessageToServer(session, reply);
         Sound.playSample(soundFile, Sound.VOL_MAX);
         reply.setMessage("音楽再生後");
         sendMessageToServer(session, reply);
         Delay.msDelay(224000);
         } else {
         break;
         }
         }
         });
         */
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.session = null;
        ursensor.close();
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a
     * client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void receivedMessage(ClientMessageObject message, Session session) {
        ClientOrderString order = message.getOrder();

        ClientMessageObject reply = new ClientMessageObject();
        reply.setMachineType(TypeOfMachine.LEGO);
        reply.setOrder(ClientOrderString.BRWS_WARN);
        reply.setMessage("クライアントとの接続完了");
        sendMessageToServer(session, reply);

        if (order == ClientOrderString.LEGO_FORWARD) {
            reply.setMessage("前進命令受信");
            sendMessageToServer(session, reply);

            leftMotor.forward();
            rightMotor.forward();

            LCD.drawString("FORWARD", 0, 0);
            Button.LEDPattern(1);
        } else if (order == ClientOrderString.LEGO_BACKWARD) {
            reply.setMessage("後進命令受信");
            sendMessageToServer(session, reply);

            leftMotor.backward();
            rightMotor.backward();
            LCD.drawString("BACK", 0, 0);
            Button.LEDPattern(2);
        } else if (order == ClientOrderString.LEGO_LEFTTURN) {
            reply.setMessage("左回転命令受信");
            sendMessageToServer(session, reply);
            // 左回転の場合は右のモータを回転
            rightMotor.rotate(360 + 90);
            LCD.drawString("LEFT TURN", 0, 0);
            Button.LEDPattern(3);
        } else if (order == ClientOrderString.LEGO_RIGHTTURN) {
            reply.setMessage("右回転命令受信");
            sendMessageToServer(session, reply);
            // 右回転の場合は左のモータを回転
            leftMotor.rotate(360 + 90);
            LCD.drawString("RIGHT TURN", 0, 0);
            Button.LEDPattern(4);
        } else if (order == ClientOrderString.LEGO_SPPEDUP) {
            reply.setMessage("スピードアップ命令受信");
            sendMessageToServer(session, reply);

            int currentLeftSpeed = leftMotor.getSpeed();
            int currentRightSpeed = rightMotor.getSpeed();

            leftMotor.setSpeed(currentLeftSpeed + 50);
            rightMotor.setSpeed(currentRightSpeed + 50);
            LCD.drawString("SPEED UP", 0, 0);
            Button.LEDPattern(5);
        } else if (order == ClientOrderString.LEGO_SPEEDDOWN) {
            reply.setMessage("スピードダウン命令受信");
            sendMessageToServer(session, reply);

            int currentLeftSpeed = leftMotor.getSpeed();
            int currentRightSpeed = rightMotor.getSpeed();

            leftMotor.setSpeed(currentLeftSpeed - 50);
            rightMotor.setSpeed(currentRightSpeed - 50);
            LCD.drawString("SPEED DOWN", 0, 0);
            Button.LEDPattern(6);
        } else if (order == ClientOrderString.LEGO_STOP) {
            reply.setMessage("停止命令受信");
            sendMessageToServer(session, reply);

            leftMotor.stop();
            rightMotor.stop();
            LCD.drawString("STOP", 0, 0);
            Button.LEDPattern(0);
        }
    }

    protected static void sendMessageToServer(Session session, ClientMessageObject message) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(WebSocketClientEndpoint.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
