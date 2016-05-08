/*
MyOpenLab by Carmelo Salafia www.myopenlab.de
Copyright (C) 2004  Carmelo Salafia cswi@gmx.de

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package VisualLogic;

// FontChooser.java
// A font chooser that allows users to pick a font by name, size, style, and
// color.  The color selection will be provided by a JColorChooser pane.  This
// dialog builds an AttributeSet suitable for use with JTextPane.
//
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooser extends JDialog implements ActionListener {
  
  JComboBox fontName;
  JCheckBox fontBold, fontItalic;
  JTextField fontSize;
  JLabel previewLabel;
  SimpleAttributeSet attributes;
  Font newFont;
  Color newColor;
  
  public static boolean result=false;

  public FontChooser(Frame parent)
  {
    super(parent, "Font Chooser", true);
    setSize(400, 200);
    
    setModal(true);
    
    setResizable(false);
    attributes = new SimpleAttributeSet();

    // make sure that any way they cancel the window does the right thing
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        closeAndCancel();
      }
    });

    // Start the long process of setting up our interface
    Container c = getContentPane();

    JPanel fontPanel = new JPanel();
    fontName = new JComboBox(new String[] {"TimesRoman",
                                           "Helvetica", "Courier"});
    fontName.setSelectedIndex(1);
    fontName.addActionListener(this);
    fontSize = new JTextField("12", 4);
    fontSize.setHorizontalAlignment(SwingConstants.RIGHT);
    fontSize.addActionListener(this);
    fontBold = new JCheckBox("Bold");
    fontBold.setSelected(true);
    fontBold.addActionListener(this);
    fontItalic = new JCheckBox("Italic");
    fontItalic.addActionListener(this);

    fontPanel.add(fontName);
    fontPanel.add(new JLabel(" Size: "));
    fontPanel.add(fontSize);
    fontPanel.add(fontBold);
    fontPanel.add(fontItalic);

    c.add(fontPanel, BorderLayout.NORTH);

    // Set up the color chooser panel and attach a change listener so that color
    // updates get reflected in our preview label.

    JPanel previewPanel = new JPanel(new BorderLayout());
    previewLabel = new JLabel("Here's a sample of this font.");

    previewPanel.add(previewLabel, BorderLayout.CENTER);

    // Add in the Ok and Cancel buttons for our dialog box
    JButton okButton = new JButton("Ok");
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndSave();
        result=true;
      }
    });
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndCancel();
        result=false;
      }
    });

    JPanel controlPanel = new JPanel();
    controlPanel.add(okButton);
    controlPanel.add(cancelButton);
    previewPanel.add(controlPanel, BorderLayout.SOUTH);

    // Give the preview label room to grow.
    previewPanel.setMinimumSize(new Dimension(100, 100));
    previewPanel.setPreferredSize(new Dimension(100, 100));

    c.add(previewPanel, BorderLayout.SOUTH);
  }
  // Ok, something in the font changed, so figure that out and make a
  // new font for the preview label
  public void actionPerformed(ActionEvent ae) {
    // Check the name of the font
    if (!StyleConstants.getFontFamily(attributes)
                       .equals(fontName.getSelectedItem())) {
      StyleConstants.setFontFamily(attributes,
                                   (String)fontName.getSelectedItem());
    }
    // Check the font size (no error checking yet)
    if (StyleConstants.getFontSize(attributes) !=
                                   Integer.parseInt(fontSize.getText())) {
      StyleConstants.setFontSize(attributes,
                                 Integer.parseInt(fontSize.getText()));
    }
    // Check to see if the font should be bold
    if (StyleConstants.isBold(attributes) != fontBold.isSelected()) {
      StyleConstants.setBold(attributes, fontBold.isSelected());
    }
    // Check to see if the font should be italic
    if (StyleConstants.isItalic(attributes) != fontItalic.isSelected()) {
      StyleConstants.setItalic(attributes, fontItalic.isSelected());
    }
    // and update our preview label
    updatePreviewFont();
  }

  // Get the appropriate font from our attributes object and update
  // the preview label
  protected void updatePreviewFont() {
    String name = StyleConstants.getFontFamily(attributes);
    boolean bold = StyleConstants.isBold(attributes);
    boolean ital = StyleConstants.isItalic(attributes);
    int size = StyleConstants.getFontSize(attributes);

    //Bold and italic don't work properly in beta 4.
    Font f = new Font(name, (bold ? Font.BOLD : 0) +
                            (ital ? Font.ITALIC : 0), size);
    previewLabel.setFont(f);
  }

  // Get the appropriate color from our chooser and update previewLabel
  protected void updatePreviewColor() {
    // manually force the label to repaint
    previewLabel.repaint();
  }
  public Font getNewFont() { return newFont; }
  public Color getNewColor() { return newColor; }
  public AttributeSet getAttributes() { return attributes; }

  public void closeAndSave() {
    // save font & color information
    newFont = previewLabel.getFont();
    newColor = previewLabel.getForeground();

    // and then close the window
    setVisible(false);
  }

  public void closeAndCancel() {
    // erase any font information and then close the window
    newFont = null;
    newColor = null;
    setVisible(false);
  }
}

