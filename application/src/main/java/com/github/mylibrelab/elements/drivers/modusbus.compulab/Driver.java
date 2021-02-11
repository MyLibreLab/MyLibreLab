/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.mylibrelab.elements.drivers.modusbus.compulab;// *****************************************************************************

public class Driver implements MyOpenLabDriverIF {
    private boolean isOpen = false;
    private CLusb lib;

    private boolean oldInp1, oldInp2, oldInp3, oldInp4, oldInp5, oldInp6, oldInp7, oldInp8;
    public boolean xStop = false;
    private double A1, A2;
    private double oldA1, oldA2;


    private boolean inp1 = false;
    private boolean inp2 = false;
    private boolean inp3 = false;
    private boolean inp4 = false;
    private boolean inp5 = false;
    private boolean inp6 = false;
    private boolean inp7 = false;
    private boolean inp8 = false;
    private final boolean running = false;

    private boolean inOut1;
    private boolean inOut2;
    private boolean inOut3;
    private boolean inOut4;
    private boolean inOut5;
    private boolean inOut6;
    private boolean inOut7;
    private boolean inOut8;

    private double inAC1;
    private double inAC2;


    private final VSDouble outA1 = new VSDouble(0);
    private final VSDouble outA2 = new VSDouble(0);

    private final javax.swing.Timer timer;

    private MyOpenLabDriverOwnerIF owner;
    private boolean firstTime = true;


    int dValue = 0;


    public void sendCommand(String commando, Object value) {
        // System.out.println("Commando="+commando);


        if (commando.equalsIgnoreCase("ClearAllDigital")) {
            lib.DOUT(0);
        } else if (value instanceof Boolean) {
            Boolean val = (Boolean) value;

            if (commando.equals("out1"))
                if (val)
                    dValue |= (1 << 0);
                else
                    dValue &= ~(1 << 0);
            else if (commando.equals("out2"))
                if (val)
                    dValue |= (1 << 1);
                else
                    dValue &= ~(1 << 1);
            else if (commando.equals("out3"))
                if (val)
                    dValue |= (1 << 2);
                else
                    dValue &= ~(1 << 2);
            else if (commando.equals("out4"))
                if (val)
                    dValue |= (1 << 3);
                else
                    dValue &= ~(1 << 3);
            else if (commando.equals("out5"))
                if (val)
                    dValue |= (1 << 4);
                else
                    dValue &= ~(1 << 4);
            else if (commando.equals("out6"))
                if (val)
                    dValue |= (1 << 5);
                else
                    dValue &= ~(1 << 5);
            else if (commando.equals("out7"))
                if (val)
                    dValue |= (1 << 6);
                else
                    dValue &= ~(1 << 6);
            else if (commando.equals("out8")) if (val)
                dValue |= (1 << 7);
            else
                dValue &= ~(1 << 7);


            lib.DOUT(dValue);
        }
    }

    public void registerOwner(MyOpenLabDriverOwnerIF owner) {
        this.owner = owner;
        firstTime = true;
    }

    int test = 0;

    public Driver() {

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (isOpen) {
                    try {
                        int din = lib.DIN();

                        inp1 = (din & 1) > 0;
                        inp2 = (din & 2) > 0;
                        inp3 = (din & 4) > 0;
                        inp4 = (din & 8) > 0;
                        inp5 = (din & 16) > 0;
                        inp6 = (din & 32) > 0;
                        inp7 = (din & 64) > 0;
                        inp8 = (din & 128) > 0;

                        int a1 = lib.AIN(1);
                        int a2 = lib.AIN(2);


                        if (inp1 != oldInp1) {
                            oldInp1 = inp1;
                            owner.getCommand("inp1", inp1);
                        }
                        if (inp2 != oldInp2) {
                            oldInp2 = inp2;
                            owner.getCommand("inp2", inp2);
                        }
                        if (inp3 != oldInp3) {
                            oldInp3 = inp3;
                            owner.getCommand("inp3", inp3);
                        }
                        if (inp4 != oldInp4) {
                            oldInp4 = inp4;
                            owner.getCommand("inp4", inp4);
                        }
                        if (inp5 != oldInp5) {
                            oldInp5 = inp5;
                            owner.getCommand("inp5", inp5);
                        }
                        if (inp6 != oldInp6) {
                            oldInp6 = inp6;
                            owner.getCommand("inp6", inp6);
                        }
                        if (inp7 != oldInp7) {
                            oldInp7 = inp7;
                            owner.getCommand("inp7", inp7);
                        }
                        if (inp8 != oldInp8) {
                            oldInp8 = inp8;
                            owner.getCommand("inp8", inp8);
                        }


                        if (a1 != oldA1) {
                            oldA1 = a1;
                            owner.getCommand("DAC1", new Integer(a1));
                        }
                        if (a2 != oldA2) {
                            oldA2 = a2;
                            owner.getCommand("DAC2", new Integer(a2));
                        }

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        };

        timer = new javax.swing.Timer(1, taskPerformer);

    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Attention!", JOptionPane.ERROR_MESSAGE);
    }

    public void driverStart(ArrayList args) {
        if (isOpen) {
            showMessage("CompuLAB already Open!");
            return;
        }
        isOpen = false;
        try {
            System.out.println("OpenDevice: CompuLAB");
            lib = CLusb.INSTANCE;
            // if (args!=null && args.size()==3)
            {
                /*
                 * Integer adresse=(Integer)args.get(0);
                 * Integer dbt1=(Integer)args.get(1);
                 * Integer dbt2=(Integer)args.get(2);
                 */


                lib.INIT();
                lib.DOUT(0); // Alle Outputs auf 0.

                /*
                 * System.out.println("Adresse = "+adresse.intValue());
                 * System.out.println("DeboundTime C-1 = "+dbt1.intValue());
                 * System.out.println("DeboundTime C-2 = "+dbt2.intValue());
                 */
            }
            isOpen = true;
        } catch (RuntimeException pvException) {
            showMessage("CompuLAB : " + pvException.getLocalizedMessage());
        }
        if (isOpen) {

        }

        timer.start();
    }

    public void driverStop() {
        if (timer != null) {
            timer.stop();
        }
        if (isOpen) {

            isOpen = false;
        }
    }


}
