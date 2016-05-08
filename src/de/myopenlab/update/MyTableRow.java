package de.myopenlab.update;

import javax.swing.ImageIcon;

public class MyTableRow {
 
    private boolean selected;
    private ImageIcon icon;
    private String name;
    private String categorie; 
    private String date; 
    private String author; 
    private String type; 
    private String caption_de; 
    private String caption_en; 
    private String caption_es; 
 
    public MyTableRow() {
        this(false, null, "", "", "", "", "");
    }
 
    public MyTableRow(boolean selected, ImageIcon icon, String name, String categorie, String date,  String author, String type) {
        this.selected = selected;
        this.icon=icon;
        this.name = name;
        this.categorie = categorie;
        this.date = date;
        this.author = author;        
        this.type = type;        
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * @param categorie the categorie to set
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the caption_de
     */
    public String getCaption_de() {
        return caption_de;
    }

    /**
     * @param caption_de the caption_de to set
     */
    public void setCaption_de(String caption_de) {
        this.caption_de = caption_de;
    }

    /**
     * @return the caption_en
     */
    public String getCaption_en() {
        return caption_en;
    }

    /**
     * @param caption_en the caption_en to set
     */
    public void setCaption_en(String caption_en) {
        this.caption_en = caption_en;
    }

    /**
     * @return the caption_es
     */
    public String getCaption_es() {
        return caption_es;
    }

    /**
     * @param caption_es the caption_es to set
     */
    public void setCaption_es(String caption_es) {
        this.caption_es = caption_es;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the icon
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
}
