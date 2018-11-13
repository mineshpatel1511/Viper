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

package com.ebates.restaurants.poc.view.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.ebates.restaurants.poc.BaseApplication
import com.ebates.restaurants.poc.DetailContract
import poc.ebates.com.restaurants.R
import com.ebates.restaurants.poc.entity.MainEntity
import com.ebates.restaurants.poc.presenter.DetailPresenter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.toolbar_view_custom_layout.*
import org.jetbrains.anko.toast
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command

class DetailActivity : BaseActivity(), DetailContract.View {

  companion object {
    val TAG = "DetailActivity"
  }

  private val navigator: Navigator? by lazy {
    object : Navigator {
      override fun applyCommand(command: Command) {
        if (command is Back) {
          back()
        }
      }

      private fun back() {
        finish()
      }
    }
  }

  private var presenter: DetailContract.Presenter? = null
  private val toolbar: Toolbar by lazy { toolbar_toolbar_view }
  private val tvId: TextView? by lazy { tv_data_id_activity_detail }
  private val tvData: TextView? by lazy { tv_data_activity_detail }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)

    presenter = DetailPresenter(this)
  }

  override fun onResume() {
    super.onResume()
    // add back arrow to toolbar
    supportActionBar?.let {
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    // load invoking arguments
    val argument = intent.getParcelableExtra<MainEntity>("data")
    argument?.let { presenter?.onViewCreated(it) }

    BaseApplication.INSTANCE.cicerone.navigatorHolder.setNavigator(navigator)
  }

  override fun onPause() {
    super.onPause()
    BaseApplication.INSTANCE.cicerone.navigatorHolder.removeNavigator()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        presenter?.backButtonClicked()
        true
      }
      else -> false
    }
  }

  override fun onDestroy() {
    presenter?.onDestroy()
    presenter = null
    super.onDestroy()
  }

  override fun getToolbarInstance(): android.support.v7.widget.Toolbar? = toolbar

  override fun showData(id: String, data: String) {
    tvId?.text = id
    tvData?.text = data
  }

  override fun showInfoMessage(msg: String) {
    toast(msg)
  }
}