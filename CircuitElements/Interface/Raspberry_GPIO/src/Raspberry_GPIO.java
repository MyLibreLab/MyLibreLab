//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2016  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************

import VisualLogic.*;
import VisualLogic.variables.*;

import java.awt.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import tools.*;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Raspberry_GPIO extends JVSMain implements MyOpenLabDriverOwnerIF {

    private final int GPIO_INPUT = 0;
    private final int GPIO_OUTPUT = 1;

    private GpioController gpio;
    
    private Image image;
    private VS1DByte inBytes = new VS1DByte(1);
    private ArrayList<String> cmd = new ArrayList<>();

    private VSComboBox gpio_left_7 = new VSComboBox();
    private VSComboBox gpio_left_0 = new VSComboBox();
    private VSComboBox gpio_left_2 = new VSComboBox();
    private VSComboBox gpio_left_3 = new VSComboBox();
    private VSComboBox gpio_left_21 = new VSComboBox();
    private VSComboBox gpio_left_22 = new VSComboBox();
    private VSComboBox gpio_left_23 = new VSComboBox();
    private VSComboBox gpio_left_24 = new VSComboBox();
    private VSComboBox gpio_left_25 = new VSComboBox();

    private VSComboBox gpio_right_1 = new VSComboBox();
    private VSComboBox gpio_right_4 = new VSComboBox();
    private VSComboBox gpio_right_5 = new VSComboBox();
    private VSComboBox gpio_right_6 = new VSComboBox();
    private VSComboBox gpio_right_26 = new VSComboBox();
    private VSComboBox gpio_right_27 = new VSComboBox();
    private VSComboBox gpio_right_28 = new VSComboBox();
    private VSComboBox gpio_right_29 = new VSComboBox();

    private GpioPinDigital pin_left_7;
    private GpioPinDigital pin_left_0;
    private GpioPinDigital pin_left_2;
    private GpioPinDigital pin_left_3;
    private GpioPinDigital pin_left_21;
    private GpioPinDigital pin_left_22;
    private GpioPinDigital pin_left_23;
    private GpioPinDigital pin_left_24;
    private GpioPinDigital pin_left_25;

    private GpioPinDigital pin_right_1;
    private GpioPinDigital pin_right_4;
    private GpioPinDigital pin_right_5;
    private GpioPinDigital pin_right_6;
    private GpioPinDigital pin_right_26;
    private GpioPinDigital pin_right_27;
    private GpioPinDigital pin_right_28;
    private GpioPinDigital pin_right_29;

    private VSObject io_pin_right_1;
    private VSObject io_pin_right_4;
    private VSObject io_pin_right_5;
    private VSObject io_pin_right_6;
    private VSObject io_pin_right_26;
    private VSObject io_pin_right_27;
    private VSObject io_pin_right_28;
    private VSObject io_pin_right_29;

    private VSObject io_pin_left_7;
    private VSObject io_pin_left_0;
    private VSObject io_pin_left_2;
    private VSObject io_pin_left_3;
    private VSObject io_pin_left_21;
    private VSObject io_pin_left_22;
    private VSObject io_pin_left_23;
    private VSObject io_pin_left_24;
    private VSObject io_pin_left_25;

    private VSObject pins[];

    public void fillCombo(VSComboBox combo) {
        combo.clear();
        combo.addItem("D-Input");
        combo.addItem("D-Output");
        combo.selectedIndex = 0;
    }

    public Raspberry_GPIO() {

        fillCombo(gpio_left_7);
        fillCombo(gpio_left_0);
        fillCombo(gpio_left_2);
        fillCombo(gpio_left_3);
        fillCombo(gpio_left_21);
        fillCombo(gpio_left_22);
        fillCombo(gpio_left_23);
        fillCombo(gpio_left_24);
        fillCombo(gpio_left_25);

        fillCombo(gpio_right_1);
        fillCombo(gpio_right_4);
        fillCombo(gpio_right_5);
        fillCombo(gpio_right_6);
        fillCombo(gpio_right_26);
        fillCombo(gpio_right_27);
        fillCombo(gpio_right_28);
        fillCombo(gpio_right_29);
    }

    @Override
    public void paint(java.awt.Graphics g) {
        if (image != null) {
            Rectangle bounds = element.jGetBounds();
            g.setColor(Color.WHITE);
            g.fillRect(10, 1, bounds.width - 4, bounds.height - 2);

            drawImageCentred(g, image);
        }
    }

    @Override
    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    @Override
    public void setPropertyEditor() {
      
        element.jAddPEItem("gpio_left_7", gpio_left_7, 0, 0);
        element.jAddPEItem("gpio_left_0", gpio_left_0, 0, 0);
        element.jAddPEItem("gpio_left_2", gpio_left_2, 0, 0);
        element.jAddPEItem("gpio_left_3", gpio_left_3, 0, 0);
        element.jAddPEItem("gpio_left_21", gpio_left_21, 0, 0);
        element.jAddPEItem("gpio_left_22", gpio_left_22, 0, 0);
        element.jAddPEItem("gpio_left_23", gpio_left_23, 0, 0);
        element.jAddPEItem("gpio_left_24", gpio_left_24, 0, 0);
        element.jAddPEItem("gpio_left_25", gpio_left_25, 0, 0);

        element.jAddPEItem("gpio_right_1", gpio_right_1, 0, 0);
        element.jAddPEItem("gpio_right_4", gpio_right_4, 0, 0);
        element.jAddPEItem("gpio_right_5", gpio_right_5, 0, 0);
        element.jAddPEItem("gpio_right_6", gpio_right_6, 0, 0);
        element.jAddPEItem("gpio_right_26", gpio_right_26, 0, 0);
        element.jAddPEItem("gpio_right_27", gpio_right_27, 0, 0);
        element.jAddPEItem("gpio_right_28", gpio_right_28, 0, 0);
        element.jAddPEItem("gpio_right_29", gpio_right_29, 0, 0);

    }

    @Override
    public void propertyChanged(Object o) {

        init();

    }

    private void setPinType(VSComboBox combo, int pinNumber, String GPIO_Number) {

        String io = "";
        if (combo.selectedIndex == GPIO_INPUT) {
            setPin(pinNumber, ExternalIF.C_BOOLEAN, ExternalIF.PIN_OUTPUT);
            io = "IN";
        } else {
            setPin(pinNumber, ExternalIF.C_BOOLEAN, ExternalIF.PIN_INPUT);
            io = "OUT";
        }

        element.jSetPinDescription(pinNumber, "GPIO_" + GPIO_Number + "_" + io);

    }

    @Override
    public void init() {

        initPins(0, 8, 0, 9);
        setSize(80, 40 + (9 * 10));

        initPinVisibility(false, true, false, true);
        element.jSetInnerBorderVisibility(true);

        int c = 0;

        setPinType(gpio_right_1, c++, "1");
        setPinType(gpio_right_4, c++, "4");
        setPinType(gpio_right_5, c++, "5");
        setPinType(gpio_right_6, c++, "6");
        setPinType(gpio_right_26, c++, "26");
        setPinType(gpio_right_27, c++, "27");
        setPinType(gpio_right_28, c++, "28");
        setPinType(gpio_right_29, c++, "29");

        setPinType(gpio_left_7, c++, "7");
        setPinType(gpio_left_0, c++, "0");
        setPinType(gpio_left_2, c++, "2");
        setPinType(gpio_left_3, c++, "3");
        setPinType(gpio_left_21, c++, "21");
        setPinType(gpio_left_22, c++, "22");
        setPinType(gpio_left_23, c++, "23");
        setPinType(gpio_left_24, c++, "24");
        setPinType(gpio_left_25, c++, "25");

        element.jSetInnerBorderVisibility(true);

        String fileName = element.jGetSourcePath() + "icon_64.png";
        image = element.jLoadImage(fileName);

        element.jSetCaptionVisible(true);
        element.jSetCaption("Raspberry PI GPIO");
        setName("Raspberry PI GPIO");

    }

    public VSObject setInput(VSComboBox combo, VSObject pin, int c) {
        if (combo.selectedIndex == GPIO_OUTPUT) {
            pin = new VSBoolean();
            pin = (VSBoolean) element.getPinInputReference(c);

            return pin;
        }
        return null;

    }

    @Override
    public void initInputPins() {

        int c = 0;

        VSObject tmp1 = setInput(gpio_right_1, io_pin_right_1, c++);
        VSObject tmp4 = setInput(gpio_right_4, io_pin_right_4, c++);
        VSObject tmp5 = setInput(gpio_right_5, io_pin_right_5, c++);
        VSObject tmp6 = setInput(gpio_right_6, io_pin_right_6, c++);
        VSObject tmp26 = setInput(gpio_right_26, io_pin_right_26, c++);
        VSObject tmp27 = setInput(gpio_right_27, io_pin_right_27, c++);
        VSObject tmp28 = setInput(gpio_right_28, io_pin_right_28, c++);
        VSObject tmp29 = setInput(gpio_right_29, io_pin_right_29, c++);

        VSObject tmp7 = setInput(gpio_left_7, io_pin_left_7, c++);
        VSObject tmp0 = setInput(gpio_left_0, io_pin_left_0, c++);
        VSObject tmp2 = setInput(gpio_left_2, io_pin_left_2, c++);
        VSObject tmp3 = setInput(gpio_left_3, io_pin_left_3, c++);
        VSObject tmp21 = setInput(gpio_left_21, io_pin_left_21, c++);
        VSObject tmp22 = setInput(gpio_left_22, io_pin_left_22, c++);
        VSObject tmp23 = setInput(gpio_left_23, io_pin_left_23, c++);
        VSObject tmp24 = setInput(gpio_left_24, io_pin_left_24, c++);
        VSObject tmp25 = setInput(gpio_left_25, io_pin_left_25, c++);

        if (tmp1 != null) {
            io_pin_right_1 = tmp1;
        }
        if (tmp4 != null) {
            io_pin_right_4 = tmp4;
        }
        if (tmp5 != null) {
            io_pin_right_5 = tmp5;
        }
        if (tmp6 != null) {
            io_pin_right_6 = tmp6;
        }
        if (tmp26 != null) {
            io_pin_right_26 = tmp26;
        }
        if (tmp27 != null) {
            io_pin_right_27 = tmp27;
        }
        if (tmp28 != null) {
            io_pin_right_28 = tmp28;
        }
        if (tmp29 != null) {
            io_pin_right_29 = tmp29;
        }

        if (tmp7 != null) {
            io_pin_left_7 = tmp7;
        }
        if (tmp0 != null) {
            io_pin_left_0 = tmp0;
        }
        if (tmp2 != null) {
            io_pin_left_2 = tmp2;
        }
        if (tmp3 != null) {
            io_pin_left_3 = tmp3;
        }
        if (tmp21 != null) {
            io_pin_left_21 = tmp21;
        }
        if (tmp22 != null) {
            io_pin_left_22 = tmp22;
        }
        if (tmp23 != null) {
            io_pin_left_23 = tmp23;
        }
        if (tmp24 != null) {
            io_pin_left_24 = tmp24;
        }
        if (tmp25 != null) {
            io_pin_left_25 = tmp25;
        }
    }

    public VSObject setOutput(VSComboBox combo, VSObject pin, int c) {
        if (combo.selectedIndex == GPIO_INPUT) {

            pin = new VSBoolean();
            element.setPinOutputReference(c, pin);

            return pin;
        }
        return null;
    }

    @Override
    public void initOutputPins() {
        int c = 0;

        VSObject tmp1 = setOutput(gpio_right_1, io_pin_right_1, c++);
        VSObject tmp4 = setOutput(gpio_right_4, io_pin_right_4, c++);
        VSObject tmp5 = setOutput(gpio_right_5, io_pin_right_5, c++);
        VSObject tmp6 = setOutput(gpio_right_6, io_pin_right_6, c++);
        VSObject tmp26 = setOutput(gpio_right_26, io_pin_right_26, c++);
        VSObject tmp27 = setOutput(gpio_right_27, io_pin_right_27, c++);
        VSObject tmp28 = setOutput(gpio_right_28, io_pin_right_28, c++);
        VSObject tmp29 = setOutput(gpio_right_29, io_pin_right_29, c++);

        VSObject tmp7 = setOutput(gpio_left_7, io_pin_left_7, c++);
        VSObject tmp0 = setOutput(gpio_left_0, io_pin_left_0, c++);
        VSObject tmp2 = setOutput(gpio_left_2, io_pin_left_2, c++);
        VSObject tmp3 = setOutput(gpio_left_3, io_pin_left_3, c++);
        VSObject tmp21 = setOutput(gpio_left_21, io_pin_left_21, c++);
        VSObject tmp22 = setOutput(gpio_left_22, io_pin_left_22, c++);
        VSObject tmp23 = setOutput(gpio_left_23, io_pin_left_23, c++);
        VSObject tmp24 = setOutput(gpio_left_24, io_pin_left_24, c++);
        VSObject tmp25 = setOutput(gpio_left_25, io_pin_left_25, c++);

        if (tmp1 != null) {
            io_pin_right_1 = tmp1;
        }
        if (tmp4 != null) {
            io_pin_right_4 = tmp4;
        }
        if (tmp5 != null) {
            io_pin_right_5 = tmp5;
        }
        if (tmp6 != null) {
            io_pin_right_6 = tmp6;
        }
        if (tmp26 != null) {
            io_pin_right_26 = tmp26;
        }
        if (tmp27 != null) {
            io_pin_right_27 = tmp27;
        }
        if (tmp28 != null) {
            io_pin_right_28 = tmp28;
        }
        if (tmp29 != null) {
            io_pin_right_29 = tmp29;
        }

        if (tmp7 != null) {
            io_pin_left_7 = tmp7;
        }
        if (tmp0 != null) {
            io_pin_left_0 = tmp0;
        }
        if (tmp2 != null) {
            io_pin_left_2 = tmp2;
        }
        if (tmp3 != null) {
            io_pin_left_3 = tmp3;
        }
        if (tmp21 != null) {
            io_pin_left_21 = tmp21;
        }
        if (tmp22 != null) {
            io_pin_left_22 = tmp22;
        }
        if (tmp23 != null) {
            io_pin_left_23 = tmp23;
        }
        if (tmp24 != null) {
            io_pin_left_24 = tmp24;
        }
        if (tmp25 != null) {
            io_pin_left_25 = tmp25;
        }

    }

    private void processPin(GpioPinDigitalStateChangeEvent event, GpioPinDigital pin, VSObject obj, int c) {
        if (event.getPin() == pin) {

            if (event.getState() == PinState.HIGH) {
                ((VSBoolean) obj).setValue(true);
            } else {
                ((VSBoolean) obj).setValue(false);
            }
            element.notifyPin(c);
        }

    }

    private GpioPinDigital ddd(VSComboBox combo, Pin raspin) {
        GpioPinDigital pin;
        if (combo.selectedIndex == GPIO_INPUT) {
            //pin = gpio.provisionDigitalInputPin(raspin);

            pin = gpio.provisionDigitalInputPin(raspin, PinPullResistance.PULL_DOWN);

            // create and register gpio pin listener
            pin.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    // display pin state on console
                    //System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

                    
                    /*
                    if (event.getPin() == pin_left_25) {

                        if (event.getState() == PinState.HIGH) {
                            ((VSBoolean) io_pin_left_25).setValue(true);
                        } else {
                            ((VSBoolean) io_pin_left_25).setValue(false);
                        }
                        element.notifyPin(16); // GPIO 25
                    }*/

                    int c = 0;                    
                    processPin(event, pin_right_1, io_pin_right_1, c++);
                    processPin(event, pin_right_4,io_pin_right_4,  c++);
                    processPin(event, pin_right_5,io_pin_right_5, c++);
                    processPin(event, pin_right_6, io_pin_right_6, c++);
                    processPin(event, pin_right_26, io_pin_right_26, c++);
                    processPin(event, pin_right_27, io_pin_right_27, c++);
                    processPin(event, pin_right_28, io_pin_right_28, c++);
                    processPin(event, pin_right_29, io_pin_right_29, c++);

                    processPin(event, pin_left_7, io_pin_left_7, c++);
                    processPin(event, pin_left_0, io_pin_left_0, c++);
                    processPin(event, pin_left_2,io_pin_left_2,  c++);
                    processPin(event, pin_left_3, io_pin_left_3, c++);
                    processPin(event, pin_left_21, io_pin_left_21, c++);
                    processPin(event, pin_left_22, io_pin_left_22, c++);
                    processPin(event, pin_left_23, io_pin_left_23, c++);
                    processPin(event, pin_left_24, io_pin_left_24, c++);
                    processPin(event, pin_left_25,io_pin_left_25, c++);                    

                }

            });

        } else {
            pin = gpio.provisionDigitalOutputPin(raspin);
        }

        return pin;

    }

    @Override
    public void start() {

        try {
            // create gpio controller

            if (gpio == null) {
                gpio = GpioFactory.getInstance();
            } else {
                gpio.shutdown();
                gpio.unprovisionPin(pin_right_1);
                gpio.unprovisionPin(pin_right_4);
                gpio.unprovisionPin(pin_right_5);
                gpio.unprovisionPin(pin_right_6);
                gpio.unprovisionPin(pin_right_26);
                gpio.unprovisionPin(pin_right_27);
                gpio.unprovisionPin(pin_right_28);
                gpio.unprovisionPin(pin_right_29);

                gpio.unprovisionPin(pin_left_7);
                gpio.unprovisionPin(pin_left_0);
                gpio.unprovisionPin(pin_left_2);
                gpio.unprovisionPin(pin_left_3);
                gpio.unprovisionPin(pin_left_21);
                gpio.unprovisionPin(pin_left_22);
                gpio.unprovisionPin(pin_left_23);
                gpio.unprovisionPin(pin_left_24);
                gpio.unprovisionPin(pin_left_25);

                gpio = GpioFactory.getInstance();
            }

            if (gpio != null) {

                pin_right_1 = ddd(gpio_right_1, RaspiPin.GPIO_01);
                pin_right_4 = ddd(gpio_right_4, RaspiPin.GPIO_04);
                pin_right_5 = ddd(gpio_right_5, RaspiPin.GPIO_05);
                pin_right_6 = ddd(gpio_right_6, RaspiPin.GPIO_06);
                pin_right_26 = ddd(gpio_right_26, RaspiPin.GPIO_26);
                pin_right_27 = ddd(gpio_right_27, RaspiPin.GPIO_27);
                pin_right_28 = ddd(gpio_right_28, RaspiPin.GPIO_28);
                pin_right_29 = ddd(gpio_right_29, RaspiPin.GPIO_29);

                pin_left_7 = ddd(gpio_left_7, RaspiPin.GPIO_07);
                pin_left_0 = ddd(gpio_left_0, RaspiPin.GPIO_00);
                pin_left_2 = ddd(gpio_left_2, RaspiPin.GPIO_02);
                pin_left_3 = ddd(gpio_left_3, RaspiPin.GPIO_03);
                pin_left_21 = ddd(gpio_left_21, RaspiPin.GPIO_21);
                pin_left_22 = ddd(gpio_left_22, RaspiPin.GPIO_22);
                pin_left_23 = ddd(gpio_left_23, RaspiPin.GPIO_23);
                pin_left_24 = ddd(gpio_left_24, RaspiPin.GPIO_24);
                pin_left_25 = ddd(gpio_left_25, RaspiPin.GPIO_25);

            }

            // set shutdown state for this pin
            //pin.setShutdownOptions(true, PinState.LOW);
            // stop all GPIO activity/threads by shutting down the GPIO controller
            // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
            //gpio.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(Raspberry_GPIO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Attention!", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void stop() {

    }

    @Override
    public void elementActionPerformed(ElementActionEvent evt) {
        int idx = evt.getSourcePinIndex();
        //System.out.println("idx=" + idx);

        switch (idx) {
            case 0: {
                // GPIO_1
                //if ()                
                //pin_right_1.high();

                break;
            }

            case 9: {
                // S1

                break;
            }
            case 10: {
                // S2

                break;
            }
            case 11: {
                // S3

                break;
            }

        }
    }

    boolean changed = false;

    @Override
    public void destElementCalled() {
        if (changed) {
            //outReceived.setValue(false);
            element.notifyPin(1);
            changed = false;
        }
    }

    @Override
    public void getCommand(String commando, Object value) {

        if (value instanceof VS1DByte) {
            VS1DByte values = (VS1DByte) value;

            if (commando.equals("BYTESRECEIVED")) {

            }
        }

    }

    private void ppp(VSComboBox combo, VSObject xxxx, GpioPinDigital pinx) {
        if (combo.selectedIndex == GPIO_OUTPUT) {
            VSBoolean pin = (VSBoolean) xxxx;
            if (pin.getValue()) {
                ((GpioPinDigitalOutput) pinx).high();
            } else {
                ((GpioPinDigitalOutput) pinx).low();
            }
        }
    }

    @Override
    public void process() {

        ppp(gpio_right_1, io_pin_right_1, pin_right_1);
        ppp(gpio_right_4, io_pin_right_4, pin_right_4);
        ppp(gpio_right_5, io_pin_right_5, pin_right_5);
        ppp(gpio_right_6, io_pin_right_6, pin_right_6);
        ppp(gpio_right_26, io_pin_right_26, pin_right_26);
        ppp(gpio_right_27, io_pin_right_27, pin_right_27);
        ppp(gpio_right_28, io_pin_right_28, pin_right_28);
        ppp(gpio_right_29, io_pin_right_29, pin_right_29);

        ppp(gpio_left_7, io_pin_left_7, pin_left_7);
        ppp(gpio_left_0, io_pin_left_0, pin_left_0);
        ppp(gpio_left_2, io_pin_left_2, pin_left_2);
        ppp(gpio_left_3, io_pin_left_3, pin_left_3);
        ppp(gpio_left_21, io_pin_left_21, pin_left_21);
        ppp(gpio_left_22, io_pin_left_22, pin_left_22);
        ppp(gpio_left_23, io_pin_left_23, pin_left_23);
        ppp(gpio_left_24, io_pin_left_24, pin_left_24);
        ppp(gpio_left_25, io_pin_left_25, pin_left_25);

    }

    @Override
    public void loadFromStream(java.io.FileInputStream fis) {

        gpio_left_7.loadFromStream(fis);
        gpio_left_0.loadFromStream(fis);
        gpio_left_2.loadFromStream(fis);
        gpio_left_3.loadFromStream(fis);
        gpio_left_21.loadFromStream(fis);
        gpio_left_22.loadFromStream(fis);
        gpio_left_23.loadFromStream(fis);
        gpio_left_24.loadFromStream(fis);
        gpio_left_25.loadFromStream(fis);

        gpio_right_1.loadFromStream(fis);
        gpio_right_4.loadFromStream(fis);
        gpio_right_5.loadFromStream(fis);
        gpio_right_6.loadFromStream(fis);
        gpio_right_26.loadFromStream(fis);
        gpio_right_27.loadFromStream(fis);
        gpio_right_28.loadFromStream(fis);
        gpio_right_29.loadFromStream(fis);


        init();
    }

    @Override
    public void saveToStream(java.io.FileOutputStream fos) {

        gpio_left_7.saveToStream(fos);
        gpio_left_0.saveToStream(fos);
        gpio_left_2.saveToStream(fos);
        gpio_left_3.saveToStream(fos);
        gpio_left_21.saveToStream(fos);
        gpio_left_22.saveToStream(fos);
        gpio_left_23.saveToStream(fos);
        gpio_left_24.saveToStream(fos);
        gpio_left_25.saveToStream(fos);

        gpio_right_1.saveToStream(fos);
        gpio_right_4.saveToStream(fos);
        gpio_right_5.saveToStream(fos);
        gpio_right_6.saveToStream(fos);
        gpio_right_26.saveToStream(fos);
        gpio_right_27.saveToStream(fos);
        gpio_right_28.saveToStream(fos);
        gpio_right_29.saveToStream(fos);

    }

    @Override
    public void getSingleByte(int i) {

        char ch = (char) i;

        /*if (ch == '>') {
            verarbeiteCMD();
            cmd.clear();

        } else {
            cmd.add("" + ch);
        }*/
    }
}
