package com.example.passwordmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.passwordmanager.data.PasswordDatabase
import com.example.passwordmanager.data.PasswordEntry
import com.example.passwordmanager.util.EncryptionUtil

@Composable
fun AddPasswordScreen(navController: NavHostController) {
    var label by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val encrypted = EncryptionUtil.encrypt(password)
            if (encrypted != null) {
                val entry = PasswordEntry(0, label, encrypted)
                PasswordDatabase.getInstance().passwordDao().insert(entry)
                navController.popBackStack()
            }
        }) {
            Text("Save")
        }
    }
}