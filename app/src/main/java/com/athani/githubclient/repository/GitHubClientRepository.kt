package com.athani.githubclient.repository

import com.athani.githubclient.model.GitHubUserDetails
import com.athani.githubclient.model.GitHubUserItem
import com.athani.githubclient.model.GitHubUserRepositoryItem
import com.athani.githubclient.network.GitHUbClientApi
import javax.inject.Inject

class GitHubClientRepository @Inject constructor(private val api: GitHUbClientApi) {
    suspend fun getUsers(): List<GitHubUserItem> {
        return api.getUsers()
    }
    suspend fun getUserDetails(username: String): GitHubUserDetails {
        return api.getUserDetails(username)
    }
    suspend fun getUserRepository(username: String): List<GitHubUserRepositoryItem> {
        return api.getUserRepository(username)
    }
}