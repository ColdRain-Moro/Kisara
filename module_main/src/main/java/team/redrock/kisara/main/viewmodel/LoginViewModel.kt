package team.redrock.kisara.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import net.mamoe.mirai.utils.BotConfiguration
import team.redrock.kisara.main.model.mirai.BotManager

/**
 * team.redrock.kisara.main.viewmodel.LoginViewModel
 * Kisara
 *
 * @author 寒雨
 * @since 2022/9/12 下午7:49
 */
class LoginViewModel : ViewModel() {

    private val _login = MutableSharedFlow<Unit>()

    val login: SharedFlow<Unit>
        get() = _login

    fun login(
        id: Long,
        password: String,
        protocol: BotConfiguration.MiraiProtocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE,
    ) {
        viewModelScope.launch {
            BotManager.login(id, password, protocol)
            _login.emit(Unit)
        }
    }
}