package com.example.crmapp.data.repository

import com.example.crmapp.api.interfaces.UserApiService
import com.example.crmapp.api.requests.UserRequest
import com.example.crmapp.domain.model.entities.JwtEntity
import com.example.crmapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(private val userApiService: UserApiService): UserRepository {

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

    override suspend fun loginUser(username: String, password: String): Result<JwtEntity> =
        withContext(Dispatchers.IO) {
            try {
                val userRequest = UserRequest(username, password)
                val response = userApiService.getJwtToken(userRequest)

                if(response.isSuccessful) {
                    val jwtResponseCall = response.body()
                        ?: return@withContext Result.failure(Exception("Empty JWT response"))

                    val innerResponse = jwtResponseCall.execute()

                    if(innerResponse.isSuccessful) {
                        val jwtResponse = innerResponse.body()
                            ?: return@withContext Result.failure(Exception("Empty JWT data"))

                        val formattedResponse = JwtEntity(
                            token = jwtResponse.token,
                            userId = response.headers()["userId"] ?: ""
                        )

                        Result.success(formattedResponse)
                    } else {
                        return@withContext Result.failure(Exception("JWT extraction failed: ${innerResponse.code()} ${innerResponse.message()}"))
                    }

                } else {
                    Result.failure(Exception("Login failed: ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
        }

}