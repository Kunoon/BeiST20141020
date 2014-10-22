package com.bei.smartravel.wicket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.Database;
import com.bei.smartravel.chen.ITable;
import com.bei.smartravel.chen.TBTicMger;
import com.bei.smartravel.chen.TickWorker;
import com.bei.smartravel.chen.TicketMger;
import com.bei.smartravel.chen.configer;
import com.bei.smartravel.chen.log;
import com.bei.smartravel.customization.MyEditText4Point;
import com.bei.smartravel.customization.MyIDTextViewActivity;
import com.bei.smartravel.customization.MyFormatDateTime;
import com.bei.smartravel.customization.MyProgressBar;
import com.bei.smartravel.customization.MyToast;
import com.bei.smartravel.customization.MyVoicePoint;
import com.bei.smartravel.customization.MySimpleAdapter4TickectsTable;
import com.bei.smartravel.idreader.IDRService4ShenSi;
import com.bei.smartravel.idreader.IDRService4XiaoBing;
import com.bei.smartravel.macro.Macro;
import com.bei.smartravel.slidingmenu.FlipperView;
import com.bei.smartravel.taobao.xml.TBValidData;
import com.bei.smartravel.customization.IdcardValidator;
import com.wizarpos.drivertest.jniinterface.MyPrinter4XiaoBing;
import com.bei.smartravel.version.down.DownloadVersionFile;
import com.bei.smartravel.version.update.UpdateDialog;
import com.example.pc700demo.MyPrinter4ZhiGuLian;
import com.sdses.idreader.IDRService4ShenSiWPt;
import com.synjones.sdt.IDRService4ZhiGuLian;
import com.sdses.printer.MyPrinter4ShenSi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.DisplayMetrics;
/**
 * @author YongKun
 *
 */			
public class BeiSTWicket extends Activity {
	
	private int CurrentItemID = 0, ShowItemCount = 0, EndItemID = 0, PaperNum = 0;
	private final String[] myData4SpinnerPayStatus = {"全部订单", "未付订单",  "已付订单"};
	private List<String> myData4SpinnerUsedStatus = new ArrayList<String>();
	private ArrayAdapter<String> myAdapter4SpinnerPayStatus, myAdapter4SpinnerUsedStatus;
	
	public static BeiSTWicket instance;
	
	public static Intent idIntent = null;
	private Bundle bundle;
	
	private FlipperView myFlipperView;
//	private GestureDetector myGestureDetector;

	private String myIDNum = "";
	private int myCheckGoodStatusPay = -1, myCheckGoodStatusUsed = -1;
	// 保持屏幕常亮
	private PowerManager myPowerManager = null;
	private WakeLock myWakeLock = null;

	private MyReceiver myReceiver;
	private static boolean isAutoWicket;

	private boolean isMyWicketContent = false, isMyCheckContent = false;
	private View myMenu = null, myMainContent = null, myWicketContent = null, myCheckContent = null;
	private ListView myWicketGoodsListView = null;
	private List<HashMap<String, Object>> myListTicketData4WicketPrinter = null;
	private List<HashMap<String, Object>> myListTicketData4Wicket = null, myListShowWicketData = null;
	private MySimpleAdapter4TickectsTable myWicketSimpleAdapter = null;

	private EditText myCheckIDEditText = null, myCheckGoodsIDEditText = null, myCheckGoodNameEditText = null;
	private CheckBox mySelectAllCheckBox, myPrintCheckBox;
	private MyProgressBar myProgressBar;
	private TextView myWicketTextView4ID = null, myWicketTextView4OrderNum = null,
					 myWicketTextView4OrderPayNum = null, myWicketTextView4PaperNum;
	private Button myWicketImageButton = null, myCheckiImageButton = null, myMainImageButton = null
					, myWicketButton = null, myCheckTimeBeginButton = null,
					myCheckTimeEndButton = null, myCheckCheckButton = null, myButtonIDMain = null,
					myWicketUpPageButton = null, myWicketDownPageButton = null;
	public static TextView netStatusTextView = null;
	private Spinner mySpinnerPayStatus = null, mySpinnerUsedStatus = null;

	private int myYear, myMonthOfYear, myDayOfMonth;
	private boolean isFirst = true;	// 程序首次运行标志
//	private boolean isBFirst = false;
	private boolean isSetingTime = false;
	private Dialog myWaitDialog = null;
	private AlertDialog menuDialog;// menu菜单Dialog
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 初始化数据库
		log.init(log.DEBUG_LOG);
		Database.init(this, "bz.db");

