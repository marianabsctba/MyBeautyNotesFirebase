package br.infnet.marianabs.notesauth.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import br.infnet.marianabs.notesauth.R
import br.infnet.marianabs.notesauth.database.Data
import br.infnet.marianabs.notesauth.ui.user.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdateNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        val titleup: TextView = findViewById(R.id.tv_title_up)
        val noteup: TextView = findViewById(R.id.tv_note_up)

        val bundle: Bundle? = intent.extras
        val title = bundle!!.getString("title")
        val note = bundle.getString("note")
        val id = bundle.get("id")

        titleup.text = title
        noteup.text = note

        val updatebtn = findViewById<Button>(R.id.btn_update_note)
        val deletebtn = findViewById<Button>(R.id.btn_delete_note)


        updatebtn.setOnClickListener {

            val title = titleup.text.toString()
            val note = noteup.text.toString()

            val data = Data(title,note, id as String)

            val reference = FirebaseDatabase.getInstance().reference.child("note").child(
                FirebaseAuth.getInstance().currentUser!!.uid
            )
            reference.child(id).setValue(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "update feito com sucesso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                } else {
                    Toast.makeText(this, "falha no update " + task.exception, Toast.LENGTH_SHORT).show()
                }
            }

        }

        deletebtn.setOnClickListener {

            val reference = FirebaseDatabase.getInstance().reference.child("note").child(
                FirebaseAuth.getInstance().currentUser!!.uid
            )
            reference.child(id as String).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "excluido com sucesso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                } else {
                    Toast.makeText(this,"falha ao excluir " + task.exception, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

}