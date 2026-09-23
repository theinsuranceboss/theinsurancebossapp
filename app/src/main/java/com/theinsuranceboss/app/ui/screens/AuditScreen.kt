package com.theinsuranceboss.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.rememberAsyncImagePainter
import com.theinsuranceboss.app.ui.components.BossPrimaryButton
import com.theinsuranceboss.app.ui.components.DisclosureText
import com.theinsuranceboss.app.ui.components.SectionLabel
import com.theinsuranceboss.app.ui.theme.BossBorder
import com.theinsuranceboss.app.ui.theme.BossGold
import com.theinsuranceboss.app.ui.theme.BossMuted
import com.theinsuranceboss.app.ui.theme.BossSurface

@Composable
fun AuditScreen(
    loading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSubmit: (name: String, email: String, phone: String?, notes: String?, photos: List<android.net.Uri>) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var photos by remember { mutableStateOf(listOf<android.net.Uri>()) }
    var localError by remember { mutableStateOf<String?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris -> photos = (photos + uris).distinct().take(8) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = BossGold)
            }
            SectionLabel("Free Policy Audit")
        }

        Spacer(Modifier.height(8.dp))
        Text(
            "Upload your declarations page",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "A licensed agent will review coverage gaps. Photos stay in secure storage.",
            color = BossMuted,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
            photoPicker.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BossSurface),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = BossGold)
                Spacer(Modifier.width(12.dp))
                Text(
                    if (photos.isEmpty()) "Add photos (camera roll or camera)" else "${photos.size} photo(s) attached",
                    color = if (photos.isEmpty()) BossMuted else BossGold,
                )
            }
        }

        if (photos.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                photos.take(4).forEach { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(10.dp)),
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full name *", color = BossMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = auditColors(),
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email *", color = BossMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = auditColors(),
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone", color = BossMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = auditColors(),
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (optional)", color = BossMuted) },
            modifier = Modifier.fillMaxWidth()
                .height(100.dp),
            colors = auditColors(),
        )

        (localError ?: error)?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(20.dp))
        DisclosureText("Source tag: android_app_audit")
        Spacer(Modifier.height(16.dp))
        BossPrimaryButton(
            text = "Submit Free Audit",
            loading = loading,
            onClick = {
                localError = null
                when {
                    photos.isEmpty() -> localError = "Add at least one policy photo"
                    name.trim().length < 2 -> localError = "Enter your name"
                    !email.contains("@") -> localError = "Enter a valid email"
                    else -> onSubmit(name.trim(), email.trim(), phone.ifBlank { null }, notes.ifBlank { null }, photos)
                }
            },
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun auditColors() = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedBorderColor = BossGold,
    unfocusedBorderColor = BossBorder,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = BossGold,
    unfocusedLabelColor = BossMuted,
    cursorColor = BossGold,
    focusedContainerColor = BossSurface,
    unfocusedContainerColor = BossSurface,
)
