package sir.mazer.feture.room

import sir.mazer.core.room.models.PanelData

interface PanelRepository {
    suspend fun insertPanel(data: PanelData)
    fun deletePanel(data: PanelData)
    fun getAllPanels(): List<PanelData>
}