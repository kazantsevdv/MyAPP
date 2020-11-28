package ru.geekbrains.poplib.mvp.presenter

import com.example.myapp.mvp.model.IConvrter
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.geekbrains.poplib.mvp.view.MainView

class MainPresenter(val imgConverter: IConvrter, val scheduler: Scheduler) :
    MvpPresenter<MainView>() {
    lateinit var dispos: Disposable

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun convertFile(name: String) {
        dispos = imgConverter.convertImg(name)
            .observeOn(scheduler)
            .doOnSubscribe { viewState.showProgress(true) }
            .doOnComplete { viewState.showProgress(false) }
            .doOnDispose { viewState.setResult("Операция отменена") }
            .doOnError { viewState.showProgress(false) }
            .subscribe({ viewState.setResult("Успешно") },
                { viewState.setResult("Ошибка: ${it.localizedMessage}") })

        viewState.setResult(name)
    }

    fun canselConvert() {
        dispos.dispose()
    }

    fun showFileChooser() {
        viewState.showFileChooser()
    }
}