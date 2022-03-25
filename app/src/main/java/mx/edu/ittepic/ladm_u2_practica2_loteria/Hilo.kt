package mx.edu.ittepic.ladm_u2_practica2_loteria

import android.media.MediaPlayer
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.delay

class Hilo(context: MainActivity, arreglo: ArrayList<Carta>, name: TextView, img: ImageView): Thread() {
    private val layout = context
    private var cartas = arreglo
    private var txtNombre = name
    private var txtImagen = img
    private var ejecutar = true
    private var pausar = false
    private var i = 0
    lateinit var mp: MediaPlayer

    override fun run(){
        super.run()
        while(ejecutar){
            layout.runOnUiThread {
                if(!pausar) {
                    if (i < cartas.size) {
                        txtNombre.text = cartas[i].nombre
                        txtImagen.setImageResource(cartas[i].imagen)
                        mp = MediaPlayer.create(layout, cartas[i].audio)
                        mp.start()
                        i++
                    } else {
                        i = 0
                        AlertDialog.Builder(layout,)
                            .setMessage("Juego terminado")
                            .show()
                    }
                }
            }
            sleep(3000L)
        }
    }

    fun terminarHilo(){
        ejecutar=false;
    }

    public fun pausarHilo(){
        pausar = true
    }

    public fun despausarHilo(){
        pausar = false
    }

    public fun isPaused(): Boolean{
        return pausar
    }
}