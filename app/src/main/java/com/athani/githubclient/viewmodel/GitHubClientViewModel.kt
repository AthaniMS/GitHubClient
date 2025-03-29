package com.athani.githubclient.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athani.githubclient.model.GitHubUserDetails
import com.athani.githubclient.model.GitHubUserItem
import com.athani.githubclient.model.GitHubUserRepositoryItem
import com.athani.githubclient.model.Resource
import com.athani.githubclient.repository.GitHubClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubClientViewModel @Inject constructor(private val repository: GitHubClientRepository) :
    ViewModel() {
    var users by mutableStateOf<Resource<List<GitHubUserItem>>>(Resource.Loading)
        private set
    var userDetails by mutableStateOf<Resource<GitHubUserDetails>>(Resource.Loading)
        private set
    var userRepositories by mutableStateOf<Resource<List<GitHubUserRepositoryItem>>>(Resource.Loading)
        private set

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            users = try {
                Resource.Success(repository.getUsers())
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Unknown Error!")
            }
        }
    }

    fun fetchUserDetails(username: String) {
        viewModelScope.launch {
            userDetails = try {
                Resource.Success(repository.getUserDetails(username))
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Unknown Error!")
            }
        }
    }

    fun fetchUserRepositories(username: String) {
        viewModelScope.launch {
            userRepositories = try {
                Resource.Success(repository.getUserRepository(username))
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Unknown Error!")
            }
        }
    }
}