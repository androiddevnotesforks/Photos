package com.github.sikv.photos.feedback

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.sikv.photos.common.PreferencesService
import com.github.sikv.photos.feedback.domain.Feedback
import com.github.sikv.photos.feedback.domain.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FeedbackUiState {
    object Empty : FeedbackUiState

    data class Data(
        val requestStatus: RequestStatus
    ) : FeedbackUiState
}

@HiltViewModel
internal class FeedbackViewModel @Inject constructor(
    application: Application,
    private val feedbackRepository: FeedbackRepository,
    private val preferencesService: PreferencesService
) : AndroidViewModel(application) {

    private val mutableUiState = MutableStateFlow<FeedbackUiState>(FeedbackUiState.Empty)
    val uiState: StateFlow<FeedbackUiState> = mutableUiState

    fun send(email: String?, description: String?) {
        if (!email.isValidEmail()) {
            val error = getApplication<Application>().getString(R.string.enter_valid_email)
            updateState(RequestStatus.ValidationError(error, 1))
            return
        }
        if (description.isNullOrBlank()) {
            val error = getApplication<Application>().getString(R.string.enter_some_text)
            updateState(RequestStatus.ValidationError(error, 2))
            return
        }

        val feedback = Feedback(
            preferencesService.getSessionId(),
            email,
            description
        )

        updateState(RequestStatus.InProgress)

        viewModelScope.launch {
            val sent = feedbackRepository.sendFeedback(feedback)

            val messageId =
                if (sent) R.string.feedback_sent else R.string.error_sending_feedback
            val message = getApplication<Application>().getString(messageId)

            updateState(
                RequestStatus.Done(
                    success = sent,
                    message = message
                )
            )
        }
    }

    private fun updateState(requestStatus: RequestStatus) {
        mutableUiState.value = FeedbackUiState.Data(
            requestStatus = requestStatus
        )
    }
}

fun String?.isValidEmail(): Boolean {
    val email = this
    return !email.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}