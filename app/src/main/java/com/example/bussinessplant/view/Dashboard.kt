package com.example.bussinessplant.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bussinessplant.model.PitchModel
import com.example.bussinessplant.repository.PitchRepoImpl
import com.example.bussinessplant.ui.theme.Green
import com.example.bussinessplant.ui.theme.White
import com.example.bussinessplant.viewmodel.PitchViewModel
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody() {
    val context = LocalContext.current
    val pitchViewModel = remember { PitchViewModel(PitchRepoImpl()) }
    val pitches by pitchViewModel.pitches.observeAsState(initial = emptyList<PitchModel>())
    val loading by pitchViewModel.loading.observeAsState(initial = false)
    
    var showDialog by remember { mutableStateOf(false) }
    var selectedPitch by remember { mutableStateOf<PitchModel?>(null) }

    LaunchedEffect(Unit) {
        pitchViewModel.getAllPitches()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Startup Pitches", color = White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Green)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    selectedPitch = null
                    showDialog = true 
                },
                containerColor = Green,
                contentColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Pitch")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Green)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pitches ?: emptyList<PitchModel>()) { pitch ->
                        PitchCard(
                            pitch = pitch,
                            onEdit = {
                                selectedPitch = pitch
                                showDialog = true
                            },
                            onDelete = {
                                pitchViewModel.deletePitch(pitch.id) { success, message ->
                                    if (success) {
                                        // Optional: additional logic on success
                                    }
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AddEditPitchDialog(
                pitch = selectedPitch,
                onDismiss = { showDialog = false },
                onSave = { name, desc ->
                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    if (selectedPitch == null) {
                        pitchViewModel.addPitch(PitchModel(startupName = name, description = desc, userId = currentUserId)) { success, message ->
                            if (success) {
                                // Optional: additional logic on success
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val updates = mapOf("startupName" to name, "description" to desc)
                        pitchViewModel.updatePitch(selectedPitch!!.id, updates) { success, message ->
                            if (success) {
                                // Optional: additional logic on success
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun PitchCard(pitch: PitchModel, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = pitch.startupName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Green)
                Row {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray) }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red) }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = pitch.description, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPitchDialog(pitch: PitchModel?, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var name by remember { mutableStateOf(pitch?.startupName ?: "") }
    var desc by remember { mutableStateOf(pitch?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (pitch == null) "Add New Pitch" else "Edit Pitch") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Startup Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank() && desc.isNotBlank()) onSave(name, desc) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
