package com.example.androidgetter;

/*
 * http://stackoverflow.com/questions/2322234/how-to-find-serial-number-of-android-device 
 * http://stackoverflow.com/questions/2556495/get-owners-email-address
 */

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView emFb = ((TextView) findViewById(R.id.feedback_email));
		emFb.setText("");
		// Getting synchronized accounts (Skype ==> username)
		Context context = this.getApplicationContext();
		List<String> emails = getEmails(context);
		for (String email : emails) {
			emFb.append(email + "\n");
		}

		// Getting profile information ( !!! API 14 required <=> android 4.0+ )
		emFb.append("\n" + readProfile());

		// Getting android device's ID (1) -- semble fiable sauf en cas de reset
		// usine ou sur version 2.2(?)
		TextView idFb = (TextView) findViewById(R.id.feedback_id);
		final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		idFb.setText("Android ID: " + androidId);

		// Getting android device's ID (2)
		String ts = Context.TELEPHONY_SERVICE;
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
		String imsi = mTelephonyMgr.getSubscriberId();
		idFb.append("\n IMSI: " + imsi);

		// Getting android device's ID (3)
		String imei = mTelephonyMgr.getDeviceId();
		idFb.append("\n IMEI: " + imei);

		// Getting phone number (READ_PHONE_STATE needed)
		TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		idFb.append("\n Phone number : " + mPhoneNumber);

	}

	private String readProfile() {
		String columnValue = "";
		String columnName = "";
		String returnedString = "";
		Cursor c = MainActivity.this.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		int count = c.getCount();
		String[] columnNames = c.getColumnNames();
		int position = c.getPosition();
		if (count == 1 && position == 0) {
			for (int j = 0; j < columnNames.length; j++) {
				columnName = columnNames[j];
				columnValue = c.getString(c.getColumnIndex(columnName));
				if (columnName.equals("display_name")) {
					returnedString += "\n" + columnName + " " + columnValue;
				}
			}
		}
		c.close();
		System.out.println(returnedString);
		return returnedString;
	}

	private List<String> getEmails(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = getAccounts(accountManager);
		List<String> emails = new ArrayList<String>();
		if (accounts != null) {
			int i = 0;
			for (i = 0; i < accounts.length; i++) {
				emails.add(accounts[i].name);
			}
		}
		return emails;
	}

	private static Account[] getAccounts(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType(null);
		return accounts;
	}
}
