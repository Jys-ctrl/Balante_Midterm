package com.example.balante_midterm;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Game implements Serializable {

    String gTit;
    Timestamp gDate;
    String gDev;
    String gGen;
    String gPub;

    public Game( String gTit, Timestamp gDate, String gDev, String gGen, String gPub) {
        this.gTit = gTit;
        this.gDate = gDate;
        this.gDev = gDev;
        this.gGen = gGen;
        this.gPub = gPub;
    }

    public Game() {
    }

    public String getgTit() {
        return gTit;
    }

    public void setgTit(String gTit) {
        this.gTit = gTit;
    }

    public Timestamp getgDate() {
        return gDate;
    }

    public void setgDate(Timestamp gDate) {
        this.gDate = gDate;
    }

    public String getgDev() {
        return gDev;
    }

    public void setgDev(String gDev) {
        this.gDev = gDev;
    }

    public String getgGen() {
        return gGen;
    }

    public void setgGen(String gGen) {
        this.gGen = gGen;
    }

    public String getgPub() {
        return gPub;
    }

    public void setgPub(String gPub) {
        this.gPub = gPub;
    }
}
