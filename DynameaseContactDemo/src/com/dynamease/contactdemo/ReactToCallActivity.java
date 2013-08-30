package com.dynamease.contactdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class ReactToCallActivity extends Activity {
	private TextView contactFullName;
	private TextView contactID;
	private TextView contactFirsname;
	private TextView contactLastName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_contact);
		contactFullName = (TextView) findViewById(R.id.show_contact_fullname);
		contactID = (TextView) findViewById(R.id.show_contact_id);
		contactFirsname = (TextView) findViewById(R.id.show_contact_firstname);
		contactLastName = (TextView) findViewById(R.id.show_contact_lastname);

		if (getIntent() != null && getIntent().getAction() == Intent.ACTION_VIEW && getIntent().getData() != null) {
			Uri data = getIntent().getData();
			System.out.println(data);
			Cursor cursor = getContentResolver().query(data, new String[] { Constants.DISPLAY_COL_NAME, Constants.ID_COL_NAME, Constants.FIRSTNAME_COL_NAME, Constants.LASTNAME_COL_NAME }, null, null,
					null);
			// Cursor cursor = getContentResolver().query(data, null, null, null, null);
			if (cursor != null) {

				// Show cursor :
				System.out.println("nb of row: " + cursor.getCount() + ", nb of columns: " + cursor.getColumnCount() + "");
				while (cursor.moveToNext()) {
					for (int i = 0; i < cursor.getColumnNames().length; i++) {
						System.out.println(i + "ème: " + cursor.getColumnNames()[i] + ", Value: " + cursor.getString(i));
					}
				}

				// Retrieve important informations on contact:
				cursor.moveToFirst();
				contactFullName.setText("Fullname: \t" + cursor.getString(cursor.getColumnIndex(Constants.DISPLAY_COL_NAME)));
				contactID.setText("Id: \t" + cursor.getString(cursor.getColumnIndex(Constants.ID_COL_NAME)));
				contactFirsname.setText("Firstname: \t" + cursor.getString(cursor.getColumnIndex(Constants.FIRSTNAME_COL_NAME)));
				contactLastName.setText("Lastname: \t" + cursor.getString(cursor.getColumnIndex(Constants.LASTNAME_COL_NAME)));
				cursor.close();
			}
		}
	}
}
