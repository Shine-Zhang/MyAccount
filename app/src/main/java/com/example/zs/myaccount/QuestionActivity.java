package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by 钟云婷 on 2016/8/30.
 */
public class QuestionActivity extends AppCompatActivity {

    private static final String TAG ="QuestionActivity";
    //新建数组,用做ListView显示的数据
    private String[] question_data = new String[]{"1. 如何修改、删除账目", "2. 忘了记账，如何补记",
            "3. 记账时如何写备注和拍照", "4. 愿望资金的计算公式",
            "5. 如何实现愿望或调整愿望的优先级", "6. 如何查看余额",
            "7. 如何查看统计报表", "8. 如何编辑现有账目分类",
            "9. 如何添加自己需要的账目", "10. 如何获得徽章"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        //隐藏标题栏
        getSupportActionBar().hide();
        //初始化控件(左上角回退图标)
        ImageButton ib_questionactivity_back = (ImageButton) findViewById(R.id.ib_questionactivity_back);
        //初始化ListView控件
        ListView lv_questionactivity = (ListView) findViewById(R.id.lv_questionactivity);
        //ArrayAdapter够用,new出填充
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_activity_question,R.id.tv_questionactivity_item, question_data);
        //绑定适配器
        lv_questionactivity.setAdapter(stringArrayAdapter);
        //监听ListView点击事件
        lv_questionactivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i(TAG,"ListViewItem"+(position+1));
                //分别处理对应item点击事件
                if (position==0){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_01.class);
                    startActivity(intent);
                }
                else if(position==1){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_02.class);
                    startActivity(intent);
                }
                else if(position==2){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_03.class);
                    startActivity(intent);
                }
                else if(position==3){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_04.class);
                    startActivity(intent);
                }
                else if(position==4){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_05.class);
                    startActivity(intent);
                }
                else if(position==5){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_06.class);
                    startActivity(intent);
                }
                else if(position==6){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_07.class);
                    startActivity(intent);
                }
                else if(position==7){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_08.class);
                    startActivity(intent);
                }
                else if(position==8){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_09.class);
                    startActivity(intent);
                }
                else if(position==9){
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity_10.class);
                    startActivity(intent);
                }
            }
        });
    }
    //给左上角回退图标设置点击方法,在XMLonClick属性调用
    public void Back_FromQuestionActivity(View view){
        finish();
    }
}
