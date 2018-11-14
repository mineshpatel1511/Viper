/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.ebates.restaurants.poc.presenter

import com.ebates.restaurants.poc.BaseApplication
import com.ebates.restaurants.poc.DetailContract
import com.ebates.restaurants.poc.entity.JokeEntity
import com.ebates.restaurants.poc.interactor.JokeListInteractor
import com.ebates.restaurants.poc.view.activities.DetailActivity
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.terrakok.cicerone.Router


class DetailPresenter(private var view: DetailActivity?) : DetailContract.Presenter, DetailContract.InteractorOutput {

  private var interactor: DetailContract.Interactor? = JokeListInteractor()
  private val router: Router? by lazy { BaseApplication.INSTANCE.cicerone.router }

  override fun backButtonClicked() {
    router?.exit()
  }

  override fun onViewCreated(data: JokeEntity) {
    view?.showData(data.id.toString(), data.text)

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

  override fun onDestroy() {
    view = null
  }

  override fun onQuerySuccess(data: List<JokeEntity>) {
    view?.hideLoading()
    view?.publishDataList(data)
  }

  override fun onQueryError() {
    view?.hideLoading()
    view?.showInfoMessage("Error when loading data")
  }
}