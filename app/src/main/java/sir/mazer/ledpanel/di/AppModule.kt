package sir.mazer.ledpanel.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sir.mazer.feture.room.LEDPanelRepository
import sir.mazer.feture.room.database.LEDRoom

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRoom(@ApplicationContext context: Context): LEDRoom =
        Room.databaseBuilder(context, LEDRoom::class.java, "led_panel_database")
            .build()

    @Provides
    fun provideLEDPanelRepository(db: LEDRoom): LEDPanelRepository = LEDPanelRepository(db.roomDao)
}