package com.example.bussinessplant.view

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bussinessplant.R
import com.example.bussinessplant.ui.theme.Green
import com.example.bussinessplant.ui.theme.White

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFF0FDF4)) // Light green tint at bottom
                )
            )
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append("Business")
                        }
                        withStyle(style = SpanStyle(color = Green)) {
                            append("Plant")
                        }
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Header Buttons (Simplified for mobile)
            Row(verticalAlignment = Alignment.CenterVertically) {
                 // Browse Businesses - maybe hide on small screens or keep simple
                 /* TextButton(onClick = { }) {
                     Text("Browse", color = Color.Gray)
                 } */
                 
                 // Sign In Button
                 TextButton(onClick = {
                     val intent = Intent(context, Login::class.java)
                     context.startActivity(intent)
                 }) {
                     Text("Sign In", color = Color.Black)
                 }
                 
                 Spacer(modifier = Modifier.width(4.dp))

                 Button(
                    onClick = { 
                        // Navigate to Registration
                        val intent = Intent(context, SignUpActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Get Started", color = White, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Hero Section
        
        // Tagline
        Box(
            modifier = Modifier
                .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                 Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Crowdfunding for Everyone",
                    color = Green,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Headline
        Text(
            text = "Invest in Real Businesses,",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )
        Text(
            text = "Earn Real Returns",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Green,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sub-headline
        Text(
            text = "BusinessPlant connects everyday investors with promising businesses.\nFund entrepreneurs, earn interest on loans, and grow together.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Hero Buttons
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { /* Start Investing Logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Green),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(50.dp)
            ) {
                Text("Start Investing", color = White)
                Spacer(modifier = Modifier.width(8.dp))
                // Arrow icon could go here
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { /* List Business Logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = White),
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(50.dp)
            ) {
                Text("List Your Business", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        // Stats Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(value = "$1M+", label = "Total Funded")
            StatItem(value = "500+", label = "Businesses")
            StatItem(value = "10K+", label = "Investors")
        }
        
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Green
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}