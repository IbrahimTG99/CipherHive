package com.devsinc.cipherhive.presentation.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.devsinc.cipherhive.R
import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.getMockList
import com.devsinc.cipherhive.ui.theme.BebasNue
import com.devsinc.cipherhive.ui.theme.Poppins
import com.devsinc.cipherhive.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Home() {

    val clipboardManager =
        LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val scaffoldState = rememberScaffoldState()

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var searchQueryEmpty by rememberSaveable {
        mutableStateOf(false)
    }


    val passwordStored by rememberSaveable {
        mutableStateOf(2)
    }

    val passwordBreached by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = CircleShape,
                contentColor = Color.White,
                modifier = Modifier.padding(top = 48.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add icon")
            }
        },
        bottomBar = {
            HomeBottomMenu(
                scaffoldState = scaffoldState, scope = rememberCoroutineScope()
            )
        }) { it ->
        BoxWithConstraints(Modifier.padding(it)) {
            ConstraintLayout(homeConstraintSet(), modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Outlined.Password,
                    contentDescription = null,
                    tint = Color(0xFFFF6464),
                    modifier = Modifier.layoutId("ivIcon")
                )
                Row(
                    modifier = Modifier.layoutId("tvName")
                ) {
                    Text(
                        text = "Cipher",
                        fontFamily = BebasNue(),
                        color = Color(0xFFFF6464),
                        fontSize = 20.sp
                    )
                    Text(
                        text = " Hive",
                        fontFamily = BebasNue(),
                        color = Color(0xFF545974),
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("headerContainer"),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HeaderBoxes(
                        passwordStored, "Passwords\nStored",
                        Modifier
                            .weight(1f)
                            .height(160.dp)
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    HeaderBoxes(
                        passwordBreached,
                        "Passwords\nCompromised",
                        Modifier
                            .weight(1f)
                            .height(160.dp)
                    )
                }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    shape = RoundedCornerShape(12.dp),
                    label = {
                        Text(
                            text = "Search here.",
                            fontFamily = BebasNue(),
                            color = Color(0xFF545974)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("etSearch"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                LazyColumn(modifier = Modifier.layoutId("rv")) {
                    val filteredList =
                        getMockList().filter { s -> s.label.toString().contains(searchQuery) }
                    if (filteredList.isEmpty()) {
                        searchQueryEmpty = true
                    } else {
                        searchQueryEmpty = false
                        itemsIndexed(filteredList) { index, item ->
                            RecyclerItems(index, item, clipboardManager)
                        }
                    }
                }
                AnimatedVisibility(
                    visible = searchQueryEmpty, enter = fadeIn(
                        initialAlpha = 0.4f
                    ), exit = fadeOut(
                        animationSpec = tween(durationMillis = 250)
                    ), modifier = Modifier.layoutId("ivNotFound")
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_not_found),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}


@Composable
fun RecyclerItems(
    index: Int,
    item: Credential,
    clipboardManager: ClipboardManager,
    localContext: Context = LocalContext.current
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(
                2.dp, color = Color(0xFFF1F1F1), shape = RoundedCornerShape(10.dp)
            )
    ) {
        val (ivIcon, name, ivCopy) = createRefs()
        val color = if (index % 2 == 0) {
            Color(0xFFFF6464)
        } else Color(0xFF545974)
        Text(text = item.label[0].toString(),
            color = Color.White,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontFamily = BebasNue(),
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .background(color, RoundedCornerShape(10.dp))
                .padding(8.dp)
                .size(30.dp)
                .constrainAs(ivIcon) {
                    start.linkTo(parent.start, 8.dp)
                    centerVerticallyTo(parent)
                })
        Text(text = item.label,
            color = Color(0xFF545974),
            fontSize = 16.sp,
            fontFamily = Poppins(),
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(ivIcon.end, 16.dp)
                end.linkTo(ivCopy.start, 16.dp)
                width = Dimension.fillToConstraints
                centerVerticallyTo(parent)
            })
        IconButton(onClick = {
            val clipData = ClipData.newPlainText("Password", item.password)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                Toast.makeText(
                    localContext,
                    "Password copied to clipboard",
                    Toast.LENGTH_SHORT
                ).show()
            }
            clipData.apply {
                description.extras = PersistableBundle().apply {
                    putBoolean("android.content.extra.IS_SENSITIVE", true)
                }
            }
            clipboardManager.setPrimaryClip(clipData)

        }, modifier = Modifier.constrainAs(ivCopy) {
            end.linkTo(parent.end, 8.dp)
            centerVerticallyTo(parent)
        }) {
            Icon(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFFFF6464),
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun HeaderBoxes(count: Int, string: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .height(140.dp)
            .fillMaxWidth()
            .background(Color(0xFFF1F1F1), RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = count.toString(),
            style = Typography.displayLarge,
            modifier = Modifier.padding(start = 24.dp)
        )
        Text(
            text = string,
            fontFamily = Poppins(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color(0xFF545974),
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

fun homeConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val ivIcon = createRefFor("ivIcon")
        val tvName = createRefFor("tvName")
        val headerContainer = createRefFor("headerContainer")
        val etSearch = createRefFor("etSearch")
        val rv = createRefFor("rv")
        val ivNotFound = createRefFor("ivNotFound")

        constrain(ivIcon) {
            start.linkTo(parent.start, 24.dp)
            top.linkTo(parent.top, 20.dp)
        }
        constrain(tvName) {
            start.linkTo(parent.start, 20.dp)
            top.linkTo(ivIcon.bottom)
        }
        constrain(headerContainer) {
            width = Dimension.matchParent
            start.linkTo(parent.start, 24.dp)
            end.linkTo(parent.end, 24.dp)
            top.linkTo(tvName.bottom, 24.dp)
        }
        constrain(etSearch) {
            width = Dimension.matchParent
            start.linkTo(parent.start, 24.dp)
            end.linkTo(parent.end, 24.dp)
            top.linkTo(headerContainer.bottom, 16.dp)
        }
        constrain(rv) {
            width = Dimension.matchParent
            start.linkTo(parent.start, 24.dp)
            end.linkTo(parent.end, 24.dp)
            top.linkTo(etSearch.bottom, 16.dp)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        }
        constrain(ivNotFound) {
            width = Dimension.matchParent
            start.linkTo(parent.start, 24.dp)
            end.linkTo(parent.end, 24.dp)
            top.linkTo(etSearch.bottom, 16.dp)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        }
    }
}
