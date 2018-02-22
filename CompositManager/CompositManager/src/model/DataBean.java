package model;

import java.io.InputStream;
import java.io.Serializable;


public class DataBean implements Serializable {

    InputStream in;

    public DataBean() {
    }

    void setIn(InputStream in) {
        this.in = in;
    }

    InputStream getIn() {
        return this.in;
    }


}
