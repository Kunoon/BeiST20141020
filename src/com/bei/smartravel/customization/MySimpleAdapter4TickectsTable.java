package com.bei.smartravel.customization;

import java.util.HashMap;
import java.util.List;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.configer;
import com.bei.smartravel.macro.Macro;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * ����tables	�Զ���Adapter
 * @author YongKun
 *
 */
public class MySimpleAdapter4TickectsTable extends BaseAdapter {

	private Context myContext = null;
	private Handler handler = null;
	private boolean isWichet = false;
	private LayoutInflater myLayoutInflater = null;
	// ������ݵ�list
	private List<HashMap<String ,Object>> myListShowWicketData = null;
	// ��������CheckBox��ѡ��״��
    private static HashMap<Integer,Boolean> isSelected;
	/*��ſؼ�*/
	public final class MyListItemView{
		public TextView myTextViewGoodID;
		public TextView myTextViewGoodName;
		public Button myTextViewGoodQuantity;
		public TextView myTextViewGoodStatus;
		public CheckBox myCheckBox;
	}
	/**
	 * ���캯��
	 * @param myContext 
	 * @param handler
	 * @param myListShowWicketData
	 * @param isCheck
	 */
	public MySimpleAdapter4TickectsTable(Context myContext, Handler handler,
			List<HashMap<String ,Object>> myListShowWicketData, boolean isCheck) {  
		this.myContext = myContext;
		this.handler = handler;
		this.isWichet = isCheck;
		this.myListShowWicketData = myListShowWicketData;
		myLayoutInflater = LayoutInflater.from(this.myContext);
        isSelected = new HashMap<Integer, Boolean>();
	}
    