		if(!isServiceRunning()) {
			MyVoicePoint.getMyVoicePoint(BeiSTWicket.this);
			if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE) {
				idIntent = new Intent(this, IDRService4XiaoBing.class);
				if( !IDRService4XiaoBing.isIDRServiceRuning && (idIntent !=null) )
					startService(idIntent);
			} else if(Macro.ACTION_SHENSI) {
				idIntent = new Intent(this, IDRService4ShenSi.class);
				if( !IDRService4ShenSi.isIDRServiceRuning && (idIntent !=null) ) {
					startService(idIntent);
				}
			} else if(Macro.ACTION_SHENSI_PRINT){
				idIntent = new Intent(this, IDRService4ShenSiWPt.class);
				if( !IDRService4ShenSiWPt.isBeiWicketActivityRuning && (idIntent != null) )
					startService(idIntent);
			} else if(Macro.ACTION_ZHIGULIAN) {
				idIntent = new Intent(this, IDRService4ZhiGuLian.class);
				if( !IDRService4ZhiGuLian.isIDRServiceRuning && (idIntent !=null) ) {
					startService(idIntent);
				}
			}
		}
		
        initView();
        
        if(TicketMger.getTicketMger(handler4NetStatusAndUpdata) != null)
			TicketMger.getTicketMger(handler4NetStatusAndUpdata).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		myWicketUpPageButton.setTextSize(20);
		myWicketDownPageButton.setTextSize(20);
		GetSystemConfiger(configer.getConfiger().getonfValue(configer.CMD_CONF_AU, "")
					.equalsIgnoreCase("1")?true:false, 
					Integer.valueOf(configer.getConfiger().getonfValue(configer.CMD_CONF_TM, "")));
		
		if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE) {
			IDRService4XiaoBing.isBeiWicketActivityRuning = true;
		} else if(Macro.ACTION_SHENSI) {
			IDRService4ShenSi.isBeiWicketActivityRuning = true;
		}else if (Macro.ACTION_SHENSI_PRINT) {
			IDRService4ShenSiWPt.isBeiWicketActivityRuning = true;
		} else if(Macro.ACTION_ZHIGULIAN) {
			IDRService4ZhiGuLian.isBeiWicketActivityRuning = true;
		}
		
		if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE 
				|| Macro.ACTION_SHENSI_PRINT || Macro.ACTION_ZHIGULIAN) {
			if(isAutoWicket) {
				myPrintCheckBox.setVisibility(View.GONE);
				myWicketButton.setTextSize(20);
				myWicketButton.setText("打印并检票");
			} else {
				myPrintCheckBox.setVisibility(View.VISIBLE);
				myWicketButton.setTextSize(30);
				myWicketButton.setText("检票");
			}
		} else if(Macro.ACTION_SHENSI) {
			myPrintCheckBox.setVisibility(View.GONE);
			myWicketButton.setTextSize(30);
			myWicketButton.setText("检票");
		}
		
		myMainImageButton.setOnClickListener(HomeImageButtonClickListener);
		myCheckiImageButton.setOnClickListener(HomeImageButtonClickListener);
		myWicketImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mySwitchContents(myMainContent);
				myListShowWicketData.clear();
				CancelALL(myWicketSimpleAdapter);
			}
		});
		
		// 测试Button监听事件 检票
		myWicketButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE 
						|| Macro.ACTION_SHENSI_PRINT || Macro.ACTION_ZHIGULIAN) {
					if(isAutoWicket) {
						MyWicket();
						MyPrint();
					} else {
						MyWicket();
						if(myPrintCheckBox.isChecked())
							MyPrint();
					}
				} else if(Macro.ACTION_SHENSI) {
					MyWicket();
				}
			}
		});
		
		myWicketGoodsListView.setOnScrollListener(new OnScrollListener() {
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
					myWicketTextView4PaperNum.setText("第 " + ((firstVisibleItem+6)/6) 
							+ " 页，共 " + PaperNum + "页");
				} else {
					myWicketTextView4PaperNum.setText("第 " + ((firstVisibleItem+6)/6+1) 
							+ " 页，共 " + PaperNum + "页");
				}
			}
		});
		
		myWicketUpPageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CurrentItemID -= ShowItemCount;
				if(CurrentItemID > 0) {
					myWicketGoodsListView.setSelection(CurrentItemID);
				} else {
					CurrentItemID = 0;
					myWicketGoodsListView.setSelection(CurrentItemID);
				}
			}
		});
		
		myWicketDownPageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CurrentItemID += ShowItemCount;
				if(CurrentItemID < EndItemID) {
					myWicketGoodsListView.setSelection(CurrentItemID);
				} else {
					CurrentItemID = 0;
					myWicketGoodsListView.setSelection(CurrentItemID);
				}
			}
		});
		
		// 用户订单全选监听 
		mySelectAllCheckBox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)	// 全选
					SelectALL(myWicketSimpleAdapter);
				else	// 取消全选
					CancelALL(myWicketSimpleAdapter);
				countPastAndPayNum();
			}
		});

		myButtonIDMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(getApplicationContext(), MyIDTextViewActivity.class)
						, Macro.REQUEST_MYKEYBOARD);
			}
		});
		
		// 测试Button监听事件 查询
		myCheckCheckButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String IDNum, GoodsID, GoodName, BeginTime, EndTime;
				IDNum = myCheckIDEditText.getText().toString();
				if (IDNum.length() == 0)
					IDNum = null;
				GoodsID = myCheckGoodsIDEditText.getText().toString();
				if (GoodsID.length() == 0)
					GoodsID = null;
				GoodName = myCheckGoodNameEditText.getText().toString();
				if (GoodName.length() == 0)
					GoodName = null;
				BeginTime = myCheckTimeBeginButton.getText().toString();
				if (BeginTime.equalsIgnoreCase(getString(R.string.check_time_defaul))) {
					if(IDNum == null && GoodsID == null && GoodName == null 
							&& myCheckGoodStatusPay == TickWorker.ALL_STAT)
						BeginTime = MyFormatDateTime.getMyFormatDateTime().GetDate(null);
					else
						BeginTime = null;
				}
				EndTime = myCheckTimeEndButton.getText().toString();
				if (EndTime.equalsIgnoreCase(getString(R.string.check_time_defaul))) {
					if(IDNum == null && GoodsID == null && GoodName == null 
							&& myCheckGoodStatusPay == TickWorker.ALL_STAT)
						EndTime = MyFormatDateTime.getMyFormatDateTime().GetDate(null);
					else
						EndTime = null;
				}

				Intent myIntent4CheckShow = new Intent(getApplicationContext(), BeiSTCheckShow.class);
				Bundle bundle = new Bundle();
				bundle.putString(Macro.CHECK_QUERY_PARAMETER_IDNUM, IDNum);
				bundle.putString(Macro.CHECK_QUERY_PARAMETER_GOODSID, GoodsID);
				bundle.putString(Macro.CHECK_QUERY_PARAMETER_GOODNAME, GoodName);
				bundle.putInt(Macro.CHECK_QUERY_PARAMETER_STATUS_USED, myCheckGoodStatusUsed);
				bundle.putInt(Macro.CHECK_QUERY_PARAMETER_STATUS_PAY, myCheckGoodStatusPay);
				bundle.putString(Macro.CHECK_QUERY_PARAMETER_BEGIN_TIME, BeginTime);
				bundle.putString(Macro.CHECK_QUERY_PARAMETER_END_TIME, EndTime);
				myIntent4CheckShow.putExtras(bundle);
				if((IDNum != null) && (IDNum.length() == 18) && (!IdcardValidator.getIdcardValidator().isValidate18Idcard(IDNum))) {
					MyToast.getMyToast().WordToast(getApplicationContext(), getString(R.string.id_error));
				} else {
					startActivityForResult(myIntent4CheckShow, Macro.REQUEST_CHECK_SHOW);
				}
			}
		});
		// 测试Button监听事件 时间段查询 起始时间
		myCheckTimeBeginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyDatePickerDialog(v, myCheckTimeBeginButton).show();
			}
		});
		// 测试Button监听事件 时间段查询 结束时间
		myCheckTimeEndButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyDatePickerDialog(v, myCheckTimeEndButton).show();
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE) {
			IDRService4XiaoBing.isBeiWicketActivityRuning = false;
		} else if(Macro.ACTION_SHENSI) {
			IDRService4ShenSi.isBeiWicketActivityRuning = false;
		} else if(Macro.ACTION_SHENSI_PRINT) {
			IDRService4ShenSiWPt.isBeiWicketActivityRuning = false;
		} else if(Macro.ACTION_ZHIGULIAN) {
			IDRService4ZhiGuLian.isBeiWicketActivityRuning = false;
		}
		isMyWicketContent = true;
		isMyCheckContent = false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// 关闭保持屏幕常亮
		this.myWakeLock.release();
		finish();
//		// 解除注册接收器
		this.unregisterReceiver(myReceiver);
