package com.example.tipcalculator

import android.icu.text.NumberFormat
import android.icu.util.ULocale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                TipLayout()
            }
        }
    }
}

@Composable
fun EditNumberField(@StringRes label:Int,
                    @DrawableRes leadingIcon:Int,
                    value:String,
                    keyboardOptions: KeyboardOptions,
                    onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        label = {Text(stringResource(label))},
        keyboardOptions = keyboardOptions,
        leadingIcon = {
            Icon(painter = painterResource(id = leadingIcon),
                contentDescription = null,modifier=Modifier.size(18.dp))
        }
    )
}
@Composable
fun RoundTheTipRow(roundUp:Boolean,onRoundUpChanged: (Boolean) -> Unit,modifier: Modifier = Modifier)
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .size(48.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = stringResource(R.string.round_up_tip))
        Switch(modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
        )

    }
}
@Composable
fun TipLayout()
{
    var tipInput by remember { mutableStateOf("") }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    var amtInput by remember {mutableStateOf("")}
    val amt= amtInput.toDoubleOrNull() ?: 0.0
    var roundUp by remember { mutableStateOf(false)}
    val tip= calculateTip(amt,roundUp,tipPercent)

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(horizontal = 40.dp)
        .verticalScroll(rememberScrollState())
        .safeDrawingPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start))
        EditNumberField(
            label= R.string.bill_amount,
            leadingIcon = R.drawable.rupee,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            value = amtInput,
            onValueChange = {amtInput = it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())
        EditNumberField(
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent_icon,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            value = tipInput,
            onValueChange = {tipInput=it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        RoundTheTipRow(roundUp = roundUp,
            onRoundUpChanged = {roundUp = it},
            modifier = Modifier.padding(bottom = 32.dp))
        Text(text = stringResource(R.string.tip_amount, tip),
            style= MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(150.dp))


    }

}
private fun calculateTip(amount: Double,roundUp: Boolean,tipPercent: Double = 15.0): String {
    var tip = tipPercent / 100 * amount
    if(roundUp)
    {
        tip= kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)  // works on the
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TipCalculatorTheme {
      TipLayout()
    }
}