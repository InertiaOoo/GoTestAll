package com.dfzt.olinemusic.mvp.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchMusic implements Parcelable {

    /**
     * songid : 273993
     * songname : 庐州月
     * encrypted_songid : 93081D670D2608599BFAE6
     * bitrate_fee : {"0":"0|0","1":"0|0"}
     * has_mv : 0
     * resource_provider : 1
     * yyr_artist : 0
     * artistname : 许嵩
     * control : 0000000000
     * info :
     * weight : 3546099
     * resource_type_ext : 0
     * resource_type : 0
     */

    private String songid;
    private String songname;
    private String encrypted_songid;
    private String bitrate_fee;
    private String has_mv;
    private String resource_provider;
    private String yyr_artist;
    private String artistname;
    private String control;
    private String info;
    private String weight;
    private String resource_type_ext;
    private String resource_type;

    public SearchMusic(){

    }

    protected SearchMusic(Parcel in) {
        songid = in.readString();
        songname = in.readString();
        encrypted_songid = in.readString();
        bitrate_fee = in.readString();
        has_mv = in.readString();
        resource_provider = in.readString();
        yyr_artist = in.readString();
        artistname = in.readString();
        control = in.readString();
        info = in.readString();
        weight = in.readString();
        resource_type_ext = in.readString();
        resource_type = in.readString();
    }

    public static final Creator<SearchMusic> CREATOR = new Creator<SearchMusic>() {
        @Override
        public SearchMusic createFromParcel(Parcel in) {
            return new SearchMusic(in);
        }

        @Override
        public SearchMusic[] newArray(int size) {
            return new SearchMusic[size];
        }
    };

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getEncrypted_songid() {
        return encrypted_songid;
    }

    public void setEncrypted_songid(String encrypted_songid) {
        this.encrypted_songid = encrypted_songid;
    }

    public String getBitrate_fee() {
        return bitrate_fee;
    }

    public void setBitrate_fee(String bitrate_fee) {
        this.bitrate_fee = bitrate_fee;
    }

    public String getHas_mv() {
        return has_mv;
    }

    public void setHas_mv(String has_mv) {
        this.has_mv = has_mv;
    }

    public String getResource_provider() {
        return resource_provider;
    }

    public void setResource_provider(String resource_provider) {
        this.resource_provider = resource_provider;
    }

    public String getYyr_artist() {
        return yyr_artist;
    }

    public void setYyr_artist(String yyr_artist) {
        this.yyr_artist = yyr_artist;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getResource_type_ext() {
        return resource_type_ext;
    }

    public void setResource_type_ext(String resource_type_ext) {
        this.resource_type_ext = resource_type_ext;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songid);
        dest.writeString(songname);
        dest.writeString(encrypted_songid);
        dest.writeString(bitrate_fee);
        dest.writeString(has_mv);
        dest.writeString(resource_provider);
        dest.writeString(yyr_artist);
        dest.writeString(artistname);
        dest.writeString(control);
        dest.writeString(info);
        dest.writeString(weight);
        dest.writeString(resource_type_ext);
        dest.writeString(resource_type);
    }
}
