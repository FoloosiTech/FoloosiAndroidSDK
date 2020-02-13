package com.foloosi.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.foloosi.core.FPayListener;
import com.foloosi.core.FoloosiPay;
import com.foloosi.models.CurrencyCode;
import com.foloosi.models.Customer;
import com.foloosi.models.OrderAddress;
import com.foloosi.models.OrderData;
import com.foloosi.models.TransactionData;
import com.foloosi.util.FConstants;
import com.foloosi.util.FoloosiLog;
import com.foloosi.util.TToast;

import java.util.Random;

public class ActDemoPay extends AppCompatActivity implements FPayListener {

    private Button btnPaywithFoloosi;

    private EditText edtAmount;

    private String amount;

    private OrderData orderData;

    private Customer customer;

    private OrderAddress orderAddress;

    private Spinner spinnerCurrency;

    private Bundle bundle;

    private static final String MERCHANT_KEY = "YOUR_MERCHANT_KEY";

    private CheckBox checkBoxAddress, checkBoxProfile, checkClear, checkCustomColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_start);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        btnPaywithFoloosi = findViewById(R.id.btn_guest_pay);
        edtAmount = findViewById(R.id.edt_cost);
        checkClear = findViewById(R.id.check_clear);
        spinnerCurrency = findViewById(R.id.spinner);
        checkCustomColor = findViewById(R.id.checkbox_include_custom_color);
        checkBoxAddress = findViewById(R.id.checkbox_include_address);
        checkBoxProfile = findViewById(R.id.checkbox_include_profile);
        btnPaywithFoloosi.setOnClickListener(v -> onPaymentClick());
        bundle = new Bundle();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);
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

            if (amount.isEmpty()) {
                TToast.showToast(this, "Amount is empty");
                return;
            }
            FoloosiLog.setLogVisible(true);
            FoloosiPay.init(this, MERCHANT_KEY);
            orderData = new OrderData();
            orderData.setTitle("Testing payment title");
            orderData.setOrderAmount(Double.parseDouble(amount));
            Random rand = new Random();
            int orderId = rand.nextInt(100000);
            orderData.setOrderId(String.valueOf(orderId));
            orderData.setOrderDescription("Mobile Phone");

            int spinnerSelectedItem = spinnerCurrency.getSelectedItemPosition();
            CurrencyCode currencyCode;
            switch (spinnerSelectedItem) {
                case 1:
                    currencyCode = CurrencyCode.USD;
                    break;
                case 2:
                    currencyCode = CurrencyCode.INR;
                    break;
                case 3:
                    currencyCode = CurrencyCode.EUR;
                    break;
                default:
                    currencyCode = CurrencyCode.AED;
            }
            orderData.setCurrencyCode(currencyCode);

            if (checkBoxProfile.isChecked()) {
                customer = new Customer();
                customer.setName("name");
                customer.setEmail("email@gmail.com");
                customer.setMobile("123456789");
                customer.setAddress("xxxxxxxxx");
                customer.setCity("xxxxxxx");
                orderData.setCustomer(customer);
            }
            if (checkBoxAddress.isChecked()) {
                orderAddress = new OrderAddress();
                orderAddress.setCity("xxxx");
                orderAddress.setCountry("ARE");
                orderAddress.setAddress("xxxxxxxxxxxxxxxxxxx");
                orderAddress.setState("xxxx");
                orderAddress.setPostalCode("123455");
                orderData.setOrderAddress(orderAddress);
                orderData.setShippingSameAsBilling(true);
            }

            if (checkCustomColor.isChecked())
                orderData.setCustomColor("#666666");
            if (checkClear.isChecked())
                FoloosiPay.clearUserData();

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
                        FoloosiLog.v("Transaction Data:::" + transactionData.getTransactionID());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTransactionSuccess(TransactionData data) {
        TToast.showToast(this, "isSuccess::" + data.getTransactionID());
    }

    @Override
    public void onTransactionFailure(String error) {
        FoloosiLog.v("Error::" + error);
        TToast.showToast(this, error);
    }

}