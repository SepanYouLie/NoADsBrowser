package com.example.medvediki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "YES!";
    protected Button button;
    protected TextView textview, textview2;
    protected WebView webview;
    protected Boolean flag=false;
    protected Toast toast;
    protected int iterDoc=0,y=0;
    protected String title;
    protected Document doc;
    protected EditText edittext, edittext2;
    protected String str, str2, exception, outContent="";
    protected List<Document> list = new ArrayList();
    protected Exec exec;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"SetJavaScriptEnabled"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textview = findViewById(R.id.textView);
        textview2 = findViewById(R.id.textView2);
        edittext = findViewById(R.id.editText);
        edittext2 = findViewById(R.id.editText2);
        exec = new Exec();
        textview.setMovementMethod(new ScrollingMovementMethod());
        textview.setBackgroundColor(Color.WHITE);
        textview2.setMovementMethod(new ScrollingMovementMethod());
        textview2.setBackgroundColor(Color.WHITE);
        int ii = 0;
        int margin=0;
        ///ЭТО ТЕСТ. НЕ ЗАБУДЬ УДАЛИТЬ!!
        //StringBuilder stringBuilder = new StringBuilder();
        //textview2.setElevation(4);
        //textview2.setVisibility(View.VISIBLE);

        /*
        Resources res = this.getResources();
        XmlResourceParser xmlResourceParser = res.getXml(R.xml.site_res);

        try {xmlResourceParser.next();
            int eventType = xmlResourceParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                stringBuilder.append("Начало XML");
                margin++;
            } else if (eventType == XmlPullParser.START_TAG) {
                margin++;
                stringBuilder.append("\n");
                for(int i=0;i<margin;i++)stringBuilder.append("   ");
                stringBuilder.append("<").append(xmlResourceParser.getName()).append(">");

            } else if (eventType == XmlPullParser.END_TAG) {
                stringBuilder.append("\n");
                for(int i=0;i<margin;i++)stringBuilder.append("   ");
                stringBuilder.append("</").append(xmlResourceParser.getName()).append(">");
                margin--;
            } else if (eventType == XmlPullParser.TEXT) {
                margin++;
                stringBuilder.append("\n");
                for(int i=0;i<margin;i++)stringBuilder.append("   ");
                stringBuilder.append("TEXT: ").append(xmlResourceParser.getText());
                margin--;
            }
            eventType = xmlResourceParser.next();
        }
        }catch (XmlPullParserException | IOException e){}
        stringBuilder.append("\nКонец XML");




        textview2.setText(stringBuilder.toString());
        */
        ///КОНЕЦ ТЕСТА. НЕ ЗАБУДЬ УДАЛИТЬ!!

