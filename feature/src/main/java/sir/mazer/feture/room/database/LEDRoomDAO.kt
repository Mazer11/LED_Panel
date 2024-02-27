package sir.mazer.feture.room.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import sir.mazer.core.room.models.PanelData

@Dao
interface LEDRoomDAO {

    //Insert panel
    @Upsert
    suspend fun insertPanel(data: PanelData)

    //Delete panel
    @Delete
    suspend fun deletePanel(data: PanelData)

    //Get panels
    @Query("SELECT * FROM panels")
    suspend fun getAllPanels(): List<PanelData>

}