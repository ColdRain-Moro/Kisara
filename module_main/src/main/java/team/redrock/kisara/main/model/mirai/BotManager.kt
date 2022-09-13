package team.redrock.kisara.main.model.mirai

import com.ndhzs.lib.utils.extensions.appContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration

object BotManager {
    var bot: Bot? = null

    suspend fun login(
        id: Long,
        password: String,
        protocol: BotConfiguration.MiraiProtocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE,
    ) = withContext(Dispatchers.IO) {
        bot = BotFactory.newBot(id, password) {
            fileBasedDeviceInfo()
            workingDir = appContext.filesDir.resolve("bot")
            this.protocol = protocol
            loginSolver = KisaraLoginSolver
        }
        bot!!.login()
    }
}