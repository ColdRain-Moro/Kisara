@file:Suppress("ObjectPropertyName")

package com.ndhzs.convention.config

import org.gradle.api.Project

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/26 15:13
 */
object Config {
  const val minSdk = 24
  const val targetSdk = 31
  const val compileSdk = 31
  
  const val versionCode = 1
  const val versionName = "0.1.0"
  
  fun getApplicationId(project: Project): String {
    return when (project.name) {
      "module_app" -> {
        if (project.gradle.startParameter.taskNames.any { it.contains("Release") }) {
          "team.redrock.kisara"
        } else {
          // debug 状态下使用 debug 的包名，方便测试
          "team.redrock.kisara.debug"
        }
      }
      else -> "team.redrock.kisara.${project.name}"
    }
  }
}
