package cache

class FeatureFlagManager private constructor() {

    companion object {
        val instance: FeatureFlagManager by lazy { Holder.INSTANCE }
    }

    private object Holder {
        val INSTANCE = FeatureFlagManager()
    }

    private var shouldEnableLaughButton : Boolean = false

    fun setLaughButtonEnabled(shouldEnableLaughButton: Boolean) {
        FeatureFlagManager.instance.shouldEnableLaughButton = shouldEnableLaughButton
    }

    fun getLaughButtonEnabled() : Boolean {
        return shouldEnableLaughButton
    }
}
