package poc.ebates.com.restaurants.commonViewInterfaces

import com.ebates.restaurants.poc.entity.JokeEntity

interface JokeRecyclerViewInterface {
    fun showLoading()
    fun hideLoading()
    fun publishDataList(data: List<JokeEntity>)
    fun showInfoMessage(msg: String)
    fun updateShowLaughButton(showLaughButton: Boolean)
    fun refreshView()
}
