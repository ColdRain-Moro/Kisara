package com.ndhzs.convention.publish

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import gradle.kotlin.dsl.accessors._b7719bb009bf77985775c5b9fa4e40d9.publishing
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

plugins {
  `maven-publish`
}

// 开启模块缓存的总开关
var isOpenModuleCache = true

if (plugins.hasPlugin("com.android.application")) {
  extensions.configure<BaseAppModuleExtension> {
    publishing {
      singleVariant("debug")
    }
  }
} else if (plugins.hasPlugin("com.android.library")) {
  extensions.configure<LibraryExtension> {
    publishing {
      singleVariant("debug")
    }
  }
} else {
  throw RuntimeException("只允许给 application 和 library 进行缓存，如有其他模块，请额外实现逻辑！")
}

// 增加 cache 闭包
val cache = extensions.create("cache", Cache::class, project)

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("moduleCache") {
        from(components["debug"])
      }
      
      // https://docs.gradle.org/current/userguide/publishing_maven.html#header
      repositories {
        maven {
          url = cache.localMavenUri
          group = cache.localMavenGroup
          version = cache.localMavenVersion
          name = cache.localMavenName
        }
      }
    }
  }
}

cache.isAllowSelfUseCache { // 允许自身使用缓存的时候
  if (isOpenModuleCache) {
    configurations.all {
      resolutionStrategy.dependencySubstitution.all {
        val requested = requested
        if (requested is ProjectComponentSelector) {
          val projectPath = requested.projectPath
          val otherProject = project(projectPath)
          // 判断当前被依赖的模块是否允许用缓存替换
          if (cache.isAllowOtherUseCache(otherProject)) {
            val file = (otherProject.extensions["cache"] as Cache).getLocalMavenFile()
            if (file.exists()) {
              // 存在就直接替换依赖
              println("正在编译的模块：${project.name}，依赖的 $projectPath 模块被替换为缓存")
              useTarget("${cache.localMavenGroup}:${otherProject.name}:${otherProject.version}")
            }
          }
        }
      }
    }
  }
}

val publishTaskName = "publishModuleCachePublicationTo${cache.localMavenName.capitalize()}Repository"

tasks.register("cacheToLocalMaven") {
  group = "publishing"
  /*
  * module_app 因为需要依赖所有模块，但缓存是需要进行完整打包的，
  * 存在某 module 模块是单模块调试状态，这种情况下 module_app 的构建会失败
  *
  * 意思就是：缓存时不允许两个使用 application 插件的模块之间存在依赖关系
  *
  * 但是否存在互相依赖且都同时使用了 application 插件有点难判断，所以 module_app 就默认不缓存了
  * */
  if (project.name != "module_app"
    && !gradle.startParameter.taskNames.any {
      it == "${project.path}:assembleDebug" // 自身模块打包时不允许缓存，因为启动模块在单模块调试时经常被修改
    }
    && cache.deleteOldCacheIfNeedNewCache()
  ) {
    dependsOn(publishTaskName)
  }
}

tasks.whenTaskAdded {
  if (name == publishTaskName) {
    doFirst {
      println("正在缓存 ${project.name} 模块")
    }
  }
}

if (isOpenModuleCache && (name.startsWith("module_") || name.startsWith("lib_"))) {
  /*
  * 这里有个很奇怪的问题，如果给 api 模块加上，api 模块会报错：
  * Cannot access built-in declaration 'kotlin.String'. Ensure that you have a dependency on the Kotlin standard library
  * 只要给 api 模块调用了 tasks.whenTaskAdded 就会报，
  * 反正 api 模块不会调用打包，所以就判断了一下
  * */
  tasks.whenTaskAdded {
    if (name == "assembleDebug") {
      dependsOn(rootProject.tasks.named("cacheToLocalMaven"))
    }
  }
}



