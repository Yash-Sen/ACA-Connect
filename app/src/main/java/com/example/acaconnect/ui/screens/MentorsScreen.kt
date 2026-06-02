package com.example.acaconnect.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.acaconnect.Mentor
import com.example.acaconnect.sampleMentors

@Composable
fun MentorsScreen() {
    var mentors by remember { mutableStateOf(sampleMentors) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Peer Mentors",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(mentors) { mentor ->
                MentorCard(
                    mentor = mentor,
                    onRatingChange = { newRating ->
                        mentors = mentors.map { if (it.id == mentor.id) it.copy(rating = newRating) else it }
                    }
                )
            }
        }
    }
}

@Composable
fun MentorCard(mentor: Mentor, onRatingChange: (Float) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = mentor.profileImage,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(64.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    error = rememberVectorPainter(Icons.Default.Person),
                    placeholder = rememberVectorPainter(Icons.Default.Person)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = mentor.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = "${mentor.department} - Batch ${mentor.batch}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = mentor.bio, style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Rating Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Rate Mentor:", style = MaterialTheme.typography.labelLarge)
                StarRatingBar(
                    rating = mentor.rating,
                    onRatingChanged = onRatingChange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:${mentor.email}")
                            putExtra(Intent.EXTRA_SUBJECT, "ACA Connect: Mentor Support Request")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Email")
                }

                Button(
                    onClick = {
                        val url = "https://api.whatsapp.com/send?phone=${mentor.whatsapp}"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(url)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)) // WhatsApp Green
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("WhatsApp")
                }
            }
        }
    }
}

@Composable
fun StarRatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    maxStars: Int = 5
) {
    Row {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            Icon(
                imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarOutline,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onRatingChanged(i.toFloat()) }
            )
        }
    }
}
