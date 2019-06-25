package com.android.enclave.androidrecipeapp.presenters;

import android.app.Application;

public class BasePresenter {

    private Application mApplication;

    public BasePresenter(Application application){
        mApplication = application;
    }

    public <T extends Application> T getApplication(){
        return (T) mApplication;
    }

}
