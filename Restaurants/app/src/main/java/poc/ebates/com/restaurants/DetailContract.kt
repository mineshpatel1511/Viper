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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ebates.restaurants.poc

import com.ebates.restaurants.poc.entity.JokeEntity
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result

interface DetailContract {
  interface View {
    fun showData(id: String, data: String)
  }

  interface Presenter {
    // User actions
    fun backButtonClicked()

    // Model updates
    fun onViewCreated(data: JokeEntity)

    fun onDestroy()
  }

  interface Interactor {
    fun loadDataList(interactorOutput: (result: Result<Json, FuelError>) -> Unit)
  }

  interface ApiInteractor {
    fun loadFeatureFlag(interactorOutput: DetailContract.InteractorOutput)
  }

  interface InteractorOutput {
    fun onQuerySuccess(data: List<JokeEntity>)
    fun onQueryError()
    fun onFeatureFlagSuccess(showLaughButton: Boolean)
    fun onFeatureFlagFailure()
  }
}
