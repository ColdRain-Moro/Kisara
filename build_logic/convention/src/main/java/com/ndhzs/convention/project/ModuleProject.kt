package com.ndhzs.convention.project

import com.ndhzs.convention.depend.dependAndroidKtx
import com.ndhzs.convention.depend.dependAndroidView
import com.ndhzs.convention.depend.dependLifecycleKtx
import com.ndhzs.convention.project.base.BaseLibraryProject
import org.gradle.api.Project

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/28 12:22
 */
class ModuleProject(project: Project) : BaseLibraryProject(project) {
  
  override fun initProject() {
    checkIsInDebug()
    dependAndroidView()
    dependAndroidKtx()
    dependLifecycleKtx()
  }
  
  /**
   * 检查该模块是否处于 debug 状态
   */
  private fun checkIsInDebug() {
    if (plugins.hasPlugin("com.android.application")) {
      throw RuntimeException("取消单模块调试才能使用多模块插件！")
    }
  }
}