package com.example.bussinessplant.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    val pitches by pitchViewModel.pitches.observeAsState(initial = emptyList())
    val loading by pitchViewModel.loading.observeAsState(initial = false)
    
    var showDialog by remember { mutableStateOf(false) }
    var selectedPitch by remember { mutableStateOf<PitchModel?>(null) }

    LaunchedEffect(Unit) {
        pitchViewModel.getAllPitches()
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "My Startup Pitches", 
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ) 
                },
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
                contentColor = White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Pitch")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Green)
            } else if (pitches.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No pitches yet",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Gray
                    )
                    Text(
                        "Start your journey by adding your first startup idea!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
                ) {
                    items(pitches ?: emptyList()) { pitch ->
                        PitchCard(
                            pitch = pitch,
                            onEdit = {
                                selectedPitch = pitch
                                showDialog = true
                            },
                            onDelete = {
                                pitchViewModel.deletePitch(pitch.id) { _, message ->
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
                        pitchViewModel.addPitch(PitchModel(startupName = name, description = desc, userId = currentUserId)) { _, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val updates = mapOf("startupName" to name, "description" to desc)
                        pitchViewModel.updatePitch(selectedPitch!!.id, updates) { _, message ->
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Green.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Business, contentDescription = null, tint = Green, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = pitch.startupName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 180.dp)
                    )
                }
                Row {
                    FilledIconButton(
                        onClick = onEdit,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFF1F3F4),
                            contentColor = Color.DarkGray
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledIconButton(
                        onClick = onDelete,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFFFEBEE),
                            contentColor = Color.Red
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F3F4))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = pitch.description,
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
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
        content = {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (pitch == null) "Create Startup Pitch" else "Update Your Pitch",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Startup Name") },
                        placeholder = { Text("Enter startup name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Green,
                            focusedLabelColor = Green
                        ),
                        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Idea Description") },
                        placeholder = { Text("Describe your business idea...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Green,
                            focusedLabelColor = Green
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { if (name.isNotBlank() && desc.isNotBlank()) onSave(name, desc) },
                            colors = ButtonDefaults.buttonColors(containerColor = Green),
                            shape = RoundedCornerShape(12.dp),
                            enabled = name.isNotBlank() && desc.isNotBlank()
                        ) {
                            Text("Save Pitch")
                        }
                    }
                }
            }
        }
    )
}
