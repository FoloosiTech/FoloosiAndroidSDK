# Foloosi Android SDK Integration

## Step - 1 - Add Dependency

We distribute our SDK from the Maven Central Repository. To begin with this SDK open your build.gradle file of Module:app and
add the following dependency.

```
implementation 'com.foloosi:FoloosiSDK:1.0.4'
```

## Step - 2 - Initialize SDK 

To initialize the SDK add the below line of code with the merchant key you retrieved from foloosi merchant panel. If you don't have a merchant key create new one.

```
FoloosiPay.init(this, "Your Unique Merchant Key");
```

## Step - 3 - Create Order Data Object with necessary inputs

You can create the order data or payment input with our OrderData Model class. Here you need to provide order id, title, descripiton, currency code, order amount and customer details like name, email, mobile number.

```

OrderData orderData = new OrderData(); // Foloosi Order Data Model Class Instance
orderData.setOrderAmount(Double.parseDouble(amount)); // in double format ##,###.##
Random rand = new Random();
int orderId = rand.nextInt(100000);
orderData.setCustomColor("#ab34fd"); // make payment page loading color as app color. 
orderData.setOrderId(String.valueOf(orderId)); // unique order id. 
orderData.setOrderDescription("Mobile Phone");  // any description.
orderData.setCurrencyCode(currencyCode);
Customer customer = new Customer();
customer.setName("name");
customer.setEmail("email@gmail.com");
customer.setMobile("1234567890");
orderData.setCustomer(customer);

```

## Step - 4 - Implement Payment Listener

Set and Implement our payment listener to receive the payment result for the payment we going to make in Step - 5. Use the below code to obtain the payment result.

```


public class ActDemoPay extends AppCompatActivity implements FPayListener {

    @Override
    public void onTransactionSuccess(String transactionId) {
        // Success Callback
    }

    @Override
    public void onTransactionFailure(String error) {
        // Failure Callback.
    }

    @Override
    public void onTransactionCancelled() {
        // Transaction Cancelled by User
    }

    private void initiatePayment(){
        //Setting payment listener
      FoloosiPay.setPaymentListener(this);
    }

}

```

## Step - 5 - Make Transaction with Foloosi

Use the below line of code to make the payment with the order data you created in Step - 3

```
 private void initiatePayment(){
      //Setting payment listener
      FoloosiPay.setPaymentListener(this);

      //making payment
      FoloosiPay.makePayment(orderData);
 }

```

## Foloosi Log 

You can enable / disable the SDK logs by using the below line of code. By default it will be disabled. 

```
FoloosiLog.setLogVisible(true or false);
```

## Progurad Rules

If you are using Proguard for your builds, modify the Proguard rule file:

```

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.foloosi.**
-keep class com.foloosi.** {*;}

-optimizations !method/inlining/*

```

## Sample Payment Reference.

Please check [this link](https://github.com/FoloosiTech/FoloosiAndroidSDK/blob/master/app/src/main/java/com/foloosi/sample/ActDemoPay.java) for sample payment with above steps.

