package mx.edu.ittepic.ladm_u2_practica2_loteria

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import mx.edu.ittepic.ladm_u2_practica2_loteria.databinding.ActivityMainBinding
import kotlin.coroutines.EmptyCoroutineContext

//JOSÉ GONZALO ZÁRATE RIVERA 17401336

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    var audios = listOf(R.raw.gallo,R.raw.diablo,R.raw.dama,R.raw.catrin,R.raw.paraguas,R.raw.sirena,R.raw.escalera,R.raw.botella,
        R.raw.barril,R.raw.arbol,R.raw.melon,R.raw.valiente,R.raw.gorrito,R.raw.muerte,R.raw.pera,R.raw.bandera,R.raw.bandolon,R.raw.violoncello,
        R.raw.garza,R.raw.pajaro,R.raw.mano,R.raw.bota,R.raw.luna,R.raw.cotorro,R.raw.borracho,R.raw.negrito,R.raw.corazon,R.raw.sandia,R.raw.tambor,
        R.raw.camaron,R.raw.jaras,R.raw.musico,R.raw.arana,R.raw.soldado,R.raw.estrella,R.raw.cazo,R.raw.mundo,R.raw.apache,R.raw.nopal,R.raw.alacran,
        R.raw.rosa,R.raw.calavera,R.raw.campana,R.raw.cantarito,R.raw.venado,R.raw.sol,R.raw.corona,R.raw.chalupa,R.raw.pino,R.raw.pescado,R.raw.palma,
        R.raw.maceta,R.raw.arpa,R.raw.rana)

    var nombres = arrayOf("El gallo","El diablo","La dama","El catrín","El paraguas","La sirena","La escalera","La botella",
        "El barril","El árbol","El melón","El valiente","El gorrito","La muerte","La pera","La bandera","El bandolón","El violoncello",
        "La garza","El pájaro","La mano","La bota","La luna","El cotorro","El borracho","El negrito","El corazón","La sandía","El tambor",
        "El camarón","Las jaras","El músico","La araña","El soldado","La estrella","El cazo","El mundo","El apache","El nopal","El alacrán",
        "La rosa","La calavera","La campana","El cantarito","El venado","El sol","La corona","La chalupa","El pino","El pescado","La palma",
        "La maceta","El arpa","La rana")

    var imgs = listOf(R.drawable.carta1,R.drawable.carta2,R.drawable.carta3,R.drawable.carta4,R.drawable.carta5,
        R.drawable.carta6,R.drawable.carta7,R.drawable.carta8,R.drawable.carta9,R.drawable.carta10,R.drawable.carta11,
        R.drawable.carta12,R.drawable.carta13,R.drawable.carta14,R.drawable.carta15,R.drawable.carta16,R.drawable.carta17,
        R.drawable.carta18,R.drawable.carta19,R.drawable.carta20,R.drawable.carta21,R.drawable.carta22,R.drawable.carta23,
        R.drawable.carta24,R.drawable.carta25,R.drawable.carta26,R.drawable.carta27,R.drawable.carta28,R.drawable.carta29,
        R.drawable.carta30,R.drawable.carta31,R.drawable.carta32,R.drawable.carta33,R.drawable.carta34,R.drawable.carta35,
        R.drawable.carta36,R.drawable.carta37,R.drawable.carta38,R.drawable.carta39,R.drawable.carta40,R.drawable.carta41,
        R.drawable.carta42,R.drawable.carta43,R.drawable.carta44,R.drawable.carta45,R.drawable.carta46,R.drawable.carta47,
        R.drawable.carta48,R.drawable.carta49,R.drawable.carta50,R.drawable.carta51,R.drawable.carta52,R.drawable.carta53,
        R.drawable.carta54)

    var cartas = ArrayList<Carta>(53)
    private lateinit var imagen: ImageView

    lateinit var hiloTemp:Hilo
    private var case = 0
    private var revueltas = false

    val scope = CoroutineScope(Job() + Dispatchers.Main)
    var exec = true

    val corrutina = scope.launch(EmptyCoroutineContext, CoroutineStart.LAZY) {
        var it = 0
        while(exec){
            runOnUiThread {
                if (it < nombres.size) {
                    cartas.add(Carta(nombres[it], imgs[it], audios[it]))
                    it++
                } else {
                    binding.txtNombre.text = cartas[0].nombre
                    imagen.setImageResource(cartas[0].imagen)
                    exec = false
                }
            }
            delay(10)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStop.setVisibility(View.INVISIBLE)
        imagen = binding.txtImagenes

        try{
            corrutina.start()
        }catch(e:Exception){
            AlertDialog.Builder(this,)
                .setMessage("Error al iniciar el hilo")
                .show()
        }


        binding.btnRevolver.setOnClickListener {
            binding.btnStop.text = "Detener"
            case = 0
            revolver()
            revueltas = true
            AlertDialog.Builder(this,)
                .setMessage("Las cartas han sido revueltas")
                .show()
        }

        binding.btnStart.setOnClickListener {
            //inicia el juego
            if(!revueltas){
                revolver()
            }
            binding.btnStart.setVisibility(View.INVISIBLE)
            binding.btnRevolver.setVisibility(View.INVISIBLE)
            binding.btnStop.setVisibility(View.VISIBLE)
            binding.btnStop.text = "Detener"
            case = 0
            hiloTemp = Hilo(this,cartas,binding.txtNombre,binding.txtImagenes)
            revueltas = false
            try{
                hiloTemp.start()
            }catch(e:Exception){
                AlertDialog.Builder(this,)
                    .setMessage("Error")
                    .show()
            }
        }

        binding.btnStop.setOnClickListener {
            when(case){
                0 ->{
                    case = 1
                    binding.btnStart.setVisibility(View.VISIBLE)
                    binding.btnRevolver.setVisibility(View.VISIBLE)
                    binding.btnStop.text = "Ver cartas restantes"
                    hiloTemp.pausarHilo()
                }
                1 -> {
                    case = 2
                    binding.btnStart.setVisibility(View.INVISIBLE)
                    binding.btnRevolver.setVisibility(View.INVISIBLE)
                    binding.btnStop.text = "Terminar revisión"
                    hiloTemp.despausarHilo()
                    }
                2 ->{
                    case = 0
                    binding.btnStart.setVisibility(View.VISIBLE)
                    binding.btnRevolver.setVisibility(View.VISIBLE)
                    hiloTemp.pausarHilo()
                    binding.btnStop.text = "Detener"
                    binding.btnStop.setVisibility(View.INVISIBLE)
                }
            }
        }
    }
    fun revolver(){
        cartas.shuffle()
        binding.txtNombre.text=cartas[0].nombre
        imagen.setImageResource(cartas[0].imagen)
    }
}