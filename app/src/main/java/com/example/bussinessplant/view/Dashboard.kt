package com.example.bussinessplant.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
            DashboardMain()
        }
    }
}

@Composable
fun DashboardMain() {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Pitches") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Green, selectedTextColor = Green)
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    label = { Text("Alerts") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Green, selectedTextColor = Green)
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Green, selectedTextColor = Green)
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> DashboardBody()
                1 -> NotificationScreen()
                2 -> MoreScreen()
            }
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
    
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedPitch by remember { mutableStateOf<PitchModel?>(null) }

    LaunchedEffect(Unit) {
        pitchViewModel.getAllPitches()
    }

    val filteredPitches = pitches?.filter { 
        it.startupName.contains(searchQuery, ignoreCase = true) 
    }?.sortedByDescending { it.timestamp }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column(modifier = Modifier.background(Green)) {
                CenterAlignedTopAppBar(
                    title = { Text("Startup Ecosystem", color = White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Green)
                )
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search startups...", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = White) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = White.copy(alpha = 0.5f),
                        focusedBorderColor = White,
                        cursorColor = White,
                        focusedTextColor = White,
                        unfocusedTextColor = White
                    ),
                    singleLine = true
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { selectedPitch = null; showDialog = true },
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
            } else if (filteredPitches.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Lightbulb, null, modifier = Modifier.size(100.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No startups found", style = MaterialTheme.typography.headlineSmall, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
                ) {
                    items(filteredPitches) { pitch ->
                        PitchCard(
                            pitch = pitch,
                            onEdit = { selectedPitch = pitch; showDialog = true },
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
                onSave = { name, desc, cat ->
                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    if (selectedPitch == null) {
                        pitchViewModel.addPitch(PitchModel(startupName = name, description = desc, category = cat, userId = currentUserId)) { _, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val updates = mapOf("startupName" to name, "description" to desc, "category" to cat)
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Green.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Business, null, tint = Green, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = pitch.startupName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                            Text(text = pitch.category, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), color = Green)
                        }
                    }
                }
                Row {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = Color.Gray) }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = pitch.description, fontSize = 14.sp, color = Color.Gray, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun AddEditPitchDialog(pitch: PitchModel?, onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf(pitch?.startupName ?: "") }
    var desc by remember { mutableStateOf(pitch?.description ?: "") }
    var category by remember { mutableStateOf(pitch?.category ?: "Technology") }
    val categories = listOf("Technology", "Health", "Finance", "Education", "Food", "Other")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (pitch == null) "Create Startup Pitch" else "Update Pitch") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Startup Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Text("Category", fontSize = 12.sp, color = Color.Gray)
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.take(3).forEach { cat ->
                        FilterChip(selected = category == cat, onClick = { category = cat }, label = { Text(cat) })
                    }
                }
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onSave(name, desc, category) }, colors = ButtonDefaults.buttonColors(containerColor = Green)) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
