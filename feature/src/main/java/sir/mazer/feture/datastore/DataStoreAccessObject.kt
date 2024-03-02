package sir.mazer.feture.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreAccessObject {

    val getLanguage: Flow<String>
    val getPremium: Flow<Boolean>

    suspend fun setLanguage(locale: String)
    suspend fun setPremium(isPremium: Boolean)

}