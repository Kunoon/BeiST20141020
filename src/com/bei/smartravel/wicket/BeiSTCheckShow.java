package com.bei.smartravel.wicket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.ITable;
import com.bei.smartravel.chen.TickWorker;
import com.bei.smartravel.customization.MySimpleAdapter4TickectsTable;
import com.bei.smartravel.macro.Macro;
import com.wizarpos.drivertest.jniinterface.MyPrinter4XiaoBing;
import com.bei.smartravel.idreader.IDRService4XiaoBing;
import com.example.pc700demo.MyPrinter4ZhiGuLian;
import com.sdses.printer.MyPrinter4ShenSi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
/**
 * @author YongKun
 *
 */
public class BeiSTCheckShow extends Activity{
	private String checkIDStr, checkGIDStr, checkGNameStr, checkGBTimeStr, checkGETimeStr;
	private int CurrentItemID = 0, ShowItemCount = 0, EndItemID = 0, PaperNum, checkGUsedInt, checkGPayInt;
	
	private TickWorker myTICTicketChecker = null;
	private List<HashMap<String ,Object>> myListShowCheckData = null;
	private MySimpleAdapter4TickectsTable myList4CheckSimpleAdapter = null;
	
	private TextView myTextViewCheckCountTickets = null, myTextViewCheck4PaperNum = null;
	private ListView myCheckGoodsListView = null;
	private Button myImageButton = null;
	private CheckBox myCheckBox4All;
	private Button myPrintfButton, myUpPageButton, myDownPageButton;
	
