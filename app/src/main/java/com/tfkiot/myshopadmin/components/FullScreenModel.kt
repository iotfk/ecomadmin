package com.tfkiot.myshopadmin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.tfkiot.myshopadmin.model.UserModel

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.tfkiot.myshopadmin.utill.AppUtil.fetchProductNameById
import androidx.compose.foundation.lazy.items


@Composable
fun FullScreenModal(
    user: UserModel,
    title: String,
    items: Map<String, Long>? = null,
    favItems: List<String>? = null,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.76f),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header
                Text(
                    text = "${user.name}'s $title",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                Spacer(Modifier.height(8.dp))

                // Scrollable content (takes up remaining space)
                Box(modifier = Modifier.weight(1f, fill = true)) {
                    when {
                        items != null -> { // Cart
                            if (items.isEmpty()) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No items in cart.", color = Color.Gray, fontSize = 16.sp)
                                }
                            } else {
                                LazyColumn {
                                    items(items.toList()) { (itemId, quantity) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            ProductName(itemId)
                                            Surface(
                                                color = MaterialTheme.colorScheme.secondaryContainer,
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.padding(start = 12.dp)
                                            ) {
                                                Text(
                                                    "Qty: $quantity",
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                    fontWeight = FontWeight.Medium,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                        Divider()
                                    }
                                }
                            }
                        }

                        favItems != null -> { // Favourites
                            if (favItems.isEmpty()) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No favourite items.", color = Color.Gray, fontSize = 16.sp)
                                }
                            } else {
                                LazyColumn {
                                    items(favItems) { productId ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            ProductName(productId)
                                        }
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Fixed Bottom Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text("Close", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                }
            }
        }
    }
}


@Composable
fun ProductName(productId: String) {
    var productName by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(productId) {
        loading = true
        productName = fetchProductNameById(productId)
        loading = false
    }

    Text(
        when {
            loading -> "Loading..."
            !productName.isNullOrEmpty() -> productName!!
            else -> productId // fallback
        },
        fontWeight = FontWeight.Medium
    )
}

