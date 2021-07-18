package com.androidhandy.utils

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.ContentValues
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.hardware.Camera
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.ExifInterface
import android.media.Image
import android.media.MediaScannerConnection
import android.media.ThumbnailUtils
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utils {
    internal var permissions =
        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
    val MULTIPLE_PERMISSIONS = 10
    var REQUEST_CAMERA = 0
    var SELECT_FILE = 1

    //TAG
    var TAG = Utils::class.java.simpleName

    /**
     * The Perm array.
     */
    var perm_array = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @SuppressLint("MissingPermission")
    fun isOnline(cContext: Context): Boolean {
        try {
            val cm = cContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            /*val netInfo = cm.activeNetworkInfo
            if (netInfo != null && netInfo.isConnectedOrConnecting) {
                return true
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }



    /**
     * Gets version name.
     * @return the version name
     */
    val versionName: String
        get() {
            val builder = StringBuilder()

            val fields = Build.VERSION_CODES::class.java.fields
            for (field in fields) {
                val fieldName = field.name
                var fieldValue = -1

                try {
                    fieldValue = field.getInt(Any())
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                if (fieldValue == Build.VERSION.SDK_INT) {
                    builder.append(fieldName)
                }
            }
            return builder.toString()
        }

    val deviceTimeZone: String
        get() {

            val tz = TimeZone.getDefault()
            return tz.id
        }
    /**
     * The constant placeIDList.
     */
    var placeIDList: ArrayList<*>? = null

    fun dpToPx(context: Context, dp: Float): Int {
        return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    fun pxToDp(context: Context, px: Float): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * Make toast.
     * @param context the context
     * @param message the message
     */
    fun makeToast(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Split string string [ ].
     * @param string   the string
     * @param seprator the seprator
     * @return countryList by seprator
     */
    fun splitString(string: String, seprator: String): Array<String> {
        return string.split(seprator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    /**
     * Format date from string string.
     *
     * @param inputFormat  the input format
     * @param outputFormat the output format
     * @param inputDate    the input date
     * @return string
     */
    fun formatDateFromString(inputFormat: String, outputFormat: String, inputDate: String): String {
        var parsed: Date? = null
        var outputDate = ""

        val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())
        val df_output = SimpleDateFormat(outputFormat, Locale.getDefault())

        try {
            parsed = df_input.parse(inputDate)
            outputDate = df_output.format(parsed)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDate
    }

    /**
     * Count spaces int.
     *
     * @param string the string
     * @return int
     */
    fun countSpaces(string: String): Int {
        var spaces = 0
        for (i in 0 until string.length) {
            spaces += if (Character.isWhitespace(string[i])) 1 else 0
        }
        return spaces
    }

    /**
     * Sets view visibility.
     *
     * @param view      the view
     * @param isVisible the is visible
     */
    fun setViewVisibility(view: View, isVisible: Boolean) {
        if (isVisible)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.GONE
    }

    /**
     * Sets view background resource.
     *
     * @param view     the view
     * @param drawable the drawable
     */
    fun setViewBackgroundResource(view: View, drawable: Int) {
        view.setBackgroundResource(drawable)
    }

    /**
     * Sets view background color.
     *
     * @param view  the view
     * @param color the color
     */
    fun setViewBackgroundColor(view: View, color: Int) {
        view.setBackgroundColor(color)
    }

    /**
     * Sets image resource.
     *
     * @param view  the view
     * @param resId the res id
     */
    fun setImageResource(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    /**
     * Sets image drawable.
     *
     * @param view     the view
     * @param drawable the drawable
     */
    fun setImageDrawable(view: ImageView, drawable: Drawable) {
        view.setImageDrawable(drawable)
    }

    /**
     * Sets view text color.
     *
     * @param view  the view
     * @param color the color
     */
    fun setViewTextColor(view: Button, color: Int) {
        view.setTextColor(color)
    }

    /**
     * Sets view background.
     *
     * @param view     the view
     * @param drawable the drawable
     */
    fun setViewBackground(view: View, drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        }
    }

    /*public static String getUTCFromDateTime(String pattern, String dateTimeString) {
        String dateString = "";
        try {
            long dateInMillis = Info.dateFormat_new.parse(dateTimeString).getTime();
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateString = formatter.format(new Date(dateInMillis));
            //System.out.println("finalUTCDate -> "+dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }*/


    /**
     * Gets localfrom utc.
     *
     * @param utc_data      the utc data
     * @param inputPattern  the input pattern
     * @param outputPattern the output pattern
     * @return localfrom utc
     */
    fun getLocalfromUtc(utc_data: String?, inputPattern: String, outputPattern: String): String {
        var formattedDate = ""
        if (!TextUtils.isEmpty(utc_data)) {
            try {
                val df = SimpleDateFormat(inputPattern, Locale.getDefault())
                df.timeZone = TimeZone.getTimeZone("UTC")
                var date: Date? = null
                try {
                    date = df.parse(utc_data)
                } catch (e: ParseException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                //System.out.println(utc_data);
                df.timeZone = TimeZone.getDefault()
                val df1 = SimpleDateFormat(outputPattern, Locale.getDefault())
                formattedDate = df1.format(date)
                //System.out.println("finalLocalTimeDate -> "+formattedDate);
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
        return formattedDate
    }

    /*public static String getTimeFromUTC(String givenDateString) {

        String dateStr = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;

            date = df.parse(givenDateString);


            df.setTimeZone(TimeZone.getDefault());
            dateStr = df.format(date);
            dateStr = Info.Hr24TimeFormat.format(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;
    }*/

    /**
     * Gets time from date.
     *
     * @param date the date
     * @return time from date
     */
    fun getTimeFromDate(date: Date): String {
        val localDateFormat = SimpleDateFormat("HH:mm")
        val time = localDateFormat.format(date)
        println(time)
        return time
    }


    fun getCurrentDate(outputFormate: String): String {
        return SimpleDateFormat(outputFormate, Locale.getDefault()).format(Date())
    }

    fun getCalculatedDate(date: String, inputFormat: String, outputFormat: String, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(inputFormat, Locale.getDefault())
        try {
            cal.time = s.parse(date)
            cal.add(Calendar.DAY_OF_YEAR, days)
            return formatDateFromString(inputFormat, outputFormat, s.format(cal.time))
        } catch (e: ParseException) {
            e.printStackTrace()
            println("Utils.getCalculatedDate : " + e.message)
        }

        return null

    }

    /**
     * Gets date time from date.
     *
     * @param date the date
     * @return date time from date
     */
    fun getDateTimeFromDate(date: Date): String {
        //        2017-04-13 12:00
        val localDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val time = localDateFormat.format(date)
        println(time)
        return time
    }

    /**
     * Gets day.
     *
     * @param date the date
     * @return the day
     */
    fun getDay(date: Date): Int {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.time = date
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    /**
     * Gets days diffrance.
     *
     * @param inputFormate the input formate
     * @param currentDate  the current date
     * @param endDate      the end date
     * @return days diffrance
     */
    fun getDaysDiffrance(inputFormate: String, currentDate: String, endDate: String): Int {
        val myFormat = SimpleDateFormat(inputFormate, Locale.getDefault())
        //        endDate = Utils.getLocalfromUtc(endDate, "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd");
        println("Utils.getDaysDiffrance : $endDate")
        var diffrance = 0

        try {
            val date1 = myFormat.parse(currentDate)
            val date2 = myFormat.parse(endDate)
            val diff = date2.time - date1.time
            diffrance = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
            println("Days: $diffrance")

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return diffrance

    }


    /**
     * Validation Method for Edit Text
     *
     * @param editText          the edit text
     * @param emptyErrorMessage the empty error message
     * @return boolean
     */
    fun isEmptyString(editText: EditText, emptyErrorMessage: String): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.length == 0) {
            editText.setText("")
            editText.error = emptyErrorMessage
            editText.requestFocus()
            return true
        } else
            return false
    }

    fun isStringLengthValid(editText: EditText, length: Int, emptyErrorMessage: String): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.length < length) {
            //            editText.setText("");
            editText.error = emptyErrorMessage
            editText.requestFocus()
            return true
        } else
            return false
    }

    fun isStringLengthValid(editText: AppCompatEditText, length: Int, emptyErrorMessage: String): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.length < length) {
            //            editText.setText("");
            editText.error = emptyErrorMessage
            editText.requestFocus()
            return true
        } else
            return false
    }

    /**
     * Validation Method for Edit Text
     *
     * @param editText          the edit text
     * @param emptyErrorMessage the empty error message
     * @return boolean
     */
    fun isEmptyString(editText: AppCompatEditText, emptyErrorMessage: String): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.length == 0) {
            editText.setText("")
            editText.error = emptyErrorMessage
            editText.requestFocus()
            return true
        } else
            return false
    }

    /**
     * Is gps enabled boolean.
     *
     * @param mContext the m context
     * @return the boolean
     */
    fun isGPSEnabled(mContext: Context): Boolean {
        val manager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Compress img  string.
     *
     * @param filePath the file path
     * @return the string
     */
    fun compressImg(filePath: String): String {
        //System.out.println("File path : " + filePath);
        val file = File(filePath)
        // System.out.println("File path name : " + file.getName());

        var bmp = BitmapFactory.decodeFile(filePath)
        var out: FileOutputStream? = null
        val filename = getFilename(file.name)

        try {
            val ei = ExifInterface(filePath)
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            println("Orientation---<> $orientation")
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 ->
                    //                    bitmap = rotateBitmap(bitmap, 90f);
                    bmp = rotateBitmap(bmp, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 ->
                    //                    bitmap = rotateBitmap(bitmap, 180f);
                    bmp = rotateBitmap(bmp, 180f)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            out = FileOutputStream(filename)
            //   write the compressed bitmap at the destination specified by filename.
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return filePath
        }
        //        System.out.println("File path final name : " + filename);
        return filename
    }

    /**
     * Gets filename.
     *
     * @param filename the filename
     * @return the filename
     */
    fun getFilename(filename: String): String {
        val fileName: String
        val file1 = File(filename)
        val file = File(Environment.getExternalStorageDirectory(), "Jolly Motors/Jolly Motors Pictures/")
        if (!file.exists()) {
            file.mkdirs()
        }
        val filenameArray = file1.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val extension = filenameArray[filenameArray.size - 1]
        if (extension.equals("png", ignoreCase = true) || extension.equals("jpeg", ignoreCase = true)) {
            fileName = filenameArray[0] + ".jpg"
        } else {
            fileName = file1.name
        }
        return file.absolutePath + "/" + fileName
    }

    /**
     * Call activity.
     *
     * @param mContext     the m context
     * @param activityName the activity name
     */
    fun callActivity(mContext: Context, activityName: Class<*>) {
        val intent = Intent(mContext, activityName)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mContext.startActivity(intent)

    }

    /**
     * Make snakebare.
     * @param view the view
     * @param msg  the msg
     */
    fun makeSnakebare(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Convert dp to pixel int.
     * @param dp      the dp
     * @param context the context
     * @return the int
     */
    fun convertDpToPixel(dp: Int, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * Convert pixels to dp int.
     * @param px      the px
     * @param context the context
     * @return the int
     */
    fun convertPixelsToDp(px: Int, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * Share app.
     * @param mContext the m context
     * @param message  the message
     */
    fun shareApp(mContext: Context, message: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, message)
        sendIntent.type = "text/plain"
        mContext.startActivity(Intent.createChooser(sendIntent, "Share to"))
    }

    /**
     * The interface Snake bar click.
     */
    interface SnakeBarClick {
        /**
         * On item click.
         * @param view the view
         */
        fun onItemClick(view: View)
    }

    /**
     * Make snakebar.
     * @param view          the view
     * @param msg           the msg
     * @param snakeBarClick the snake bar click
     */
    fun makeSnakebar(view: View, msg: String, snakeBarClick: SnakeBarClick) {
        Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("RETRY") { view -> snakeBarClick.onItemClick(view) }.setActionTextColor(Color.WHITE).show()
    }

    /**
     * Sets location.
     * @param lat     the lat
     * @param lng     the lng
     * @param context the context
     */
    fun setLocation(lat: Double?, lng: Double?, context: Context) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val mEdit = sp.edit()
        mEdit.remove("userLat")
        mEdit.remove("userLng")
        mEdit.putString("userLat", lat.toString())
        mEdit.putString("userLng", lng.toString())
        mEdit.commit()
        return
    }

    /**
     * Gets user location.
     * @param context the context
     * @return the user location
     */
    fun getUserLocation(context: Context): Location {
        val mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val lat = java.lang.Double.valueOf(mSharedPreference.getString("userLat", "0.00"))
        val lng = java.lang.Double.valueOf(mSharedPreference.getString("userLng", "0.00"))
        val user = Location("user")
        user.latitude = lat!!
        user.longitude = lng!!
        return user
    }

    /**
     * Open app store.
     * @param activity the activity
     */
    fun openAppStore(activity: Activity) {
        try {
            // "market://details?id=com.example.abc"
            val playStorePath = "market://details?id=" + activity.packageName
            val uri = Uri.parse(playStorePath)
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(myAppLinkToMarket)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Gets complete address string.
     *
     * @param context   the context
     * @param LATITUDE  the latitude
     * @param LONGITUDE the longitude
     * @return complete address string
     */
    fun getCompleteAddressString(context: Context, LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                println("Utils.getCompleteAddressString : " + returnedAddress.featureName)
                val strReturnedAddress = StringBuilder("")

                for (i in 0 until returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("My  loction address", "" + strReturnedAddress.toString())
            } else {
                Log.w("My  loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("My  loction address", "Canont get Address!")
        }

        return strAdd
    }

    /**
     * Hides the soft keyboard
     * @param activity the activity
     */
    fun hideSoftKeyboard(activity: Activity?) {
        if (activity?.currentFocus != null) {
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    /**
     * Gets address from location.
     * @param context     the context
     * @param m_latitude  the m latitude
     * @param m_longitude the m longitude
     * @return address from location
     */
    @Synchronized
    fun getAddressFromLocation(context: Context, m_latitude: Double, m_longitude: Double): Address? {
        val my_city_name = ""
        var address: Address? = null
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(m_latitude, m_longitude, 1)
            val sb = StringBuilder()
            if (addresses.size > 0) {
                address = addresses[0]
                //                sb.append(address.getLocality());
            }
            //            my_city_name = sb.toString();
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return address
    }

    /**
     * Gets custom date string.
     * @param strdate the strdate
     * @return custom date string
     */
    fun getCustomDateString(strdate: String): String {
        var str = ""
        try {
            val strStartDateTime = getLocalfromUtc(strdate, "", "")
            str = formatDateFromString("MMM dd, yyyy HH:mm", "MMM d", strStartDateTime)
            /*     Date oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strdate);
            Date oldDate1 = Info.dbDateTimeFormat1.parse(strdate);*/
            val tmp = SimpleDateFormat("MMMM d")
            //            Date date = tmp.parse(strdate);
            //            str = tmp.format(oldDate);
            val oldDate = tmp.parse(str)
            str = str.substring(0, 1).toUpperCase() + str.substring(1)
            if (oldDate.date > 10 && oldDate.date < 14)
                str = "$str th"
            else {
                if (str.endsWith("1"))
                    str = "$str st"
                else if (str.endsWith("2"))
                    str = "$str nd"
                else if (str.endsWith("3"))
                    str = "$str rd"
                else
                    str = "$str th"
            }
            //        tmp = new SimpleDateFormat("yyyy");
            //            str = str + tmp.format(oldDate);
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    /**
     * Gets deviceid.
     * @param context the context
     * @return the deviceid
     */
    fun getDeviceid(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * Is valid email boolean.
     * @param target the target
     * @return boolean
     */
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    /**
     * Is valid mobile boolean.
     * @param phone the phone
     * @return boolean
     */
    fun isValidMobile(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    /**
     * Gets city name.
     * @param context  the context
     * @param location the location
     * @return city name
     */
    fun getCityName(context: Context, location: Location): String {
        var Cityname = ""
        val geoCoder = Geocoder(context, Locale.getDefault())
        try {
            val address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            val maxLines = address[0].maxAddressLineIndex
            for (i in 0 until maxLines) {
                Cityname = address[0].locality
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return Cityname
    }

    /**
     * Make dialog with ok.
     * @param mContext the m context
     * @param message  the message
     */
    fun makeDialogWithOk(mContext: Context, message: String) {
        AlertDialog.Builder(mContext).setMessage(message).setCancelable(false)
            .setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }.show()
    }

    /**
     * Change date formate string.
     * @param time the time
     * @return the string
     */
    fun ChangeDateFormate(time: String): String? {
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        val outputPattern = "dd MMM, yyyy"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
        val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }

    /**
     * Keyboard hide.
     * @param context the context
     */
    fun KeyboardHide(context: Context) {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    /**
     * Rotate image if needed bitmap.
     * @param photoPath the photo path
     * @return the bitmap
     */
    fun rotateImageIfNeeded(photoPath: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val bmOptions = BitmapFactory.Options()
            bitmap = BitmapFactory.decodeFile(photoPath, bmOptions)

            val ei = ExifInterface(photoPath)
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            println("Orientation---<> $orientation")
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> bitmap = rotateBitmap(bitmap, 180f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    /**
     * Rotate bitmap bitmap.
     *
     * @param source the source
     * @param angle  the angle
     * @return the bitmap
     */
    // Rotate given bitmap to desired angle like 90,180 degrees
    private fun rotateBitmap(source: Bitmap?, angle: Float): Bitmap {
        println("Rotate--> $angle")
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source!!, 0, 0, source.width, source.height, matrix, true)
    }

    fun isMyServiceRunning(serviceClass: Class<*>, mContext: Context): Boolean {
        val manager = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    @Throws(IOException::class)
    fun getThumbnailFile(mContext: Context, filePath: File): File {
        println("Utils.getThumbnailFile : $filePath")
        val fileName = filePath.name
        val ext = fileName.substring(fileName.lastIndexOf("."), fileName.length)
        val f = File(mContext.cacheDir, filePath.name.replace(ext, ".png"))
        f.createNewFile()
        val bitmap = ThumbnailUtils.createVideoThumbnail(filePath.path, MediaStore.Video.Thumbnails.MICRO_KIND)
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        println("Utils.getThumbnailFile : File : $f")
        return f
    }

    fun rotateAnimation(imageView: ImageView, degree: Float) {
        imageView.animate().rotation(degree).start()
    }

    fun checkPermissions(mContext: Context): Boolean {
        var result: Int
        val listPermissionsNeeded = ArrayList<String>()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(mContext, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                mContext as AppCompatActivity,
                listPermissionsNeeded.toTypedArray(),
                MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    @Throws(ParseException::class)
    fun isTimeBetweenTwoTime(initialTime: String, finalTime: String, currentTime: String): Boolean {
        val reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"
        if (initialTime.matches(reg.toRegex()) && finalTime.matches(reg.toRegex()) &&
            currentTime.matches(reg.toRegex())
        ) {
            //Start Time
            //all times are from java.util.Date
            val inTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(initialTime)
            val calendar1 = Calendar.getInstance()
            calendar1.time = inTime

            //Current Time
            val checkTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(currentTime)
            val calendar3 = Calendar.getInstance()
            calendar3.time = checkTime

            //End Time
            val finTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(finalTime)
            val calendar2 = Calendar.getInstance()
            calendar2.time = finTime

            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1)
                calendar3.add(Calendar.DATE, 1)
            }

            val actualTime = calendar3.time
            return (actualTime.after(calendar1.time) || actualTime.compareTo(calendar1.time) == 0) && actualTime.before(
                calendar2.time
            )
        }
        return false
    }

    fun scanGallery(imageFile:File,context:Context){
        if (imageFile.exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }

    }

    fun scanDir(act:Activity) {
        MediaScannerConnection.scanFile(
            act, arrayOf(Environment.getExternalStorageDirectory().toString()), null
        ) { path, uri ->
            Log.i("ExternalStorage", "Scanned $path:")
            Log.i("ExternalStorage", "-> uri=$uri")
        }
    }

    fun getDisHeight(act: FragmentActivity?): Int {
        val displayMetrics = DisplayMetrics()
        act?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getDisWidth(act: Activity?): Int {
        val displayMetrics = DisplayMetrics()
        act?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun popAllFragment(activity: FragmentActivity) {
        activity.supportFragmentManager.popBackStackImmediate(null, 1)
    }

    fun backFromFragment(act: FragmentActivity, fragment: Fragment) {
        act.supportFragmentManager.popBackStackImmediate(fragment.javaClass.name, 1)
    }

    fun replaceFragment(act: FragmentActivity, fragment: Fragment, container: Int) {
        act.supportFragmentManager.beginTransaction()
            .replace(container, fragment, null)
            .addToBackStack(fragment.javaClass.name)
            .commit()
    }

    fun setFragment(act: FragmentActivity, fragment: Fragment, container: Int) {
        act.supportFragmentManager
            .beginTransaction()
            .add(container, fragment, null)
            .addToBackStack(fragment.javaClass.name)
            .commit()
    }

    fun FragmentManager.removeFragment(tag: String) {
        this.beginTransaction()
            .disallowAddToBackStack()
            .remove(this.findFragmentByTag(tag)!!)
            .commitNow()
    }

    fun FragmentManager.addFragment(containerViewId: Int, fragment: Fragment, tag: String) {
        this.beginTransaction().disallowAddToBackStack()
            .add(containerViewId, fragment, tag)
            .commit()
    }

    /*fun pushFragment(act: FragmentActivity, fragment: Fragment) {
        val manager = act.supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.tabContent, fragment)
        ft.commit()
    }*/

    fun getTimeStamp(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }

    fun getDirectoryPath(activity: Activity): String {
        try {
            val fileDir =
                File(Environment.getExternalStorageDirectory().toString() + File.separator + activity.packageName)
            fileDir.mkdirs()
            return fileDir.path
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getCollectionDirectoryPath(activity: Activity): String {
        try {
            val fileDir = File(Environment.getExternalStorageDirectory().toString() + File.separator + activity.packageName+ File.separator +".collections")
            fileDir.mkdirs()
            return fileDir.path
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun changeBG(background:Drawable?,colorInt:Int){
        try {
            when (background) {
                is ShapeDrawable -> (background.mutate() as ShapeDrawable).paint.color = colorInt
                is GradientDrawable -> (background.mutate() as GradientDrawable).setColor(colorInt)
                is ColorDrawable -> (background.mutate() as ColorDrawable).color = colorInt
                else -> Log.w("TAG", "Not a valid background type")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun createFileFromBitmap(bitmap: Bitmap?, imageDirPath: String, imageName: String): Uri? {
        try {
            var fOut: OutputStream? = null
            val fileRootPath = File(imageDirPath)
            fileRootPath.mkdirs()

            val fileImage = File(imageDirPath, imageName)
            try {
                fOut = FileOutputStream(fileImage)
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fOut)
                } else {
                    //  Log.d("bitmap", "bitmap is null");
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fOut?.close()
            }
            return Uri.fromFile(fileImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun getCameraFunctionsPermissions(activity: Activity): ArrayList<String> {
        val perArray = ArrayList<String>()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return perArray
        }
        if (!checkPermission(activity, Manifest.permission.CAMERA)) {
            perArray.add(Manifest.permission.CAMERA)
        }
        if (!checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            perArray.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            perArray.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return perArray
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun getStoragePermissions(activity: Activity): ArrayList<String> {
        val perArray = ArrayList<String>()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return perArray
        }
        if (!checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            perArray.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            perArray.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return perArray
    }

    fun hasStoragePermission(act:Activity):Boolean{
        val perArray = Utils.getStoragePermissions(act)
        if (perArray.size != 0) {
            val sPer = perArray.toTypedArray()
            ActivityCompat.requestPermissions(act, sPer, 123)
            return false
        }
        return true
    }

    /**
     * Check wiather the user granted the permission or not.
     *
     * @param permission
     * @return
     */
    fun checkPermission(activity: Activity, permission: String): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, permission)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    fun configureTransform(viewWidth: Int, viewHeight: Int, mPreviewSize: Camera.Size, context: Activity): Matrix {

        val rotation = context.windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(0f, 0f, mPreviewSize.height.toFloat(), mPreviewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale = Math.max(
                viewHeight.toFloat() / mPreviewSize.height,
                viewWidth.toFloat() / mPreviewSize.width
            )
            matrix.postScale(scale, scale, centerX, centerY)
            matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180f, centerX, centerY)
        }
        return matrix
    }

    fun chooseOptimalSize(
        choices: Array<Camera.Size>,
        textureViewWidth: Int,
        textureViewHeight: Int,
        maxWidth: Int,
        maxHeight: Int,
        aspectRatio: Camera.Size
    ): Camera.Size {

        // Collect the supported resolutions that are at least as big as the preview Surface
        val bigEnough = ArrayList<Camera.Size>()
        // Collect the supported resolutions that are smaller than the preview Surface
        val notBigEnough = ArrayList<Camera.Size>()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.width <= maxWidth && option.height <= maxHeight &&
                option.height == option.width * h / w
            ) {
                if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                    bigEnough.add(option)
                } else {
                    notBigEnough.add(option)
                }
            }
        }

        if (bigEnough.size > 0) {
            return Collections.min(bigEnough, Utils.CompareSizesByArea())
        } else if (notBigEnough.size > 0) {
            return Collections.max(notBigEnough, Utils.CompareSizesByArea())
        } else {
            Log.e("tag", "Couldn't find any suitable preview size")
            return choices[0]
        }
    }

    internal class CompareSizesByArea : Comparator<Camera.Size> {
        override fun compare(lhs: Camera.Size, rhs: Camera.Size): Int {
            return java.lang.Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
        }
    }

    class ImageSaver(private val mImage: Image, private val mFile: File) : Runnable {
        override fun run() {
            val buffer = mImage.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            var output: FileOutputStream? = null
            try {
                output = FileOutputStream(mFile)
                output.write(bytes)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                mImage.close()
                if (null != output) {
                    try {
                        output.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}