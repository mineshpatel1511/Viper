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

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.ebates.restaurants.poc.BaseApplication
import com.ebates.restaurants.poc.MainContract
import poc.ebates.com.restaurants.R
import com.ebates.restaurants.poc.entity.JokeEntity
import com.ebates.restaurants.poc.presenter.MainPresenter
import com.ebates.restaurants.poc.view.adapters.MainListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_view_custom_layout.*
import org.jetbrains.anko.toast
import poc.ebates.com.restaurants.viewInterface.JokeRecyclerViewInterface
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward

class MainActivity : BaseActivity(), JokeRecyclerViewInterface {

  companion object {
    val TAG: String = "MainActivity"
  }

  private val navigator: Navigator? by lazy {
    object : Navigator {
      override fun applyCommand(command: Command) {
        if (command is Forward) {
          forward(command)
        }
      }

      private fun forward(command: Forward) {
        val data = (command.transitionData as JokeEntity)

        when (command.screenKey) {
          DetailActivity.TAG -> startActivity(Intent(this@MainActivity, DetailActivity::class.java)
              .putExtra("data", data as Parcelable))   // 4
          else -> Log.e("Cicerone", "Unknown screen: " + command.screenKey)
        }
      }
    }
  }

  private var presenter: MainContract.Presenter? = null
  private val toolbar: Toolbar by lazy { toolbar_toolbar_view }
  private val recyclerView: RecyclerView by lazy { rv_data_list_activity_main }
  private val progressBar: ProgressBar by lazy { prog_bar_loading_data_activity_main }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    presenter = MainPresenter(this)
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    recyclerView.adapter = MainListAdapter({ data -> presenter?.listItemClicked(data) }, null)
  }

  override fun onResume() {
    super.onResume()
    presenter?.onViewCreated()
    BaseApplication.INSTANCE.cicerone.navigatorHolder.setNavigator(navigator)
  }

  override fun onPause() {
    super.onPause()
    BaseApplication.INSTANCE.cicerone.navigatorHolder.removeNavigator()
  }

  override fun onDestroy() {
    presenter?.onDestroy()
    presenter = null
    super.onDestroy()
  }

  override fun getToolbarInstance(): Toolbar? = toolbar

  override fun updateShowLaughButton(showLaughButton: Boolean) {
    val adapter = recyclerView.adapter as? MainListAdapter
    adapter?.updateShowLaughButton(showLaughButton)
  }

  override fun refreshView() {
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun showLoading() {
    recyclerView.isEnabled = false
    progressBar.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    recyclerView.isEnabled = true
    progressBar.visibility = View.GONE
  }

  override fun publishDataList(data: List<JokeEntity>) {
    (recyclerView.adapter as MainListAdapter).updateData(data)
  }

  override fun showInfoMessage(msg: String) {
    toast(msg)
  }
}