//		myThread4TicketMger.cancel();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 打印显示统计的可通过人数和合计实收金额
	 */
	private void countPastAndPayNum() {
		myWicketTextView4OrderPayNum.setText(
				"实收: ￥" + myWicketSimpleAdapter.countAllData().get(3) );
		myWicketTextView4OrderNum.setText(
				"通过人数: " + myWicketSimpleAdapter.countAllData().get(2).intValue() );
	}
	/**
	 * 显示查检票数据
	 * @param IDNum：身份证或贝竹验证码
	 * @param otherID：他网订单号
	 * @param myListData
	 * @param mySimpleAdapter
	 * @param isRefresh
	 * @return 有票返回true，没票返回false
	 */
	private boolean ShowDataForWicket(String IDNum, String otherID, List<HashMap<String, Object>> myListData, 
			MySimpleAdapter4TickectsTable mySimpleAdapter, boolean isRefresh) {
		boolean validBool = false, payBool = false;
		ITable re = TickWorker.getAction().query(IDNum, null, null, TickWorker.UNUSED_TYPE
				, TickWorker.ALL_STAT, null, null, otherID);
		myListData.clear();
		while (re.moveNext()) {
			HashMap<String, Object> myHashMapItem = new HashMap<String, Object>();
			myHashMapItem.put(Macro.GOODS_ID, re.getData("ID"));
			myHashMapItem.put(Macro.GOODS_NAME, re.getData("类型"));
			myHashMapItem.put(Macro.GOODS_QUANTITY, re.getData("数量"));
			myHashMapItem.put(Macro.GOODS_QUANTITY_OLD, re.getData("数量"));
			myHashMapItem.put(Macro.GOODS_PRICE, re.getData("price"));
			myHashMapItem.put(Macro.GOODS_YEARTICK, re.getData("isYearTick"));
			
			myHashMapItem.put(Macro.GOODS_START_TIME, re.getData("startime"));
			myHashMapItem.put(Macro.GOODS_END_TIME, re.getData("endtime"));
			myHashMapItem.put(Macro.GOODS_FROM_WHERE, re.getData("pname"));
			myHashMapItem.put(Macro.GOODS_WEEK, re.getData("week"));
			myHashMapItem.put(Macro.GOODS_VALID, re.getData("valid"));
			//////////////////////////////////////////////////////////////////////////
			myHashMapItem.put(Macro.GOODS_PAY_STATUS, re.getData("PayState"));
			if( re.getData("usedStat").equalsIgnoreCase("已检") ) {
				myHashMapItem.clear();
				continue;
			}
			myHashMapItem.put(Macro.GOODS_USED_STATUS, re.getData("usedStat"));
			if( configer.getConfiger().getonfValue(configer.CMD_CONF_CR, "").equalsIgnoreCase("0") ) {
				if( re.getData("PayState").equalsIgnoreCase("未付") ) {	// 不支持到付，不显示到付订单
					myHashMapItem.clear();
					continue;
				}
				if( re.getData("valid").equalsIgnoreCase("true") ) {
					myListData.add(0, myHashMapItem);
				} else {
					myListData.add(myHashMapItem);
				}
			} else if( configer.getConfiger().getonfValue(configer.CMD_CONF_CR, "").equalsIgnoreCase("1") ) {	// 支持到付
				if(re.getData("valid").equalsIgnoreCase("true") 
						&& !validBool && re.getData("PayState").equalsIgnoreCase("未付")) {
					payBool = true;
				}
				if(configer.getConfiger().getonfValue(configer.CMD_CONF_MD, "").equalsIgnoreCase("1")) {
					if( re.getData("valid").equalsIgnoreCase("true") ) {
						myListData.add(0, myHashMapItem);
					} else {
						myListData.add(myHashMapItem);
					}
				} else if(configer.getConfiger().getonfValue(configer.CMD_CONF_MD, "").equalsIgnoreCase("0")) {
					if( re.getData("PayState").equalsIgnoreCase("未付") ) {	// 仅显示未付订单
						if( re.getData("valid").equalsIgnoreCase("true") ) {
							myListData.add(0, myHashMapItem);
						} else {
							myListData.add(myHashMapItem);
						}
					}
				}
			}
			if(re.getData("valid").equalsIgnoreCase("true") && !validBool) {
				validBool = true;
			}
		}
		SelectALL(mySimpleAdapter);
		if ( mySimpleAdapter.getCount() == 0 && !isRefresh ) {
			MyToast.getMyToast().NoTickectsImageToast(BeiSTWicket.this, 0);
			MyVoicePoint.getMyVoicePoint(BeiSTWicket.this).BrushFail();
			return false;
		}
		
		countPastAndPayNum();
		
		if(mySimpleAdapter.getCount() > 6) {
			myWicketUpPageButton.setVisibility(View.VISIBLE);
			myWicketDownPageButton.setVisibility(View.VISIBLE);
		}
		// 无可用订单
		if(!isRefresh && !validBool) {
			MyToast.getMyToast().NoTickectsImageToast(BeiSTWicket.this, 1);
			MyVoicePoint.getMyVoicePoint(BeiSTWicket.this).BrushInvalid();
			return true;
		}
		// 存在到付订单
		if(!isRefresh && payBool) {
			MyVoicePoint.getMyVoicePoint(BeiSTWicket.this).BrushToPay();
			if(Macro.ACTION_XIAOBING_NOVICE) {
				MyToast.getMyToast().WordToastLarge(getApplicationContext(), "请付款");
			}
			return true;
		}
		return true;
	}

	/**
	 * my datePicker dialog
	 * @param view
	 * @param button
	 * @return MyDatePickerDialog
	 */
	private Builder MyDatePickerDialog(View view, final Button button) {
		myYear = MyFormatDateTime.getMyFormatDateTime().GetYear(null);
		myMonthOfYear = MyFormatDateTime.getMyFormatDateTime().GetMonth(null)-1;
		myDayOfMonth = MyFormatDateTime.getMyFormatDateTime().GetDay(null);
		LayoutInflater myLayoutInflaterDate = getLayoutInflater();
		final View myDateLayout = myLayoutInflaterDate.inflate(R.layout.datepicker_dialog,
				(ViewGroup) findViewById(R.id.my_datepicker_dialog));

		DatePicker myDatePicker = (DatePicker) myDateLayout.findViewById(R.id.myDatePicker);
		myDatePicker.init(myYear, myMonthOfYear, myDayOfMonth, new OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						myYear = year;
						myMonthOfYear = monthOfYear;
						myDayOfMonth = dayOfMonth;
					}
				});
		Builder myDatePickerDialog = new AlertDialog.Builder(view.getContext()).setCancelable(false).setTitle(R.string.wicket_yerorno)
				.setView(myDateLayout).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								button.setText(myYear + "-" + (myMonthOfYear + 1) + "-" + myDayOfMonth);
							}
						}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								button.setText(R.string.check_time_defaul);
							}
						});
		return myDatePickerDialog;
	}

	/**
	 * 切换主窗体显示
	 * @param otherContent
	 */
	private void mySwitchContents(View otherContent) {
//		if(otherContent.equals(myMainContent) && isBFrist) {
//			otherContent = myWelHomeContent;
//		}
		if(otherContent.equals(myWicketContent)) {
			isMyWicketContent = true;
			isMyCheckContent = false;
		} else if(otherContent.equals(myCheckContent)) {
			isMyWicketContent = false;
			isMyCheckContent = true;
		} else {
			isMyWicketContent = false;
			isMyCheckContent = false;
		}
		myFlipperView.removeAllViews();
		myFlipperView.addView(myMenu);
		myFlipperView.addView(otherContent);
		myFlipperView.hideMenu();
		mySelectAllCheckBox.setChecked(true);
		StopMyProgressBar();
		ResetCheckContentWidget();
	}

	/**
	 * HomeImageButtonClickListener
	 */
	OnClickListener HomeImageButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			openOptionsMenu();
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		menuDialogShow();
		return false;// 返回为true 则显示系统menu
	}
	
	private OnClickListener MainMenuListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	        int tag = (Integer) v.getTag();  
	        switch(tag){  
			case 1:	// 检票
				mySwitchContents(myMainContent);
				myListShowWicketData.clear();
				CancelALL(myWicketSimpleAdapter);
				menuDialog.dismiss();
				break;
			case 2:// 查票
				mySwitchContents(myCheckContent);
				menuDialog.dismiss();
				break;
			case 3:// 修改密码
				startActivity(new Intent(getApplicationContext(), SalesCount.class));
				menuDialog.dismiss();
				break;
			case 4:// 修改密码
				startActivity(new Intent(BeiSTWicket.this, BeiSetSystemPasswd.class));
				menuDialog.dismiss();
				break;
			case 5:// 系统设置
				startActivity(new Intent(BeiSTWicket.this, BeiSetSystemParameter.class));
				menuDialog.dismiss();
				break;
			case 6:// 检测更新
				new DownloadVersionFile(BeiSTWicket.this, handler4Version).checkUpdateInfo();
				myWaitDialog = myDialog4Waiting(getString(R.string.myDialog_wait_title_update));
				myWaitDialog.show();
				menuDialog.dismiss();
				break;
			case 7:// 设备初始化
				myDialog4InitSystem();
				menuDialog.dismiss();
				break;
			default:
				break;
	        }  
		}
	};
	
	private AlertDialog menuDialogCreate() {
//		AlertDialog tmpDialog = new AlertDialog.Builder(this, R.style.aaa).create();
		AlertDialog tmpDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.aaa)).create();
		tmpDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
					dialog.dismiss();
				return false;
			}
		});
		return tmpDialog;
	}
	
	private void menuDialogShow() {
		if (menuDialog == null) {
			menuDialog = menuDialogCreate();
		}
		Window tmpW = menuDialog.getWindow();
		LayoutParams lp = tmpW.getAttributes();
		lp.x = 0;
		lp.y = -300;
		menuDialog.show();
		menuDialog.getWindow().setContentView(R.layout.menu_bar);
		DisplayMetrics dm =getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
//		String str = String.format("w_screen = %d  ###  h_screen = %d" , w_screen, h_screen);
//		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
		menuDialog.getWindow().setLayout(w_screen, 100);
		Button btWicket = (Button)menuDialog.findViewById(R.id.bwicket);
		Button btCheck = (Button)menuDialog.findViewById(R.id.bcheck);
		Button btSales = (Button) menuDialog.findViewById(R.id.bsalescount);
        Button btPwd = (Button)menuDialog.findViewById(R.id.bpwd);
        Button btSet = (Button)menuDialog.findViewById(R.id.bset);    
        Button btUpdate = (Button)menuDialog.findViewById(R.id.bupdate);  
        Button btInit = (Button)menuDialog.findViewById(R.id.binit); 
        btWicket.setOnClickListener(MainMenuListener);  
        btWicket.setTag(1);
        btCheck.setOnClickListener(MainMenuListener);
        btCheck.setTag(2);
        btSales.setOnClickListener(MainMenuListener);  
        btSales.setTag(3);  
        btPwd.setOnClickListener(MainMenuListener);  
        btPwd.setTag(4);
        btSet.setOnClickListener(MainMenuListener);  
        btSet.setTag(5);  
        btUpdate.setOnClickListener(MainMenuListener);  
        btUpdate.setTag(6);
        btInit.setOnClickListener(MainMenuListener);  
        btInit.setTag(7);
	}
	
	public class MyReceiver extends BroadcastReceiver {
		private String tmpIDNum = "";
		private boolean tmpBool = false;
		@Override
		public void onReceive(Context context, Intent intent) { // 自定义一个广播接收器
			// TODO Auto-generated method stub
			if(!isSetingTime) {
				bundle = intent.getExtras();
				tmpIDNum = bundle.getString("i");
				if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE 
						|| Macro.ACTION_SHENSI_PRINT || Macro.ACTION_ZHIGULIAN) {
					tmpBool = tmpIDNum.equalsIgnoreCase("default");
				} else if(Macro.ACTION_SHENSI) {
					tmpBool = tmpIDNum.split("_")[0].equalsIgnoreCase("msg");
				}
				if(tmpBool) { // 读卡失败，提示重新刷卡
					MyVoicePoint.getMyVoicePoint(BeiSTWicket.this).BrushAgain();
					if(Macro.ACTION_XIAOBING_NOVICE) {
						MyToast.getMyToast().WordToastLarge(getApplicationContext(), "请重新刷身份证");
					}
				} else {
					if(Macro.ACTION_SHENSI) {
						tmpIDNum = tmpIDNum.split("_")[1]; 	// 获得有效的身份证号码
					}
					MyVoicePoint.getMyVoicePoint(BeiSTWicket.this).BrushSuccess();
					if(isMyCheckContent && !isMyWicketContent) { // 如果在查询页面
						myCheckIDEditText.setText(tmpIDNum);
					} else { // 如果在检票页面
						if (IdcardValidator.getIdcardValidator().isValidate18Idcard(tmpIDNum)) { // 判断身份证是否有效
							myWicketTextView4ID.setText(EncryptedIDNum(tmpIDNum));
							if(isAutoWicket && isMyWicketContent && !tmpIDNum.equalsIgnoreCase(myIDNum) ) {	//自动模式，先检上一张票
								StopMyProgressBar();
								MyWicket();
							}
							ClearList4CheckAndPrinter();
							myIDNum = tmpIDNum;
							// 显示查检票数据
							if(ShowDataForWicket(tmpIDNum, null
									, myListShowWicketData, myWicketSimpleAdapter, false)) {
								mySwitchContents(myWicketContent);
								if(isAutoWicket)
									StartMyProgressBar();
							} else {	// 检票失败
								mySwitchContents(myMainContent);
							}
						} else {
							MyToast.getMyToast().WordToast(getApplicationContext(), getString(R.string.id_error, Toast.LENGTH_SHORT));
						}
					}
				}
			}
		}
	}
	
	/**
	 * 用于接收MyIDTextViewActivity返回的身份证号和隐藏命令字符串
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Macro.REQUEST_MYKEYBOARD:
			myIDNum = data.getExtras().getString("AAA");
			ClearList4CheckAndPrinter();
			if (myIDNum.length() > 1 ) { // myIDNum.length() >= 18 
				if( (myIDNum.length() > 6) && (myIDNum.length() < 18) ) { // 淘宝验证码
					// 获得淘宝订单号
					TBTicMger.getTBTicMger(handler4TaoBao).getTBOrderID( myIDNum );
					myWaitDialog = myDialog4Waiting(getString(R.string.myDialog_wait_title_getTB));
					myWaitDialog.show();
				} else {	// 贝竹关键词检票（身份证、验证码）
					boolean isIDBool = true;
					if(myIDNum.length() >= 18) {
						isIDBool = IdcardValidator.getIdcardValidator().isValidate18Idcard(myIDNum);
					}
					if(isIDBool) {
						if( ShowDataForWicket(myIDNum, null, myListShowWicketData, myWicketSimpleAdapter, false) ) {
							if(myIDNum.length() >= 18)
								myWicketTextView4ID.setText(EncryptedIDNum(myIDNum));
							else
								myWicketTextView4ID.setText("关键词：" + myIDNum);
							mySwitchContents(myWicketContent);
//							myPrintCheckBox.setChecked(false);
							if(isAutoWicket)
								StartMyProgressBar();
						}
					} else {
						MyToast.getMyToast().WordToast(getApplicationContext(), getString(R.string.id_error, Toast.LENGTH_SHORT));
					}
					
//					if( ShowDataForWicket(myIDNum, null, myListShowWicketData, myWicketSimpleAdapter, false) ) {
//						if(IdcardValidator.getIdcardValidator().isValidate18Idcard(myIDNum))
//							myWicketTextView4ID.setText(EncryptedIDNum(myIDNum));
//						else
//							myWicketTextView4ID.setText("关键词：" + myIDNum);
//						mySwitchContents(myWicketContent);
////						myPrintCheckBox.setChecked(false);
//						if(isAutoWicket)
//							StartMyProgressBar();
//					}
				}
			}
			break;
		case Macro.REQUEST_CHECK_SHOW:
			ResetCheckContentWidget();
			isMyWicketContent = false;
			isMyCheckContent = true;
			break;
		case Macro.REQUEST_SETTING:
			RestartMyProgressBar();
			isSetingTime = false;
			break;
		default:
			break;
		}
	}
	
	/**
	 * 全选检票列表所有订单
	 * @param tmpAdapter MyWicketList4TableSimpleAdapter
	 */
	private void SelectALL(MySimpleAdapter4TickectsTable tmpAdapter) {
		tmpAdapter.notifyDataSetChanged();
		tmpAdapter.InitMyWicketListCheckBox(true);
	}
	
	/**
	 * 取消全选检票列表所有订单
	 * @param tmpAdapter MyWicketList4TableSimpleAdapter
	 */
	private void CancelALL(MySimpleAdapter4TickectsTable tmpAdapter) {
		tmpAdapter.notifyDataSetChanged();
		tmpAdapter.InitMyWicketListCheckBox(false);
	}

	private static int waitTime = 600; // 等待时间（单位：s）
	private boolean signProgressBar = false;
	private int tmpTime = 0; // 已使用时间（单位：s）
	private int iTimes = 0; // ProgressBar值
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(signProgressBar) {
				if (iTimes > (waitTime * 10)) {
					StopMyProgressBar();
					MyWicket();
					return;
				}
				mHandler.postDelayed(mThread, 100);
			}
		}
	};

	/**
	 * 自动检票线程
	 */
	private Thread mThread = new Thread(new Runnable() {
		public void run() {
			if(signProgressBar) {
				iTimes++;
				myProgressBar.setProgress(iTimes);
				myProgressBar.setText(Integer.toString(waitTime - tmpTime) + "S 后自动检全部订单");
				if (0 == iTimes % 10)
					tmpTime++;
				Message msg = mHandler.obtainMessage();
				mHandler.sendMessage(msg);
				if (iTimes > (waitTime * 10))
					mHandler.removeCallbacks(mThread);
			}
		}
	});

	/**
	 * 停止ProgressBar计时
	 */
	private void StopMyProgressBar() {
		mHandler.removeCallbacks(mThread);
		signProgressBar = false;
		myProgressBar.setVisibility(View.GONE);
		iTimes = 0;
		tmpTime = 0;
	}

	/**
	 * 启动ProgressBar计时操作
	 */
	private void StartMyProgressBar() {
		
		StopMyProgressBar();
		myProgressBar.setMax(waitTime * 10);
		myProgressBar.setVisibility(View.VISIBLE);
		signProgressBar = true;
		mHandler.post(mThread);
	}
	
	/**
	 * 暂停ProgressBar计时
	 */
	private void PauseMyProgressBar() {
		mHandler.removeCallbacks(mThread);
		signProgressBar = false;
	}
	
	/**
	 * 重启ProgressBar计时
	 */
	private void RestartMyProgressBar() {
		mHandler.post(mThread);
		signProgressBar = true;
	}
	
	/**
	 * myDialog4InitSystem用于初始化程序
	 * @param title 标题 result
	 * @param message 提示信息
	 * @param positiveButton 确定Button
	 * @param negativeButton 取消Button
	 * @param whichFunction 需要做的操作
	 */
	protected void myDialog4InitSystem() {
		AlertDialog.Builder builder = new Builder(BeiSTWicket.this);
		builder.setCancelable(false);
		builder.setTitle(getString(R.string.myDialog_init_title));
		builder.setMessage(getString(R.string.myDialog_init_message));
		final EditText et = new EditText(this);
		et.setInputType(0x81);
		et.setHint("请输入管理员密码！");
		builder.setView(et);

		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if( et.getText().toString().equals(configer.getConfiger().getonfValue(configer.CMD_CONF_PW, ""))
						&& (TicketMger.getTicketMger() != null) ) {
					myWaitDialog = myDialog4Waiting(getString(R.string.myDialog_wait_title_init));
					myWaitDialog.show();
					TicketMger.getTicketMger().cancel();
				} else {
					MyToast.getMyToast().WordToast(BeiSTWicket.this, "对不起，您的密码不正确！");
				}
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	/**
	 * 延时操作等待Dialog
	 * @return
	 */
	private Dialog myDialog4Waiting(String tmpStr) {
		AlertDialog.Builder builder = new Builder(BeiSTWicket.this);
		builder.setCancelable(false);
		builder.setTitle(tmpStr);
		ProgressBar pb = new ProgressBar(this);
		builder.setView(pb);
		builder.setNegativeButton(getString(R.string.Hide), null);
		return builder.create();
	}

	/**
	 * 获取需要处理的订单号和数量，获取需要打印的数据
	 * @param listTicketData4Wicket	处理的订单号和数量
	 * @param listTicketData4WicketPrinter	存储打印打印数据
	 * @return false:无有效数据   ===  true:存在有效数据
	 */
	private boolean GetTicket4UsedDataPT(List<HashMap<String, Object>> listTicketData4Wicket, 
			List<HashMap<String, Object>> listTicketData4WicketPrinter) {
		boolean isExistData = false;
		ClearList4CheckAndPrinter();
		for(int i = 0; i < myWicketSimpleAdapter.getCount(); i++) {
			if(MySimpleAdapter4TickectsTable.getIsSelected().get(i)
					&& myListShowWicketData.get(i).get(Macro.GOODS_VALID)
							.toString().equalsIgnoreCase("true") ) {
				HashMap<String, Object> myHashMapItem4Wicket = new HashMap<String, Object>();
				HashMap<String, Object> myHashMapItem4WicketPrinter = new HashMap<String, Object>();
				
				if( myListShowWicketData.get(i).get(Macro.GOODS_YEARTICK).equals("false") ) {
					myHashMapItem4Wicket.put(
							(String) myListShowWicketData.get(i).get(Macro.GOODS_ID), 
							myListShowWicketData.get(i).get(Macro.GOODS_QUANTITY));
					listTicketData4Wicket.add(myHashMapItem4Wicket);
				}
				
				myHashMapItem4WicketPrinter.put(Macro.GOODS_ID, 
						myListShowWicketData.get(i).get(Macro.GOODS_ID));
				myHashMapItem4WicketPrinter.put(Macro.GOODS_NAME, 
						myListShowWicketData.get(i).get(Macro.GOODS_NAME));
				myHashMapItem4WicketPrinter.put(Macro.GOODS_QUANTITY, 
						myListShowWicketData.get(i).get(Macro.GOODS_QUANTITY));
				myHashMapItem4WicketPrinter.put(Macro.GOODS_QUANTITY_OLD, 
						myListShowWicketData.get(i).get(Macro.GOODS_QUANTITY_OLD));
				myHashMapItem4WicketPrinter.put(Macro.GOODS_PRICE, 
						myListShowWicketData.get(i).get(Macro.GOODS_PRICE));
				myHashMapItem4WicketPrinter.put(Macro.GOODS_PAY_STATUS, 
						myListShowWicketData.get(i).get(Macro.GOODS_PAY_STATUS));
				myHashMapItem4WicketPrinter.put(Macro.GOODS_START_TIME, 
						myListShowWicketData.get(i).get(Macro.GOODS_START_TIME));
				myHashMapItem4WicketPrinter.put(Macro.GOODS_END_TIME, 
						myListShowWicketData.get(i).get(Macro.GOODS_END_TIME));
				myHashMapItem4WicketPrinter.put(Macro.GOODS_FROM_WHERE, 
						myListShowWicketData.get(i).get(Macro.GOODS_FROM_WHERE));

				listTicketData4WicketPrinter.add(myHashMapItem4WicketPrinter);
				isExistData = true;
			}
		}
		return isExistData;
	}

	/**
	 * 获取需要处理的订单号和数量
	 * @param listTicketData4Wicket	处理的订单号和数量
	 * @return false:无有效数据   ===  true:存在有效数据
	 */
	private boolean GetTicket4UsedData(List<HashMap<String, Object>> listTicketData4Wicket) {
		boolean isExistData = false;
		ClearList4CheckAndPrinter();
		for(int i = 0; i < myWicketSimpleAdapter.getCount(); i++) {
			if(MySimpleAdapter4TickectsTable.getIsSelected().get(i)
					&& myListShowWicketData.get(i).get(Macro.GOODS_VALID)
							.toString().equalsIgnoreCase("true") ) {
				HashMap<String, Object> myHashMapItem4Wicket = new HashMap<String, Object>();
	
				if( myListShowWicketData.get(i).get(Macro.GOODS_YEARTICK).equals("false") ) {
					myHashMapItem4Wicket.put(
							(String) myListShowWicketData.get(i).get(Macro.GOODS_ID), 
							myListShowWicketData.get(i).get(Macro.GOODS_QUANTITY));
					listTicketData4Wicket.add(myHashMapItem4Wicket);
				}
				isExistData = true;
			}
		}
		return isExistData;
	}
	
	/**
	 * 获取系统功能设置
	 * @param isAuto
	 * @param communicateTime
	 */
	public static void GetSystemConfiger(boolean isAuto, int communicateTime) {
		isAutoWicket = isAuto;
		waitTime = communicateTime;
	}
	
	/**
	 * 加密身份证号码
	 * @param tmpIDNum 身份证号码
	 * @return 加密后的身份证号码
	 */
	private String EncryptedIDNum(String tmpIDNum) {
		return "关键词：" + tmpIDNum.substring(0, 6) + "********" + tmpIDNum.substring(14, 18);
	}
	
	/**
	 * 检票，保存打印信息，切换回主界面
	 * @return false:无有效数据   ===  true:存在有效数据
	 */
	private boolean MyWicket() {
		boolean isExistData = false;
		if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE 
				|| Macro.ACTION_SHENSI_PRINT || Macro.ACTION_ZHIGULIAN) {	// 获取需要检票的订单和打印订单数据
			isExistData = GetTicket4UsedDataPT(myListTicketData4Wicket, myListTicketData4WicketPrinter);
		} else if(Macro.ACTION_SHENSI) {	// 不需要获取打印订单数据
			isExistData = GetTicket4UsedData(myListTicketData4Wicket);
		}
		if( isExistData ) {
			TickWorker.getAction().check(myListTicketData4Wicket);
			MyVoicePoint.getMyVoicePoint(BeiSTWicket.this).CheckSuccess();
			if(Macro.ACTION_XIAOBING_NOVICE) {
				MyToast.getMyToast().WordToastLarge(getApplicationContext(), "检票成功");
			}
		}
		if( (myIDNum.length() > 1) && myIDNum.substring(0, 2).equalsIgnoreCase("TB") ) {
			ShowDataForWicket(null, myIDNum, myListShowWicketData, myWicketSimpleAdapter, true);
		} else {
			ShowDataForWicket(myIDNum, null, myListShowWicketData, myWicketSimpleAdapter, true);
		}
		mySwitchContents(myMainContent);
		return isExistData;
	}
	
	/**
	 * 清空用于打印和检票的数据
	 */
	private void ClearList4CheckAndPrinter() {
		myListTicketData4Wicket.clear();
		if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE 
				|| Macro.ACTION_SHENSI_PRINT || Macro.ACTION_ZHIGULIAN) {
			myListTicketData4WicketPrinter.clear();
		}
	}

	/**
	 * 打印检票数据
	 */
	private void MyPrint() {
		if(!myListTicketData4WicketPrinter.isEmpty() && 0 != myIDNum.length()) {
			if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE) {
				StopIDService4XiaoBing();
				if(myIDNum.length() >= 18)
					MyPrinter4XiaoBing.getMyPrinter().OrdersPrinter(myListTicketData4WicketPrinter, EncryptedIDNum(myIDNum), Macro.WICKET);
				else
					MyPrinter4XiaoBing.getMyPrinter().OrdersPrinter(myListTicketData4WicketPrinter, "关键词：" + myIDNum, Macro.WICKET);
				startService(idIntent);
			} else if(Macro.ACTION_SHENSI_PRINT) {
				if(myIDNum.length() >= 18)
					MyPrinter4ShenSi.getMyPrinter().OrdersPrinter(myListTicketData4WicketPrinter, EncryptedIDNum(myIDNum), Macro.WICKET);
				else
					MyPrinter4ShenSi.getMyPrinter().OrdersPrinter(myListTicketData4WicketPrinter, "关键词：" + myIDNum, Macro.WICKET);
			} else if(Macro.ACTION_ZHIGULIAN) {
				if(myIDNum.length() >= 18)
					MyPrinter4ZhiGuLian.getMyPrinter().OrdersPrinter(myListTicketData4WicketPrinter, EncryptedIDNum(myIDNum), Macro.WICKET);
				else
					MyPrinter4ZhiGuLian.getMyPrinter().OrdersPrinter(myListTicketData4WicketPrinter, "关键词：" + myIDNum, Macro.WICKET);
			}
			ClearList4CheckAndPrinter();
		}
	}
	
	/**
	 * 终止身份证读卡Service
	 */
	public void StopIDService4XiaoBing() {
		IDRService4XiaoBing.isExec = false;	//终止身份证读取线程
		
		while(true) {	//该循环的作用相当于，等待身份证读取线程的结束。资源也会由该线程自己释放，以给下面的打印使用
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(IDRService4XiaoBing.lists.size()==0)
				break;
		} 
		if(idIntent != null)
			stopService(idIntent);
	}
	
	/**
	 * 刷新订单使用状态Spinner数据
	 * @param tmpBoolean true：显示使用状态；false：当前Spinner不可用
	 */
	private void RefreshMyAdapter4SpinnerUsedStatus(boolean tmpBoolean) {
		myData4SpinnerUsedStatus.clear();
		if(tmpBoolean) {
			myData4SpinnerUsedStatus.add("全部");
			myData4SpinnerUsedStatus.add(getString(R.string.goods_status_unused));
			myData4SpinnerUsedStatus.add(getString(R.string.goods_status_used));
		} else {
			myData4SpinnerUsedStatus.add("当前Spinner不可用");
		}
		myAdapter4SpinnerUsedStatus.notifyDataSetChanged();
		mySpinnerUsedStatus.setClickable(tmpBoolean);
	}
	
	/**
	 * 初始化查票页面关键词控件
	 */
	private void ResetCheckContentWidget() {
		myCheckIDEditText.setText("");
		myCheckGoodsIDEditText.setText("");
		myCheckGoodNameEditText.setText("");
		mySpinnerPayStatus.setSelection(0);
		myCheckTimeBeginButton.setText(R.string.check_time_defaul);
		myCheckTimeEndButton.setText(R.string.check_time_defaul);
	}
	
	private Handler handler4ProgressBar = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				PauseMyProgressBar();
				break;
			case 2:
				RestartMyProgressBar();
				break;
			case 3:
				RestartMyProgressBar();
				break;
			case 4:
				countPastAndPayNum();
				break;
			default:
				break;
			}
		}
	};
	
	public Handler handler4NetStatusAndUpdata = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:	// 检测网络状态，并在第一次联网时判断软件是否需要更新
				netStatusTextView.setText("当前网络状态：" + msg.obj.toString());
				if( (msg.obj.toString().equalsIgnoreCase("通")) && isFirst ) {
					new DownloadVersionFile(BeiSTWicket.this, handler4Version).checkUpdateInfo();
				}
				break;
			case 1:	// 结束初始化系统Dialog，并提示系统初始化成功
				Database.getDataBase().reset();
				TicketMger.getTicketMger().start();
				if(myWaitDialog != null) {
					myWaitDialog.dismiss();
					myWaitDialog = null;
				}
				MyToast.getMyToast().WordToast(BeiSTWicket.this, "初始化成功！");
				break;
			case 2:	// 修改系统时间
				if(!isSetingTime && !signProgressBar ) {
					PauseMyProgressBar();
					isSetingTime = true;
					new AlertDialog.Builder(BeiSTWicket.this).setCancelable(false)
							.setMessage("对不起，您的系统时间与网络时间不匹配，请设置“日期和时间")
							.setPositiveButton("确定" , new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									startActivityForResult(
											new Intent(android.provider.Settings.ACTION_DATE_SETTINGS)
											, Macro.REQUEST_SETTING);
								}
							}).create().show();
				}
				break;
			default:
				break;
			}
		}
	};
	
	public Handler handler4Version = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:	// XML文件下载成功
				// 下载版本控制文件version.xml
				if(!signProgressBar) {
					isFirst = false;
					if(myWaitDialog != null) {
						myWaitDialog.dismiss();
						myWaitDialog = null;
					}
					mySwitchContents(myMainContent);
					myListShowWicketData.clear();
					CancelALL(myWicketSimpleAdapter);
					
		    		Intent myIntent = new Intent(BeiSTWicket.this, UpdateDialog.class);  
		    		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    		startActivity(myIntent);
				}
				break;
			case 1:	// 没有需要更新的版本
				if(!isFirst) {
					MyToast.getMyToast().WordToast(BeiSTWicket.this, "没有需要更新的版本！");
				}
				isFirst = false;
				if(myWaitDialog != null) {
					myWaitDialog.dismiss();
					myWaitDialog = null;
				}
				break;
			default:
				break;
			}
		}
	};
	
	public Handler handler4TaoBao = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:	// 获得淘宝订单ID号并在终端查询
				myWaitDialog.dismiss();
				String [] tmpStr = new TBValidData().getTBValidData(msg.obj.toString()).split("%");
				if(tmpStr[0].equals("sub_msg")) {
					MyToast.getMyToast().WordToast(BeiSTWicket.this, tmpStr[1]);
				} else if(tmpStr[0].equals("order_id")) {
					if( ShowDataForWicket(null, tmpStr[1], myListShowWicketData
							, myWicketSimpleAdapter, false) ) {
						myWicketTextView4ID.setText("关键词：" + myIDNum);
						mySwitchContents(myWicketContent);
//						myPrintCheckBox.setChecked(false);
						if(isAutoWicket)
							StartMyProgressBar();
					}
				}
				break;
			case 1:
				myWaitDialog.dismiss();
				MyToast.getMyToast().WordToast(BeiSTWicket.this, msg.obj.toString());
				break;
			default:
				break;
			}
		}
	};
	
	private void initView() {
		// 保持屏幕常亮
		this.myPowerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
		this.myWakeLock = this.myPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		
		isFirst = true;	// 程序首次运行标志
		myFlipperView = (FlipperView) findViewById(R.id.flipper);
		myMenu = LayoutInflater.from(this).inflate(R.layout.menu, null);
		myMainContent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
		myWicketContent = LayoutInflater.from(this).inflate(R.layout.wicket, null);
		myCheckContent = LayoutInflater.from(this).inflate(R.layout.check, null);
		
		myMainImageButton = (Button)myMainContent.findViewById(R.id.mainImageButton);
		netStatusTextView = (TextView)myMainContent.findViewById(R.id.commRightTextView);
		myButtonIDMain = (Button) myMainContent.findViewById(R.id.Button_ID);
		
		mySpinnerPayStatus = (Spinner)myCheckContent.findViewById(R.id.spinnerPayStatus);
		mySpinnerUsedStatus = (Spinner)myCheckContent.findViewById(R.id.spinnerUsedStatus);
		myCheckiImageButton = (Button)myCheckContent.findViewById(R.id.checkImageButton);
		myCheckIDEditText = (EditText) myCheckContent.findViewById(R.id.CheckIDEditText);
		myCheckGoodsIDEditText = (EditText)myCheckContent.findViewById(R.id.CheckGoodsIDEditText);
		myCheckGoodNameEditText = (EditText) myCheckContent.findViewById(R.id.CheckGoodNameEditText);
		myCheckTimeBeginButton = (Button) myCheckContent.findViewById(R.id.CheckTimeBeginButton);
		myCheckTimeEndButton = (Button) myCheckContent.findViewById(R.id.CheckTimeEndButton);
		myCheckCheckButton = (Button) myCheckContent.findViewById(R.id.CheckCheckButton);
		
		myWicketImageButton = (Button)myWicketContent.findViewById(R.id.wicketImageButton);
		myWicketTextView4ID = (TextView)myWicketContent.findViewById(R.id.wicketTextView4ID);
		myWicketTextView4OrderNum = (TextView)myWicketContent.findViewById(R.id.wicketTextView4OrderNum);
		myWicketTextView4OrderPayNum = (TextView)myWicketContent.findViewById(R.id.wicketTextView4OrderPayNum);
		myWicketTextView4PaperNum = (TextView)myWicketContent.findViewById(R.id.wicketTextView4PaperNum);
		mySelectAllCheckBox = (CheckBox)myWicketContent.findViewById(R.id.CheckBox4All);
		myPrintCheckBox = (CheckBox)myWicketContent.findViewById(R.id.isPrintCheckBox);
		myProgressBar = (MyProgressBar) myWicketContent.findViewById(R.id.myProgress);
		myWicketButton = (Button) myWicketContent.findViewById(R.id.ButtonWicket);
		myWicketUpPageButton = (Button) myWicketContent.findViewById(R.id.wicketUpPageButton);
		myWicketDownPageButton = (Button) myWicketContent.findViewById(R.id.wicketDownPageButton);
		myWicketGoodsListView = (ListView) myWicketContent.findViewById(R.id.ListView_Show_Check_Goods);
		myListShowWicketData = new ArrayList<HashMap<String, Object>>();
		myWicketSimpleAdapter = new MySimpleAdapter4TickectsTable(this, handler4ProgressBar, myListShowWicketData, true);
		myWicketGoodsListView.setAdapter(myWicketSimpleAdapter);
		
		myListTicketData4Wicket = new ArrayList<HashMap<String, Object>>();
		if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE 
				|| Macro.ACTION_SHENSI_PRINT || Macro.ACTION_ZHIGULIAN) {
			myListTicketData4WicketPrinter = new ArrayList<HashMap<String, Object>>();
		}

		myAdapter4SpinnerPayStatus = new ArrayAdapter<String>(
				BeiSTWicket.this, android.R.layout.simple_spinner_item, myData4SpinnerPayStatus);
		myAdapter4SpinnerPayStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinnerPayStatus.setAdapter(myAdapter4SpinnerPayStatus);
		
		myAdapter4SpinnerUsedStatus = new ArrayAdapter<String>(
				BeiSTWicket.this, android.R.layout.simple_spinner_item, myData4SpinnerUsedStatus);
		myAdapter4SpinnerUsedStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinnerUsedStatus.setAdapter(myAdapter4SpinnerUsedStatus);
		// 监听付款状态
		mySpinnerPayStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:		// 全部订单
					myCheckGoodStatusPay = TickWorker.ALL_STAT;
					RefreshMyAdapter4SpinnerUsedStatus(false);
					break;
				case 1:		// 未付订单
					myCheckGoodStatusPay = TickWorker.UNPAY_STAT;
					RefreshMyAdapter4SpinnerUsedStatus(false);
					break;
				case 2:		// 已付订单
					myCheckGoodStatusPay = TickWorker.PAY_STAT;
					RefreshMyAdapter4SpinnerUsedStatus(true);
					break;
				default:
					break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		mySpinnerUsedStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:		// 已付款全部订单
					myCheckGoodStatusUsed = TickWorker.USED_AND_UNUSED_TYPE;
					break;
				case 1:		// 已付款未检订单
					myCheckGoodStatusUsed = TickWorker.UNUSED_TYPE;
					break;
				case 2:		// 已付款已检订单
					myCheckGoodStatusUsed = TickWorker.USED_TYPE;
					break;
				default:
					break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myCheckIDEditText, "请输入身份证号");
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myCheckGoodsIDEditText, "请输入订单号");
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myCheckGoodNameEditText, "请输入商品名称");
		
		firstRunFun() ;
		
		mySwitchContents(myMainContent);

		instance = this;
		// 保持屏幕常亮
		this.myWakeLock.acquire();
		// 注册接收器
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.test");
		this.registerReceiver(myReceiver, filter);
	}
	
	private void firstRunFun() {
		//获取SharedPreferences对象	第一次运行修改检票打印小票数量为1
		Context ctx = BeiSTWicket.this;       
		final SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		if(!sp.getBoolean("RUN_OFF_KEY", false)) {
			Editor editor = sp.edit();	// 存入数据
			editor.putBoolean("RUN_OFF_KEY", true);
			editor.commit();
			configer.getConfiger().config(configer.CMD_CONF_IP, 
					configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.IP), "1");
		}
		if(sp.getString("SQL_ADDCOLUMN", "131120").equalsIgnoreCase("131120")) {
			Editor editor = sp.edit();	// 存入数据
			editor.putString("SQL_ADDCOLUMN", "131121");
			editor.commit();
			Database.addColumn();
		}
