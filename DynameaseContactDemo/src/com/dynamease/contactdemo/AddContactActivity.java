package com.dynamease.contactdemo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.widget.EditText;

public class AddContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_contact);
	}

	public void addContact(View v) {
		AccountManager manager = AccountManager.get(getApplicationContext());
		Account[] accounts = manager.getAccountsByType(Constants.ACCOUNT_TYPE);
		if (accounts.length > 0) {
			// Retrieve First Dynamease account on phone
			Account acc = accounts[0];
			ContentValues values = new ContentValues();
			values.put(RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
			values.put(RawContacts.ACCOUNT_NAME, acc.name);
			// Get uri to a new contact associated to this account
			Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
			System.out.println(rawContactUri + " " + acc.name);
			long rawContactId = ContentUris.parseId(rawContactUri);

			// Determine name of the contact
			String contactFullName = ((EditText) findViewById(R.id.edit_contactname)).getText().toString();
			String contactFirstName = "";
			String contactLastName = "";
			try {
				String[] contactNames = contactFullName.split(" ", 2);
				contactFirstName = contactNames[0];
				contactLastName = contactNames[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			int contactId = 42;
			// Set the name of the contact
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
			values.put(StructuredName.DISPLAY_NAME, contactFullName);
			getContentResolver().insert(Data.CONTENT_URI, values);

			// Add Dynamease contact proposition a info to the contact.
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, "vnd.android.cursor.item/vnd.dynamease.profile");
			values.put(Data.DATA3, "Contacter avec Dynamease.");
			values.put(Constants.ID_COL_NAME, contactId);
			values.put(Constants.FIRSTNAME_COL_NAME, contactFirstName);
			values.put(Constants.LASTNAME_COL_NAME, contactLastName);
			getContentResolver().insert(Data.CONTENT_URI, values);
		}
	}
}
