package com.example.myapp.mvp.model

import io.reactivex.rxjava3.core.Completable

interface IConvrter {
    fun convertImg(path: String): Completable

}