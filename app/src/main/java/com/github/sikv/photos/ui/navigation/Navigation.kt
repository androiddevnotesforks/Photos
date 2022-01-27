package com.github.sikv.photos.ui.navigation

import androidx.fragment.app.Fragment

interface Navigation {
    fun addFragment(fragment: Fragment, animation: NavigationAnimation = NavigationAnimation.NONE)
    fun backPressed(): Boolean
    fun backToRoot(): Fragment?
    fun setOnDestinationChangedListener(destinationChangedListener: OnDestinationChangedListener?)
}
