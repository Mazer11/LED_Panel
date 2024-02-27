package sir.mazer.feture.room

import sir.mazer.feture.room.database.LEDRoomDAO
import sir.mazer.core.room.models.PanelData

class LEDPanelRepository(private val dao: LEDRoomDAO) : PanelRepository {
    override suspend fun insertPanel(data: PanelData) {
        dao.insertPanel(data)
    }

    override suspend fun deletePanel(data: PanelData) {
        dao.deletePanel(data)
    }

    override suspend fun getAllPanels(): List<PanelData> = dao.getAllPanels()
}