package com.georgeren.myboring.read.mvp.entity;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by georgeRen on 2017/7/19.
 */
@Table("picture")
public class SimplePicture implements Serializable {
    @PrimaryKey(AssignType.BY_MYSELF)
    public String id;

    private String big;
    private String small;
    private String middle;

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }
}
