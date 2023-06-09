package com.codenesia.donasein.ui.payment

import android.app.DownloadManager.STATUS_FAILED
import android.app.DownloadManager.STATUS_PENDING
import android.app.LocaleConfig.STATUS_SUCCESS
import android.content.Intent
import android.content.res.Resources
import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.codenesia.donasein.databinding.ActivityPaymentBinding
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.LocaleListCompat
import com.codenesia.donasein.R
import com.codenesia.donasein.ui.main.MainActivity
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CANCELED
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_FAILED
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_INVALID
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_PENDING
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_SUCCESS
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    companion object {
        const val TARGET_AMOUNT = "targetAmount"
        var targetAmount = String()

        const val TITLE_DONATION = "titleDonation"
        var titleDonation = String()

        const val REMAINING_AMOUNT = "remainingAmount"
        var remainingAmount = String()

        const val DONATION = "donation"
        var donation = String()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val localeID : Locale = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)

        targetAmount = intent.getStringExtra(TARGET_AMOUNT).toString()
        binding.paymentTvTarget.text = formatRupiah.format(targetAmount.toDouble())

        titleDonation = intent.getStringExtra(TITLE_DONATION).toString()
        binding.paymentTvTitle.text = titleDonation

        remainingAmount = intent.getStringExtra(REMAINING_AMOUNT).toString()
        binding.paymentTvRemaining.text = formatRupiah.format(remainingAmount.toDouble())

        donation = intent.getStringExtra(DONATION).toString()
        binding.paymentTvDonation.text = formatRupiah.format(donation.toDouble())


        initMidtransSdk()
        binding.paymentBtnEmoney.setOnClickListener {
            initMidtransPayment(donation.toInt())
        }

        binding.paymentBtnBank.setOnClickListener {
            initMidtransBank(donation.toInt())
        }

        binding.paymentBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun initMidtransBank(money: Int) {
        val itemDetails = listOf(ItemDetails("donasein01", money.toDouble(), 1, "Bantuan Donasi"))
        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            this,
            launcher,
            SnapTransactionDetail(UUID.randomUUID().toString(), money.toDouble(), "IDR"),
            CustomerDetails(customerDetails.customerIdentifier),
            itemDetails,
            CreditCard(false, null, null, null, null, null, null, null, null, null), // Credit Card
            "customerIdentifier", // User Id
            PaymentCallback("codenesia://midtrans"), // UobEzpayCallback
            GopayPaymentCallback("codenesia://midtrans"), // GopayCallback
            PaymentCallback("codenesia://midtrans"), // ShopeepayCallback
            Expiry(getFormattedTime(System.currentTimeMillis()), Expiry.UNIT_DAY, 1), // expiry (null: default expiry time)
            PaymentMethod.BANK_TRANSFER,
        )
    }

    val shippingAddress = Address(
        null,  // First Name
        null,  // Last Name
        "Jalan Andalas Gang Sebelah No. 1",  // Address
        "Jakarta",  // City
        "10220",  // PostCode
        null,  // Phone Number
        null // Country Code
    )
    val billingAddress = Address(
        null,  // First Name
        null,  // Last Name
        "Jalan Andalas Gang Sebelah No. 1",  // Address
        "Jakarta",  // City
        "10220",  // PostCode
        null,  // Phone Number
        null // Country Code
    )

    val customerDetails = CustomerDetails(
        "orang-baik-donasein",  // Customer Identifier
        "Orang",  // First Name
        "Baik",  // Last Name
        "donasein@codenesia.com",  // Email
        "0213213123",  // Phone Number
        shippingAddress,  // Shipping Address
        billingAddress // Billing Address
    )

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result?.resultCode == RESULT_OK) {
            result.data?.let {
                val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
            }
        }
    }

    private fun initMidtransPayment(target: Int){

        val itemDetails = listOf(ItemDetails("donasein01", target.toDouble(), 1, "Bantuan Donasi"))
        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            this,
            launcher,
            SnapTransactionDetail(UUID.randomUUID().toString(), target.toDouble(), "IDR"),
            CustomerDetails(customerDetails.customerIdentifier),
            itemDetails,
            CreditCard(false, null, null, null, null, null, null, null, null, null), // Credit Card
            "customerIdentifier", // User Id
            PaymentCallback("codenesia://midtrans"), // UobEzpayCallback
            GopayPaymentCallback("codenesia://midtrans"), // GopayCallback
            PaymentCallback("codenesia://midtrans"), // ShopeepayCallback
            Expiry(getFormattedTime(System.currentTimeMillis()), Expiry.UNIT_DAY, 1), // expiry (null: default expiry time)
            PaymentMethod.BANK_TRANSFER,
            listOf(PaymentType.CREDIT_CARD, PaymentType.GOPAY, PaymentType.SHOPEEPAY, PaymentType.UOB_EZPAY), // Enabled Payment (null: enabled all available payment)
        )


    }

    private fun getFormattedTime(currentTimeMillis: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z")
        format.timeZone = TimeZone.getDefault()
        return format.format(currentTimeMillis)
    }


    private fun initMidtransSdk() {
        val clientKey: String = PaymentConfig.MERCHANT_CLIENT_KEY
        val baseUrl: String = PaymentConfig.MERCHANT_BASE_CHECKOUT_URL
        UiKitApi.Builder()
            .withMerchantClientKey(clientKey)
            .withMerchantUrl(baseUrl)
            .withContext(this)
            .enableLog(true)
            .withColorTheme(com.midtrans.sdk.uikit.api.model.CustomColorTheme("#04BA71", "#04BA71", "#047f5f")) // set theme. it will replace theme on snap theme on MAP ( optional)
            .build()
        setLocaleNew("id")

    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(
                UiKitConstants.KEY_TRANSACTION_RESULT,
            )
            if (transactionResult != null) {
                when (transactionResult.status) {
                    UiKitConstants.STATUS_SUCCESS -> {
                        showMessage(1, "Transaction Success")
                    }
                    UiKitConstants.STATUS_PENDING -> {
                        showMessage(3, "Transaction Pending")
                    }
                    UiKitConstants.STATUS_FAILED -> {
                        showMessage(2, "Transaction Failed")
                    }
                    STATUS_CANCELED -> {
                        showMessage(4, "Transaction Canceled")
                    }
                    STATUS_INVALID -> {
                        showMessage(2, "Transaction Invalid")
                    }
                    else -> {
                        showMessage(2, transactionResult.message.toString())
                    }
                }
            } else {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showMessage(check: Int, message: String) {
        if (check == 1) {
            MotionToast.createColorToast(
                this,
                "Success",
                message,
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_medium)
            )
        } else if (check == 2) {
            MotionToast.createColorToast(
                this,
                "Failed",
                message,
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_medium)
            )
        } else if (check == 3) {
            MotionToast.createColorToast(
                this,
                "Pending",
                message,
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_medium)
            )
        } else if (check == 4) {
            MotionToast.createColorToast(
                this,
                "Canceled",
                message,
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_medium)
            )
        }
    }


}