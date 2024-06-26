package com.github.sikv.photos.di

import com.github.sikv.photos.navigation.route.FeedbackRoute
import com.github.sikv.photos.navigation.route.PhotoDetailsRoute
import com.github.sikv.photos.navigation.route.SearchRoute
import com.github.sikv.photos.navigation.route.SetWallpaperRoute
import com.github.sikv.photos.route.impl.FeedbackRouteImpl
import com.github.sikv.photos.route.impl.PhotoDetailsRouteImpl
import com.github.sikv.photos.route.impl.SearchRouteImpl
import com.github.sikv.photos.route.impl.SetWallpaperRouteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RouteModule {

    @Binds
    abstract fun bindPhotoDetailsRoute(route: PhotoDetailsRouteImpl): PhotoDetailsRoute

    @Binds
    abstract fun bindSearchRoute(route: SearchRouteImpl): SearchRoute

    @Binds
    abstract fun bindSetWallpaperRoute(route: SetWallpaperRouteImpl): SetWallpaperRoute

    @Binds
    abstract fun bindFeedbackRoute(route: FeedbackRouteImpl): FeedbackRoute
}
