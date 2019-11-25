package us.mzhang.shoppinglistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.splash_screen.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        tagline.alpha = 0F

        val flyRight = AnimationUtils.loadAnimation(
            this@SplashActivity, R.anim.fly_in_right
        )

        val flyLeft = AnimationUtils.loadAnimation(
            this@SplashActivity, R.anim.fly_in_left
        )

        val fadeIn = AnimationUtils.loadAnimation(
            this@SplashActivity, R.anim.fade_in
        )

        val zoomIn = AnimationUtils.loadAnimation(
            this@SplashActivity, R.anim.zoom
        )

        zoomIn.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    var intentMain = Intent()
                    intentMain.setClass(
                        this@SplashActivity,
                        ScrollingActivity::class.java
                    )

                    startActivity(intentMain)
                    finish()

                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

            }
        )

        flyLeft.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                tagline.startAnimation(fadeIn)
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })

        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                tagline.alpha = 1F
            }

            override fun onAnimationEnd(p0: Animation?) {
                splashScreen.startAnimation(zoomIn)
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })

        ivTitle.startAnimation(flyRight)
        list_image.startAnimation(flyLeft)

    }
}
