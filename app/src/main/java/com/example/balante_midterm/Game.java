package com.example.balante_midterm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Game implements Serializable, Parcelable {

    String gID;
    String gTit;
    Timestamp gDate;
    String gDev;
    String gGen;
    String gPub;

    public Game(String gID, String gTit, Timestamp gDate, String gDev, String gGen, String gPub) {
        this.gID = gID;
        this.gTit = gTit;
        this.gDate = gDate;
        this.gDev = gDev;
        this.gGen = gGen;
        this.gPub = gPub;
    }

    public Game() {
    }

    protected Game(Parcel in) {
        gID = in.readString();
        gTit = in.readString();
        gDate = in.readParcelable(Timestamp.class.getClassLoader());
        gDev = in.readString();
        gGen = in.readString();
        gPub = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public String getgID() {
        return gID;
    }

    public void setgID(String gID) {
        this.gID = gID;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gID);
        parcel.writeString(gTit);
        parcel.writeParcelable(gDate, i);
        parcel.writeString(gDev);
        parcel.writeString(gGen);
        parcel.writeString(gPub);
    }
}
