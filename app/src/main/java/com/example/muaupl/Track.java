package com.example.muaupl;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Track implements Parcelable {
    private String title;
    private String time;
    public String trackUri;   private Date date; // Добавляем поле для даты
    private String type; // Добавляем поле для типа трека

    public Track(String title, String time, String trackUri) {
        this.title = title;
        this.time = time;
        this.trackUri = trackUri;
    }

    // Добавляем конструктор с пятью параметрами
    public Track(String title, String time, String trackUri, Date date, String type) {
        this.title = title;
        this.time = time;
        this.trackUri = trackUri;
        this.date = date;
        this.type = type;
    }

    // Добавляем методы для получения даты и типа трека
    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getTrackUri() {
        return trackUri;
    }

    public String getTime() {
        return time;
    }

    protected Track(Parcel in) {
        title = in.readString();
        time = in.readString();
        trackUri = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(time);
        dest.writeString(trackUri);
    }

    //метод для парсинга времени в длительность в миллисекундах
    public long getDuration() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        try {
            Date timeDate = sdf.parse(time);
            return timeDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
