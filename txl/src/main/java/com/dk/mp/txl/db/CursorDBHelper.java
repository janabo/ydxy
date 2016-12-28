package com.dk.mp.txl.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;

import com.dk.mp.core.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @since 
 * @version 2013-4-2
 * @author wwang
 */
public class CursorDBHelper {
	/** 获取库Phon表字**/

	private static final String[] PHONES_PROJECTION = new String[] {

	Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** 联系人显示名�?**/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** 头像ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;

	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	/**
	 * 向手机通讯录添加联系人
	 * @param context Context
	 * @param name 姓名
	 * @param tel 号码
	 */
	public static void insertPerson(Context context, String name,String tel) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentValues values = new ContentValues();
		//向raw_contacts插入�?��除ID外全部为null的数据，ID自动生成
		long id = ContentUris.parseId(resolver.insert(uri, values));
		//添加联系人姓名
		uri = Uri.parse("content://com.android.contacts/data");
		values.put("raw_contact_id", id);
		values.put("data2", name);
		values.put("mimetype", "vnd.android.cursor.item/name");
		resolver.insert(uri, values);

		String dhhm = tel;

		//添加联系人电话   
		if (StringUtils.isNotEmpty(dhhm)) {
			values.clear(); // 清空上次的数据
			values.put("raw_contact_id", id);
			values.put("data1", dhhm);
			values.put("data2", "2");
			values.put("mimetype", "vnd.android.cursor.item/phone_v2");
			resolver.insert(uri, values);
		}

		//添加联系人邮件
		values.clear();
		values.put("raw_contact_id", id);
		values.put("data1", "");
		values.put("data2", "1");
		values.put("mimetype", "vnd.android.cursor.item/email_v2");
		resolver.insert(uri, values);
	}

	private static Bitmap getPhoto(Context mContext, String tel) {
		ContentResolver resolver = mContext.getContentResolver();

		Bitmap contactPhoto = null;
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, null,
				Phone.NUMBER + "=?", new String[] { tel }, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或�?为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名�?
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				// 得到联系人头像ID
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				// 得到联系人头像Bitamp
				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他�?��默认�?
				if (photoid > 0) {
					Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
					contactPhoto = BitmapFactory.decodeStream(input);
				}
			}
			phoneCursor.close();
		}
		return contactPhoto;
	}

	/**
	 * 更新或新增用户的头像
	 *
	 * @param rawContactId
	 * @param photo
	 */
	private static void updateOrInsertPhoto(Context mContext, String tel, Long rawContactId, Bitmap photo) {
		Bitmap oldPhoto = getPhoto(mContext, tel);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 将Bitmap压缩成PNG编码，质量为100%存储
		photo.compress(Bitmap.CompressFormat.PNG, 100, os);
		byte[] avatar = os.toByteArray();

		if (oldPhoto != null) {
			ContentValues values = new ContentValues();
			values.put(Photo.PHOTO, avatar);

			String selection = Data.RAW_CONTACT_ID + "=? and " + Data.MIMETYPE + "=?";
			String[] selectionArgs = new String[] { Long.toString(rawContactId), Photo.CONTENT_ITEM_TYPE };

			mContext.getContentResolver().update(Data.CONTENT_URI, values, selection, selectionArgs);
		} else {
			ContentValues values = new ContentValues();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
			values.put(Photo.PHOTO, avatar);

			mContext.getContentResolver().insert(Data.CONTENT_URI, values);
		}
	}

	public static void UpdateContactsPhoto(Context context, int contacts_id, Bitmap bitmap) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 将Bitmap压缩成PNG编码，质量为100%存储
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		byte[] avatar = os.toByteArray();

		int photoRow = -1;
		String where = "raw_contact_id = " + contacts_id + " AND mimetype ='vnd.android.cursor.item/photo'";
		Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI, null, where, null, null);

		int idIdx = cursor.getColumnIndexOrThrow("_id");
		if (cursor.moveToFirst()) {
			photoRow = cursor.getInt(idIdx);
		}
		cursor.close();
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, contacts_id);
		values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
		values.put("is_super_primary", 1);
		values.put(Photo.PHOTO, avatar);
		if (photoRow >= 0) {
			context.getContentResolver().update(Data.CONTENT_URI, values, "_id=" + photoRow, null);
		} else {
			context.getContentResolver().insert(Data.CONTENT_URI, values);
		}
	}

	/**
	 * 判断该号码是否已经存在于手机通讯�?
	 * @param context Context
	 * @param phone 号码
	 * @return boolean
	 */
	public static boolean checkNumber(Context context, String phone) {
		Cursor cursor = null;
		try {
			String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
					Phone.NUMBER };

			// 将自己添加到 msPeers �?
			cursor = context.getContentResolver().query(Phone.CONTENT_URI, projection, // Which columns to return.
					Phone.NUMBER + " = '" + phone + "'", // WHERE clause.
					null, // WHERE clause value substitution
					null); // Sort order.

			if (cursor != null) {
				if (cursor.getCount() > 0) {
					return true;
				}
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return false;
	}
}
