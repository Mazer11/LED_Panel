package sir.mazer.core.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "panels")
data class PanelData(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val text: String,
    val textColorIndex: Int,
    val backgroundIndex: Int,
    val textSize: Int,
    val textStyleIndex: Int,
    val showCells: Boolean,
    val scrollType: String,
    val isGlowingText: Boolean,
    val speed: Int,
    val isBlink: Boolean,
    val blinkFrequency: Long
)