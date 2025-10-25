package com.tfkiot.myshopadmin.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tfkiot.myshopadmin.components.UserCard
import com.tfkiot.myshopadmin.model.UserModel
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import com.tfkiot.myshopadmin.utill.AppUtil.fetchProductNameById


@Composable
fun Users(
    modifier: Modifier = Modifier,
    userOrderCounts: Map<String, Int>, // Precomputed: userId->order count
    onOrderClick: (UserModel) -> Unit,
    onCartClick: (UserModel) -> Unit,
    onFavClick: (UserModel) -> Unit,
) {
    val userList = remember { mutableStateOf<List<UserModel>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var filterType by remember { mutableStateOf("All") }

    // Fetch from Firestore
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("users").get().await()
            .let { result ->
                userList.value = result.documents.mapNotNull {
                    it.toObject(UserModel::class.java)
                }
            }
    }

    // Apply search/filter
    val filteredUsers = userList.value.filter { user ->
        val matchesSearch = user.name.contains(searchQuery, ignoreCase = true) ||
                user.email.contains(searchQuery, ignoreCase = true) ||
                user.userId.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (filterType) {
            "With Cart" -> user.cartItems.isNotEmpty()
            "With Favorites" -> user.favoriteItems.isNotEmpty()
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search users...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        // Filter Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = filterType == "All",
                onClick = { filterType = "All" },
                label = { Text("All") }
            )
            FilterChip(
                selected = filterType == "With Cart",
                onClick = { filterType = "With Cart" },
                label = { Text("With Cart") }
            )
            FilterChip(
                selected = filterType == "With Favorites",
                onClick = { filterType = "With Favorites" },
                label = { Text("With Favorites") }
            )
        }
        Spacer(Modifier.height(16.dp))

        // User list
        LazyColumn(Modifier.fillMaxSize()) {
            items(filteredUsers) { user ->
                val orderCount = userOrderCounts[user.userId] ?: 0
                UserCard(
                    user = user,
                    orderCount = orderCount,
                    onOrderClick = onOrderClick,
                    onCartClick = onCartClick,
                    onFavClick = onFavClick
                )
            }
        }
    }
}

