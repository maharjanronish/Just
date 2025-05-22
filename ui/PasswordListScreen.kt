package com.example.passwordmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.passwordmanager.data.PasswordDatabase
import com.example.passwordmanager.data.PasswordEntry
import com.example.passwordmanager.util.EncryptionUtil

@Composable
fun PasswordListScreen(navController: NavHostController) {
    val dao = PasswordDatabase.getInstance().passwordDao()
    val passwords by dao.getAll().collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add") }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(passwords) { entry ->
                ListItem(entry)
            }
        }
    }
}

@Composable
fun ListItem(entry: PasswordEntry) {
    val decrypted = remember { EncryptionUtil.decrypt(entry.encryptedPassword) ?: "Error" }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp)
    ) {
        Text(text = entry.label, style = MaterialTheme.typography.titleMedium)
        Text(text = decrypted, style = MaterialTheme.typography.bodyMedium)
    }
}