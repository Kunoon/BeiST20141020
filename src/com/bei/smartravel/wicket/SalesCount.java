package com.bei.smartravel.wicket;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.ITable;
import com.bei.smartravel.chen.TickWorker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SalesCount extends Activity {
	
	private Button backBtn, countBtn;
	private LinearLayout scDatePickerL, scItemsL;
	private RelativeLayout scRelativeLayout;
	private DatePicker startDatePicker, endDatePicker;
	private ListView listView;
	private Calendar c;
	private TickWorker myTICTicketChecker = null;
	private long lSTime, lETime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sales_count);
		
		myTICTicketChecker = TickWorker.getAction();
		
		scDatePickerL = (LinearLayout) findViewById(R.id.sc_date_picker);
		scItemsL = (LinearLayout) findViewById(R.id.sc_items);
		scRelativeLayout = (RelativeLayout) findViewById(R.id.sales_count_relativeLayout);
		
		listView = (ListView) scItemsL.findViewById(R.id.sales_count_item_listview);
		backBtn = (Button) findViewById(R.id.salesCountBack);
		countBtn = (Button) findViewById(R.id.salesCountButton);
		
		startDatePicker = (DatePicker) scDatePickerL.findViewById(R.id.scStartDatePicker);
		endDatePicker = (DatePicker) scDatePickerL.findViewById(R.id.scEndDatePicker);
		
		
		
		c = Calendar.getInstance();

		try {
			lSTime = new SimpleDateFormat("yyyy/MM/dd").parse(
					new SimpleDateFormat("yyyy/MM/dd").format(c.getTime())).getTime()-1;
			lETime = new SimpleDateFormat("yyyy/MM/dd").parse(
					new SimpleDateFormat("yyyy/MM/dd").format(c.getTime())).getTime()-1;
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		startDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 
				new DatePicker.OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						c.set(year, monthOfYear, dayOfMonth);
						try {
							lSTime = new SimpleDateFormat("yyyy/MM/dd").parse(
									new SimpleDateFormat("yyyy/MM/dd").format(c.getTime())).getTime()-1;
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				});
		endDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 
				new DatePicker.OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						c.set(year, monthOfYear, dayOfMonth);
						try {
							lETime = new SimpleDateFormat("yyyy/MM/dd").parse(
									new SimpleDateFormat("yyyy/MM/dd").format(c.getTime())).getTime()-1;
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				});
		
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		countBtn.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unused")
			@Override
			public void onClick(View arg0) {
				long lg = 86400000;
				if(lSTime > lETime) {
					Toast.makeText(getApplicationContext(), "时间设置不正确", Toast.LENGTH_SHORT).show();
				} else {
					ITable re = myTICTicketChecker.salesQuery(lSTime, (lETime+lg));
					List<HashMap<String, Object>> listData = new ArrayList<HashMap<String,Object>>();
					listData.clear();
					try {
						int ordeNum = 0, peopleNum = 0;
						while (re.moveNext()) {
							HashMap<String, Object> hashMapItem = new HashMap<String, Object>();
							hashMapItem.put("goods_name", 
									URLDecoder.decode(re.getData("goods_name"), "GBK"));
							hashMapItem.put("order_num", re.getData("order_num"));
							hashMapItem.put("people_num", re.getData("people_num"));
							ordeNum += Integer.valueOf(re.getData("order_num"));
							peopleNum += Integer.valueOf(re.getData("people_num"));
							listData.add(hashMapItem);
						}
						HashMap<String, Object> hashMapItem = new HashMap<String, Object>();
						hashMapItem.put("goods_name", "合计：");
						hashMapItem.put("order_num", Integer.toString(ordeNum));
						hashMapItem.put("people_num", Integer.toString(peopleNum));
						listData.add(hashMapItem);
						
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					ListAdapter adapter = new SimpleAdapter(getApplicationContext(), listData, 
							R.layout.sales_count_items, new String[] {"goods_name", "order_num", "people_num"}, 
							new int[] {R.id.sales_item_goodname, R.id.sales_item_ordernum, R.id.sales_item_peoplenum});
					listView.setAdapter(adapter);

					scDatePickerL.setVisibility(View.GONE);
					scItemsL.setVisibility(View.VISIBLE);
					scRelativeLayout.setVisibility(View.GONE);
				}
			}
		});
	}

}
