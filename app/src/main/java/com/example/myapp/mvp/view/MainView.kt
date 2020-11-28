package ru.geekbrains.poplib.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface MainView : MvpView {
    fun init()
    fun setResult(result: String)

    @StateStrategyType(SkipStrategy::class)
    fun showFileChooser()
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress(show: Boolean)

}