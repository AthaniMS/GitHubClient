package com.athani.githubclient.ui.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.athani.githubclient.R
import com.athani.githubclient.model.GitHubUserDetails
import com.athani.githubclient.model.Resource
import com.athani.githubclient.viewmodel.GitHubClientViewModel

@Composable
fun GitHubUserDetailsScreen(
    viewModel: GitHubClientViewModel,
    username: String
) {
    val context = LocalContext.current
    LaunchedEffect(username) {
        viewModel.fetchUserDetails(username)
    }
    when(val result = viewModel.userDetails) {
        is Resource.Loading -> LoadingScreen()
        is Resource.Success -> GitHubUserProfile(result.data, context, viewModel)
        is Resource.Error -> ErrorDialogScreen()
    }
}

@Composable
fun GitHubUserProfile(
    userDetails: GitHubUserDetails,
    context: Context,
    viewModel: GitHubClientViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBarScreen()
        userDetails?.let {
            ProfileHeader(it)
            ProfileStats(it, context)
            GitHubUserRepositoryList(
                viewModel = viewModel,
                username = it.login
            )
        }
    }
}

@Composable
fun ProfileHeader(user: GitHubUserDetails) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.avatar_url,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = user.name ?: "Unknown",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "@${user.login}",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = user.bio ?: "No bio available",
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ProfileStats(
    user: GitHubUserDetails,
    context: Context
) {
    user.location?.takeIf { it.isNotEmpty() }?.let {
       StatsRow(
           text = it,
           hasExternalLink = false,
           painter = painterResource(id = R.drawable.location),
           description = "Location Icon",
           context = context
       )
   }
    user.email?.takeIf { it.isNotEmpty() }?.let {
        StatsRow(
            text = it,
            hasExternalLink = false,
            painter = painterResource(id = R.drawable.email),
            description = "Email Icon",
            context = context
        )
    }
    user.blog?.takeIf { it.isNotEmpty() }?.let {
        StatsRow(
            text = it,
            hasExternalLink = true,
            painter = painterResource(id = R.drawable.url_link),
            description = "Url Icon",
            context = context
        )
    }
    user.twitter_username?.takeIf { it.isNotEmpty() }?.let {
        StatsRow(
            text = "@$it",
            hasExternalLink = false,
            painter = painterResource(id = R.drawable.x_logo),
            description = "X Icon",
            context = context
        )
    }
    user.company?.takeIf { it.isNotEmpty() }?.let {
        StatsRow(
            text = it,
            hasExternalLink = false,
            painter = painterResource(id = R.drawable.office),
            description = "Office Icon",
            context = context
        )
    }
    StatsRow(
        text = "${formatFollowersCount(user.followers)} Followers • ${formatFollowersCount(user.following)} Following",
        hasExternalLink = false,
        painter = painterResource(id = R.drawable.follower),
        description = "Follower Icon",
        context = context
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.primary)
    Spacer(modifier = Modifier.height(16.dp))
    RepositoryRow(user.public_repos)
}

@Composable
fun StatsRow(
    text: String,
    hasExternalLink: Boolean,
    painter: Painter,
    description: String,
    context: Context
) {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = description
        )
        Text(
            text = text,
            fontSize = 14.sp,
            textDecoration = if (hasExternalLink) TextDecoration.Underline else TextDecoration.None,
            modifier = Modifier
                .padding(start = 4.dp)
                .then(
                    if (hasExternalLink) Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
                        context.startActivity(intent)
                    } else Modifier
                )
        )
    }
}


@Composable
fun RepositoryRow(publicRepos: Int) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.repo),
            contentDescription = "Repo Icon",
            modifier = Modifier.size(18.dp),
            tint = Color.Black
        )
        Text(
            text = "Repositories(${publicRepos})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp)
        )
    }
}

//Function to Format Count
fun formatFollowersCount(countString: String): String {
    val count = countString.toLongOrNull() ?: return "0"
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}