//		if(sp.getString("SQL_UPDATESTASK", "140521").equalsIgnoreCase("140521")) {
//			Editor editor = sp.edit();	// 存入数据
//			editor.putString("SQL_UPDATESTASK", "140522");
//			editor.commit();
			ITable retOrder = Database.getDataBase().rawQuery("SELECT action  FROM 'task'");
			if(retOrder.isEmpty()) {
				Database.intiTaskTable(1);
			}
//			System.out.println("@@@@@##### ==> " +  (retOrder.isEmpty()? "1" : retOrder.getData(0, "order_id")));
//			Database.intiTaskTable(Integer.valueOf( (retOrder.isEmpty()? "1" : retOrder.getData(0, "order_id")) ));
//		}
		
		myPrintCheckBox.setChecked(sp.getBoolean("PRINTF_OFF_KEY", true));
		myPrintCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();	// 存入数据
				editor.putBoolean("PRINTF_OFF_KEY", arg1);
				editor.commit();
			}
		});
	}
	
	private boolean isServiceRunning() {
		String whichService = null;
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	    	if(Macro.ACTION_XIAOBING || Macro.ACTION_XIAOBING_NOVICE) {
	    		whichService = "com.bei.smartravel.idreader.IDRService4XiaoBing";
	    	} else if(Macro.ACTION_SHENSI) {
	    		whichService = "com.bei.smartravel.idreader.IDRService4ShenSi";
	    	} else if(Macro.ACTION_SHENSI_PRINT){
	    		whichService = "com.sdses.idreader.IDRService4ShenSiWPt";
	    	} else if(Macro.ACTION_ZHIGULIAN) {
	    		whichService = "com.synjones.sdt.IDRService4ZhiGuLian";
	    	}
	    	if (whichService.equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
}