package com.example.bjcolor.myapplication.presenter;

import com.example.bjcolor.myapplication.model.mzModel;
import com.example.bjcolor.myapplication.myBean;
import com.example.bjcolor.myapplication.view.ImzDemo;

/**
 * Created by BJColor on 2017/9/14.
 */

public class mzPresenter {

    private final ImzDemo view;
    private final mzModel model;

    public mzPresenter(ImzDemo view1) {
        view = view1;
        model = new mzModel();
    }

    public void setText(){
        myBean load = model.load();
        view.setAge(load.age);
        view.setSex(load.sex);
        view.setName(load.name);
    }

}
