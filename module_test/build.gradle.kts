import com.ndhzs.convention.depend.api.dependApiMain
import com.ndhzs.convention.depend.dependHilt
import com.ndhzs.convention.depend.lib.dependLibBase
import com.ndhzs.convention.depend.lib.dependLibConfig
import com.ndhzs.convention.depend.lib.dependLibUtils

plugins {
  id("module-debug")
}

dependLibBase()
dependLibUtils()
dependLibConfig()

// 还有其他比如协程、Retrofit、Glide 等，可以去 build_logic 中寻找
// 基本上能用到的全都有依赖方式
dependApiMain()

dependHilt()

dependencies {
  // 这里面写只有自己模块才会用到的额外依赖
  // 如果 build_logic 中已有，请直接使用
  // 可以通过 Ctrl + Shift + F 搜索项目关键词快速查看是否存在相同依赖
}

//cache {
//  isAllowSelfUseCache = false // 是否允许自身使用缓存，默认是允许的
//  isNeedCreateNewCache = false // 是否需要创建自己的缓存，默认是允许的
//
//  exclude("xxx") // 对名字叫 xxx 的不替换缓存
//
//  exclude {
//    it.name == "xxx" // 与上面相同，只是这个是动态的判断
//  }
//}