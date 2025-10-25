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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.tfkiot.myshopadmin.model.CategoryModel
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button

@Composable
fun CategoryTable(
    categories: List<CategoryModel>,
    selectedCategoryId: String?,
    onRowClick: (CategoryModel) -> Unit,
    onDeleteClick: (CategoryModel) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<CategoryModel?>(null) }
    val actionColWidth = 64.dp

    Column(Modifier.fillMaxWidth().padding(8.dp)) {
        // Table header
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Name", Modifier.weight(1f), fontWeight = FontWeight.Bold)
           // Text("Image", Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier.width(actionColWidth),
                contentAlignment = Alignment.Center
            ) {
                Text("Action", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)
            }
        }

        categories.forEach { category ->
            val isSelected = category.id == selectedCategoryId
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.LightGray))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                    )
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .clickable{ onRowClick(category) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(category.name, Modifier.weight(1f))
               // Text(category.imageUrl, Modifier.weight(1f)) // use Coil/Image for preview if needed
                Box(
                    modifier = Modifier.width(actionColWidth),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        categoryToDelete = category
                        showDialog = true
                    }) {
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

    // Confirmation popup
    if (showDialog && categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete \"${categoryToDelete!!.name}\"?") },
            confirmButton = {
                Button(onClick = {
                    onDeleteClick(categoryToDelete!!)
                    showDialog = false
                }) { Text("Delete") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}
