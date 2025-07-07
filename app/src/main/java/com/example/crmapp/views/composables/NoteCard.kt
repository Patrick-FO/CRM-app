package com.example.crmapp.views.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crmapp.domain.model.entities.NoteEntity

@Composable
fun NoteCard(
    onEditClick: () -> Unit,
    note: NoteEntity,
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        ),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray
        )
    ) {
        Row() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                //TODO Figure out what's what. Is description even nullable in our backend? Can the description come back as null? Or does it come back as blank?
                //I believe that the description only comes back as blank, and not as null. Worth looking into though

                if (note.description != null) {
                    if(note.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = note.description,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit note",
                tint = Color.Black,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        onEditClick()
                    }
            )
        }
    }
}