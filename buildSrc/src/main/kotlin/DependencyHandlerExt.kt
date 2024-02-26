import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependency: String) {
    add("implementation", dependency)
}

fun DependencyHandler.ksp(dependency: String) {
    add("ksp", dependency)
}

fun DependencyHandler.testImplementation(dependency: String) {
    add("testImplementation", dependency)
}

fun DependencyHandler.androidTestImplementation(dependency: String) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandler.debugImplementation(dependency: String) {
    add("debugImplementation", dependency)
}

fun DependencyHandler.annotationProcessor(dependency: String) {
    add("annotationProcessor", dependency)
}

fun DependencyHandler.implementPlatform(dependencyNotation: Any): org.gradle.api.artifacts.Dependency? {
    return add("implementation", dependencyNotation)
}

fun DependencyHandler.androidTestImplementPlatform(dependencyNotation: Any): org.gradle.api.artifacts.Dependency? {
    return add("androidTestImplementation", dependencyNotation)
}

fun DependencyHandler.core() {
    implementation(CoreDependency.core)
}

fun DependencyHandler.compose() {
    implementPlatform(platform(ComposeDependency.composeBom))
    implementation(ComposeDependency.composeUi)
    implementation(ComposeDependency.composeGraphics)
    implementation(ComposeDependency.composePreview)
    implementation(ComposeDependency.composeMaterial3)
    implementation(ComposeDependency.composeRuntimeLivedata)
}

fun DependencyHandler.activity() {
    implementation(ActivityDependency.activityCompose)
    implementation(ActivityDependency.activityKtx)
}

fun DependencyHandler.coroutines() {
    implementation(CoroutineLifecycleScopesDependency.lifecycleRuntime)
    implementation(CoroutineLifecycleScopesDependency.lifecycleViewmodel)
    implementation(CoroutineLifecycleScopesDependency.lifecycleLivedata)
}

fun DependencyHandler.navigation() {
    implementation(NavigationDependency.navigationCompose)
    implementation(NavigationDependency.navigationFragment)
}

fun DependencyHandler.hilt() {
    implementation(HiltDependency.hilt)
    implementation(HiltDependency.hiltNavigationFilter)
    ksp(HiltDependency.hiltCompiler)
}

fun DependencyHandler.datastore() {
    implementation(DatastoreDependency.datastorePreferences)
}

fun DependencyHandler.tests() {
    testImplementation(TestDependency.junit)
    androidTestImplementation(TestDependency.androidJunitExt)
    androidTestImplementation(TestDependency.espressoCore)
    androidTestImplementPlatform(platform(ComposeDependency.composeBom))
    androidTestImplementation(TestDependency.composeJunit)
    debugImplementation(TestDependency.composeTooling)
    debugImplementation(TestDependency.composeManifest)
}

fun DependencyHandler.room(){
    implementation(RoomDependency.roomKtx)
    annotationProcessor(RoomDependency.roomCompiler)
    ksp(RoomDependency.roomKsp)
}