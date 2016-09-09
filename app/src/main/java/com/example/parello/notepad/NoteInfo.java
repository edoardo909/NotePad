package com.example.parello.notepad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Parello on 08/09/2016.
 */
public class NoteInfo implements Parcelable {

    private int idCode;
    private String title;
    private String body;

    private NoteInfo(Parcel in) {
        idCode = in.readInt();
        title = in.readString();
        body = in.readString();
    }

    public NoteInfo() {

    }

    public NoteInfo(String title) {
        this.title = title;
    }


    public int getIdCode() {
        return idCode;
    }

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCode);
        dest.writeString(body);
        dest.writeString(title);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public NoteInfo createFromParcel(Parcel in) {
            return new NoteInfo(in);
        }

        @Override
        public NoteInfo[] newArray(int size) {
            return new NoteInfo[size];
        }

    };
}