package br.infnet.marianabs.notesauth.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import br.infnet.marianabs.notesauth.database.Data
import br.infnet.marianabs.notesauth.ui.user.HomeActivity
import br.infnet.marianabs.notesauth.databinding.ActivityCreateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var database: DatabaseReference
    private var mAuth: FirebaseAuth? = null
    private var onlineUserId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        onlineUserId = mAuth!!.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().reference.child("note").child(onlineUserId)

        binding.btnSaveNote.setOnClickListener {

            val notes: String = binding.tvNoteCr.text.toString()

            if (notes.isEmpty()) {
                binding.tvNoteCr.error = "Escreva algo!"
            }else{

                val title = binding.tvTitleCr.text.toString()
                val note = binding.tvNoteCr.text.toString()
                val id: String? = database.push().key

                val data = Data(title,note,id)
                if (id != null) {
                    database.child(id).setValue(data).addOnSuccessListener {
                        Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }.addOnFailureListener{
                        Toast.makeText(this,"Opssss.. falha ao salvar",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}