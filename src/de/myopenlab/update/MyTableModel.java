package de.myopenlab.update;

import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

    public String[] HEADER = {"Install", "", "Package Name", "Caption", "Category", "Date", "Author", "Type"};
    public List<MyTableRow> data;

    public MyTableModel(List<MyTableRow> data) {
        this.data = data;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return HEADER.length;
    }

    private String getCaption(MyTableRow rowData) {

        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        switch (lang) {
            case "de":
                return rowData.getCaption_de();
            case "en":
                return rowData.getCaption_en();
            case "es":
                return rowData.getCaption_es();
        }
        return "";
    }

    @Override
    public Object getValueAt(int row, int column) {
        MyTableRow rowData = data.get(row);

        switch (column) {
            case 0:
                return rowData.isSelected();                
            case 1:{                
                return rowData.getIcon();
            }
            case 2:
                return rowData.getName();
            case 3:
                return getCaption(rowData);
            case 4:
                return rowData.getCategorie();
            case 5:
                return rowData.getDate();
            case 6:
                return rowData.getAuthor();
            case 7:
                return rowData.getType();
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        MyTableRow rowData = data.get(rowIndex);

        switch (columnIndex) {
            case 0:
                rowData.setSelected((Boolean) aValue);
                break;
        }
    }

    @Override
    public String getColumnName(int column) {
        return HEADER[column];
    }

// Hier kann man die Klasse für eine Spalte ändern.
    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0) {
            return Boolean.class;
        }
        if (column == 1) {
            return ImageIcon.class;            
        }
        return super.getColumnClass(column);
    }

}
