package com.athani.githubclient.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.athani.githubclient.model.GitHubUserItem
import com.athani.githubclient.model.Resource
import com.athani.githubclient.viewmodel.GitHubClientViewModel

@Composable
fun GitHubUserListScreen(
    viewModel: GitHubClientViewModel,
    onUserClick: (String) -> Unit
) {
    when (val result = viewModel.users) {
        is Resource.Loading -> LoadingScreen()
        is Resource.Success -> GitHubUserColumn(result.data, onUserClick)
        is Resource.Error -> ErrorDialogScreen()
    }
}

@Composable
fun GitHubUserColumn(userItemList: List<GitHubUserItem>, onUserClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarScreen()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 25.dp)
        ) {
            items(userItemList) { user ->
                GitHubUserListItem(user, onUserClick)
            }
        }
    }
}

@Composable
fun GitHubUserListItem(user: GitHubUserItem, onUserClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clickable { onUserClick(user.login) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.avatar_url,
            contentDescription = "User Avatar",
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = user.login,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${user.type} - ${user.user_view_type}",
                color = Color.Gray
            )
        }
    }
    HorizontalDivider(modifier = Modifier.height(0.2.dp), color = MaterialTheme.colorScheme.primary)
}
