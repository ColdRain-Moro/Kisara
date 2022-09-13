package team.redrock.kisara.main.exception

import net.mamoe.mirai.network.CustomLoginFailedException

/**
 * team.redrock.kisara.main.exception.LoginSolveCancelException
 * Kisara
 *
 * @author 寒雨
 * @since 2022/9/12 下午9:59
 */
class LoginSolveCancelException : CustomLoginFailedException(true, "未正常解决登录验证")