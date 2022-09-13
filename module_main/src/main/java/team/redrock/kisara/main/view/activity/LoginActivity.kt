package team.redrock.kisara.main.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ndhzs.lib.base.ui.BaseBindActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import team.redrock.kisara.main.databinding.MainActivityLoginBinding
import team.redrock.kisara.main.exception.LoginSolveCancelException
import team.redrock.kisara.main.model.mirai.KisaraLoginSolver
import team.redrock.kisara.main.viewmodel.LoginViewModel

/**
 * team.redrock.kisara.main.view.activity.LoginActivity
 * Kisara
 *
 * @author 寒雨
 * @since 2022/9/12 下午7:49
 */
class LoginActivity : BaseBindActivity<MainActivityLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var loginSolverLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginSolverLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                lifecycleScope.launch {
                    if (it.data != null) {
                        KisaraLoginSolver.resultChan.send(Result.success(it.data!!.getStringExtra("result")))
                    } else {
                        KisaraLoginSolver.resultChan.send(Result.failure(LoginSolveCancelException()))
                    }
                }
            }
        // 注册LoginSolver触发后的行为
        KisaraLoginSolver.picChan.use {
            LoginSolverActivity.start(
                loginSolverLauncher,
                this,
                LoginSolverActivity.Type.PIC,
                byteArray = it
            )
        }
        KisaraLoginSolver.urlChan.use { (type, url) ->
            LoginSolverActivity.start(
                loginSolverLauncher,
                this,
                type,
                url = url
            )
        }
        binding.apply {
            btnLogin.setOnClickListener {
                val account = etAccount.text?.toString()
                val password = etPassword.text?.toString()
                if (account.isNullOrBlank() || password.isNullOrBlank()) {
                    Toasty.error(this@LoginActivity, "帐号或密码不能为空").show()
                    return@setOnClickListener
                }
                viewModel.login(account.toLong(), password)
            }
        }
        viewModel.login.collectLaunch {
            // 成功登录

        }
    }

    private fun <T> ReceiveChannel<T>.use(onReceive: (T) -> Unit) {
        lifecycleScope.launch {
            onReceive(receive())
        }
    }
}