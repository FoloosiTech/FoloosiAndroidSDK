package com.foloosi.sample;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.foloosi.core.FPayListener;
import com.foloosi.core.FoloosiLog;
import com.foloosi.core.FoloosiPay;
import com.foloosi.models.Customer;
import com.foloosi.models.OrderData;

import java.util.Random;

public class ActDemoPay extends AppCompatActivity implements FPayListener {

    private EditText edtAmount, edtCurrencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_demo_pay);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        edtAmount = findViewById(R.id.edt_cost);
        edtCurrencyCode = findViewById(R.id.edt_currency);
        findViewById(R.id.btn_guest_pay).setOnClickListener(v -> onPaymentClick());
    }

    private void onPaymentClick() {
        try {
            String amount = edtAmount.getText().toString();
            String currencyCode = edtCurrencyCode.getText().toString();
            if (amount.isEmpty())
                showToast("Amount is empty");
            else if (currencyCode.isEmpty())
                showToast("CurrencyCode is empty");
            else {
                FoloosiLog.setLogVisible(true);
                FoloosiPay.init(this, "test_$2y$10$nBFlhIbZ0xA1A0.-MPvoP.v45N5oiAJeBPomyWw-dya-GEUtqZKiy");

                OrderData orderData = new OrderData();
                orderData.setOrderAmount(Double.parseDouble(amount));
                Random rand = new Random();
                int orderId = rand.nextInt(100000);
                orderData.setCustomColor("#ab34fd");
                orderData.setOrderId(String.valueOf(orderId));
                orderData.setOrderDescription("Mobile Phone");
                orderData.setCurrencyCode(currencyCode);
                Customer customer = new Customer();
                customer.setName("name");
                customer.setEmail("email@gmail.com");
                customer.setMobile("123456789");
                orderData.setCustomer(customer);
                FoloosiPay.setPaymentListener(this);
                FoloosiPay.makePayment(orderData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransactionSuccess(String transactionId) {
        showToast("isSuccess::" + transactionId);
    }

    @Override
    public void onTransactionFailure(String error) {
        showToast(error);
    }

    @Override
    public void onTransactionCancelled() {
        // Cancelled by User
        Log.v("Cancel::", "Cancelled");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}