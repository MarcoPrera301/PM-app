package plat.lab1.composelab4.lab7.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
)
