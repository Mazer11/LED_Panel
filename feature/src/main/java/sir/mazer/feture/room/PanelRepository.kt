package sir.mazer.feture.room

import sir.mazer.core.room.models.PanelData

interface PanelRepository {
    suspend fun insertPanel(data: PanelData)
    suspend fun deletePanel(data: PanelData)
    suspend fun getAllPanels(): List<PanelData>
}