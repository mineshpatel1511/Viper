package poc.ebates.com.restaurants.viewInterface

import com.ebates.restaurants.poc.entity.JokeEntity

interface JokeRecyclerViewInterface {
    fun showLoading()
    fun hideLoading()
    fun publishDataList(data: List<JokeEntity>)
    fun showInfoMessage(msg: String)
}
