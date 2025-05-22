package com.example.passwordmanager.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.passwordmanager.SessionManager
import com.example.passwordmanager.util.EncryptionUtil

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Master Password", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Master Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (EncryptionUtil.tryUnlock(password)) {
                SessionManager.unlock()
                navController.navigate("list") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Invalid password", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Unlock")
        }
    }
}