/*        ///ЭТО ТЕСТ. НЕ ЗАБУДЬ УДАЛИТЬ!!
        textview2.setElevation(4);
        textview2.setVisibility(View.VISIBLE);
        Resources res = this.getResources();
        try {
            XmlPullParser parser = res.getXml(R.xml.site_res);

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("contact")) {
                    textview2.setText(textview2.getText()+"\n"+parser.getAttributeValue(0) + " "
                            + parser.getAttributeValue(1) + "\n"
                            + parser.getAttributeValue(2));
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(this,
                    "Ошибка при загрузке XML-документа: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }

        ///КОНЕЦ ТЕСТА. НЕ ЗАБУДЬ УДАЛИТЬ!!*/



        ////////////////TEST DOUBLE CLICK on small textview
        textview.setOnClickListener(new View.OnClickListener() {
            int ii = 0;
            int color=1;
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ii++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        ii = 0;
                    }
                };

                if (ii == 1) {
                    //Single click
                    handler.postDelayed(r, 250);
                } else
                    //Double click
                    if (ii == 2)
                {
                        //if (flag)textview2.setText(outContent);
                        textview.setVisibility(View.INVISIBLE);
                        textview2.setVisibility(View.VISIBLE);
                        textview2.setElevation(4);
                        flag=false;
                    Log.d(TAG, "closeSmallTextView ");
                    Log.d(TAG, "openBigTextView");

                }

            }
        });
        textview2.setOnClickListener(new View.OnClickListener() {
            int ii = 0;
            int color=1;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ii++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        ii = 0;
                    }
                };

                if (ii == 1) {
                    //Single click
                    handler.postDelayed(r, 250);
                } else
                    //Double click
                    if (ii == 2)
                    {
                        //if (flag)textview.setText(outContent);
                        textview2.setVisibility(View.INVISIBLE);
                        textview.setVisibility(View.VISIBLE);
                        flag=false;

                        Log.d(TAG, "openSmallTextView ");
                        Log.d(TAG, "closeBigTextView");

                    }

            }
        });

        /*textview.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onLongClick(View v) {

                return true;
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if(iterDoc>1) {
            iterDoc--;
            //edittext.setText((iterDoc>1)?"true":"false");
            exec.outThread();
        } else {
            super.onBackPressed();
        }
    }

    //public View vV = new View(this);
    /*class TaskRedraw extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected  Void doInBackground(Void... params)
        {
            //textview.setText(outContent);
            return null;
        };
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

        }
    }*/


    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            //Тут пишем основной код
            doc = null;//Здесь хранится будет разобранный html документ
            try {
                //Считываем заглавную страницу http://harrix.org
                ///////////////////////////////
               /* Connection.Response res = Jsoup.connect(str2).
                        timeout(5000).ignoreHttpErrors(true).followRedirects(true).execute();
                if (res.statusCode() == 307) {
                    String sNewUrl = res.header("Location");
                    if (sNewUrl != null && sNewUrl.length() > 7)
                        str2 = sNewUrl;
                    res = Jsoup.connect(str2).
                            timeout(5000).execute();
                }
                doc = res.parse();*/
                ////////////////////////////////

                //////////////////////////
                /*Connection.Response response = Jsoup.connect(str2)
                        .method(Connection.Method.GET)
                        .timeout(50000)
                        .followRedirects(true)
                        .execute();
                        doc = Jsoup.connect(str2)
                        .cookies(response.cookies())
                        .get();
                */

                ///////////////////////////
                doc = Jsoup.connect(str2).get();
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
                exception = e.getMessage();

                Log.d(TAG, "FUCK: "+e.getMessage());


            };
            //Если всё считалось, что вытаскиваем из считанного html документа заголовок
            /*if (doc!=null)
                {
                    //title = doc.title() + " " + str + " " + doc.getElementById(str) + doc.getElementsByClass(str);

                    if (doc.getElementsByClass(str)!=null)doc.getElementsByClass(str).remove();
                    if (doc.getElementById(str)!=null)doc.getElementById(str).remove();

                    //title2 = doc.title() + " " + str + " " + doc.getElementById(str) + doc.getElementsByClass(str);

                }
            else
                title = "Ошибка";*/

                        return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Elements links;
            //Тут выводим итоговые данные
            try
            {
                links = doc.select("a");
            for (Element link : links) {
                String url  = link.absUrl("href");
                link.attr("href", url); }
            }
            catch(Exception e){}
            try
            {
                Elements select = doc.select("img");
                for (Element e : select) {
                    e.attr("src", e.absUrl("src"));
                }
            }
            catch (Exception e){}

            exec.doThread();
            exec.outThread();



        }
    }
    private class MyWebView extends WebViewClient
    {
        /*@Override
        public void onLoadResource(WebView view, String url)
        {
            if (url.equals("http://redirectexample.com"))
            {
                //do your own thing here
            }
            else
            {
                super.onLoadResource(view, url);
                //textview.setText(url);
            }
        }*/

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            // все ссылки, в которых содержится домен
            // будут открываться внутри приложения
            super.shouldOverrideUrlLoading(webView,url);
            if (url.contains("my-site.ru")) {
                return false;
            }
            // все остальные ссылки будут спрашивать какой браузер открывать
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            //activity.startActivity(intent);
            //textview.setText(webView.getUrl());
            edittext2.setText(url);

            onClick(button);
            return true;



        }
        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldOverrideUrlLoading:getMethod: "+request.getMethod());
            Log.d(TAG, "shouldOverrideUrlLoading:getRequestHeaders: "+request.getRequestHeaders());
            Log.d(TAG, "shouldOverrideUrlLoading:getURL: "+request.getUrl());
            edittext2.setText(request.getUrl().toString());
            onClick(button);
            return true;

        }
    }
    public class Exec
    {

        private void outThread()
        {
            webview = findViewById(R.id.webView);
            webview.setWebViewClient(new MyWebView());
            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress)
                {
                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setProgress(progress);
                    if(progress == 100)
                        progressBar.invalidate();
                }
            });
            //textview.setText(strTest);
            try{webview.getSettings().setJavaScriptEnabled(true);}catch(Exception e){}
            try{webview.getSettings().setDomStorageEnabled(true);}catch(Exception e){}
            try{webview.getSettings().setAllowUniversalAccessFromFileURLs(true);}catch(Exception e){}
            try{webview.getSettings().setAllowContentAccess(true);}catch(Exception e){}
            try{webview.getSettings().setAllowFileAccess(true);}catch(Exception e){}
            try{webview.getSettings().setAllowFileAccessFromFileURLs(true);}catch(Exception e){}
            try{webview.getSettings().setLoadsImagesAutomatically(true);}catch(Exception e){}
            try{webview.getSettings().setBuiltInZoomControls(true);}catch(Exception e){}
            try{webview.getSettings().setDisplayZoomControls(false);}catch(Exception e){}
            outContent = list.get(iterDoc-1).html();
            flag=true;


            webview.loadDataWithBaseURL(null, outContent, "text/html", "ru_RU", null);
            textview.setText(outContent);
            textview2.setText(outContent);
        }
        private void doThread()
        {
            String doWork="";
            if(edittext.getText().toString()!="")
            try {
                if(edittext.getText().toString().startsWith("id_"))
                {
                    doc.getElementById(edittext.getText().toString().substring(3, edittext.getText().toString().length())).remove();
                } else if (edittext.getText().toString().startsWith("tag_"))
                {doc.getElementsByTag(edittext.getText().toString().substring(4, edittext.getText().toString().length())).remove();   }
                else doc.getElementsByClass(edittext.getText().toString()).remove();}
            catch(Exception e){}

            //Чтение из xml-ресурса и правка doc
            String siteName="",id_name="",class_name="",tag_name="";
            Resources res = getResources();

            XmlResourceParser xmlResourceParser = res.getXml(R.xml.site_res);
            String edittext = edittext2.getText().toString();
            String flagE="";
            try {
                xmlResourceParser.next();

                int eventType = xmlResourceParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    Log.d("STOP!",xmlResourceParser.getEventType()+" !"+siteName+"!"+" Name:"+xmlResourceParser.getName()+", Text:"+xmlResourceParser.getText()+", doWork: "+doWork);

                    if(eventType!=XmlPullParser.END_DOCUMENT && (eventType==XmlPullParser.START_TAG||eventType==XmlPullParser.END_TAG))

                    switch (xmlResourceParser.getName()) {
                        case "URL":
                            if(eventType==XmlPullParser.END_TAG){siteName="";break;}
                            xmlResourceParser.next();
                            try{siteName = xmlResourceParser.getText();}catch(Exception e){}

                            break;
                        case "Remove":
                            if(eventType==XmlPullParser.END_TAG){doWork="";break;}
                            doWork="remove";
                            break;
                        case "id":
                            if(eventType==XmlPullParser.END_TAG){break;}
                            xmlResourceParser.next();
                            id_name=xmlResourceParser.getText();
                            flagE="id";
                            if(edittext.contains(siteName)||siteName.equals("*"))
                            try{if (doWork.equals("remove"))doc.getElementById(id_name).remove();}catch(Exception e){}
                            break;
                        case "class":
                            if(eventType==XmlPullParser.END_TAG){break;}
                            xmlResourceParser.next();
                            flagE="class";
                            if(edittext.contains(siteName)||siteName.equals("*"))
                            try{class_name=xmlResourceParser.getText();
                            if (doWork.equals("remove"))doc.getElementsByClass(class_name).remove();}catch(Exception e){}
                            break;
                        case "tag":
                            if(eventType==XmlPullParser.END_TAG){break;}
                            xmlResourceParser.next();
                            flagE="tag";
                            if(edittext.contains(siteName)||siteName.equals("*")){
                            try{tag_name=xmlResourceParser.getText();
                                if (doWork.equals("remove"))doc.getElementsByTag(tag_name).remove();}catch(Exception e){}
                            Log.d("STOP2!","tagName:"+tag_name+", flagE: "+flagE+", siteName:"+siteName+"!");}
                            break;
                        case "style":
                            if(eventType==XmlPullParser.END_TAG){break;}
                            xmlResourceParser.next();
                            if(edittext.contains(siteName)||siteName.equals("*")){
                                if(flagE.equals("id"))try{doc.getElementById(id_name).attr("style", xmlResourceParser.getText());}catch (Exception e){}
                                if(flagE.equals("class"))try{doc.getElementsByClass(class_name).attr("style", xmlResourceParser.getText());}catch (Exception e){}
                                if(flagE.equals("tag"))try{doc.getElementsByTag(tag_name).attr("style", xmlResourceParser.getText());}catch (Exception e){}
                            }
                            break;
                        default:
                            break;
                    }
                    eventType = xmlResourceParser.next();
                }
            }catch (XmlPullParserException | IOException e){}



