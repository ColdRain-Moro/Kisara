package team.redrock.kisara.main.view.fragment.loginsolver

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.ndhzs.lib.base.ui.BaseBindFragment
import team.redrock.kisara.main.databinding.MainFragmentPicSolverBinding

/**
 * team.redrock.kisara.main.view.fragment.loginsolver.PicSolverFragment
 * Kisara
 *
 * @author 寒雨
 * @since 2022/9/13 上午12:36
 */
class PicSolverFragment : BaseBindFragment<MainFragmentPicSolverBinding>() {

    private val pic by lazy { requireArguments().getByteArray("pic") }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(binding.ivPic)
            .load(pic)
            .into(binding.ivPic)

        binding.btnSubmit.setOnClickListener {
            requireActivity().apply {
                setResult(
                    RESULT_OK,
                    Intent().putExtra("result", binding.etVerifyCode.text.toString())
                )
                finishAfterTransition()
            }
        }
    }

    companion object {
        fun newInstance(pic: ByteArray): PicSolverFragment{
            val args = Bundle()
                .apply { putByteArray("pic", pic) }
            val fragment = PicSolverFragment()
            fragment.arguments = args
            return fragment
        }
    }
}