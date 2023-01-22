package com.kuluruvineeth.freeebooks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.navigation.Screens
import com.kuluruvineeth.freeebooks.ui.common.CustomTopAppBar
import com.kuluruvineeth.freeebooks.ui.theme.comfortFont
import com.kuluruvineeth.freeebooks.ui.viewmodels.CategoryViewModel

@Composable
fun CategoriesScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomTopAppBar(
            headerText = stringResource(id = R.string.categories_header),
            icon = R.drawable.ic_category_header
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 70.dp
                )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(168.dp),
                content = {
                    items(CategoryViewModel.CATEGORIES_ARRAY.size){ i ->
                        val category = CategoryViewModel.CATEGORIES_ARRAY[i].replaceFirstChar{
                            if(it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            )else it.toString()
                        }
                        CategoriesItem(category){
                            navController.navigate(
                                Screens.CategoryDetailScreen.withCategory(
                                    category
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesItem(
    category: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(90.dp)
            .width(160.dp)
            .padding(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                2.dp
            )
        ),
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                modifier = Modifier.padding(2.dp),
                text = category,
                fontSize = 18.sp,
                fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                fontFamily = comfortFont,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}

@Preview
@Composable
fun CategoriesScreenPreview() {
    //CategoriesScreen()
}