    @Override
    public int getCount() {  
       return myListShowWicketData.size();
    }  
    @Override
    public Object getItem(int position) {  
    	return myListShowWicketData.get(position);  
	}  
	@Override
	public long getItemId(int position) {  
		return position;  
	}  
	public View getView(final int position, View convertView, ViewGroup parent) {
		// ��ʼ�����ּ��ؼ�
		MyListItemView wicketListItemView = null;
        if(convertView ==null){
        	wicketListItemView = new MyListItemView();
            convertView = (LinearLayout)myLayoutInflater.inflate(R.layout.wicket_item, null);
            //��ÿؼ�����
            wicketListItemView.myTextViewGoodID = (TextView)convertView.findViewById(R.id.Wicket_TextView_GoodID);
            wicketListItemView.myTextViewGoodName = (TextView)convertView.findViewById(R.id.Wicket_TextView_GoodName);
            wicketListItemView.myTextViewGoodQuantity = (Button)convertView.findViewById(R.id.Wicket_TextView_GoodQuantity);
            wicketListItemView.myTextViewGoodStatus = (TextView)convertView.findViewById(R.id.Wicket_TextView_GoodStatus);
            wicketListItemView.myCheckBox = (CheckBox)convertView.findViewById(R.id.wicket_chechbox);
            //���ÿռ伯��convertView, ��Ϊview���ñ�ǩ
            convertView.setTag(wicketListItemView);
        }else{
        	// ȡ��MyListItemView
        	wicketListItemView = (MyListItemView) convertView.getTag();
        }
        
        if(isWichet) {
        	if(myListShowWicketData.get(position).get(Macro.GOODS_VALID).equals("false")) {
        		wicketListItemView.myTextViewGoodQuantity.setClickable(false);
        		wicketListItemView.myTextViewGoodQuantity.setBackgroundColor(Color.TRANSPARENT);
        	} else {
        		wicketListItemView.myTextViewGoodQuantity.setClickable(true);
        		wicketListItemView.myTextViewGoodQuantity.setBackgroundResource(R.drawable.bt_et);
        	}
        } else {
        	wicketListItemView.myTextViewGoodQuantity.setClickable(false);
        	wicketListItemView.myTextViewGoodQuantity.setBackgroundColor(Color.TRANSPARENT);
        }
        
        // ����list��������ʾ0392c7
        /****************************************************************************************************/
        if(myListShowWicketData.get(position).get(Macro.GOODS_VALID).equals("false")) {
        	if( myListShowWicketData.get(position).get(Macro.GOODS_PAY_STATUS)
            		.toString().indexOf("����") > -1 ) {
            	RowForTickectStatus(wicketListItemView, myListShowWicketData.get(position), "#dff305", false);
        	} else {
            	RowForTickectStatus(wicketListItemView, myListShowWicketData.get(position), "white", false);
        	}
        } else {
            if( myListShowWicketData.get(position).get(Macro.GOODS_PAY_STATUS)
            		.toString().indexOf(Macro.TICKET_STATUS_UNPAID) > -1) {
    ///////////////////===========================////////////////////////////////
            	if(isWichet) {
                	myListShowWicketData.get(position).put(Macro.GOODS_PAY_STATUS,
                			Macro.TICKET_STATUS_UNPAID+ "[��" +
                			Float.valueOf( (String) myListShowWicketData.get(position).get(Macro.GOODS_PRICE) )*
                			Integer.valueOf( (String) myListShowWicketData.get(position).get(Macro.GOODS_QUANTITY) )
                			+ "]");
            	}
    ///////////////////===========================////////////////////////////////
            	RowForTickectStatus(wicketListItemView, myListShowWicketData.get(position), "red", false);
            } else {
            	RowForTickectStatus(wicketListItemView, myListShowWicketData.get(position), "black", true); 
            }
        }
        /****************************************************************************************************/
        // ����isSelected������checkbox��ѡ��״��
        wicketListItemView.myCheckBox.setChecked(getIsSelected().get(position));
        wicketListItemView.myCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getIsSelected().put(position, !getIsSelected().get(position));
				if(isWichet && handler != null)
					handler.obtainMessage(4, countAllData()).sendToTarget();
			}
		});
        
        wicketListItemView.myTextViewGoodQuantity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isWichet && handler != null
						&& myListShowWicketData.get(position).get(Macro.GOODS_VALID).equals("true") ) {
					myDialgo(v, position, configer.getConfiger()
							.getonfValue(configer.CMD_CONF_AU, "")
									.equalsIgnoreCase("1")?true:false);
				}
			}
		});
		return convertView;
	}
	
	/**
	 * ����ͳ������
	 * 1����������
	 * 2��ͨ������
	 * 3��֧�����
	 * @return
	 */
	public HashMap<Integer, Float> countAllData() {
		HashMap<Integer, Float> myHashMap = new HashMap<Integer, Float>();
		float orderNum = (float) 0, pastNum = (float) 0, payNum = (float) 0;
		for(int i = 0; i < getCount(); i++) {
			if( getIsSelected().get(i) 
					&& myListShowWicketData.get(i).get(Macro.GOODS_VALID)
							.toString().equalsIgnoreCase("true") ){
				orderNum++;
				pastNum += Integer.valueOf( 
						(String) myListShowWicketData.get(i).get(Macro.GOODS_QUANTITY) );
				if( myListShowWicketData.get(i).get(Macro.GOODS_PAY_STATUS)
        		.toString().indexOf(Macro.TICKET_STATUS_UNPAID) > -1 ) {
					payNum += Float.valueOf( (String) myListShowWicketData.get(i).get(Macro.GOODS_PRICE) )*
		        			Integer.valueOf( (String) myListShowWicketData.get(i).get(Macro.GOODS_QUANTITY) );
				}
			}
		}
		myHashMap.put(1, orderNum);
		myHashMap.put(2, pastNum);
		myHashMap.put(3, payNum);
		return myHashMap;
	}
	/**
     * ��ʼ��isSelected������
     */
    public void InitMyWicketListCheckBox(boolean bool){
    	for (int i = 0; i < getCount(); i++) {
			getIsSelected().put(i, bool);
		}
    }
	
    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
    	MySimpleAdapter4TickectsTable.isSelected = isSelected;
    }
    
    /**
     * ���ݶ����Ĳ�ͬ״̬���ö�����ʾЧ��
     * @param myListItemView �����пؼ�
     * @param hashMap ��������HashMap
     * @param rowColor ��ɫЧ��
     * @param isPay �Ƿ񸶿�
     */
    private void RowForTickectStatus(MyListItemView myListItemView, HashMap<String ,Object> hashMap
    		, String rowColor, boolean isPay) {
    	String htmlHead = "<font color='" + rowColor + "'>";
    	myListItemView.myTextViewGoodID.setText( 
    			Html.fromHtml(htmlHead + hashMap.get(Macro.GOODS_ID).toString() + "</font>"));
    	myListItemView.myTextViewGoodName.setText( 
    			Html.fromHtml(htmlHead + hashMap.get(Macro.GOODS_NAME).toString().split("��")[0] + "</font>"));
    	myListItemView.myTextViewGoodQuantity.setText( 
    			Html.fromHtml(htmlHead + hashMap.get(Macro.GOODS_QUANTITY).toString() + "</font>"));
    	if(rowColor.equalsIgnoreCase("white")) {
    		myListItemView.myTextViewGoodStatus.setText(weekString(htmlHead
					, hashMap.get(Macro.GOODS_PAY_STATUS).toString(), hashMap.get(Macro.GOODS_WEEK).toString()));
    	} else {
        	if(isPay) {
	    		if(hashMap.get(Macro.GOODS_USED_STATUS).toString().equalsIgnoreCase(Macro.TICKET_STATUS_UNUSED)) {
	    			myListItemView.myTextViewGoodStatus.setText(weekString("<font color='red'>"
	    					, hashMap.get(Macro.GOODS_USED_STATUS).toString(), hashMap.get(Macro.GOODS_WEEK).toString()));
	    		} else {
	    			myListItemView.myTextViewGoodStatus.setText(weekString(htmlHead
	    					, hashMap.get(Macro.GOODS_USED_STATUS).toString(), hashMap.get(Macro.GOODS_WEEK).toString()));
	    		}
	    	} else {
	    		if(isWichet)
	    			myListItemView.myTextViewGoodStatus.setText( 
	    					Html.fromHtml(htmlHead + hashMap.get(Macro.GOODS_PAY_STATUS).toString() + "</font>"));
	    		else
	    			myListItemView.myTextViewGoodStatus.setText(weekString(htmlHead
	    					, hashMap.get(Macro.GOODS_PAY_STATUS).toString(), hashMap.get(Macro.GOODS_WEEK).toString()));
	    	}
    	}

    }
    
    public void myDialgo(final View view, final int tmpPosition, final boolean isAutoWickte) {
    	if(isAutoWickte) {
    		handler.obtainMessage(1).sendToTarget();
    	}
    	AlertDialog.Builder builder = new Builder(view.getContext());
		builder.setCancelable(false);
		builder.setTitle("�޸Ķ���");
		builder.setMessage("�����ţ�" + myListShowWicketData.get(tmpPosition).get(Macro.GOODS_ID).toString() +
				"\n" + "��Ʒ���ƣ�" + myListShowWicketData.get(tmpPosition).get(Macro.GOODS_NAME).toString() +
				"\n" + "��������Ҫ�޸ĵ�������");
		final EditText et = new EditText(view.getContext()); 
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		builder.setView(et);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if( 0 != et.getText().toString().length() ) {
					myListShowWicketData.get(tmpPosition).put(Macro.GOODS_QUANTITY, 
					( (Integer.valueOf(et.getText().toString()) > Integer.valueOf(
							myListShowWicketData.get(tmpPosition).get(Macro.GOODS_QUANTITY_OLD).toString()))
							|| (Integer.valueOf(et.getText().toString()) <= 0) ) 
									? myListShowWicketData.get(tmpPosition).get(Macro.GOODS_QUANTITY_OLD)
									: et.getText().toString());
//					myListShowWicketData.get(tmpPosition).put(Macro.GOODS_PAY_STATUS, "�Ѹ�");
	///////////////////===========================////////////////////////////////
					if(myListShowWicketData.get(tmpPosition).get(Macro.GOODS_PAY_STATUS)
							.toString().indexOf(Macro.TICKET_STATUS_UNPAID) > -1) {
						myListShowWicketData.get(tmpPosition).put(Macro.GOODS_PAY_STATUS,
			        			Macro.TICKET_STATUS_UNPAID+ "[��" +
			        			Float.valueOf( (String) myListShowWicketData.get(tmpPosition).get(Macro.GOODS_PRICE) )*
			        			Integer.valueOf( (String) myListShowWicketData.get(tmpPosition).get(Macro.GOODS_QUANTITY) )
			        			+ "]");
					}
	///////////////////===========================////////////////////////////////
					if( (Integer.valueOf(et.getText().toString()) <= Integer.valueOf(
								myListShowWicketData.get(tmpPosition).get(Macro.GOODS_QUANTITY_OLD).toString()))
								&& (Integer.valueOf(et.getText().toString()) > 0) ) {
						MyVoicePoint.getMyVoicePoint(myContext).ChangeSuccess();
						if(Macro.ACTION_XIAOBING_NOVICE)
							MyToast.getMyToast().WordToastLarge(myContext, "��Ʊ�ɹ�");
					} else {
						MyVoicePoint.getMyVoicePoint(myContext).ChangeFail();
						if(Macro.ACTION_XIAOBING_NOVICE)
							MyToast.getMyToast().WordToastLarge(myContext, "��Ʊʧ��");
					}
				} else {
					MyVoicePoint.getMyVoicePoint(myContext).ChangeFail();
				}
                MySimpleAdapter4TickectsTable.getIsSelected().put(tmpPosition, true);
				notifyDataSetChanged();
				dialog.dismiss();
				handler.obtainMessage(4, countAllData()).sendToTarget();
				if(isAutoWickte) {
					handler.obtainMessage(2).sendToTarget();
		    	}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				notifyDataSetChanged();
				dialog.dismiss();
				if(isAutoWickte) {
					handler.obtainMessage(3).sendToTarget();
		    	}
			}
		});
		builder.create().show();
    }
    
    private Spanned weekString(String headStr, String conStr, String isWeek) {
		switch (Integer.valueOf(isWeek)) {
		case 0:
			return Html.fromHtml(headStr + conStr + "/ȫ��" + "</font>");
		case 1:
			return Html.fromHtml(headStr + conStr + "/ƽ��" + "</font>");
		case 2:
			return Html.fromHtml(headStr + conStr + "/����" + "</font>");
		default:
			break;
		}
		return null;
	}
}  
