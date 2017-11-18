package com.example.listviewmysqladapter;

import java.util.ArrayList;



import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
/**
 *
 * 1.联系人数据库
 * 2.制作ListView
 * 3.增删该查，结果刷新到ListView上
 * 4.优化（数据库优化，ListView优化）
 *
 */
public class MainActivity extends Activity implements OnClickListener, OnItemLongClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	SQLiteDatabase db;
	ArrayList<Person> data = new ArrayList<Person>();
	ContactsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		findViewById(R.id.button1).setOnClickListener(this);

		MySqliteOpenHelper helper = new MySqliteOpenHelper(this, "contacts.db",
				null, 1);
		db = helper.getWritableDatabase();

		adapter = new ContactsAdapter(this, data);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(this);

		// 查询数据库 --> 保存data中
		// 刷新ListView
		queryAndRefresh();

	}

	/**
	 * 查询数据库 --> 保存data中 刷新ListView
	 */
	private void queryAndRefresh() {

		Cursor cursor = db.query(MySqliteOpenHelper.TABLE_NAME, null, null,
				null, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String name = cursor.getString(cursor
					.getColumnIndex(MySqliteOpenHelper.FIELD_NAME));
			String phone = cursor.getString(cursor
					.getColumnIndex(MySqliteOpenHelper.FIELD_PHONE));

			data.add(new Person(name, phone, id));
			Log.e(TAG, "显示的数据 =" + data);
		}

		// 刷新适配器。通知系统调用适配器中的方法，更新视图
		adapter.notifyDataSetChanged();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// 关闭数据库
		db.close();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// 添加记录
		// 1.用户输入的
		// 2.添加到数据库中
		// 3.更新视图

		Builder builder = new Builder(this);
		builder.setTitle("添加");
		View view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
		final EditText etName = (EditText) view.findViewById(R.id.editText1);
		final EditText etPhone = (EditText) view.findViewById(R.id.editText2);
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String name = etName.getText().toString();
				String phone = etPhone.getText().toString();

				//如何插入信息到数据库有一个类ContentValues
				ContentValues values = new ContentValues();
				values.put(MySqliteOpenHelper.FIELD_NAME, name);
				values.put(MySqliteOpenHelper.FIELD_PHONE, phone);
				//插入数据库
				db.insert(MySqliteOpenHelper.TABLE_NAME, null, values);

				//插入的信息显示在tv上就要tv上就要遍历数据库
				Cursor cursor = db.query(MySqliteOpenHelper.TABLE_NAME, null, null,
						null, null, null, null);
				cursor.moveToLast();  //Move the cursor to the last row.
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				//更新链表，更新视图
				data.add(new Person(name, phone, id));
				Log.e(TAG, "data =" + data);
				adapter.notifyDataSetChanged();  //不仅仅前面所学的控件刷新（计数），适配器也可以刷新。
			}
		});

		builder.setNegativeButton("取消", null);
		builder.create();
		builder.show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   final int position, long id) {
		// TODO Auto-generated method stub

		Builder builder = new Builder(this);
		builder.setTitle("更新数据");
		View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog, null);
		final EditText etName = (EditText) viewDialog.findViewById(R.id.editText1);
		final EditText etPhone = (EditText) viewDialog.findViewById(R.id.editText2);

		//为了长按能够显示出对话框上面有当前信息需要将数据从data里面取出来设置到et上
		etName.setText(data.get(position).getName());
		etPhone.setText(data.get(position).getPhone());
		builder.setView(viewDialog);
		//增加了删除与更新数据操作----------
		builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//删除的话数据操作从id删除，所以对象也通过id来取
				String deleteName = String.valueOf(data.get(position).getId());
				db.delete(MySqliteOpenHelper.TABLE_NAME, "id = ?", new String[]{deleteName});
				//链表删除数据,刷新适配器
				data.remove(position);
				adapter.notifyDataSetChanged();

			}
		});
		builder.setNeutralButton("更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//更新信息就是name与phone的改变，并更新链表、数据库、视图
				String name = etName.getText().toString();
				String phone = etPhone.getText().toString();

				String updateinfo = String.valueOf(data.get(position).getId());
				ContentValues values = new ContentValues();
				values.put("name", String.valueOf(name));
				values.put("phone", String.valueOf(phone));
				db.update(MySqliteOpenHelper.TABLE_NAME, values, "id = ?", new String[]{updateinfo});
				//更新链表，刷新适配器
				Person person = new Person();
				person.setName(name);
				person.setPhone(phone);
				data.set(position, person);   //对于链表没有data.update()方法，但是data有set()与add()方法，这里考虑用set()方法
				adapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create();
		builder.show();

		return false;
	}
}
