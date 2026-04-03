package ru.urfu.droidpractice1

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import com.bumptech.glide.Glide
import ru.urfu.droidpractice1.databinding.ActivitySecondBinding
import androidx.core.content.edit

class SecondActivity : ComponentActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivitySecondBinding
    private var likesCount = 0
    private var isLiked = false
    private var dislikesCount = 0
    private var isDisliked = false
    private var isRead = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SecondActivity", "onCreate")
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        likesCount = prefs.getInt(KEY_LIKES, 0)
        isLiked = prefs.getBoolean(KEY_IS_LIKED, false)
        dislikesCount = prefs.getInt(KEY_DISLIKES, 0)
        isDisliked = prefs.getBoolean(KEY_IS_DISLIKED, false)
        isRead = prefs.getBoolean(KEY_IS_READ, false)

        setupUI()
        setupClickListeners()
    }

    override fun onStart() {
        super.onStart()
        Log.d("SecondActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("SecondActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("SecondActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SecondActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SecondActivity", "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("SecondActivity", "onRestart")
    }

    private fun setupUI() {
        updateRatings()
        binding.wasRead.isChecked = isRead

        Glide.with(binding.article2Pic)
            .asBitmap()
            .load(IMAGE_URL)
            .into(binding.article2Pic)
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        onBackPressedDispatcher.addCallback {
            val intent = Intent().apply { putExtra(KEY_IS_READ, binding.wasRead.isChecked) }
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
            }
            startActivity(intent)
        }

        binding.likeButton.setOnClickListener {
            isLiked = !isLiked
            likesCount += if (isLiked) 1 else -1
            dislikesCount += if (isDisliked) -1 else 0
            isDisliked = false

            updateRatings()
            saveState()
        }

        binding.dislikeButton.setOnClickListener {
            isDisliked = !isDisliked
            dislikesCount += if (isDisliked) 1 else -1
            likesCount += if (isLiked) -1 else 0
            isLiked = false

            updateRatings()
            saveState()
        }

        binding.wasRead.setOnCheckedChangeListener { _, isChecked ->
            isRead = isChecked
            prefs.edit { putBoolean(KEY_IS_READ, isRead) }
        }
    }

    private fun updateRatings() {
        binding.likeButton.setImageResource(
            if (isLiked) R.drawable.pressed_like
            else R.drawable.unpressed_like
        )

        binding.dislikeButton.setImageResource(
            if (isDisliked) R.drawable.pressed_dislike
            else R.drawable.unpressed_dislike
        )

        binding.likesCount.text = getString(R.string.likes_count, likesCount)
        binding.dislikesCount.text = getString(R.string.dislikes_count, dislikesCount)
    }

    private fun saveState() {
        prefs.edit {
            putInt(KEY_LIKES, likesCount)
                .putInt(KEY_DISLIKES, dislikesCount)
                .putBoolean(KEY_IS_LIKED, isLiked)
                .putBoolean(KEY_IS_DISLIKED, isDisliked)
        }
    }

    private companion object {
        const val PREFS_NAME = "articlesPrefs"
        const val KEY_LIKES = "article2LikesCount"
        const val KEY_DISLIKES = "article2DislikesCount"
        const val KEY_IS_LIKED = "article2IsLiked"
        const val KEY_IS_DISLIKED = "article2IsDisliked"
        const val KEY_IS_READ = "isRead"
        const val IMAGE_URL = "https://www.pokemon.com/static-assets/content-assets/cms2/img/video-games/video-games/pokemon_pokopia/screenshots/08.png"
    }
}