/*
            if(edittext2.getText().toString().contains("fishki.net")) {
               try{doc.getElementsByClass("sidebar").remove();} catch(Exception e){}
               try{doc.getElementsByClass("tiny__info").remove();} catch(Exception e){}
               try{doc.getElementsByClass("header-nav").remove();} catch(Exception e){}
               try{doc.getElementsByClass("header-settings").remove();} catch(Exception e){}
               try{doc.getElementsByClass("content__top-links").remove();} catch(Exception e){}
               try{doc.getElementsByClass("popup-meta").remove();} catch(Exception e){}
               try{doc.getElementsByClass("header_announcement").remove();} catch(Exception e){}
               try{doc.getElementsByClass("content__figure__share").remove();} catch(Exception e){}
               try{doc.getElementsByClass("on--community").remove();} catch(Exception e){}
               try{doc.getElementsByClass("community-post-header").remove();} catch(Exception e){}
               try{doc.getElementsByClass("comment-share").remove();} catch(Exception e){}

               try {
                    doc.getElementById("main-content").attr("style", "width:inherit");
                } catch (Exception e) {}
            }
*/
            list.add(iterDoc, doc);
            iterDoc++;
            //edittext.setText((iterDoc>1)?"true":"false");



        }
    }





    public void onClick(View v)
    {

        //textview.computeScroll();
        switch (v.getId()){
            case R.id.button:

            str = edittext.getText().toString();
            str2 = edittext2.getText().toString();

            if(URLUtil.isValidUrl(str2))
            {
                if(!str2.contains("about:blank"))
                    new MyTask().execute();
                else
                {
                    toast = Toast.makeText(getApplicationContext(),
                            "Не иду на about:blank", Toast.LENGTH_SHORT);
                    toast.show();  ;

                }
            }
            else {
                toast = Toast.makeText(getApplicationContext(),
                    "Неверный url", Toast.LENGTH_SHORT);
                toast.show(); } ;
            Log.d(TAG, "onClick: "+this.toString());


            break;

            default:break;
        }

    }
}
