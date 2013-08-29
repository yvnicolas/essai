package com.dynamease.contactdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.widget.TextView;

public class ReactToCallActivity extends Activity {
	private TextView contactName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contactName = (TextView) findViewById(R.id.show_contact_name);
		setContentView(R.layout.show_contact);

		if (getIntent() != null && getIntent().getAction() == Intent.ACTION_VIEW && getIntent().getData() != null) {
			Uri data = getIntent().getData();
			System.out.println(data);
			Cursor cursor = getContentResolver().query(data, null, null, null, null);
			if (cursor != null) {
				String colname = null;
				for (int i = 0; i < cursor.getColumnNames().length; i++) {
					colname = cursor.getColumnNames()[i];
					System.out.println("Count: " + cursor.getColumnCount() + ", " + i + "ème: " + cursor.getColumnNames()[i]);
				}
				contactName.setText(cursor.getString(cursor.getColumnIndex(colname)));
				cursor.close();
			}
		}
	}
}
