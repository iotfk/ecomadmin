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



//
//@Composable
//fun UserCard(
//    user: UserModel,
//    isExpanded: Boolean,
//    onClick: () -> Unit,
//    onOutsideClick: () -> Unit,
//    onOrderClick: (UserModel) -> Unit
//) {
//    var showFavModal by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    ) {
//        // 🟦 Main Card
//        Card(
//            shape = RoundedCornerShape(12.dp),
//            elevation = CardDefaults.cardElevation(4.dp),
//            modifier = Modifier.fillMaxWidth().clickable { onClick() }
//        ) {
//            Column(Modifier.padding(16.dp)) {
//                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                Spacer(Modifier.height(4.dp))
//                Text(user.email, fontSize = 14.sp, color = Color.Gray)
//                Spacer(Modifier.height(4.dp))
//                Text("User ID: ${user.userId}", fontSize = 12.sp, color = Color.Gray)
//                Spacer(Modifier.height(8.dp))
//
//                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//
//                    Surface(
//                        color = MaterialTheme.colorScheme.primaryContainer,
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Row(
//                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(Icons.Default.Menu , null, Modifier.size(16.dp))
//                            Spacer(Modifier.width(4.dp))
//                            Text("${user.cartItems.size}", fontSize = 12.sp)
//                        }
//                    }
//
//
//
//                    Surface(
//                        color = MaterialTheme.colorScheme.primaryContainer,
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Row(
//                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp).clickable { onOrderClick(user) },
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(Icons.Default.ShoppingCart, null, Modifier.size(16.dp))
//                            Spacer(Modifier.width(4.dp))
//                            Text("${user.cartItems.size}", fontSize = 12.sp)
//                        }
//                    }
//
//                    Surface(
//                        color = MaterialTheme.colorScheme.secondaryContainer,
//                        shape = RoundedCornerShape(16.dp)
//                    ) {
//                        Row(
//                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(Icons.Default.Favorite, null, Modifier.size(16.dp))
//                            Spacer(Modifier.width(4.dp))
//                            Text("${user.favoriteItems.size}", fontSize = 12.sp)
//                        }
//                    }
//                }
//
//                Spacer(Modifier.height(8.dp))
//                Text(
//                    "Address: ${user.address}",
//                    fontSize = 13.sp,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        }
//
//        // 🟦 Expanded Options
////        AnimatedVisibility(
////            visible = isExpanded,
////            enter = fadeIn(),
////            exit = fadeOut()
////        ) {
////            Box(
////                Modifier
////                    .matchParentSize()
////                    .background(Color.Black.copy(alpha = 0.3f))
////                    .clickable { onOutsideClick() } // dismiss on outside click
////            )
////
////            Column(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(vertical = 25.dp),
////                horizontalAlignment = Alignment.CenterHorizontally,
////                verticalArrangement = Arrangement.Center
////            ) {
////                Row(
////                    modifier = Modifier.wrapContentWidth().padding(vertical = 8.dp),
////                    horizontalArrangement = Arrangement.spacedBy(16.dp),
////                    verticalAlignment = Alignment.CenterVertically
////                ) {
////                    TransparentButton("Orders") { onOrderClick(user) }
////                    TransparentButton("Cart") { /* TODO: handle cart */ }
////                    TransparentButton("Fav") { showFavModal = true }
////                }
////            }
////        }
//
////        // 🟦 Transparent Modal for Favorites
////        if (showFavModal) {
////            TransparentModal(isVisible = mutableStateOf(showFavModal)) {
////                Column(horizontalAlignment = Alignment.CenterHorizontally) {
////                    Text("Favorite Items of ${user.name}", color = Color.White, fontWeight = FontWeight.Bold)
////                    Spacer(Modifier.height(16.dp))
////                    user.favoriteItems.forEach { item ->
////                        Text("- $item", color = Color.White)
////                    }
////                }
////            }
////        }
//    }
//}
//
//@Composable
//fun TransparentButton(text: String, onClick: () -> Unit) {
//    Button(
//        onClick = onClick,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.White.copy(alpha = 0.6f),
//            contentColor = Color.Black
//        ),
//        shape = RoundedCornerShape(20.dp)
//    ) {
//        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
//    }
//}