/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package create_new_group;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class SummaryCart {
    
  private String caption="";
  private String caption_en="";
  private String caption_es="";
  private String categorie="";
  private String author="";
  private String email="";
  private String web="";
  private String version="";
  private String date="";
  private String short_description="";
  private String dest_path="";
  private String type="";  

    @XmlElement( name = "caption" )
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
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
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the web
     */
    public String getWeb() {
        return web;
    }

    /**
     * @param web the web to set
     */
    public void setWeb(String web) {
        this.web = web;
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
}
