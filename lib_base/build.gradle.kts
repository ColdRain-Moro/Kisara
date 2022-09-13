import com.ndhzs.convention.depend.Network
import com.ndhzs.convention.depend.dependCoroutines
import com.ndhzs.convention.depend.dependRxjava
import com.ndhzs.convention.depend.lib.dependLibConfig
import com.ndhzs.convention.depend.lib.dependLibUtils

plugins {
  id("module-manager")
}

dependLibUtils()
dependLibConfig()

dependCoroutines()
dependRxjava()

dependencies {
  implementation(Network.okhttp) // 为了拿到 CookieJar
}