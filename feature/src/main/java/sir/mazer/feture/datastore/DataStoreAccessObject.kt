package sir.mazer.feture.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreAccessObject {

    val getLanguage: Flow<String>

    suspend fun setLanguage(locale: String)

}