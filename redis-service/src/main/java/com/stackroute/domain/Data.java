package com.stackroute.domain;

import java.util.List;

public class Data {
    private List<DataModel> info;

    public Data(List<DataModel> info) {
        this.info = info;
    }

    public List<DataModel> getInfo() {
        return info;
    }

    public void setInfo(List<DataModel> info) {
        this.info = info;
    }
}
