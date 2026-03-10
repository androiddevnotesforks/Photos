package com.github.sikv.photos.data.persistence.favorites

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.github.sikv.photos.data.SortBy
import com.github.sikv.photos.data.persistence.DbConfig
import javax.inject.Inject

class FavoritePhotosDbQueryBuilder @Inject constructor() {

    fun buildGetPhotosQuery(sortBy: SortBy): SupportSQLiteQuery {
        val orderBy = when (sortBy) {
            SortBy.DATE_ADDED_NEWEST -> "dateAdded DESC"
            SortBy.DATE_ADDED_OLDEST -> "dateAdded ASC"
        }
        return SimpleSQLiteQuery("SELECT * from ${DbConfig.FAVORITE_PHOTOS_TABLE} WHERE markedAsDeleted=0 ORDER BY $orderBy")
    }
}
