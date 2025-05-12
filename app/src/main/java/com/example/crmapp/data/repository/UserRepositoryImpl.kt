package com.example.crmapp.data.repository

import com.example.crmapp.api.interfaces.UserApiService
import com.example.crmapp.api.requests.UserRequest
import com.example.crmapp.data.storage.JwtStorage
import com.example.crmapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val jwtStorage: JwtStorage
): UserRepository {

    override suspend fun createUser(username: String, password: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val userRequest = UserRequest(username, password)
                val response = userApiService.createUser(userRequest)

                if(response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Error creating user: ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
        }

    //TODO put the userId fetching logic into a use case?
    override suspend fun loginUser(username: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val userRequest = UserRequest(username, password)
                val response = userApiService.getJwtToken(userRequest)

                if(response.isSuccessful) {
                    val jwtResponse = response.body()
                        ?: return@withContext Result.failure(Exception("Empty JWT response"))

                    val jwtToken = jwtResponse.token

                    jwtStorage.saveJwt(jwtToken)

                    Result.success(jwtToken)
                } else {
                    Result.failure(Exception("Login failed: ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getUserId(username: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val userRequest = UserRequest(username, password)
                val response = userApiService.getJwtToken(userRequest)

                if(response.isSuccessful) {
                    val userId = response.headers()["userId"]
                        ?: return@withContext Result.failure(Exception("User ID empty"))

                    Result.success(userId)
                } else {
                    Result.failure(Exception("Couldn't get user ID ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun isUserLoggedIn(): Boolean {
        return jwtStorage.hasJwt()
    }

    override suspend fun getStoredJwt(): String? {
        return jwtStorage.getJwtToken()
    }

    override suspend fun logoutUser() {
        jwtStorage.clearJwt()
    }

}