package com.tsinghua.sec.domain;

/**
 * Created by ji on 16-5-26.
 */
public class Solder {

    //名字
    private String name;

    //级别
    private Integer rank;

    //在线状态
    private int status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
