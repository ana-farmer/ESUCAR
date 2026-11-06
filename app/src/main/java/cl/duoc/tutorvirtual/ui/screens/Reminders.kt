package cl.duoc.tutorvirtual.ui.screens

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import cl.duoc.tutorvirtual.MainActivity
import cl.duoc.tutorvirtual.viewmodel.TutorState
import java.text.SimpleDateFormat
import java.util.*

@Composable fun ReminderSettings(state: TutorState) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    fun schedule() {
        val alarms = context.getSystemService(AlarmManager::class.java)
        var count = 0
        for (i in 0..3) {
            val intent = Intent(context, DeadlineReceiver::class.java).putExtra("hito", i + 1)
            val pending = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarms.cancel(pending)
            val date = runCatching { SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).apply { isLenient = false }.parse(state.value("date_$i")) }.getOrNull()
            if (date != null && state.value("sent_$i") != "true") {
                val time = Calendar.getInstance().apply { time = date; set(Calendar.HOUR_OF_DAY, 9) }.timeInMillis
                if (time > System.currentTimeMillis()) { alarms.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pending); count++ }
            }
        }
        message = "$count recordatorios programados para las 09:00 del día de entrega. Android puede retrasarlos. Vuelve a programarlos si cambias fechas o reinicias el teléfono."
    }
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> if (granted) schedule() else message = "Las notificaciones están desactivadas. Puedes habilitarlas desde Ajustes." }
    Text("Recordatorios de entregas", style = MaterialTheme.typography.titleMedium)
    Button(onClick = {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) permission.launch(Manifest.permission.POST_NOTIFICATIONS) else schedule()
    }) { Text("Programar / actualizar recordatorios") }
    if (message.isNotEmpty()) Text(message)
}
class DeadlineReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(NotificationChannel("deadlines", "Entregas académicas", NotificationManager.IMPORTANCE_DEFAULT))
        }
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val open = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        manager.notify(intent.getIntExtra("hito", 0), NotificationCompat.Builder(context, "deadlines")
            .setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Tutor Virtual ESUCAR")
            .setContentText("Hoy vence el hito ${intent.getIntExtra("hito", 0)}. Revisa tus avances.")
            .setContentIntent(open).setAutoCancel(true).build())
    }
}
