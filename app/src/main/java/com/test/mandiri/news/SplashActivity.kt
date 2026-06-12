package com.test.mandiri.news

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.mandiri.news.core.router.api.Router
import com.test.mandiri.news.core.router.impl.navigate
import com.test.mandiri.news.feature.news.categories.CategoriesRouteHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(SPLASH_DELAY)
            router.navigate(
                context = this@SplashActivity,
                path = CategoriesRouteHandler.PATH,
                navigator = { intent ->
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                },
            )
            finish()
        }
    }

    companion object {
        private const val SPLASH_DELAY = 2000L
    }
}
