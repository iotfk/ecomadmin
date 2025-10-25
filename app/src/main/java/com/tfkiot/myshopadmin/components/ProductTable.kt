package com.tfkiot.myshopadmin.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.tfkiot.myshopadmin.model.ProductModel

@Composable
fun ProductTable(
    products: List<ProductModel>,
    selectedProductId: String?,
    onRowClick: (ProductModel) -> Unit,
    onDeleteClick: (ProductModel) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }

    Column(Modifier.fillMaxWidth().padding(8.dp)) {
        // Table header
        val actionColWidth = 64.dp

        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Title", Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("Category", Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier.width(actionColWidth),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Action",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }

        products.forEach { product ->
            val isSelected = product.id == selectedProductId

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.LightGray))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                    )
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .clickable { onRowClick(product) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(product.title, Modifier.weight(1f))
                Text(product.category, Modifier.weight(1f))
                Box(
                    modifier = Modifier.width(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            productToDelete = product
                            showDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }

ConfirmDialog(
    visible = showDialog,
    title = "Confirm Delete",
    message = "Are you sure you want to delete \"${productToDelete?.title}\"?",
    onConfirm = {
        if (productToDelete != null) onDeleteClick(productToDelete!!)
        showDialog = false
    },
    onDismiss = { showDialog = false }
)

}
