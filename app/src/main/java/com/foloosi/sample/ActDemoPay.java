package com.foloosi.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
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
import com.foloosi.util.FConstants;

import java.util.Random;

public class ActDemoPay extends AppCompatActivity implements FPayListener {

    private Button btnPayWithFoloosi;

    private EditText edtAmount, edtCurrencyCode;

    private String amount, currencyCode;

    private OrderData orderData;

    private Customer customer;

    private Bundle bundle;

    private static final String MERCHANT_KEY = "test_$2y$10$nBFlhIbZ0xA1A0.-MPvoP.v45N5oiAJeBPomyWw-dya-GEUtqZKiy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_demo_pay);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        btnPayWithFoloosi = findViewById(R.id.btn_guest_pay);
        edtAmount = findViewById(R.id.edt_cost);
        edtCurrencyCode = findViewById(R.id.edt_currency);
        btnPayWithFoloosi.setOnClickListener(v -> onPaymentClick());
        bundle = new Bundle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void onPaymentClick() {
        try {
            amount = edtAmount.getText().toString();
            currencyCode = edtCurrencyCode.getText().toString();

            if (amount.isEmpty()) {
                showToast("Amount is empty");
                return;
            } else if (currencyCode.isEmpty()) {
                showToast("CurrencyCode is empty");
                return;
            }

            FoloosiLog.setLogVisible(true);
            FoloosiPay.init(this, MERCHANT_KEY);
            orderData = new OrderData();
            orderData.setTitle("Testing payment title");
            orderData.setOrderAmount(Double.parseDouble(amount));
            Random rand = new Random();
            int orderId = rand.nextInt(100000);
            orderData.setCustomColor("#ab34fd");
            orderData.setOrderId(String.valueOf(orderId));
            orderData.setOrderDescription("Mobile Phone");
            orderData.setCurrencyCode(currencyCode);
            customer = new Customer();
            customer.setName("name");
            customer.setEmail("email@gmail.com");
            customer.setMobile("123456789");
            orderData.setCustomer(customer);

            FoloosiPay.makePayment(orderData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TransactionData transactionData = null;

        try {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    bundle = data.getExtras();
                    if (bundle != null)
                        transactionData = bundle.getParcelable(FConstants.TRANSACTION_DATA);

                    if (transactionData != null)
                        com.foloosi.core.FoloosiLog.v("Transaction Data:::" + transactionData.getTransactionID());
                }
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