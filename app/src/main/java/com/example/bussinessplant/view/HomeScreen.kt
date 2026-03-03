package com.example.bussinessplant.view

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black)) { append("Business") }
                        withStyle(style = SpanStyle(color = Green)) { append("Plant") }
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { /* Browse Businesses */ }) {
                    Text("Browse Businesses", color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { context.startActivity(Intent(context, DashboardActivity::class.java)) },
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Dashboard", color = Color.Black, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.LightGray)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.face),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // --- Hero Section ---
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.background(Color(0xFFE8F5E9), CircleShape).padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Green, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Crowdfunding for Everyone", color = Green, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Invest in Real Businesses,\nEarn Real Returns",
                fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                textAlign = TextAlign.Center, lineHeight = 44.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "BusinessPlant connects everyday investors with promising businesses. Fund entrepreneurs, earn interest on loans, and grow together.",
                fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Button(
                    onClick = { /* Start Investing */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Start Investing", color = White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = { /* List Business */ },
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("List Your Business", color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatItem("NPR 100M+", "Total Funded")
                StatItem("500+", "Businesses")
                StatItem("10K+", "Investors")
            }
        }

        // --- Testimonials Section (New) ---
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Trusted by Founders & Investors", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                TestimonialCard("Saroj Sharma", "Investor", "BusinessPlant made it easy for me to diversify my portfolio with local startups.")
                Spacer(modifier = Modifier.width(16.dp))
                TestimonialCard("Anita Rai", "Founder", "We raised our first NPR 500K here in just two weeks! Amazing community.")
            }
        }

        // --- How It Works ---
        Column(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFFAFAFA)).padding(vertical = 60.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("How It Works", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Whether you're looking to invest or seeking funding, BusinessPlant makes it simple.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(40.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                HowItWorksColumn(
                    title = "For Investors",
                    icon = Icons.Default.Star,
                    steps = listOf("Browse verified business listings", "Review documents and loan terms", "Invest any amount you choose", "Earn interest on your investment"),
                    iconColor = Green
                )
                HowItWorksColumn(
                    title = "For Business Owners",
                    icon = Icons.Default.Info,
                    steps = listOf("Create your business profile", "Upload verification documents", "Set your loan terms and interest rate", "Receive funding from multiple investors"),
                    iconColor = Color(0xFFB8860B)
                )
            }
        }

        // --- Featured Businesses ---
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column {
                    Text("Featured Businesses", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Discover promising businesses seeking investment", fontSize = 14.sp, color = Color.Gray)
                }
                OutlinedButton(onClick = { /* View All */ }, shape = RoundedCornerShape(8.dp)) {
                    Text("View All", color = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                BusinessCard("GreenLeaf Organics", "Food & Beverage", 42500, 75000, 8.5, 24)
                Spacer(modifier = Modifier.width(16.dp))
                BusinessCard("TechRepair Hub", "Technology", 28000, 50000, 7.5, 18)
                Spacer(modifier = Modifier.width(16.dp))
                BusinessCard("Urban Fitness Studio", "Health & Wellness", 67000, 120000, 8.0, 36)
            }
        }

        // --- Footer ---
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF8F9FA)).padding(40.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(R.drawable.logo), null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BusinessPlant", fontWeight = FontWeight.Bold, color = Green)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Connecting everyday investors with promising businesses. Invest in real businesses, earn real returns.", color = Color.Gray, fontSize = 12.sp)
                }
                Column(modifier = Modifier.weight(0.5f).padding(start = 32.dp)) {
                    Text("Platform", fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(12.dp))
                    FooterLink("Browse Businesses")
                    FooterLink("List Your Business")
                    FooterLink("Dashboard")
                }
                Column(modifier = Modifier.weight(0.5f)) {
                    Text("Support", fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(12.dp))
                    FooterLink("Help Center")
                    FooterLink("Terms of Service")
                    FooterLink("Privacy Policy")
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(20.dp))
            Text("© 2025 BusinessPlant. All rights reserved.", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun TestimonialCard(name: String, role: String, quote: String) {
    Card(
        modifier = Modifier.width(240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.FormatQuote, null, tint = Green, modifier = Modifier.size(32.dp))
            Text(quote, fontSize = 14.sp, color = Color.DarkGray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            Spacer(modifier = Modifier.height(12.dp))
            Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(role, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Green
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun HowItWorksColumn(title: String, icon: ImageVector, steps: List<String>, iconColor: Color) {
    Column(modifier = Modifier.width(180.dp).padding(horizontal = 8.dp)) {
        Box(modifier = Modifier.size(40.dp).background(iconColor.copy(0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        steps.forEachIndexed { i, step ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Box(modifier = Modifier.size(20.dp).background(iconColor, CircleShape), contentAlignment = Alignment.Center) {
                    Text("${i+1}", color = White, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(step, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun BusinessCard(name: String, category: String, raised: Int, total: Int, interest: Double, months: Int) {
    Card(modifier = Modifier.width(280.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = White), border = BorderStroke(1.dp, Color.LightGray)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp).background(Color(0xFFEEEEEE), RoundedCornerShape(8.dp)), contentAlignment = Alignment.TopStart) {
                Text(category, modifier = Modifier.padding(8.dp).background(White, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("A sustainable business delivering value directly to customers.", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Raised", fontSize = 12.sp, color = Color.Gray)
                Text("NPR $raised of $total", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = { raised.toFloat() / total }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape), color = Green, trackColor = Color(0xFFE8F5E9))
            Spacer(modifier = Modifier.height(4.dp))
            Text("${(raised.toFloat()/total * 100).toInt()}% funded", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Green, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$interest% Interest", fontSize = 12.sp, color = Color.Black)
                }
                Text("$months months", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun WhyChooseItem(icon: ImageVector, title: String, desc: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(100.dp)) {
        Box(modifier = Modifier.size(48.dp).background(Green.copy(0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Green, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(desc, textAlign = TextAlign.Center, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun FooterLink(text: String) {
    Text(text, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp).clickable { /* Navigate */ })
}
