package com.tfkiot.myshopadmin.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfkiot.myshopadmin.model.UserModel




@Composable
fun UserCard(
    user: UserModel,
    orderCount: Int,
    onOrderClick: (UserModel) -> Unit,
    onCartClick: (UserModel) -> Unit,
    onFavClick: (UserModel) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(4.dp))
            Text(user.email, fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(4.dp))
            Text("User ID: ${user.userId}", fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(8.dp))

            // Feature Badges
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Orders badge
                FeatureBadge(
                    icon = Icons.Default.Menu,
                    label = orderCount.toString(),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    onClick = { onOrderClick(user) }
                )
                // Cart badge
                FeatureBadge(
                    icon = Icons.Default.ShoppingCart,
                    label = user.cartItems.size.toString(),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = { onCartClick(user) }
                )
                // Favorites badge
                FeatureBadge(
                    icon = Icons.Default.Favorite,
                    label = user.favoriteItems.size.toString(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    onClick = { onFavClick(user) }
                )
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Address: ${user.address}",
                fontSize = 13.sp,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

// Generic badge composable for icon + count
@Composable
fun FeatureBadge(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Surface(
        color = color,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}



