package sir.mazer.feture.room.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import sir.mazer.core.room.models.PanelData

@Dao
interface LEDRoomDAO {

    //Insert panel
    @Insert
    suspend fun insertPanel(data: PanelData)

    //Delete panel
    @Delete
    fun deletePanel(data: PanelData)

    //Get panels
    @Query("SELECT * FROM panels")
    fun getAllPanels(): List<PanelData>

}