package org.mozilla.social.model.entity.request

import org.mozilla.social.model.entity.Token

/**
 * Object used to revoke a [Token].
 */
data class TokenRevoke(

    /**
     * Client ID, obtained during app registration
     */
    val clientId: String,

    /**
     * Client secret, obtained during app registration
     */
    val clientSecret: String,

    /**
     * The previously obtained token, to be invalidated
     */
    val token: String,
)
