package org.mozilla.social.feature.followers

import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.EngagementType

class FollowersAnalytics(private val analytics: Analytics) {

    fun followsScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = FOLLOWERS_SCREEN_IMPRESSION,
        )
    }

    fun followClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = FOLLOWS_SCREEN_FOLLOW,
        )
    }

    fun unfollowClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = FOLLOWS_SCREEN_UNFOLLOW,
        )
    }

    companion object {
        private const val FOLLOWERS_SCREEN_IMPRESSION = "followers.screen.impression"
        private const val FOLLOWS_SCREEN_FOLLOW = "follows.screen.follow"
        private const val FOLLOWS_SCREEN_UNFOLLOW = "follows.screen.unfollow"
    }
}