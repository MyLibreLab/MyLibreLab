/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.myopenlab.update;

public class TestItem {
    public String name = "";
    public String type = "";
    public String caption_de = "";
    public String caption_en = "";
    public String caption_es = "";
        
    public TestItem(String name, String type, String caption_de, String caption_en,String caption_es){
        this.name=name;
        this.type=type;
        
        this.caption_de=caption_de;
        this.caption_en=caption_en;
        this.caption_es=caption_es;
    
    }
}
