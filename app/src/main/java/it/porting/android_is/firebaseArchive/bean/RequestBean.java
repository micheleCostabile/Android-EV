package it.porting.android_is.firebaseArchive.bean;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

/**
 * Bean per requests
 */


public class RequestBean {
    private int id;
    private String level;
    private int serial;
    private int validated_cfu;
    private String year;
    private Timestamp release_date;
    private Timestamp expiry_date;
    private String user_key;
    private String user_name;
    private String user_surname;
    private String ente;
    private String stato;
    private String matricola;


    public RequestBean(int id, String level, int serial, int validated_cfu, String year, Timestamp release_date, Timestamp expiry_date, String user_key, String user_name, String user_surname, String ente, String stato,String matricola) {
        this.id = id;
        this.level = level;
        this.serial = serial;
        this.validated_cfu = validated_cfu;
        this.year = year;
        this.release_date = release_date;
        this.expiry_date = expiry_date;
        this.user_key = user_key;
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.ente = ente;
        this.stato = stato;
        this.matricola= matricola;

    }

    public RequestBean(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getValidated_cfu() {
        return validated_cfu;
    }

    public void setValidated_cfu(int validated_cfu) {
        this.validated_cfu = validated_cfu;
    }
    public String getMatricola(){ return matricola;}
    public void setMatricola(String matricola){ this.matricola = matricola;}

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Timestamp getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Timestamp release_date) {
        this.release_date = release_date;
    }

    public Timestamp getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Timestamp expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getUser_key(){
        return this.user_key;
    }

    public void setUser_key(String user_key){
        this.user_key = user_key;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
    }

    public String getEnte() {
        return ente;
    }

    public void setEnte(String ente) {
        this.ente = ente;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }




    @Override
    public String toString() {
        return "RequestBean{" +
                "id=" + id +
                ", level='" + level + '\'' +
                ", serial=" + serial +
                ", validated_cfu=" + validated_cfu +
                ", matricola='" + matricola + '\'' +
                ", year='" + year + '\'' +
                ", release_date=" + release_date +
                ", expiry_date=" + expiry_date +
                ", user_key='" + user_key + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_surname='" + user_surname + '\'' +
                ", ente= '" + ente + '\'' +
                ", stato= '"+ stato +'\'' +
                '}';
    }
}
