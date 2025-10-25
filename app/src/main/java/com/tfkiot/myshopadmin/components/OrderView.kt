package com.tfkiot.myshopadmin.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import com.tfkiot.myshopadmin.model.OrderModel
import com.tfkiot.myshopadmin.model.UserModel
import com.tfkiot.myshopadmin.utill.AppUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderView(orderItem: OrderModel,
              modifier: Modifier= Modifier,
              user : UserModel? = null,
              statusList: List<String>,
              onStatusChange: (String, String) -> Unit,
              onClick: () -> Unit ){

    var statusExpanded by remember { mutableStateOf(false) }
   // var selectedStatus by remember { mutableStateOf(orderItem.status) }
    var selectedStatus by remember(orderItem.status) { mutableStateOf(orderItem.status) }

    Card (
        modifier = modifier.padding(8.dp).fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text("Order ID: " + orderItem.id, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))

            Text(AppUtil.formatdate(orderItem.date), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))

            Text("User: ${user?.name ?: orderItem.userId} (${user?.email ?: ""})")
            Spacer(modifier = Modifier.height(4.dp))

            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                OutlinedTextField(
                    value = selectedStatus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Order Status") },
                    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                DropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }

                ) {
                    statusList.forEach { statusOpt ->
                        DropdownMenuItem(
                            text = { Text(statusOpt) },
                            onClick = {
                                selectedStatus = statusOpt

                                onStatusChange(orderItem.id, statusOpt)

                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Text(
                    orderItem.items.size.toString() + " Items",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
        }
    }
}