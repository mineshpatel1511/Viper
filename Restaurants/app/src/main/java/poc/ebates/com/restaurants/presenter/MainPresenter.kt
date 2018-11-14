package com.ebates.restaurants.poc.presenter

import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ebates.restaurants.poc.BaseApplication
import com.ebates.restaurants.poc.MainContract
import com.ebates.restaurants.poc.entity.JokeEntity
import com.ebates.restaurants.poc.interactor.JokeListInteractor
import com.ebates.restaurants.poc.view.activities.DetailActivity
import poc.ebates.com.restaurants.viewInterface.JokeRecyclerViewInterface
import ru.terrakok.cicerone.Router


class MainPresenter(private var view: JokeRecyclerViewInterface?) : MainContract.Presenter, MainContract.InteractorOutput {

  private var interactor: MainContract.Interactor? = JokeListInteractor()
  private val router: Router? by lazy { BaseApplication.INSTANCE.cicerone.router }

  override fun listItemClicked(data: JokeEntity?) {
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

          val type = object : TypeToken<List<JokeEntity>>() {}.type
          val dataList: List<JokeEntity> =
              Gson().fromJson(dataJsonObject.getJSONArray("value").toString(), type)

          this.onQuerySuccess(dataList)
        }
      }
    }
  }

  override fun onQuerySuccess(data: List<JokeEntity>) {
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
