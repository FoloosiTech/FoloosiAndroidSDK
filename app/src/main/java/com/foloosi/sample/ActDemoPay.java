package com.foloosi.sample;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.foloosi.core.FPayListener;
import com.foloosi.core.FoloosiLog;
import com.foloosi.core.FoloosiPay;
import com.foloosi.models.Customer;
import com.foloosi.models.OrderData;
import com.foloosi.models.TransactionData;

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
                FoloosiPay.init(this, "Your Merchant Key");

                OrderData orderData = new OrderData();
                orderData.setTitle("Testing payment title");
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

                FoloosiPay.makePayment(orderData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransactionSuccess(TransactionData data) {
        showToast("isSuccess::" + data.getTransactionID());
    }

    @Override
    public void onTransactionFailure(String error) {
        FoloosiLog.v("Error::" + error);
        showToast(error);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}