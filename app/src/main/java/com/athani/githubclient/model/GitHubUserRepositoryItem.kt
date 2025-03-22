package com.athani.githubclient.model

data class GitHubUserRepositoryItem(
    val description: String,
    val forks_count: String,
    val html_url: String,
    val language: String,
    val stargazers_count: Int,
    val updated_at: String,
    val name: String,
    val visibility: String
)