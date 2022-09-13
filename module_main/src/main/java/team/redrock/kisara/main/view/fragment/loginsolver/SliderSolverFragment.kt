package team.redrock.kisara.main.view.fragment.loginsolver

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.JsonParser
import com.ndhzs.lib.base.ui.BaseBindFragment
import team.redrock.kisara.main.databinding.MainFragmentSliderSolverBinding

/**
 * team.redrock.kisara.main.view.fragment.loginsolver.SliderSolverFragment
 * Kisara
 *
 * @author 寒雨
 * @since 2022/9/13 上午12:37
 */
class SliderSolverFragment : BaseBindFragment<MainFragmentSliderSolverBinding>() {

    private val url by lazy { requireArguments().getString("url") }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return onJsBridgeInvoke(request!!.url)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    url: String?
                ): Boolean {
                    return onJsBridgeInvoke(Uri.parse(url))
                }
            }
            loadUrl(url!!)
        }
    }

    private fun onJsBridgeInvoke(request: Uri): Boolean {
        if (request.path.equals("/onVerifyCAPTCHA")) {
            val p = request.getQueryParameter("p")
            val jsData = JsonParser.parseString(p).asJsonObject
            requireActivity().apply {
                setResult(RESULT_OK, Intent().putExtra("result", jsData["ticket"].asString))
                finishAfterTransition()
            }
        }
        return false
    }

    companion object {
        fun newInstance(url: String): SliderSolverFragment {
            val args = Bundle()
                .apply { putString("url", url) }
            val fragment = SliderSolverFragment()
            fragment.arguments = args
            return fragment
        }
    }
}