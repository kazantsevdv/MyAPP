package com.example.myapp

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapp.mvp.model.ImgConverter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.geekbrains.poplib.mvp.presenter.MainPresenter
import ru.geekbrains.poplib.mvp.view.MainView


class MainActivity : MvpAppCompatActivity(), MainView {
    val REQUEST_CODE = 1000
    val REQUEST_CODE_PERMISSION_READ_CONTACTS = 999
    private var cancelDialog: AlertDialog? = null

    val presenter by moxyPresenter {
        MainPresenter(ImgConverter(this), AndroidSchedulers.mainThread())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun init() {
        bt_open.setOnClickListener { presenter.showFileChooser() }
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)

        cancelDialog = dialog.setTitle("Конвертация")
            .setMessage("Выполяется...")
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> presenter.canselConvert() }

            .setOnCancelListener { presenter.canselConvert() }
            .create()
    }

    override fun showFileChooser() {
        val permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            val mimeTypes: Array<String> = arrayOf(
                "image/jpeg",
            )
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(intent, REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_READ_CONTACTS
            );
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    showFileChooser()
                } else {

                }
                return
            }
        }
    }

    override fun showProgress(show: Boolean) {
        if (show)
            cancelDialog?.show()
        else
            cancelDialog?.dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_CODE || resultCode != RESULT_OK) {
            return
        }
        data?.data?.toString()?.let {
            presenter.convertFile(it)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setResult(result: String) {
        tv_rez.text = result
    }


}


