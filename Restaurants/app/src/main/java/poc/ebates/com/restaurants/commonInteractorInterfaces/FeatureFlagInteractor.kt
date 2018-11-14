package com.ebates.restaurants.poc.interactor

import android.os.Handler
import com.ebates.restaurants.poc.DetailContract


class FeatureFlagInteractor : DetailContract.ApiInteractor {

  override fun loadFeatureFlag(interactorOutput : DetailContract.InteractorOutput) {
    Handler().postDelayed({
      interactorOutput.onFeatureFlagSuccess(true)
    }, 3000)
  }
}
