package team.redrock.kisara.main.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ndhzs.lib.base.ui.BaseBindActivity
import team.redrock.kisara.main.databinding.MainActivityMainBinding
import team.redrock.kisara.main.R
import team.redrock.kisara.main.viewmodel.MainViewModel

/**
 * team.redrock.kisara.main.view.activity.MainActivity
 * Kisara
 *
 * @author 寒雨
 * @since 2022/9/13 下午3:55
 */
class MainActivity : BaseBindActivity<MainActivityMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            rvChat.layoutManager = LinearLayoutManager(this@MainActivity)
            setSupportActionBar(toolbar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.main_ic_baseline_menu_24)
            }
        }
    }

}