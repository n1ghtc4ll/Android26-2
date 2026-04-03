@file:OptIn(ExperimentalMaterial3Api::class)

package ru.urfu.droidpractice1.content

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.urfu.droidpractice1.R
import ru.urfu.droidpractice1.SecondActivity
import ru.urfu.droidpractice1.ui.theme.DroidPractice1Theme
import androidx.core.content.edit

private const val IMAGE_URL = "https://www.pokemon.com/static-assets/content-assets/cms2/img/video-games/_tiles/pokemon-champions/2026/03/24/pokemon-champions-169.png"
private const val PREFS_NAME = "articlesPrefs"
private const val KEY_LIKES = "article1LikesCount"
private const val KEY_DISLIKES = "article1dIsLikesCount"
private const val KEY_IS_LIKED = "article1IsLiked"
private const val KEY_IS_DISLIKED = "article1IsDisliked"
private const val KEY_IS_READ = "isRead"


@Composable
fun MainActivityScreen() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isLiked by rememberSaveable {
        mutableStateOf(prefs.getBoolean(KEY_IS_LIKED, false))
    }
    var likesCount by rememberSaveable {
        mutableIntStateOf(prefs.getInt(KEY_LIKES, 0))
    }
    var isDisliked by rememberSaveable {
        mutableStateOf(prefs.getBoolean(KEY_IS_DISLIKED, false))
    }
    var dislikesCount by rememberSaveable {
        mutableIntStateOf(prefs.getInt(KEY_DISLIKES, 0))
    }
    var isSecondActivityRead by rememberSaveable {
        mutableStateOf(prefs.getBoolean(KEY_IS_READ, false))
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val isRead = result.data?.getBooleanExtra(KEY_IS_READ, false) ?: false
            isSecondActivityRead = isRead
        }
    }

    DroidPractice1Theme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.article_title))
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                }
                                context.startActivity(intent)
                            }
                        ) {
                           Icon(
                               painter = painterResource(R.drawable.share),
                               contentDescription = null
                           )
                        }
                    }
                )
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp)
                    .verticalScroll(state = rememberScrollState())
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.article_header1),
                    style = MaterialTheme.typography.headlineSmall
                )

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    IconButton(
                        onClick = {
                            isLiked = !isLiked
                            likesCount += if (isLiked) 1 else -1
                            dislikesCount += if (isDisliked) -1 else 0
                            isDisliked = false

                            saveState(prefs, isLiked, likesCount, isDisliked, dislikesCount)
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (!isLiked) R.drawable.unpressed_like
                                else R.drawable.pressed_like
                            ),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = "$likesCount",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    IconButton(
                        onClick = {
                            isDisliked = !isDisliked
                            dislikesCount += if (isDisliked) 1 else -1
                            likesCount += if (isLiked) -1 else 0
                            isLiked = false

                            saveState(prefs, isLiked, likesCount, isDisliked, dislikesCount)
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (!isDisliked) R.drawable.unpressed_dislike
                                else R.drawable.pressed_dislike
                            ),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = "$dislikesCount",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                AsyncImage(model = IMAGE_URL,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = stringResource(R.string.article_paragraph1_1),
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.article_paragraph1_2),
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.article_paragraph1_3),
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.article_paragraph1_4),
                    modifier = Modifier.padding(bottom = 20.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = {
                        val intent = Intent(context, SecondActivity::class.java)
                        launcher.launch(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(
                            if (!isSecondActivityRead) R.color.teal_700
                            else R.color.light_gray
                        ),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(R.string.article_header2),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

fun saveState(
    prefs: SharedPreferences,
    isLiked: Boolean,
    likesCount: Int,
    isDisliked: Boolean,
    dislikesCount: Int
) {
    prefs.edit {
        putBoolean(KEY_IS_LIKED, isLiked)
            .putInt(KEY_LIKES, likesCount)
            .putBoolean(KEY_IS_DISLIKED, isDisliked)
            .putInt(KEY_DISLIKES, dislikesCount)
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainActivityScreen()
}
