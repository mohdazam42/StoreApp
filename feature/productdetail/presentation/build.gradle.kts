plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.feature.productdetail.presentation"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.bundles.common.library.dependencies)

    implementation(libs.kotlinx.serialization.json)

    api(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(project(":common"))
    implementation(project(":feature:productdetail:data"))
    implementation(project(":feature:productdetail:domain"))

    testImplementation(libs.bundles.unit.testing)
    testImplementation(libs.okhttp.mockwebserver)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}