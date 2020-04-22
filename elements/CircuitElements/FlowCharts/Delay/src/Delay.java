//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
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
import tools.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Delay extends MainFlow {

    private Image image;
    public VSFlowInfo in;
    public VSFlowInfo out = new VSFlowInfo();
    public boolean started = false;
    private VSBasisIF basis;
    long time1 = 0;
    long time2 = 0;

    long theDelay = -1;

    @Override
    public void onDispose() {
        if (image != null) {
            image.flush();
            image = null;
        }

    }

    @Override
    public void paint(java.awt.Graphics g) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();
            Graphics2D g2 = (Graphics2D) g;

            g2.setFont(font);

            int mitteX = bounds.x + (bounds.width) / 2;
            int mitteY = bounds.y + (bounds.height) / 2;

            int distanceY = 10;

            g2.setColor(new Color(204, 204, 255));
            g2.fillRect(bounds.x, mitteY - distanceY, bounds.width, 2 * distanceY);
            g2.setColor(Color.BLACK);
            g2.drawRect(bounds.x, mitteY - distanceY, bounds.width, 2 * distanceY);

            String caption = " wait(" + variable.getValue() + ")ms";

            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(caption, g2);

            g2.setColor(Color.BLACK);
            g.drawString(caption, mitteX - (int) (r.getWidth() / 2), (int) (mitteY + fm.getHeight() / 2) - 3);
            g.drawImage(image, 10, 16, null);

        }    //drawImageCentred(g,image);

        super.paint(g);
    }

    @Override
    public void init() {
        standardWidth = 130;
        width = standardWidth;
        height = 40;
        toInclude = "----wait()ms";

        initPins(1, 0, 1, 0);
        setSize(width, height);
        element.jSetInnerBorderVisibility(false);

        image = element.jLoadImage(element.jGetSourcePath() + "image.png");

        element.jInitPins();

        setPin(1, ExternalIF.C_FLOWINFO, ExternalIF.PIN_OUTPUT);
        setPin(0, ExternalIF.C_FLOWINFO, ExternalIF.PIN_INPUT);

        variable.setValue("500");
        setName("#FLOWCHART_ProcessDelay#");

    }

    @Override
    public void xOnInit() {
        super.xOnInit();

    }

    @Override
    public void start() {
        started = false;
        element.jNotifyMeForClock();
    }

    @Override
    public void stop() {

    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted() {
        started = true;
    }

    public void reset() {
        out.copyValueFrom(in);
        element.notifyPin(1);
    }

    @Override
    public void elementActionPerformed(ElementActionEvent evt) {
        if (evt.getSourcePinIndex() == 0) {
            started = true;
            time1 = System.nanoTime();

            Object o = basis.vsEvaluate(in, variable.getValue());
            theDelay = Double.valueOf(o.toString()).longValue();
            //System.out.println("theDelay="+theDelay);

            //element.notifyPin(1);
        }
    }

    @Override
    public void processClock() {
        if (started) {
            time2 = System.nanoTime();
            long diff = time2 - time1;
            
            //System.out.println("diff="+diff+", theDelay="+theDelay+"theDelay * 1000000="+(theDelay * 1000000));

            if (diff >= theDelay * 1000000) {
                reset();
                started = false;
            }
        }
    }

    @Override
    public void initInputPins() {
        in = (VSFlowInfo) element.getPinInputReference(0);
        basis = element.jGetBasis();
        if (in == null) {
            in = new VSFlowInfo();
        }
    }

    @Override
    public void initOutputPins() {
        element.setPinOutputReference(1, out);
    }

}
