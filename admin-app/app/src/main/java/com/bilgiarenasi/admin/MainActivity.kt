package com.bilgiarenasi.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class PendingQuestion(
    val id: String,
    val text: String,
    val category: String,
    val author: String,
    val duplicateScore: Double = 0.0
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AdminApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminApp() {
    var tab by remember { mutableIntStateOf(0) }
    val queue = remember {
        listOf(
            PendingQuestion("Q-1001", "Osmanlı Devleti'nin ilk başkenti neresidir?", "Tarih", "Oyuncu #4812", 1.0),
            PendingQuestion("Q-1002", "DNA'nın açılımı nedir?", "Bilim", "Oyuncu #1830"),
            PendingQuestion("Q-1003", "Dünyanın en büyük okyanusu hangisidir?", "Coğrafya", "Oyuncu #9921", 0.94)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bilgi Arenası Admin") },
                navigationIcon = { Icon(Icons.Default.Security, contentDescription = "Güvenlik") }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = tab) {
                Tab(tab == 0, { tab = 0 }, text = { Text("Onay Kuyruğu") })
                Tab(tab == 1, { tab = 1 }, text = { Text("Sistem") })
            }
            if (tab == 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(queue, key = { it.id }) { item ->
                        Card(colors = CardDefaults.cardColors()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(item.id, style = MaterialTheme.typography.labelSmall)
                                Text(item.text, style = MaterialTheme.typography.titleMedium)
                                Text("${item.category} • ${item.author}")
                                if (item.duplicateScore >= 0.92) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(Icons.Default.Flag, contentDescription = null)
                                        Text("Benzer soru adayı: %${(item.duplicateScore * 100).toInt()}")
                                    }
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(onClick = { /* production: /admin/v1/.../approve */ }) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                                        Text(" Onayla")
                                    }
                                    Button(onClick = { /* production: reject */ }) {
                                        Icon(Icons.Default.Delete, contentDescription = null)
                                        Text(" Reddet")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Yönetim Alanı", style = MaterialTheme.typography.headlineSmall)
                    Text("Oyuncu uygulaması bu uygulamaya veya private admin API'ye bağlanmaz.")
                    Text("Production'da MFA + rol tabanlı yetkilendirme + audit log zorunludur.")
                }
            }
        }
    }
}
