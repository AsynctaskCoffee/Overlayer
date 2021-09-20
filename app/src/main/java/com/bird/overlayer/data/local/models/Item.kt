package com.bird.overlayer.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey
    val overlayId: Long = 0,
    val overlayName: String = "",
    val overlayPreviewIconUrl: String = "",
    val overlayUrl: String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var dataThumb: ByteArray? = null
)