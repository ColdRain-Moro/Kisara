import com.ndhzs.convention.depend.api.dependApiTest
import com.ndhzs.convention.depend.dependGlide
import com.ndhzs.convention.depend.lib.dependLibBase
import com.ndhzs.convention.depend.lib.dependLibConfig
import com.ndhzs.convention.depend.lib.dependLibUtils

plugins {
  id("module-manager")
}

dependLibBase()
dependLibUtils()
dependLibConfig()

dependApiTest()

dependGlide()

val coreVersion = "2.11.1"

dependencies {
  implementation("net.mamoe:mirai-core-android:$coreVersion") {
    exclude(module="net.mamoe:mirai-core-api")
    exclude(module="net.mamoe:mirai-core-utils")
  }
  implementation("net.mamoe:mirai-core-api-android:$coreVersion") {
    exclude(module="net.mamoe:mirai-core-utils")
  }
  implementation("net.mamoe:mirai-core-utils-android:$coreVersion")
  // Toasty
  implementation("com.github.GrenderG:Toasty:1.5.2")
  implementation(com.ndhzs.convention.depend.Network.gson)
}
