package com.athani.githubclient.network

import com.athani.githubclient.model.GitHubUserDetails
import com.athani.githubclient.model.GitHubUserItem
import com.athani.githubclient.model.GitHubUserRepositoryItem
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHUbClientApi {
    @GET("users")
    suspend fun getUsers(): List<GitHubUserItem>

    @GET("users/{username}")
    suspend fun getUserDetails(@Path("username") username: String): GitHubUserDetails

    @GET("users/{username}/repos")
    suspend fun getUserRepository(@Path("username") username: String): List<GitHubUserRepositoryItem>
}