package com.tfkiot.myshopadmin.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tfkiot.myshopadmin.components.OrderView
import com.tfkiot.myshopadmin.model.OrderModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tfkiot.myshopadmin.model.UserModel
import kotlinx.coroutines.tasks.await
import kotlin.text.equals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Orders(modifier: Modifier = Modifier,  selectedUser: UserModel? = null,
           clearUserFilter: () -> Unit = {}    ) {
    val allOrderList = remember { mutableStateOf<List<OrderModel>>(emptyList()) }
    val userMap = remember { mutableStateOf<Map<String, UserModel>>(emptyMap()) }
    val statusList = listOf("Pending", "Processing", "Shipped", "Delivered", "Cancelled")
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var statusFilterExpanded by remember { mutableStateOf(false) }
    var sortDescending by remember { mutableStateOf(true) }

    // FETCH ORDERS AND USERS
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        val orderDocs = db.collection("orders").get().await()
        val orders = orderDocs.documents.mapNotNull { it.toObject(OrderModel::class.java) }
        allOrderList.value = orders

        val userIds = orders.map { it.userId }.distinct()
        val userMapTemp = mutableMapOf<String, UserModel>()
        userIds.forEach { uid ->
            val userDoc = db.collection("users").document(uid).get().await()
            userDoc.toObject(UserModel::class.java)?.let { user ->
                userMapTemp[user.userId] = user
            }
        }
        userMap.value = userMapTemp
    }


    // 1️⃣ Filter by status
    val statusFilteredList = if (selectedStatus.isNullOrBlank()) {
        allOrderList.value
    } else {
        allOrderList.value.filter { it.status.equals(selectedStatus, ignoreCase = true) }
    }


    // 2️⃣ Filter by selected user (if provided)
    val userFilteredList = if (selectedUser != null) {
        statusFilteredList.filter { it.userId == selectedUser.userId }
    } else {
        statusFilteredList
    }

    fun toMillis(date: Any?): Long {
        return when (date) {
            is com.google.firebase.Timestamp -> date.toDate().time
            is java.util.Date -> date.time
            is Long -> date
            else -> 0L
        }
    }

    // 4️⃣ Sort the filtered list
    val orderList = if (sortDescending) {
        userFilteredList.sortedByDescending { toMillis(it.date) }
    } else {
        userFilteredList.sortedBy { toMillis(it.date) }
    }

    // 🧱 MAIN UI
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        if (selectedUser != null) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Orders for ${selectedUser.name}",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                )
                TextButton(onClick = {clearUserFilter()}) {
                    Text("View All")
                }
            }
        }


        // 🔹 Filter Row (Status Dropdown + Sort Toggle)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            // Status Filter Dropdown
            ExposedDropdownMenuBox(
                expanded = statusFilterExpanded,
                onExpandedChange = { statusFilterExpanded = !statusFilterExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedStatus ?: "All",
                    textStyle = TextStyle(fontSize = 13.sp),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter by Status") },
                    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
                    modifier = Modifier.menuAnchor().fillMaxWidth().height(56.dp),
                    singleLine = true
                )
                DropdownMenu(
                    expanded = statusFilterExpanded,
                    onDismissRequest = { statusFilterExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            selectedStatus = null
                            statusFilterExpanded = false
                        }
                    )
                    statusList.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status) },
                            onClick = {
                                selectedStatus = status
                                statusFilterExpanded = false
                            }
                        )
                    }
                }
            }

            // 🕒 Time Sort Toggle (Recent/Oldest)
            Surface(
                modifier = Modifier
                    .clickable { sortDescending = !sortDescending }
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 2.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = if (sortDescending) Icons.Default.KeyboardArrowDown
                        else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Sort Order",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (sortDescending) "Recent" else "Oldest",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }



    Spacer(Modifier.height(12.dp))

        // 🧾 Orders List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orderList, key = { it.id }) { order ->
                val user = userMap.value[order.userId]
                OrderView(
                    orderItem = order,
                    user = user,
                    statusList = statusList,
                    onStatusChange = { orderId, newStatus ->
                        Firebase.firestore.collection("orders")
                            .document(orderId)
                            .update("status", newStatus)
                            .addOnSuccessListener {
                                allOrderList.value = allOrderList.value.map {
                                    if (it.id == orderId) it.copy(status = newStatus) else it
                                }
                            }
                    },
                 onClick = {}
                )
            }
        }
    }
}
