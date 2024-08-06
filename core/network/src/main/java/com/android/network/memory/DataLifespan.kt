package com.android.network.memory



enum class DataLifespan {

    /**
     * very-long term data lifespan.
     *
     * This data lifespan is not related to user lifespan.
     * So, this lifespan is not suitable for user data. And, usecase is for device data.
     *
     * Deletion timing:
     * - uninstall application
     * - delete storage
     */
    UntilApplicationUninstall,

    /**
     * long-term data lifespan.
     *
     * This data lifespan is most popular for saving user data.
     *
     * Deletion timing:
     * - uninstall application
     * - delete storage
     * - logout user
     */
    UntilLogout,

    /**
     * short term data lifespan.
     * This data lifespan is useful for temporary saving data.
     *
     * Deletion timing:
     * - uninstall application
     * - delete storage
     * - logout user
     * - launch application(cold start)
     */
    UntilNextApplicationLaunch,

    /**
     * very-short term data lifespan.
     *
     * This data lifespan is high volatility.
     * So, in most case, this data lifespan is not usable.
     *
     * Deletion timing:
     * - uninstall application
     * - delete storage
     * - logout user
     * - launch application(cold start)
     * - foreground application
     */
    UntilNextApplicationForeground
}
