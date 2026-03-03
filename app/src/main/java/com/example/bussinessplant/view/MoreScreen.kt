package com.example.bussinessplant.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bussinessplant.R
import com.example.bussinessplant.model.UserModel
import com.example.bussinessplant.repository.UserRepoImpl
import com.example.bussinessplant.ui.theme.Green
import com.example.bussinessplant.ui.theme.White
import com.example.bussinessplant.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MoreScreen() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val currentUser = auth.currentUser
    
    val userData by userViewModel.users.observeAsState()
    
    var showEditDialog by (remember { mutableStateOf(false) })

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userViewModel.getUserById(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Profile Header
            item {
                ProfileHeader(
                    name = "${userData?.firstName ?: ""} ${userData?.lastName ?: ""}".trim().ifEmpty { currentUser?.displayName ?: "User Name" },
                    email = userData?.email ?: currentUser?.email ?: "user@example.com",
                    role = userData?.role ?: "User"
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Account Section
            item { SectionTitle("Account Settings") }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column {
                        MenuItem(Icons.Default.Person, "Edit Profile") { 
                            showEditDialog = true
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                        MenuItem(Icons.Default.Notifications, "Notifications") { /* Navigate to alerts tab logic could go here */ }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                        MenuItem(Icons.Default.Security, "Security & Privacy") { 
                            Toast.makeText(context, "Security settings coming soon", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Support Section
            item { SectionTitle("Support & Info") }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column {
                        MenuItem(Icons.AutoMirrored.Filled.HelpOutline, "Help Center") { 
                            Toast.makeText(context, "Redirecting to Help Center...", Toast.LENGTH_SHORT).show()
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F4))
                        MenuItem(Icons.Default.Info, "About BusinessPlant") { 
                            Toast.makeText(context, "BusinessPlant v1.0", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Logout Button
            item {
                Button(
                    onClick = {
                        auth.signOut()
                        val intent = Intent(context, Login::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Out", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    if (showEditDialog && userData != null) {
        EditProfileDialog(
            user = userData!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedUser ->
                userViewModel.updateProfile(updatedUser.userId, updatedUser) { success, msg ->
                    if (success) {
                        userViewModel.getUserById(updatedUser.userId)
                        Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
                showEditDialog = false
            }
        )
    }
}

@Composable
fun ProfileHeader(name: String, email: String, role: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Green.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.face),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(email, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Green,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        role,
                        color = White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EditProfileDialog(user: UserModel, onDismiss: () -> Unit, onSave: (UserModel) -> Unit) {
    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var role by remember { mutableStateOf(user.role) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Role") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    onSave(user.copy(firstName = firstName, lastName = lastName, role = role))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun MenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F3F4)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.LightGray)
    }
}
