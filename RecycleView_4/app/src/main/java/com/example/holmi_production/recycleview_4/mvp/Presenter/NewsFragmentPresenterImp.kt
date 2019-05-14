package com.example.holmi_production.recycleview_4.mvp.Presenter

import android.net.ConnectivityManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.holmi_production.recycleview_4.NewsItems.NewsContainer
import com.example.holmi_production.recycleview_4.async
import com.example.holmi_production.recycleview_4.mvp.model.NewsRepository
import com.example.holmi_production.recycleview_4.mvp.view.ListNewsView
import com.example.holmi_production.recycleview_4.utils.DateUtils
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@InjectViewState
class NewsFragmentPresenterImp @Inject constructor(
    private val newsRepository: NewsRepository,
    private val cm: ConnectivityManager
) :
    BasePresenter<ListNewsView>() {

    fun getFavoriteNews() {
        callFavoriteNews().subscribe { listItem ->
            viewState.dismissProgressBar()
            viewState.showNews(listItem)
        }.keep()
    }

    private fun isInternetConnected(): Boolean {
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    fun updateNews(isFavorite: Boolean) {
        if (isInternetConnected()) {
            viewState.showRefreshingStart()
            callNews()
                .subscribe { listItem ->
                    viewState.showRefreshingEnd()
                    viewState.showNews(listItem)
                }
                .keep()

        } else {
            viewState.showRefreshingEnd()
            viewState.showNetworkAlertDialog()
        }
    }

    fun openSingleNews(newsId: Int) {
//        isHaveContent(newsId)
        if (!isInternetConnected() && a)
            viewState.showNetworkAlertDialog()
        else
            viewState.showSingleNews(newsId)
    }

    fun getNews() {
        if (isInternetConnected()) {callNews()
                .subscribe { listItem ->
                    viewState.dismissProgressBar()
                    viewState.showNews(listItem)
                }
            .keep()

        } else {
            viewState.dismissProgressBar()
            viewState.showNetworkAlertDialog()
        }
    }

    private fun callNews(): Single<ArrayList<NewsContainer>> {
        return newsRepository.getNewsFromNetwork()
//            .delay(4, TimeUnit.SECONDS)
            .async()
            .map { newsObject ->
                DateUtils.reformateItem(newsObject.listNews)
            }

    }

    var a: Boolean = false
//    private fun isHaveContent(newsId: Int): Boolean {
//        return (newsRepository.getAllContentIds()
//            .async()
//            .contains {
//                newsId
//            }
//            .subscribe { it->
//                return@subscribe {it}
//            }
//            .keep())
//    }

    private fun callFavoriteNews(): Flowable<ArrayList<NewsContainer>> {
        return newsRepository.getAllFavoriteNews()
            .async()
            .map { t ->
                DateUtils.reformateItem(t)
            }
    }
}