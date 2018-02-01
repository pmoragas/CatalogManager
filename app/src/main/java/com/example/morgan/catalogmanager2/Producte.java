package com.example.morgan.catalogmanager2;

/**
 * Created by MORGAN on 23/01/2018.
 */

public class Producte {

    /*
    codiArticle: PK text, identifica el producte
    descripcio: text, defineix el producte
    PVP: real, preu de venda
    stock: real, quantitat estocada
     */
    private int id;
    private String codiArticle;
    private String descripcio;
    private double PVP;
    private double stock;

    public Producte (){}

    public Producte (String codiArticle, String descripcio, double PVP, double stock){
        super();
        this.codiArticle = codiArticle;
        this.descripcio = descripcio;
        this.PVP = PVP;
        this.stock = stock;
    }

    public int getId() {return this.id;}
    public String getCodiArticle (){
        return this.codiArticle;
    }
    public String getDescripcio (){
        return this.descripcio;
    }
    public double getPVP (){
        return this.PVP;
    }
    public double getStock (){
        return this.stock;
    }


    public void setCodiArticle(String codiArticle){
        this.codiArticle = codiArticle;
    }
    public void setDescripcio(String descripcio){
        this.descripcio = descripcio;
    }
    public void setPVP (double PVP){
        this.PVP = PVP;
    }
    public void setStock(double stock){
        this.stock = stock;
    }
}
