package com.example.listviewmysqladapter;

import java.util.ArrayList;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactsAdapter extends BaseAdapter {

	ArrayList<Person> data;
	LayoutInflater inflater;

	public ContactsAdapter(Context context, ArrayList<Person> data ) {
		// TODO Auto-generated constructor stub
		this.data = data;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		/**二层优化：1.用了convertView 列表项的复用。2. 用了holder建立了setTag（holder）减少了findviewbyid的次数。*/
		Holder holder = new Holder();
		//设置数据
		Person person = data.get(position);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.textView1);
			holder.tvPhone = (TextView) convertView.findViewById(R.id.textView2);
		}else{
			holder = (Holder) convertView.getTag();
		}
		/**设置数据-------注意设置数据必须放在这个位置不然会出现重大错误！！！！*/
		holder.tvName.setText(person.getName());//这三行代码如果放在上方holder.tvPhone位置之后就会有错误。
		holder.tvPhone.setText(person.getPhone());
		convertView.setTag(holder);

		return convertView;
	}

	class Holder{
		TextView tvName;
		TextView tvPhone;
	}
}
