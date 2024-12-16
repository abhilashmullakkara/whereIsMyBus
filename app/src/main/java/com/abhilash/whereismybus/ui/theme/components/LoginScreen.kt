package com.abhilash.whereismybus.ui.theme.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen() {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val isAdmin = user != null // Check if user is authenticated
    var currentUser by remember { mutableStateOf(auth.currentUser) } // Track login state
    if (user == null) {
        // If no user is logged in, show login screen
        LoginScreen(onLoginSuccess = { userId ->
            // Handle successful login (you can store userId for admin access)
            currentUser = auth.currentUser
        })
    } else {
        // Display map and allow data update only if logged in as admin
        MMapScreen(isAdmin = isAdmin, userId = user.uid)
    }
}

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                onLoginSuccess(it.uid) // Pass the user ID after login
                            }
                        } else {
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Login")
        }
    }
}

