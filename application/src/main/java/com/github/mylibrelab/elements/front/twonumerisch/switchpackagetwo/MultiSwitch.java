package com.github.mylibrelab.elements.front.twonumerisch.switchpackagetwo;/*
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

// package javaapplication2;


class Beschriftung extends JPanel {
    public String text = "";
    private TasterPanel owner = null;
    public boolean visible = false;

    public Beschriftung(TasterPanel owner, String text) {
        this.text = text;
        this.owner = owner;
        this.setBackground(new Color(100, 100, 100, 0));
        this.setOpaque(false);
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        // if (visible)
        {
            g.setFont(owner.font.getValue());
            g.setColor(owner.fontColor.getValue());

            java.awt.FontMetrics fm = g.getFontMetrics(g.getFont());

            g.drawString(text, 1, 0 + fm.getMaxAscent());
        }
    }
}



public class MultiSwitch extends javax.swing.JPanel {
    public int status = 0;
    public int anzahlStellungen = 3;
    private ExternalIF element;
    private ArrayList listeBeschriftungen = new ArrayList();
    TasterPanel panel;

    /** Creates new form MultiSwitch */
    public MultiSwitch(ExternalIF element, TasterPanel panel) {
        this.element = element;
        this.panel = panel;
        initComponents();
        setBackground(Color.DARK_GRAY);
        createDescriptions();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);


    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void drawRect(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        g.setColor(Color.BLACK);
        double fx = ((double) width) / 100.0;

        int w = (int) (fx * 10);

        double f = height / 4.0;
        for (int i = 1; i < 4; i++) {
            int d = y + (int) (i * f);
            g.setColor(Color.WHITE);
            g.drawLine(x + w, d, x + width - w, d);
            g.setColor(Color.BLACK);
            g.drawLine(x + w, d + 1, x + width - w, d + 1);
        }

    }

    public void createFromCaptions() {
        element.jClearSubElements();
        listeBeschriftungen.clear();
        for (int i = 0; i < panel.values.getLength(); i++) {
            String str = panel.values.getValue(i);
            if (str == null) str = "";
            Beschriftung bes = new Beschriftung(panel, str);
            bes.visible = true;
            element.jAddSubElement2(bes);
            listeBeschriftungen.add(bes);
        }

    }


    public void createDescriptions() {
        element.jClearSubElements();
        panel.values.resize(anzahlStellungen);
        listeBeschriftungen.clear();
        for (int i = 0; i < anzahlStellungen; i++) {
            panel.values.setValue(i, "" + i);
            Beschriftung bes = new Beschriftung(panel, "" + i);
            bes.visible = true;
            element.jAddSubElement2(bes);
            listeBeschriftungen.add(bes);
        }
        // moveDescriptions();
    }


    public void moveDescriptions(Graphics g, int hx) {
        if (element != null) {
            Rectangle bounds = element.jGetBounds();
            int w = bounds.width - 1;
            int h = bounds.height - 1;
            double f = (((double) h) / ((double) anzahlStellungen));

            Point p = element.jGetLocation();

            for (int i = 0; i < listeBeschriftungen.size(); i++) {
                Beschriftung bes = (Beschriftung) listeBeschriftungen.get(i);


                g.setFont(panel.font.getValue());
                java.awt.FontMetrics fm = g.getFontMetrics(g.getFont());

                Rectangle2D r = fm.getStringBounds(bes.text, g);


                int d = (int) (((double) (i)) * f);

                bes.setLocation(p.x - 10 - (int) r.getWidth(), p.y + d + hx / 2);
                bes.setSize((int) r.getWidth() + 5, (int) r.getHeight());
            }

        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int d = getHeight() / anzahlStellungen;

        double fx = ((double) getWidth()) / 100.0;

        int x = (int) (fx * 5);

        int y = status * d + x;

        g.setColor(Color.WHITE);
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        g.drawLine(1, h, w, h);
        g.drawLine(w, 1, w, h);

        drawRect(g, x, y, getWidth() - 1 - x * 2, d - x * 2);

        moveDescriptions(g, d - x * 2);
        // g.setColor(Color.WHITE);
        // g.drawString(""+status,x,y);

    }

}
