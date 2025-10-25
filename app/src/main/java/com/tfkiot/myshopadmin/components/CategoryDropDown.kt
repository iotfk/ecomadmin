package com.tfkiot.myshopadmin.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import com.tfkiot.myshopadmin.model.CategoryModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tfkiot.myshopadmin.model.ProductModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    categories: List<CategoryModel>,
    selectedCategory: CategoryModel?,
    onCategorySelect: (CategoryModel) -> Unit,
    onDeleteClick: (CategoryModel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedName by remember { mutableStateOf(selectedCategory?.name ?: "") }

    var showDialog by remember { mutableStateOf(false) }
    var catToDelete by remember { mutableStateOf<CategoryModel?>(null) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Category") },
            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(category.name, Modifier.weight(1f))
                            IconButton(onClick = {
                                catToDelete = category
                                showDialog = true
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete Category",
                                    tint = Color.Red
                                )
                            }
                        }
                    },
                    onClick = {
                        selectedName = category.name
                        onCategorySelect(category)
                        expanded = false
                    }
                )
            }
        }
    }

    ConfirmDialog(
        visible = showDialog,
        title = "Confirm Delete",
        message = "Are you sure you want to delete \"${catToDelete?.name}\"?",
        onConfirm = {
            if (catToDelete != null) onDeleteClick(catToDelete!!)
            showDialog = false
        },
        onDismiss = { showDialog = false }
    )
}
