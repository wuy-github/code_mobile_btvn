plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.tuan_4_bai_1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tuan_4_bai_1"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // AndroidX Core KTX
    implementation(libs.androidx.core.ktx)
    // Lifecycle Runtime KTX
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Activity Compose
    implementation(libs.androidx.activity.compose)

    // Compose BOM (Bill of Materials) - Luôn sử dụng cái này để quản lý phiên bản Compose
    implementation(platform(libs.androidx.compose.bom))

    // Thư viện Compose UI
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Material Design 3 (Được khuyến nghị cho các dự án mới)
    implementation(libs.androidx.material3)

    // Bạn sẽ cần thêm các dependency này vào tệp libs.versions.toml của mình
    // nếu chúng chưa có ở đó, sau đó tham chiếu chúng như sau:
    // Đối với Navigation Compose
    // implementation(libs.androidx.navigation.navigationCompose) // hoặc tên bạn đặt trong libs.versions.toml
    // Đối với ViewModel Compose
    // implementation(libs.androidx.lifecycle.viewmodelCompose) // hoặc tên bạn đặt trong libs.versions.toml
    // Đối với Material Icons Extended (đảm bảo nó tương thích với Material 3)
    // implementation(libs.androidx.compose.material.iconsExtended) // hoặc tên bạn đặt trong libs.versions.toml

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM cho các dependency kiểm thử
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}