package com.github.sikv.photos.event

/**
 * A wrapper to send an event without data.
 */
class VoidEvent {

    var hasBeenHandled = false
        private set

    fun canHandle(): Boolean {
        return if (hasBeenHandled) {
            false
        } else {
            hasBeenHandled = true
            true
        }
    }
}