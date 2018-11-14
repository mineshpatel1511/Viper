package poc.ebates.com.restaurants.model

data class Reservation(
        val restaurantId: Int,
        val time: String,
        val partySize: Int
)
