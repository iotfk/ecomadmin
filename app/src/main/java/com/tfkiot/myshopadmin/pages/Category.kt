package com.tfkiot.myshopadmin.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tfkiot.myshopadmin.components.CategoryDropDown
import com.tfkiot.myshopadmin.components.CategoryTable
import com.tfkiot.myshopadmin.model.CategoryModel
import com.tfkiot.myshopadmin.utill.AppUtil


@Composable
fun Category(modifier: Modifier = Modifier){

    var categoryList by remember { mutableStateOf(listOf<CategoryModel>()) }
    var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isAddingNew by remember { mutableStateOf(true) }

    val context = LocalContext.current

    // Firebase fetch
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("data").document("stocks").collection("categories")
            .get()
            .addOnSuccessListener { result ->
                val categories = result.documents.mapNotNull { it.toObject(CategoryModel::class.java) }
                categoryList = categories
              if (categories.isNotEmpty() && selectedCategory == null){
                    selectedCategory = categories.first()
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error fetching products", it)
            }
    }

    /// show the incoming data here in text composible

        Column (modifier = modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.Start
        //    .fillMaxSize()
        ) {
            Text("Category", style = MaterialTheme.typography.titleLarge )
          //  Text("ID")
            //categorylist Table


            CategoryDropDown(
                categories = categoryList,
                selectedCategory = selectedCategory,
                onCategorySelect = { category ->
                    selectedCategory = category
                    isAddingNew = false
                    id = category.id
                    name = category.name
                    imageUrl = category.imageUrl
                },
                onDeleteClick = { category ->


                    deleteCategory(
                        category, context ){
                        categoryList = categoryList.filter { it.id != category.id }
                    }


//                    deleteCategory(
//                        category = category,
//                        onSuccess = {
//                            categoryList = categoryList.filter { it.id != category.id }
//                             if (selectedCategory?.id == category.id){
//                                 selectedCategory = null
//                                 id = ""
//                                 name = ""
//                                 imageUrl = ""
//                                 isAddingNew = true
//                             }
//                        }
//                    )
                }
            )


            Spacer(Modifier.height(16.dp))

            Text(if (isAddingNew) "Add Category" else "Edit Category", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = id, enabled = false, onValueChange = { id = it }, label = { Text("ID") })

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    id = it.lowercase().replace(" ", "-")
                 },
                label = { Text("Name") })

            OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Image URL") })

            Button(
                onClick = {

                    val newCategory = CategoryModel(
                        id = id,
                        name = name,
                        imageUrl = imageUrl
                    )

                    addcat(newCategory, context, isAddingNew)

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isAddingNew) "Add Category" else "Update Category")
            }

        }
        }


fun addcat(newCategory: CategoryModel, context: Context, isAddingNew: Boolean  ){
    val  db = Firebase.firestore
    val categoryRef = db.collection("data").document("stocks").collection("categories")

    if (isAddingNew){
        categoryRef.document(newCategory.id).set(newCategory)
            .addOnSuccessListener {
                AppUtil.showToast(context, "✅Successfully added: ${newCategory.name}")
            }
            .addOnFailureListener {
                AppUtil.showToast(context, "❌Failed to add category, ${it}")
            }
    } else {
        categoryRef.document(newCategory.id).set(newCategory)
            .addOnSuccessListener {
                AppUtil.showToast(context, "✅Successfully updated: ${newCategory.name}")
            }
            .addOnFailureListener {
                AppUtil.showToast(context, "❌Failed to update category, ${it}")
            }
    }
}


fun deleteCategory(
    category: CategoryModel, context: Context, onDeleted: () -> Unit
) {
    val db = Firebase.firestore
    val categoryRef = db.collection("data").document("stocks").collection("categories")
    categoryRef.document(category.id).delete()
        .addOnSuccessListener {
            AppUtil.showToast(context, "✅Category deleted: ${category.name}")
           onDeleted()
        }
        .addOnFailureListener {
            AppUtil.showToast(context, "❌Failed to delete Category: ${it}")
        }

}

