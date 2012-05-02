package com.android;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class ContactsManager {

	private Cursor cur;
	private ContentResolver cr;

	public Cursor getCur() {
		return cur;
	}

	public void setCur(Cursor cur) {
		this.cur = cur;
	}

	public ContentResolver getCr() {
		return cr;
	}

	public void setCr(ContentResolver cr) {
		this.cr = cr;
	}

	// Get Contact List
	public ContactList getContactList() {
		ContactList contacts = new ContactList();
		String id;
		this.cur = this.cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (this.cur.getCount() > 0) {
			while (cur.moveToNext()) {
				Contact c = new Contact();
				id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				c.setId(id);
				c.setDisplayName(cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
				c.setStructuredName(this.getStructuredName(id));
				c.setPhoto(this.getContactPhoto(id));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					c.setPhone(this.getPhoneNumbers(id));
				}
				contacts.addContact(c);
			}
		}
		return (contacts);
	}

	private MyStructuredName getStructuredName(String id){
		MyStructuredName structuredName=new MyStructuredName();
		String where=ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID +"=?";
		String[] args=new String[]{id};
		Cursor mCursor = this.cr.query(ContactsContract.Data.CONTENT_URI, null, where, args, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
		while(mCursor.moveToNext()){
			String display=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
			String family=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
			String given=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
			String middle=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
			String phoneticFamily=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME));
			String phoneticGiven=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME));
			String phoneticMiddle=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME));
			String prefix=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
			String suffix=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX));
			
			structuredName.setDisplayName(display);
			structuredName.setFamilyName(family);
			structuredName.setGivenName(given);
			structuredName.setMiddleName(middle);
			structuredName.setNamePrefix(prefix);
			structuredName.setNameSuffix(suffix);
			structuredName.setPhoneticFamilyName(phoneticFamily);
			structuredName.setPhoneticGivenName(phoneticGiven);
			structuredName.setPhoneticMiddleName(phoneticMiddle);
		}
		return structuredName;
	}
	
	public Bitmap getContactPhoto(String id){
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
		InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
		return BitmapFactory.decodeStream(input);
		//return input;
	}
	
	public ArrayList<Phone> getPhoneNumbers(String id) {
		ArrayList<Phone> phones = new ArrayList<Phone>();

		Cursor pCur = this.cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[] { id }, null);
		while (pCur.moveToNext()) {
			phones.add(new Phone(
					pCur.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
					pCur.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))));

		}
		pCur.close();
		return (phones);
	}

}
