package team.redrock.kisara.main.model.mirai

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.LoginSolver
import team.redrock.kisara.main.view.activity.LoginSolverActivity

/**
 * team.redrock.kisara.main.model.mirai.KisaraLoginSolver
 * Kisara
 *
 * 传入的回调在使用后立刻置空，防止内存泄漏
 *
 * @author 寒雨
 * @since 2022/9/11 下午8:43
 */
object KisaraLoginSolver : LoginSolver() {

    private val mutablePicChan = Channel<ByteArray>()
    private val mutableUrlChan = Channel<Pair<LoginSolverActivity.Type, String>>()
    private val mutableResultChan = Channel<Result<String?>>()

    // 只暴露可receive的channel
    val picChan: ReceiveChannel<ByteArray>
        get() = mutablePicChan
    val urlChan: ReceiveChannel<Pair<LoginSolverActivity.Type, String>>
        get() = mutableUrlChan

    // 只暴露可send的channel
    val resultChan: SendChannel<Result<String?>>
        get() = mutableResultChan

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        // 把数据发送给UI层
        mutablePicChan.send(data)
        // 等待UI层返回结果
        return mutableResultChan.receive().getOrThrow()
    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        mutableUrlChan.send(LoginSolverActivity.Type.SLIDER to url)
        return mutableResultChan.receive().getOrThrow()
    }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        mutableUrlChan.send(LoginSolverActivity.Type.UNSAFE to url)
        return mutableResultChan.receive().getOrThrow()
    }

}

