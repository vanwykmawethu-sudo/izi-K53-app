package com.k53coach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class Question(
    val text: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String,
    val topic: String
)

private val questions = listOf(
    Question("What should you do at a stop sign?", listOf("Slow down only","Stop completely","Speed up","Sound your hooter"),1,"A stop sign requires a complete stop before proceeding when it is safe.","Rules of the Road"),
    Question("What does a red traffic light mean?", listOf("Proceed carefully","Stop","Turn left","Speed up"),1,"A red traffic signal means you must stop.","Rules of the Road"),
    Question("A triangular road sign generally warns you about what?", listOf("A hazard","Parking","A destination","A fuel station"),0,"Warning signs alert road users to hazards or conditions ahead.","Road Signs"),
    Question("Before changing lanes, you should first...", listOf("Accelerate hard","Check mirrors and blind spot","Switch off lights","Open the window"),1,"Check mirrors, signal appropriately and check the blind spot before changing lanes.","Safe Driving"),
    Question("Which control is normally used to change engine speed while driving?", listOf("Accelerator","Parking brake","Horn","Windscreen washer"),0,"The accelerator controls engine power and speed.","Vehicle Controls"),
    Question("A solid line on the road should be treated as...", listOf("A line you may always cross","A restriction on crossing where applicable","A parking guide","A bicycle lane"),1,"Road markings have specific meanings; a solid line can restrict crossing depending on the marking and road rules.","Rules of the Road"),
    Question("When approaching a pedestrian crossing, you should...", listOf("Ignore pedestrians","Reduce speed and be prepared to stop","Honk continuously","Overtake everyone"),1,"Reduce speed and be prepared to stop for pedestrians where required.","Safe Driving"),
    Question("What is the purpose of a vehicle's parking brake?", listOf("To keep a parked vehicle from moving","To increase engine power","To operate headlights","To clean the windscreen"),0,"The parking brake helps secure a stationary vehicle.","Vehicle Controls"),
    Question("A learner should use a seat belt...", listOf("Only on highways","Only at night","Whenever required by law and available","Only when a passenger asks"),2,"Seat belts should be worn as required; they are an important safety restraint.","Safe Driving"),
    Question("What should you do when a traffic officer directs you to stop?", listOf("Continue if the light is green","Stop as directed when safe","Speed up","Ignore the officer"),1,"Follow lawful traffic directions when it is safe to do so.","Rules of the Road")
)

@Composable
fun K53Theme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { K53Theme { App() } }
    }
}

@Composable
fun App() {
    var tab by remember { mutableStateOf("Home") }
    Scaffold(bottomBar = {
        NavigationBar {
            listOf("Home","Learn","Test","Mistakes","Progress").forEach {
                NavigationBarItem(selected = tab == it, onClick = { tab = it },
                    icon = {}, label = { Text(it) })
            }
        }
    }) { pad ->
        Box(Modifier.padding(pad).fillMaxSize()) {
            when(tab) {
                "Home" -> Home { tab = "Test" }
                "Learn" -> Learn()
                "Test" -> DailyTest()
                "Mistakes" -> Mistakes()
                "Progress" -> Progress()
            }
        }
    }
}

@Composable
fun Home(start: () -> Unit) {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("K53 Coach 🚗", style = MaterialTheme.typography.headlineMedium)
        Text("Build your confidence with short daily practice.")
        Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(20.dp)) {
            Text("Readiness score", style = MaterialTheme.typography.titleMedium)
            Text("0%", style = MaterialTheme.typography.displaySmall)
            Text("Start practicing to build your score.")
        }}
        Button(onClick = start, Modifier.fillMaxWidth()) { Text("START DAILY TEST") }
        Text("Today's goal: 10 questions")
        Text("Current streak: 0 days 🔥")
    }
}

@Composable
fun Learn() {
    val topics = listOf("Road Signs","Rules of the Road","Vehicle Controls","Safe Driving","Learner-Test Tips")
    LazyColumn(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Learn", style = MaterialTheme.typography.headlineMedium) }
        items(topics) { topic -> Card(Modifier.fillMaxWidth()) { Text(topic, Modifier.padding(20.dp)) } }
    }
}

@Composable
fun DailyTest() {
    var current by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var submitted by remember { mutableStateOf(false) }
    val test = remember { questions.shuffled(Random(7)).take(10) }
    if (current >= test.size) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Test complete 🎉", style = MaterialTheme.typography.headlineMedium)
            Text("Score: $score / ${test.size}", style = MaterialTheme.typography.titleLarge)
            Text("Keep practicing your weaker topics.")
        }
        return
    }
    val q = test[current]
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Question ${current+1} of ${test.size}", style = MaterialTheme.typography.titleMedium)
        Text(q.text, style = MaterialTheme.typography.headlineSmall)
        q.options.forEachIndexed { i, option ->
            OutlinedButton(onClick = { if (!submitted) selected = i },
                Modifier.fillMaxWidth()) { Text("${'A'+i}. $option") }
        }
        if (!submitted) Button(onClick = {
            if (selected != null) { if (selected == q.correct) score++; submitted = true }
        }, enabled = selected != null, Modifier.fillMaxWidth()) { Text("SUBMIT ANSWER") }
        else {
            Text(if (selected == q.correct) "Correct ✅" else "Not quite ❌")
            Text(q.explanation)
            Button(onClick = { current++; selected = null; submitted = false }, Modifier.fillMaxWidth()) { Text("NEXT QUESTION") }
        }
    }
}

@Composable
fun Mistakes() {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mistake Bank", style = MaterialTheme.typography.headlineMedium)
        Text("Questions answered incorrectly will appear here in the next persistence build.")
        Text("0 questions to revise")
    }
}

@Composable
fun Progress() {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Your Progress", style = MaterialTheme.typography.headlineMedium)
        Text("Questions answered: 0")
        Text("Average accuracy: 0%")
        Text("Current streak: 0 days")
        Text("Study time: 0 minutes")
        HorizontalDivider()
        Text("K53 READINESS", style = MaterialTheme.typography.titleMedium)
        Text("🟠 KEEP PRACTISING")
        Text("This is a study-progress indicator, not a guarantee of passing the official test.")
    }
}
