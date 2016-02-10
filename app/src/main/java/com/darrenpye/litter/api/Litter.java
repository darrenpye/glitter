package com.darrenpye.litter.api;

import com.darrenpye.R;

/**
 * Created by darrenpye on 16-02-07.
 *
 * Class that represents a Litter message
 *
 */
public class Litter {
    private long litterId;
    private long userid;
    private long timestamp;
    private String message;
    private long imageId;
    private long relitters;
    private long likes;

    public Litter() {

    }

    public Litter(long userid, String message) {
        this.userid = userid;
        this.message = message;
        this.timestamp = (new java.util.Date()).getTime();
        this.imageId = 0;
        this.relitters = 0;
        this.likes = 0;
    }

    public long getLitterId() {
        return litterId;
    }

    public void setLitterId(long litterId) {
        this.litterId = litterId;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public long getRelitters() {
        return relitters;
    }

    public void setRelitters(long relitters) {
        this.relitters = relitters;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public int getUserImageResource() { return R.drawable.emo_kylo; }
}
