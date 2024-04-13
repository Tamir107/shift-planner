plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.shiftplanner"
    compileSdk = 34

    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/*.kotlin_module")
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

    defaultConfig {
        applicationId = "com.example.shiftplanner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8


    }

}

// For google calendar api
//repositories {
//    mavenCentral()
//    google()
//}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation("com.firebaseui:firebase-ui-database:8.0.2")
    implementation("com.firebaseui:firebase-ui-database:8.0.2")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // https://mvnrepository.com/artifact/com.google.api-client/google-api-client
    implementation("com.google.api-client:google-api-client:1.25.0")
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client-jetty
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.35.0")
    // https://mvnrepository.com/artifact/com.google.apis/google-api-services-calendar
    implementation("com.google.apis:google-api-services-calendar:v3-rev20240111-2.0.0")
    // https://mvnrepository.com/artifact/com.google.http-client/google-http-client-jackson2
    implementation("com.google.http-client:google-http-client-jackson2:1.44.1")
    // https://mvnrepository.com/artifact/com.google.api-client/google-api-client-gson
    implementation("com.google.api-client:google-api-client-gson:2.4.0")


}