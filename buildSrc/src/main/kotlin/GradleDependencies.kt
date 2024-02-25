object DependenciesVersions {
    const val coreVersion = "1.12.0"
    const val activityVersion = "1.8.2"
    const val lifecycleVersion = "2.6.2"
    const val navigationVersion = "2.7.6"
    const val hiltVersion = "2.48"
    const val hiltNavigation = "1.1.0"
    const val roomVersion = "2.6.1"
    const val dataStoreVersion = "1.0.0"
    const val composeBomVersion = "2023.10.01"
    const val espressoVersion = "3.5.1"
}

object CoreDependency {
    const val core = "androidx.core:core-ktx:${DependenciesVersions.coreVersion}"
}

object ComposeDependency {
    const val composeBom = "androidx.compose:compose-bom:${DependenciesVersions.composeBomVersion}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeGraphics = "androidx.compose.ui:ui-graphics"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial3 = "androidx.compose.material3:material3"
    const val composeRuntimeLivedata = "androidx.compose.runtime:runtime-livedata"
}

object ActivityDependency {
    const val activityCompose =
        "androidx.activity:activity-compose:${DependenciesVersions.activityVersion}"
    const val activityKtx = "androidx.activity:activity-ktx:${DependenciesVersions.activityVersion}"
}

object CoroutineLifecycleScopesDependency {
    const val lifecycleRuntime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersions.lifecycleVersion}"
    const val lifecycleViewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${DependenciesVersions.lifecycleVersion}"
    const val lifecycleLivedata =
        "androidx.lifecycle:lifecycle-livedata-ktx:${DependenciesVersions.lifecycleVersion}"
}

object NavigationDependency {
    const val navigationCompose =
        "androidx.navigation:navigation-compose:${DependenciesVersions.navigationVersion}"
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${DependenciesVersions.navigationVersion}"
}

object HiltDependency {
    const val hilt = "com.google.dagger:hilt-android:${DependenciesVersions.hiltVersion}"
    const val hiltNavigationFilter =
        "androidx.hilt:hilt-navigation-compose:${DependenciesVersions.hiltNavigation}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${DependenciesVersions.hiltVersion}"
}

object RoomDependency {
    const val roomKtx = "androidx.room:room-ktx:${DependenciesVersions.roomVersion}"
    const val roomCompiler = "androidx.room:room-compiler:${DependenciesVersions.roomVersion}"
    const val roomKsp = "androidx.room:room-compiler:${DependenciesVersions.roomVersion}"
}

object DatastoreDependency {
    const val datastorePreferences =
        "androidx.datastore:datastore-preferences:${DependenciesVersions.dataStoreVersion}"
}

object TestDependency {
    const val junit = "junit:junit:4.13.2"
    const val androidJunitExt = "androidx.test.ext:junit:1.1.5"
    const val espressoCore =
        "androidx.test.espresso:espresso-core:${DependenciesVersions.espressoVersion}"
    const val composeJunit = "androidx.compose.ui:ui-test-junit4"
    const val composeTooling = "androidx.compose.ui:ui-tooling"
    const val composeManifest = "androidx.compose.ui:ui-test-manifest"
}