package com.tfkiot.myshopadmin.appnav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import com.tfkiot.myshopadmin.components.FullScreenModal
import com.tfkiot.myshopadmin.dataclass.TabItems
import com.tfkiot.myshopadmin.model.OrderModel
import com.tfkiot.myshopadmin.model.UserModel
import com.tfkiot.myshopadmin.pages.Category
import com.tfkiot.myshopadmin.pages.Orders
import com.tfkiot.myshopadmin.pages.Product
import com.tfkiot.myshopadmin.pages.Users
import kotlinx.coroutines.tasks.await

@Composable
fun AppNav(modifier: Modifier = Modifier){

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedUser by remember { mutableStateOf<UserModel?>(null) }
    var cartModalUser by remember { mutableStateOf<UserModel?>(null) }
    var favModalUser by remember { mutableStateOf<UserModel?>(null) }
    val userOrderCounts = remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        val orderDocs = db.collection("orders").get().await()
        val counts = orderDocs.documents.mapNotNull { it.toObject(OrderModel::class.java) }
            .groupingBy { it.userId }
            .eachCount()
        userOrderCounts.value = counts
    }

    val tabItems = listOf(
        TabItems(
            title = "Product",
            unselectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home
        ),
        TabItems(
            title = "Category",
            unselectedIcon = Icons.Outlined.ShoppingCart,
            selectedIcon = Icons.Filled.ShoppingCart
        ),
        TabItems(
            title = "Orders",
            unselectedIcon = Icons.Outlined.Menu,
            selectedIcon = Icons.Filled.Menu
        ),

        TabItems(
            title = "Users",
            unselectedIcon = Icons.Outlined.AccountCircle,
            selectedIcon = Icons.Filled.AccountCircle
        ),
    )
    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress){
        if (!pagerState.isScrollInProgress){
            selectedTabIndex = pagerState.currentPage
        }
    }

    Column(
        Modifier.fillMaxSize(),
    ) {
       TabRow(selectedTabIndex = selectedTabIndex ) {
             tabItems.forEachIndexed { index, item ->
                 Tab(
                     selected = index == selectedTabIndex,
                     onClick = {
                         selectedTabIndex = index
                     },
                     text = {
                         Text(text = item.title)
                     },
                     icon = {
                         Icon(
                             imageVector = if (index == selectedTabIndex) {
                                 item.selectedIcon
                             } else {
                                 item.unselectedIcon
                             },
                             contentDescription = item.title
                         )
                     }
                 )
             }
         }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            when(index){
                0 -> Product()
                1 -> Category()
                2 -> Orders(
                    selectedUser = selectedUser,
                    clearUserFilter = { selectedUser = null }
                )
                3 -> Users(
                    userOrderCounts = userOrderCounts.value,
                    onOrderClick = { user ->
                        selectedUser = user
                        selectedTabIndex = 2
                    },
                    onCartClick = { user ->
                        cartModalUser = user
                    },
                    onFavClick = { user ->
                        favModalUser = user
                    }
                )
            }
        }

        if (cartModalUser != null) {
            FullScreenModal(
                user = cartModalUser!!,
                title = "Cart",
                items = cartModalUser!!.cartItems,
                favItems = null,
                onDismiss = { cartModalUser = null }
            )
        }

        if (favModalUser != null) {
            FullScreenModal(
                user = favModalUser!!,
                title = "Favourites",
                items = null,
                favItems = favModalUser!!.favoriteItems,
                onDismiss = { favModalUser = null }
            )
        }





    }
}