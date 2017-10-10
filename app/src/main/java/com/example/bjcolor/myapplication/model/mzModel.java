package com.example.bjcolor.myapplication.model;

import com.example.bjcolor.myapplication.myBean;

/**
 * Created by BJColor on 2017/9/14.
 */

public class mzModel implements ImzModel {

//
//    @Override
//    public void setAge(int id) {
//
//    }
//
//    @Override
//    public void setName(String firstName) {
//
//    }
//
//    @Override
//    public void setSex(String lastName) {
//
//    }

    @Override
    public myBean load() {
        myBean myBean = new myBean();
        myBean.age=11;
        myBean.name="小七";
        myBean.sex="女";
        return myBean;
    }
}
