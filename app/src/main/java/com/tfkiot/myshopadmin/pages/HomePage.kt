package com.tfkiot.myshopadmin.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
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
import com.tfkiot.myshopadmin.model.ProductModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

fun generateFirestoreId(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val id = StringBuilder()
    repeat(20) {
        id.append(chars.random())
    }
    return id.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onSubmit: (ProductModel, Boolean) -> Unit
) {
    var productList by remember { mutableStateOf(listOf<ProductModel>()) }
    var selectedCategory by remember { mutableStateOf("") }
    var categoryList by remember { mutableStateOf(listOf<String>()) }

    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) }
    var id by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var actualPrice by remember { mutableStateOf("") }
    var images by remember { mutableStateOf(listOf("")) }
    var specification by remember { mutableStateOf(mapOf<String, String>()) }
    var isAddingNew by remember { mutableStateOf(true) }

    // Firebase fetch
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("data").document("stocks").collection("products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.documents.mapNotNull{it.toObject(ProductModel::class.java)}
                productList = products
                categoryList = products.map { it.category }.distinct().filter { it.isNotBlank()}
                if (categoryList.isNotEmpty() && selectedCategory.isEmpty())
                    selectedCategory = categoryList.first()
            }
    }

    val filteredProducts = if (selectedCategory.isBlank()) productList else productList.filter{
        it.category == selectedCategory
    }

    var categoryExpanded by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.padding(1.dp).fillMaxSize()) {

        item {
            // Category filter
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Category: ", fontWeight = FontWeight.Bold)
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Filter by Category") },
                        trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
                        modifier = Modifier.menuAnchor().weight(1f)
                    )
                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categoryList.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedCategory = option
                                    categoryExpanded = false
                                    selectedProduct = null
                                    isAddingNew = true
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    selectedProduct = null
                    isAddingNew = true
                    id = ""; title = ""; description = ""; price = ""; actualPrice = ""
                    images = listOf("")
                    specification = mapOf()
                }) {
                    Text("Add Product")
                }
            }

            Spacer(Modifier.height(6.dp))
            Text("Products", fontWeight = FontWeight.Bold)
        }

        items(filteredProducts) { product ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedProduct = product
                        isAddingNew = false
                        id = product.id
                        title = product.title
                        description = product.description
                        price = product.price
                        actualPrice = product.actualPrice
                        images = product.images.ifEmpty { listOf("") }
                        specification = product.specification
                        selectedCategory = product.category
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(product.title, Modifier.weight(1f))
                Text(product.category, Modifier.weight(1f))
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            Text(if (isAddingNew) "Add Product" else "Edit Product", fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("Product ID") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { id = generateFirestoreId() },
                    enabled = isAddingNew
                ) {
                    Text("ID")
                }
            }

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = actualPrice, onValueChange = { actualPrice = it }, label = { Text("Actual Price") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))

            // Category selector for product
            var categoryEditExpanded by remember { mutableStateOf(false) }
            Text("Category: ", fontWeight = FontWeight.Bold)
            ExposedDropdownMenuBox(
                expanded = categoryEditExpanded,
                onExpandedChange = { categoryEditExpanded = !categoryEditExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Category") },
                    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                DropdownMenu(expanded = categoryEditExpanded, onDismissRequest = { categoryEditExpanded = false }) {
                    categoryList.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedCategory = option
                                categoryEditExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Images:", fontWeight = FontWeight.Bold)
            images.forEachIndexed { idx, img ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = img,
                        onValueChange = {
                            images = images.toMutableList().also { list -> list[idx] = it }
                        },
                        label = { Text("Image ${idx + 1} URL") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { images = images.toMutableList().also { it.removeAt(idx) } },
                        enabled = images.size > 1
                    ) { Text("Remove") }
                }
            }
            Button(onClick = { images = images.toMutableList().also { it.add("") } }) {
                Text("Add Image")
            }

            Spacer(Modifier.height(16.dp))
            Text("Specifications:", fontWeight = FontWeight.Bold)
            specification.forEach { (key, value) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = key,
                        onValueChange = { newKey ->
                            val temp = specification.toMutableMap()
                            val oldVal = temp[key] ?: ""
                            temp.remove(key)
                            temp[newKey] = oldVal
                            specification = temp
                        },
                        label = { Text("Key") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newVal ->
                            specification = specification.toMutableMap().apply { put(key, newVal) }
                        },
                        label = { Text("Value") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            specification = specification.toMutableMap().apply { remove(key) }
                        },
                        enabled = specification.size > 1
                    ) { Text("Remove") }
                }
            }

            Button(onClick = {
                specification = specification.toMutableMap().apply { put("", "") }
            }) { Text("Add Spec") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val newProduct = ProductModel(
                        id = id,
                        title = title,
                        description = description,
                        price = price,
                        actualPrice = actualPrice,
                        category = selectedCategory,
                        images = images,
                        specification = specification
                    )
                    onSubmit(newProduct, isAddingNew)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isAddingNew) "Add Product" else "Update Product")
            }
        } //
    }
}
