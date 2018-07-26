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


package Peditor;

import VisualLogic.Element;
import VisualLogic.VMObject;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSByte;
import VisualLogic.variables.VSColor;
import VisualLogic.variables.VSColorAdvanced;
import VisualLogic.variables.VSComboBox;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSFile;
import VisualLogic.variables.VSFont;
import VisualLogic.variables.VSImage;
import VisualLogic.variables.VSInteger;
import VisualLogic.variables.VSObject;
import VisualLogic.variables.VSProperties;
import VisualLogic.variables.VSPropertyDialog;
import VisualLogic.variables.VSString;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PropertyEditorItem
{        
    public String strLabel;    
    public Object value;
    
    private JPanel leftPanel;
    private JPanel rightPanel;
    
    public JLabel label;
    public JComponent component;
    public VMObject vmobject;
    public Element element;
    
    public Element getElement()
    {
      return element;    
    }

    public void processChanged() 
    {
        ((PEIF)component).changed();                
        if (element!=null) 
        {
            element.classRef.propertyChanged(value);
            element.propertyChanged(value);
            element.owner.owner.saveForUndoRedo();
        }else
        if (vmobject!=null) 
        {
            vmobject.propertyChanged(value);
            vmobject.owner.saveForUndoRedo();
        }
    }
    
    public VMObject getVMObject()
    {
        return vmobject;
    }
    
    public PropertyEditorItem(int modus,VMObject vmobject,Element element,JFrame frame,JPanel leftPanel, JPanel rightPanel, String strLabel,Object value, double min, double max,boolean editable)
    {
        this.leftPanel=leftPanel;
        this.rightPanel=rightPanel;        
        this.strLabel=strLabel;
        this.value=value;  
        this.element=element;
        this.vmobject=vmobject;      
        
        label=new JLabel(strLabel);
        //label.setBorder(new LineBorder(Color.lightGray));
        
        EmptyBorder eBorder = new EmptyBorder(2, 5, 2, 5); // oben, rechts, unten, links 
        LineBorder lBorder = new LineBorder(new Color(180, 180, 180)); 
        label.setBorder(BorderFactory.createCompoundBorder(lBorder, eBorder)); 

        
        
        label.setBackground(Color.WHITE);
        leftPanel.add(label);
        
        if (modus==0)
        {
            if (value instanceof VSProperties) 
            {
                component=new PropertiesEditor(this,frame,vmobject,(VSProperties)value);
            }else
            if (value instanceof VSString) 
            {
                component=new StringEditor(this,(VSString)value);            
            }else
            if (value instanceof VSImage) 
            {
                component=new ImageEditor(this,frame,(VSImage)value);           
            }else
            if (value instanceof VSBoolean) 
            {
                component=new BooleanEditor(this, (VSBoolean)value);                        
            }else
            if (value instanceof VSInteger) 
            {
                component=new IntegerEditor(this,(VSInteger)value, (int)min, (int)max);
            }else        
            if (value instanceof VSByte) 
            {
                component=new ByteEditor(this,(VSByte)value, (int)min, (int)max);
            }else        
            if (value instanceof VSDouble) 
            {                        
                component=new DoubleEditor(this,(VSDouble)value, min, max);            
            }else        
            if (value instanceof VSColor)
            {
                component=new ColorEditor(this,frame,(VSColor)value,min,max);
            }else
            if (value instanceof VSColorAdvanced)
            {
                component=new AdvancedColorEditor(this,frame,(VSColorAdvanced)value,min,max);
            }else
            if (value instanceof VSPropertyDialog)
            {
                component=new OpenPropertyDialogEditor(this,element,(VSPropertyDialog)value);
            }else
            if (value instanceof VSFont)
            {
                component=new FontEditor(this,frame,(VSFont)value);
            }else
            if (value instanceof VSFile)
            {            
                component=new FileEditor(this,frame,(VSFile)value);
            }else
            if (value instanceof VSComboBox)
            {
                component=new ComboBoxEditor(this,(VSComboBox)value);
            }else            
            if (value instanceof VSNope)
            {
                component=new NopeEditor();
            }
        } 
                
        if (modus==1)
        {
            component=new ReadonlySelector(this,(VSObject)value);
        }         
        
        component.addFocusListener(new java.awt.event.FocusAdapter() 
        {
            /*public void focusGained(java.awt.event.FocusEvent evt) {
              //getVMObject().properyItemFocusGained();
            }*/
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) 
            {
        //        processChanged();
            }
        });
        
               
        component.setEnabled(editable);
        
        
           
        EmptyBorder eBorder1 = new EmptyBorder(2, 5, 2, 5); // oben, rechts, unten, links 
        LineBorder lBorder1 = new LineBorder(new Color(180, 180, 180)); 
        component.setBorder(BorderFactory.createCompoundBorder(lBorder1, eBorder1)); 

      
        
        
        rightPanel.add(component);
        
    }


     
}
