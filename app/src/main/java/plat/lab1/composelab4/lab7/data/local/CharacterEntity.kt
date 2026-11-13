package plat.lab1.composelab4.lab7.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
)
