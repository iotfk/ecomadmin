package com.tfkiot.myshopadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tfkiot.myshopadmin.appnav.AppNav
import com.tfkiot.myshopadmin.pages.HomePage
import com.tfkiot.myshopadmin.ui.theme.MyShopAdminTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyShopAdminTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(vertical = 20.dp)
                    ) {
                        AppNav(modifier = Modifier.padding(vertical = 16.dp))
                    }
                }

//                HomePage(modifier = Modifier.fillMaxSize().padding(4.dp), onSubmit ={
//                    product, isAddingNew ->
//                    // Handle Firestore write logic here
//
//                    val db = Firebase.firestore
//                    val productRef = db.collection("data").document("stocks").collection("products")
//                        if (isAddingNew){
//                            productRef.document(product.id).set(product)
//                        } else {
//                            productRef.document(product.id).set(product)
//                        }
//                } )
            }
        }
    }
}



