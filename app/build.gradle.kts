plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "sir.mazer.ledpanel"
    compileSdk = 34

    defaultConfig {
        applicationId = "sir.mazer.ledpanel"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //Feature
    implementation(project(":feature"))

    //Core
    implementation(CoreDependency.core)

    //Compose
    implementation(platform(ComposeDependency.composeBom))
    implementation(ComposeDependency.composeUi)
    implementation(ComposeDependency.composeGraphics)
    implementation(ComposeDependency.composePreview)
    implementation(ComposeDependency.composeMaterial3)
    implementation(ComposeDependency.composeRuntimeLivedata)

    //Activity
    implementation(ActivityDependency.activityCompose)
    implementation(ActivityDependency.activityKtx)

    // Coroutine Lifecycle Scopes
    implementation(CoroutineLifecycleScopesDependency.lifecycleRuntime)
    implementation(CoroutineLifecycleScopesDependency.lifecycleViewmodel)
    implementation(CoroutineLifecycleScopesDependency.lifecycleLivedata)

    //Navigation
    implementation(NavigationDependency.navigationCompose)
    implementation(NavigationDependency.navigationFragment)

    // Hilt
    implementation(HiltDependency.hilt)
    implementation(HiltDependency.hiltNavigationFilter)
    ksp(HiltDependency.hiltCompiler)

    //Data Store
    implementation(DatastoreDependency.datastorePreferences)

    //Tests
    testImplementation(TestDependency.junit)
    androidTestImplementation(TestDependency.androidJunitExt)
    androidTestImplementation(TestDependency.espressoCore)
    androidTestImplementation(platform(ComposeDependency.composeBom))
    androidTestImplementation(TestDependency.composeJunit)
    debugImplementation(TestDependency.composeTooling)
    debugImplementation(TestDependency.composeManifest)
}