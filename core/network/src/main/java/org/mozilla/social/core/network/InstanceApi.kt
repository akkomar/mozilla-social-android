package org.mozilla.social.core.network

import org.mozilla.social.core.network.model.NetworkInstance
import org.mozilla.social.core.network.model.NetworkInstanceRule
import retrofit2.http.GET

interface InstanceApi {

    @GET("/api/v1/instance/rules")
    suspend fun getRules(): List<NetworkInstanceRule>

    @GET("/api/v2/instance")
    suspend fun getInstance(): NetworkInstance
}