	private boolean printSign = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_show);
		
		myTextViewCheckCountTickets = (TextView)findViewById(R.id.myTextViewCheckCountTickets);
		myTextViewCheck4PaperNum = (TextView)findViewById(R.id.myTextViewCheck4PaperNum);
		myCheckGoodsListView = (ListView)findViewById(R.id.ListView_Show_Check_Goods);
		myImageButton = (Button)findViewById(R.id.checkShowImageButton);
		myCheckBox4All = (CheckBox)findViewById(R.id.CheckBoxAll4Check);
		myPrintfButton = (Button)findViewById(R.id.ButtonCheckPrintf);
		myUpPageButton = (Button)findViewById(R.id.checkUpPageButton);
		myDownPageButton = (Button)findViewById(R.id.checkDownPageButton);

		myPrintfButton.setTextSize(30);
		myUpPageButton.setTextSize(20);
		myDownPageButton.setTextSize(20);
		myListShowCheckData = new ArrayList<HashMap<String , Object>>();
		myList4CheckSimpleAdapter = new MySimpleAdapter4TickectsTable(this, null, myListShowCheckData, false);
		myCheckGoodsListView.setAdapter(myList4CheckSimpleAdapter);
		if(Macro.ACTION_SHENSI)
			myPrintfButton.setVisibility(View.GONE);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int numInt = 0;	// 订单包含人数
		printSign = false;
		myTICTicketChecker = TickWorker.getAction();
		Bundle bundle=this.getIntent().getExtras();
		checkIDStr = bundle.getString(Macro.CHECK_QUERY_PARAMETER_IDNUM);
		checkGIDStr = bundle.getString(Macro.CHECK_QUERY_PARAMETER_GOODSID);
		checkGNameStr = bundle.getString(Macro.CHECK_QUERY_PARAMETER_GOODNAME);
		checkGUsedInt = bundle.getInt(Macro.CHECK_QUERY_PARAMETER_STATUS_USED);
		checkGPayInt = bundle.getInt(Macro.CHECK_QUERY_PARAMETER_STATUS_PAY);
		checkGBTimeStr = bundle.getString(Macro.CHECK_QUERY_PARAMETER_BEGIN_TIME);
		checkGETimeStr = bundle.getString(Macro.CHECK_QUERY_PARAMETER_END_TIME);
		
		ITable re = myTICTicketChecker.query(checkIDStr, checkGIDStr, checkGNameStr
				, checkGUsedInt, checkGPayInt, checkGBTimeStr, checkGETimeStr, null);
		while (re.moveNext()){
			HashMap<String , Object> myHashMapItem = new HashMap<String , Object>();
//			myHashMapItem.put(Macro.GOODS_NUM, Integer.toString(rownum));
			myHashMapItem.put(Macro.GOODS_ID, re.getData("ID"));
			myHashMapItem.put(Macro.GOODS_NAME, re.getData("类型")); // plantime类型useindex other_id
			myHashMapItem.put(Macro.GOODS_QUANTITY, re.getData("数量"));
			myHashMapItem.put(Macro.GOODS_PLANTIME, re.getData("plantime"));
			myHashMapItem.put(Macro.GOODS_PRICE, re.getData("price"));
			myHashMapItem.put(Macro.GOODS_USED_STATUS, re.getData("usedStat"));
			
			myHashMapItem.put(Macro.GOODS_START_TIME, re.getData("startime"));
			myHashMapItem.put(Macro.GOODS_END_TIME, re.getData("endtime"));
			myHashMapItem.put(Macro.GOODS_FROM_WHERE, re.getData("pname"));
			myHashMapItem.put(Macro.GOODS_WEEK, re.getData("week"));
			myHashMapItem.put(Macro.GOODS_VALID, re.getData("valid"));
			myHashMapItem.put(Macro.GOODS_PAY_STATUS, re.getData("PayState"));
			myListShowCheckData.add(myHashMapItem);
			numInt += Integer.valueOf(re.getData("数量"));
		}
		
		myList4CheckSimpleAdapter.InitMyWicketListCheckBox(true);
		myList4CheckSimpleAdapter.notifyDataSetChanged();
		myTextViewCheckCountTickets.append(re.getRowNum() + " 份\n本次查询到订单包含：" + numInt + " 人");
		
		if(myList4CheckSimpleAdapter.getCount() > ShowItemCount) {
			myUpPageButton.setVisibility(View.VISIBLE);
			myDownPageButton.setVisibility(View.VISIBLE);
		}
		
		myImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(Activity.RESULT_OK);
				finish();
			}
		});
		
		myCheckBox4All.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					myList4CheckSimpleAdapter.notifyDataSetChanged();
					myList4CheckSimpleAdapter.InitMyWicketListCheckBox(true);
				} else {
					myList4CheckSimpleAdapter.notifyDataSetChanged();
					myList4CheckSimpleAdapter.InitMyWicketListCheckBox(false);
				}
			}
		});
		
		myPrintfButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!printSign) {
					printSign = true;
					new Thread(printRunable).start();
				}
			}
		});
		
		myCheckGoodsListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				CurrentItemID = firstVisibleItem;
				ShowItemCount = visibleItemCount;
				EndItemID = totalItemCount;
				
				if(totalItemCount%6 == 0) {
					PaperNum = totalItemCount/6;
				} else {
					PaperNum = totalItemCount/6 + 1;
				}
				if((firstVisibleItem+6)%6 == 0) {
					myTextViewCheck4PaperNum.setText("第 " + ((firstVisibleItem+6)/6) 
							+ " 页，共 " + PaperNum + "页");
				} else {
					myTextViewCheck4PaperNum.setText("第 " + ((firstVisibleItem+6)/6+1) 
							+ " 页，共 " + PaperNum + "页");
				}
			}
		});
		
		myUpPageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CurrentItemID -= ShowItemCount;
				if(CurrentItemID > 0) {
					myCheckGoodsListView.setSelection(CurrentItemID);
				} else {
					CurrentItemID = 0;
					myCheckGoodsListView.setSelection(CurrentItemID);
				}
			}
		});
		
		myDownPageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CurrentItemID += ShowItemCount;
				if(CurrentItemID < EndItemID) {
					myCheckGoodsListView.setSelection(CurrentItemID);	
				} else {
					CurrentItemID = 0;
					myCheckGoodsListView.setSelection(CurrentItemID);
				}
			}
		});
	}
	
	Runnable printRunable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE) {
				StopIDService4XiaoBing();
				MyPrinter4XiaoBing.getMyPrinter().OrdersPrinter(GetTicketData(myListShowCheckData), null, Macro.CHECK);
				startService(BeiSTWicket.idIntent);
			} else if(Macro.ACTION_SHENSI_PRINT) {
				MyPrinter4ShenSi.getMyPrinter().OrdersPrinter(GetTicketData(myListShowCheckData), null, Macro.CHECK);
			} else if(Macro.ACTION_ZHIGULIAN) {
				MyPrinter4ZhiGuLian.getMyPrinter().OrdersPrinter(GetTicketData(myListShowCheckData), null, Macro.CHECK);
			}
			printSign = false;
		}
	};

	/**
	 * 获取需要打印的数据
	 * @param myListTicketData4WicketPrinter	存储打印数据
	 */
	private List<HashMap<String, Object>> GetTicketData(List<HashMap<String, Object>> listTicketData4CheckPrinter) {
		listTicketData4CheckPrinter = new ArrayList<HashMap<String, Object>>();
		for(int i = 0; i < myList4CheckSimpleAdapter.getCount(); i++) {
			if(MySimpleAdapter4TickectsTable.getIsSelected().get(i)) {
				listTicketData4CheckPrinter.add(myListShowCheckData.get(i));
			}
		}
		return listTicketData4CheckPrinter;
	}

	private void StopIDService4XiaoBing() {
		//终止身份证读取线程
		IDRService4XiaoBing.isExec = false;
		//该循环的作用相当于，等待身份证读取线程的结束。资源也会由该线程自己释放，以给下面的打印使用
		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(IDRService4XiaoBing.lists.size()==0) {
				break;
			}
		} 
		stopService(BeiSTWicket.idIntent);
	}
}
