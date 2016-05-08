package de.myopenlab.update;

import java.util.ArrayList;

public class MyOpenLabRow {
      
    private String entry_name;
    private String version;
    private String author;     
    private String short_description; 
    private String date;
    private String caption_de; 
    private String caption_en; 
    private String caption_es; 
    private String dest_path; 
    private String categorie;
    private String type; 
    public ArrayList<TestItem> items = null;
 
    public MyOpenLabRow() {
        this( "", "", "", "", "", "", "", "");
    }
 
    public MyOpenLabRow(String entry_name, String version, String author, String short_description, String dest_path, String categorie, String type, String date) {
        items = new ArrayList<>();
        this.entry_name = entry_name;
        this.version = version;
        this.author = author;
        this.short_description = short_description;        
        this.dest_path = dest_path;        
        this.categorie=categorie;
        this.type=type;
        this.date=date;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
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
     * @return the short_description
     */
    public String getShort_description() {
        return short_description;
    }

    /**
     * @param short_description the short_description to set
     */
    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    /**
     * @return the dest_path
     */
    public String getDest_path() {
        return dest_path;
    }

    /**
     * @param dest_path the dest_path to set
     */
    public void setDest_path(String dest_path) {
        this.dest_path = dest_path;
    }

    /**
     * @return the entry_name
     */
    public String getEntry_name() {
        return entry_name;
    }

    /**
     * @param entry_name the entry_name to set
     */
    public void setEntry_name(String entry_name) {
        this.entry_name = entry_name;
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
    
    
}