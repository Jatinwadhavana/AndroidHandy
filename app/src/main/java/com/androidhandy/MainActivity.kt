package com.androidhandy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidhandy.pop.PopupWindowCustom
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHeloWorld.setOnClickListener {
            val pop = PopupWindowCustom(this@MainActivity, it)
            pop.show(it)
            pop.setOnPopItemSelected(object : PopupWindowCustom.OnPopItemSelected {
                override fun onSaveToCollection() {
                    /*try {
                        if (Utils.hasStoragePermission(this@WallDrawingActivity)) {
                            val stringPath =
                                Utils.getCollectionDirectoryPath(this@WallDrawingActivity)
                            val fileName = getTimeStamp() + Constants.IMAGE_NAME
                            val imgPath = Utils.createFileFromBitmap(
                                layer.bitmapFromView,
                                stringPath, fileName
                            )

                            Utils.scanGallery(File(imgPath.toString()), this@WallDrawingActivity)

                            val collectModel = CollectionModel()
                            collectModel._id = System.currentTimeMillis().toString()

                            collectModel.isImage = true
                            collectModel.imagePath = stringPath + File.separator + fileName
                            collectModel.usedColor = ArrayList()
                            collectModel.usedColor.addAll(lstSelectedColor)

                            val collectionLst = ArrayList<CollectionModel>()
                            collectionLst.addAll(
                                Gson().fromJson(
                                    Prefs(this@WallDrawingActivity).collectionData,
                                    object : TypeToken<ArrayList<CollectionModel>>() {}.type
                                )
                            )
                            collectionLst.add(collectModel)

                            Prefs(this@WallDrawingActivity).collectionData =
                                Gson().toJson(collectionLst)
                            Utils.makeToast(
                                this@WallDrawingActivity,
                                getString(R.string.msg_coll_save)
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pop.dismissPop()
                    }*/
                }

                override fun onDownload() {
                    /*if (Utils.hasStoragePermission(this@WallDrawingActivity)) {
                        val imgPath = Utils.createFileFromBitmap(
                            layer.bitmapFromView,
                            Utils.getDirectoryPath(this@WallDrawingActivity),
                            getTimeStamp() + Constants.IMAGE_NAME
                        )
                        Utils.scanGallery(File(imgPath.toString()), this@WallDrawingActivity)

                        Utils.makeToast(
                            this@WallDrawingActivity,
                            getString(R.string.msg_download_image)
                        )
                        pop.dismissPop()
                    }*/
                }

                override fun onShare() {
                    /*if (Utils.hasStoragePermission(this@WallDrawingActivity)) {
                        val imgPath = Utils.createFileFromBitmap(
                            layer.bitmapFromView,
                            Utils.getDirectoryPath(this@WallDrawingActivity),
                            getTimeStamp() + Constants.IMAGE_NAME
                        )
                        Utils.scanGallery(File(imgPath.toString()), this@WallDrawingActivity)
                        startShareDialog(imgPath!!)
                        pop.dismissPop()
                    }*/
                }
            })
        }
    }
}