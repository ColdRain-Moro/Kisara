package team.redrock.kisara.main.view.fragment.loginsolver

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ndhzs.lib.base.ui.BaseBindFragment
import es.dmoral.toasty.Toasty
import team.redrock.kisara.main.databinding.MainFragmentUnsafeSolverBinding
import java.util.*

/**
 * team.redrock.kisara.main.view.fragment.loginsolver.UnsafeSolverFragment
 * Kisara
 *
 * @author 寒雨
 * @since 2022/9/13 上午12:38
 */
class UnsafeSolverFragment : BaseBindFragment<MainFragmentUnsafeSolverBinding>() {

    private val url by lazy { requireArguments().getString("url") }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            loadUrl(url!!)
        }
        requireActivity().setResult(RESULT_OK, Intent().putExtra("result", UUID.randomUUID().toString()))
        Toasty.normal(requireContext(), "操作完成后请主动退出页面").show()
    }

    companion object {
        fun newInstance(url: String): UnsafeSolverFragment {
            val args = Bundle()
                .apply { putString("url", url) }
            val fragment = UnsafeSolverFragment()
            fragment.arguments = args
            return fragment
        }
    }
}