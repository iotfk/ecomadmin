package com.tfkiot.myshopadmin.utill

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

object AppUtil {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun formatdate(timestamp: Timestamp) : String{
        val sdf =  SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(timestamp.toDate().time)

    }

    suspend fun fetchProductNameById(productId: String): String? {
        val db = Firebase.firestore
        val doc = db.collection("data")
            .document("stocks")
            .collection("products")
            .document(productId)
            .get()
            .await()
        return doc.getString("title")
    }

}