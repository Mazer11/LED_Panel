package sir.mazer.feture.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import sir.mazer.core.room.models.PanelData

@Database(entities = [PanelData::class], version = 1)
abstract class LEDRoom: RoomDatabase() {
    abstract val roomDao: LEDRoomDAO
}