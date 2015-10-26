package com.mwong56.polyrides.utils;


import rx.Observer;

/**
 * Created by micha on 10/26/2015.
 */
public abstract class OnCompletedObserver<T> implements Observer<T> {
    @Override
    public void onNext(T o) {

    }
}