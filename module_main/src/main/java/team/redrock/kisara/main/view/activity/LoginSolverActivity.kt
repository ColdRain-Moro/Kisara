package team.redrock.kisara.main.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.transition.Visibility
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.view.animation.OvershootInterpolator
import androidx.activity.result.ActivityResultLauncher
import com.ndhzs.lib.base.ui.BaseBindActivity
import team.redrock.kisara.main.databinding.MainActivityLoginSolverBinding
import team.redrock.kisara.main.view.fragment.loginsolver.PicSolverFragment
import team.redrock.kisara.main.view.fragment.loginsolver.SliderSolverFragment
import team.redrock.kisara.main.view.fragment.loginsolver.UnsafeSolverFragment
import team.redrock.kisara.main.R

/**
 * team.redrock.kisara.main.view.activity.LoginSolverActivity
 * Kisara
 *
 * @author 寒雨
 * @since 2022/9/11 下午8:44
 */
class LoginSolverActivity : BaseBindActivity<MainActivityLoginSolverBinding>() {

    private val type by lazy {
        Type.valueOf(intent.getStringExtra("type")!!)
    }
    private val url by lazy { intent.getStringExtra("url") }
    private val byteArray by lazy { intent.getByteArrayExtra("byteArray") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = buildEnterTransition()
        postponeEnterTransition()
        val decorView = window.decorView
        window.decorView.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                decorView.viewTreeObserver.removeOnPreDrawListener(this)
                supportStartPostponedEnterTransition()
                return true
            }
        })
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "登录验证"
            it.subtitle = type.subtitle
        }
        when (type) {
            Type.PIC -> {
                replaceFragment(R.id.page_container) {
                    PicSolverFragment.newInstance(byteArray!!)
                }
            }
            Type.SLIDER -> {
                replaceFragment(R.id.page_container) {
                    SliderSolverFragment.newInstance(url!!)
                }
            }
            Type.UNSAFE -> {
                replaceFragment(R.id.page_container) {
                    UnsafeSolverFragment.newInstance(url!!)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buildEnterTransition(): Visibility {
        val slide = Fade()
        slide.duration = 500
        slide.interpolator = OvershootInterpolator(0.5F)
        return slide
    }

    companion object {
        @JvmStatic
        fun start(launcher: ActivityResultLauncher<Intent>, context: Context, type: Type, url: String? = null, byteArray: ByteArray? = null) {
            val starter = Intent(context, LoginSolverActivity::class.java)
                .apply {
                    url?.let { putExtra("url", url) }
                    byteArray?.let { putExtra("byteArray", byteArray) }
                    putExtra("type", type.name)
                }
            launcher.launch(starter)
        }
    }

    enum class Type(val subtitle: String) {
        PIC("图片验证码"), SLIDER("滑行验证"), UNSAFE("危险设备验证")
    }
}