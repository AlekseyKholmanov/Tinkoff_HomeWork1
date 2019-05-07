package com.example.holmi_production.recycleview_4.mvp.model


import com.example.holmi_production.recycleview_4.source.db.NewsDatabase
import com.example.holmi_production.recycleview_4.source.db.entity.FavoriteNews
import com.example.holmi_production.recycleview_4.source.db.entity.News
import com.example.holmi_production.recycleview_4.source.network.NewsObject
import com.example.holmi_production.recycleview_4.source.network.RemoteDataSource
import com.example.holmi_production.recycleview_4.source.network.SingleNews
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewsRepository @Inject constructor(
    newsDatabase: NewsDatabase,
    private val remoteDataSource: RemoteDataSource
) {
    private val newsDao = newsDatabase.newsDao()
    private val favoriteNewsDao = newsDatabase.favoriteNewsDao()
    private val favorite = newsDatabase.favorite()


    fun getNewsFromNetwork(): Single<NewsObject> {
        return remoteDataSource.getNews()
//            .doAfterSuccess { t ->
//                insertListNews(t.news)
//                    .subscribe()
//            }
    }

    fun getNewsFromNetworkById(id: Int): Single<SingleNews> {
        return remoteDataSource.getNewsById(id)
            .subscribeOn(Schedulers.io())
    }

    fun insertFavoriteNews(news: FavoriteNews): Completable {
        return Completable.fromCallable { favoriteNewsDao.insert(news) }
    }

    fun insertListNews(news: List<News>): Completable {
        return Completable.fromCallable { newsDao.insertListNews(news) }
    }

    fun deleteFavotiteNews(newsId: Int): Completable {
        return Completable.fromCallable { favoriteNewsDao.delete(newsId) }
    }

    fun getAllNews(): Flowable<List<News>> {
        return newsDao.getAll()
    }

    fun getAllFavoriteNews(): Flowable<List<News>> {
        return favorite.getFavorite()
    }

    fun getFavoriteNewsById(newsId: Int): Maybe<FavoriteNews> {
        return favoriteNewsDao.getNewsById(newsId)
    }
}