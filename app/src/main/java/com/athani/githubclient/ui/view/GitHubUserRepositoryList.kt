package com.athani.githubclient.ui.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athani.githubclient.R
import com.athani.githubclient.model.GitHubUserRepositoryItem
import com.athani.githubclient.model.Resource
import com.athani.githubclient.ui.theme.CalendarColor
import com.athani.githubclient.ui.theme.ForkColor
import com.athani.githubclient.ui.theme.LanguageColor
import com.athani.githubclient.ui.theme.StarColor
import com.athani.githubclient.viewmodel.GitHubClientViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun GitHubUserRepositoryList(
    viewModel: GitHubClientViewModel,
    username: String
) {
    LaunchedEffect(username) {
        viewModel.fetchUserRepositories(username)
    }
    when (val result = viewModel.userRepositories) {
        is Resource.Loading -> LoadingScreen()
        is Resource.Success -> RepositoryList(result.data.sortedByDescending { it.stargazers_count })
        is Resource.Error -> ErrorDialogScreen()
    }
}

@Composable
fun RepositoryList(repositories: List<GitHubUserRepositoryItem>) {
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(repositories) { repo ->
            RepositoryItem(
                repo, Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun RepositoryItem(
    repository: GitHubUserRepositoryItem,
    modifier: Modifier
) {
    val context = LocalContext.current
    Card(
        modifier
            .fillMaxSize()
            .size(width = 360.dp, height = 200.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.html_url))
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = repository.name ?: "",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.DarkGray, Color.Blue)
                        ),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = repository.visibility ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(4.dp)
                )
            }
            Text(
                text = (repository.description ?: "Description not available"),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp),
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RepositoryRowItem(
                    painter = painterResource(id = R.drawable.language),
                    contentDescription = "Language Icon",
                    text = repository.language ?: "N/A",
                    tint = LanguageColor
                )
                RepositoryRowItem(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Star Icon",
                    text = repository.stargazers_count.toString() ?: "0",
                    tint = StarColor
                )
                RepositoryRowItem(
                    painter = painterResource(id = R.drawable.fork),
                    contentDescription = "Fork Icon",
                    text = repository.forks_count ?: "0",
                    tint = ForkColor
                )
                RepositoryRowItem(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Calendar Icon",
                    text = formatDate(repository.updated_at) ?: "N/A",
                    tint = CalendarColor
                )
            }
        }
    }
}

@Composable
fun RepositoryRowItem(
    painter: Painter,
    contentDescription: String,
    text: String,
    tint: Color
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier.size(16.dp)
    )
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(start = 4.dp, end = 8.dp)
    )
}

//Formatting the date received from server
fun formatDate(dateString: String): String {
    val parseDate = ZonedDateTime.parse(dateString)
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    return parseDate.format(formatter)
}