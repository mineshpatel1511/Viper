package com.ebates.restaurants.poc.presenter

import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ebates.restaurants.poc.BaseApplication
import com.ebates.restaurants.poc.MainContract
import com.ebates.restaurants.poc.entity.MainEntity
import com.ebates.restaurants.poc.interactor.MainInteractor
import com.ebates.restaurants.poc.view.activities.DetailActivity
import ru.terrakok.cicerone.Router


class MainPresenter(private var view: MainContract.View?) : MainContract.Presenter, MainContract.InteractorOutput {

  private var interactor: MainContract.Interactor? = MainInteractor()
  private val router: Router? by lazy { BaseApplication.INSTANCE.cicerone.router }

  override fun listItemClicked(data: MainEntity?) {
    router?.navigateTo(DetailActivity.TAG, data)
  }

  override fun onViewCreated() {
    view?.showLoading()
    interactor?.loadDataList { result ->
      when (result) {
        is Result.Failure -> {
          this.onQueryError()
        }
        is Result.Success -> {
          val dataJsonObject = result.get().obj()

          val type = object : TypeToken<List<MainEntity>>() {}.type
          val dataList: List<MainEntity> =
              Gson().fromJson(dataJsonObject.getJSONArray("value").toString(), type)

          this.onQuerySuccess(dataList)
        }
      }
    }
  }

  override fun onQuerySuccess(data: List<MainEntity>) {
    view?.hideLoading()
    view?.publishDataList(data)
  }

  override fun onQueryError() {
    view?.hideLoading()
    view?.showInfoMessage("Error when loading data")
  }

  override fun onDestroy() {
    view = null
    interactor = null
  }
}
