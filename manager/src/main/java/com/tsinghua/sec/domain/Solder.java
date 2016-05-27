package com.tsinghua.sec.domain;

/**
 * Created by ji on 16-5-26.
 */
public class Solder {

    //名字
    private String name;

    //级别
    private Integer rank;

    //密码
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
