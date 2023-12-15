package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.model.MutedUser
import org.mozilla.social.core.repository.mastodon.MutesRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableAccountListItemState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.account.UnmuteAccount
import org.mozilla.social.core.repository.paging.MutesListRemoteMediator
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.SettingsInteractions

class MutedUsersSettingsViewModel(
    repository: MutesRepository,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    remoteMediator: MutesListRemoteMediator,
    private val analytics: Analytics,
    private val muteAccount: MuteAccount,
    private val unmuteAccount: UnmuteAccount,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    @OptIn(ExperimentalPagingApi::class)
    val mutes: Flow<PagingData<ToggleableAccountListItemState<MutedButtonState>>> =
        repository.getMutesPager(remoteMediator)
            .map { pagingData -> pagingData.map { mutedUser -> mutedUser.toToggleableState() } }

    fun onButtonClicked(accountId: String, mutedButtonState: MutedButtonState) {
        viewModelScope.launch(Dispatchers.IO) {
            when (mutedButtonState) {
                is MutedButtonState.Muted -> unmuteAccount(accountId)
                is MutedButtonState.Unmuted -> muteAccount(accountId)
            }
        }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.MUTED_USERS_SCREEN_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}

fun MutedUser.toToggleableState() =
    ToggleableAccountListItemState(
        buttonState = if (isMuted) MutedButtonState.Muted else MutedButtonState.Unmuted(
            confirmationText = StringFactory.resource(
                R.string.are_you_sure_you_want_to_mute, account.acct
            )
        ),
        account = account.toQuickViewUiState()
    )
