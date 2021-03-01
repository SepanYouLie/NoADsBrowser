package com.example.medvediki;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "nmTag";
    protected Stream strWndws, strURLs;
    protected Button button;
    protected TextView textview, textview2;
    protected WebView webview;
    protected Boolean lockedDoublePress = false, urlEdit = true, inetFlag=true, flag=false,flagNoLink=false;
    protected Toast toast;
    protected int iterDoc=0,y=0;
    protected String title;
    protected Document doc;
    protected EditText edittext, edittext2;
    protected String str, str2, exception, outContent="";
    protected List<Document> list = new ArrayList();
    protected List<String> listURL = new ArrayList();
    protected Exec exec;
    public TextView textview3;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"SetJavaScriptEnabled"})

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);

        textview2 = findViewById(R.id.textView2);
        edittext = findViewById(R.id.editText);
        edittext2 = findViewById(R.id.editText2);
        edittext2.setBackgroundColor(Color.TRANSPARENT);
        exec = new Exec();
        textview2.setMovementMethod(new ScrollingMovementMethod());
        textview2.setBackgroundColor(Color.WHITE);
        textview3 = findViewById(R.id.textView3);
        int ii = 0;
        int margin=0;

        ////////////////TEST DOUBLE CLICK on edittext2
        edittext2.setOnClickListener(new View.OnClickListener() {

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
                        //textview.setVisibility(View.INVISIBLE);
                        textview2.setVisibility(View.VISIBLE);
                        textview2.setElevation(4);
                        flag=false;
                    //Log.d("close", "closeSmallTextView ");
                    //Log.d("open", "openBigTextView");
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

                        textview2.setVisibility(View.INVISIBLE);
                        flag=false;

                        //Log.d(TAG, "openSmallTextView ");
                        //Log.d(TAG, "closeBigTextView");

                    }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(iterDoc>1) {
            iterDoc--;
            edittext2.setText(listURL.get(iterDoc-1));
            exec.outThread();
        } else {
            super.onBackPressed();
        }
    }

   class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            doc = null;//Здесь будет разобранный html документ
            try {
                doc = Jsoup.connect(str2).get();
                inetFlag = true;
                }
            catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
                exception = e.getMessage();
                Log.e("FUCK: ",e.getMessage());
                inetFlag = false;
            };
                  return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Elements links;


            /*try
            {
                links = doc.select("a");
                //links = doc.getAllElements();
            for (Element link : links) {
                Log.d("Local Url: ",link.outerHtml());
                String url  = link.absUrl("href");
                link.attr("href", url);
                Log.d("Abs Url: ",link.outerHtml());
            }
            }
            catch(Exception e){}*/
            exec.doThread(inetFlag);


        }
    }
    private class MyWebView extends WebViewClient
    {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {

            super.shouldOverrideUrlLoading(webView,url);
            if(!flagNoLink){
            edittext2.setText(url);
            onClick(button);}
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url){
            webview.evaluateJavascript(addMyClickCallBackJs(),null);
                edittext2.setBackgroundColor(Color.GREEN);
                //Handler handler = new Handler();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            edittext2.setBackgroundColor(Color.TRANSPARENT);
                    }
                }, 100);
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //Log.d(TAG, "shouldOverrideUrlLoading:getMethod: "+request.getMethod());
            //Log.d(TAG, "shouldOverrideUrlLoading:getRequestHeaders: "+request.getRequestHeaders());
            //Log.d(TAG, "shouldOverrideUrlLoading:getURL: "+request.getUrl());
            edittext2.setText(request.getUrl().toString());
            onClick(button);
            return true;
        }
    }
    // JsCallBack
    class MyJsToAndroid{// extends Object{
    protected String valueOfTag;
        @JavascriptInterface
        public void outClick(String value) {
            Log.d(TAG, "myClick-> "+value);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&flagNoLink) {
                textview3.setElevation(4);
            }
            valueOfTag = value;

            webview.post(new Runnable() {
                public void run() {
                    //list.get(iterDoc-1).getElementsByAttributeValue("s",valueOfTag).attr("style","background-color: #e9e9e9");
                    textview3.setText(list.get(iterDoc-1).getElementsByAttributeValue("s",valueOfTag).outerHtml());
                    //webview.loadDataWithBaseURL(null,list.get(iterDoc-1).outerHtml(),"text/html","ru_RU",null);
                }
            });
        }
    }
        public static String addMyClickCallBackJs() {
            String js = "javascript:";
            js += "function myClick(event){" +
                    "my.outClick(event.target.getAttribute('s'));}";
            js += "document.addEventListener(\"click\",myClick,true);";
            return js;
        }

    public class Exec
    {

        public void outThread()
        {
            webview = findViewById(R.id.webView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webview.setElevation(2);
            }
            webview.setWebViewClient(new MyWebView());
            webview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!flagNoLink){flagNoLink=!flagNoLink;webview.setAlpha(0.5f);}else
                    {flagNoLink=!flagNoLink;webview.setAlpha(1f);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textview3.setElevation(1);
                        }
                    }
                    return false;
                }
            });
            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress)
                {
                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setProgress(progress);
                    if(progress == 100){
                        progressBar.invalidate();

                    }

                }

            });
            try{webview.getSettings().setJavaScriptEnabled(true);}catch(Exception e){}
            try{webview.getSettings().setDomStorageEnabled(true);}catch(Exception e){}
            try{webview.getSettings().setAllowUniversalAccessFromFileURLs(true);}catch(Exception e){}
            try{webview.getSettings().setAllowContentAccess(true);}catch(Exception e){}
            try{webview.getSettings().setAllowFileAccess(true);}catch(Exception e){}
            try{webview.getSettings().setAllowFileAccessFromFileURLs(true);}catch(Exception e){}
            try{webview.getSettings().setLoadsImagesAutomatically(true);}catch(Exception e){}
            try{webview.getSettings().setBuiltInZoomControls(true);}catch(Exception e){}
            try{webview.getSettings().setDisplayZoomControls(false);}catch(Exception e){}
            try{webview.addJavascriptInterface(new MyJsToAndroid(),"my");}catch(Exception e){}

            outContent = list.get(iterDoc-1).outerHtml();
            flag=true;
            webview.loadDataWithBaseURL(null, outContent, "text/html", "ru_RU", null);
            textview2.setText(outContent);
            lockedDoublePress = false;
        }
        private void doThread(Boolean net)
        {
            if(!net){toast = Toast.makeText(getApplicationContext(),
                    "Проблемы с сетью. Попробуйте еще раз.", Toast.LENGTH_SHORT);toast.show();return;}

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
            String text = edittext2.getText().toString();
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
                            if(text.contains(siteName)||siteName.equals("*"))
                            try{if (doWork.equals("remove"))doc.getElementById(id_name).remove();}catch(Exception e){}
                            break;
                        case "class":
                            if(eventType==XmlPullParser.END_TAG){break;}
                            xmlResourceParser.next();
                            flagE="class";
                            if(text.contains(siteName)||siteName.equals("*"))
                            try{class_name=xmlResourceParser.getText();
                            if (doWork.equals("remove"))doc.getElementsByClass(class_name).remove();}catch(Exception e){}
                            break;
                        case "tag":
                            if(eventType==XmlPullParser.END_TAG){break;}
                            xmlResourceParser.next();
                            flagE="tag";
                            if(text.contains(siteName)||siteName.equals("*")){
                            try{tag_name=xmlResourceParser.getText();
                            if (doWork.equals("remove"))doc.getElementsByTag(tag_name).remove();}catch(Exception e){}
                            Log.d("STOP2!","tagName:"+tag_name+", flagE: "+flagE+", siteName:"+siteName+"!");}
                            break;
                        case "style":
                            if(eventType==XmlPullParser.END_TAG){break;}
                            xmlResourceParser.next();
                            if(text.contains(siteName)||siteName.equals("*")){
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

            try
            {
                //Elements selects = doc.select("img");
                Elements selects = doc.getAllElements();
                for (Element e : selects) {
                    //Log.d("Local Url: ",e.outerHtml());
                    if(e.hasAttr("src")){e.attr("src", e.absUrl("src"));}
                    if(e.hasAttr("href")){e.attr("href", e.absUrl("href"));}
                    if(e.hasAttr("data-src")){e.attr("data-src", e.absUrl("data-src"));}
                    //Log.d("Abs Url: ",e.outerHtml());
                }
            }catch (Exception e){}

            if (edittext.getText().toString().equals("t")) {
                Elements sels = doc.getElementsMatchingOwnText("[а-яА-Я]");
                String temp;
                for (Element e : sels) {
                    //temp = e.text();
                    //Log.i("INTEXT:",temp);
                    e.text(e.text().replace("я", "ya"));
                    e.text(e.text().replace("Я", "Ya"));
                    e.text(e.text().replace("с", "s"));
                    e.text(e.text().replace("С", "S"));
                    e.text(e.text().replace("ш", "sh"));
                    e.text(e.text().replace("Ш", "Sh"));
                    e.text(e.text().replace("и", "i"));
                    e.text(e.text().replace("И", "I"));
                    e.text(e.text().replace("р", "r"));
                    e.text(e.text().replace("Р", "R"));
                    e.text(e.text().replace("г", "g"));
                    e.text(e.text().replace("Г", "G"));
                    e.text(e.text().replace("н", "n"));
                    e.text(e.text().replace("Н", "N"));
                    e.text(e.text().replace("й", "y"));
                    e.text(e.text().replace("Й", "Y"));
                    e.text(e.text().replace("д", "d"));
                    e.text(e.text().replace("Д", "D"));
                    e.text(e.text().replace("ё", "yo"));
                    e.text(e.text().replace("Ё", "Yo"));
                    e.text(e.text().replace("ж", "zh"));
                    e.text(e.text().replace("Ж", "Zh"));
                    e.text(e.text().replace("ч", "ch"));
                    e.text(e.text().replace("Ч", "Ch"));
                    e.text(e.text().replace("х", "h"));
                    e.text(e.text().replace("Х", "H"));
                    e.text(e.text().replace("т", "t"));
                    e.text(e.text().replace("Т", "T"));
                    e.text(e.text().replace("у", "u"));
                    e.text(e.text().replace("У", "U"));
                    e.text(e.text().replace("з", "z"));
                    e.text(e.text().replace("З", "Z"));
                    e.text(e.text().replace("п", "p"));
                    e.text(e.text().replace("П", "P"));
                    e.text(e.text().replace("ф", "f"));
                    e.text(e.text().replace("Ф", "F"));
                    e.text(e.text().replace("ю", "yu"));
                    e.text(e.text().replace("Ю", "Yu"));
                    e.text(e.text().replace("в", "v"));
                    e.text(e.text().replace("В", "V"));
                    e.text(e.text().replace("э", "e"));
                    e.text(e.text().replace("Э", "E"));
                    e.text(e.text().replace("л", "l"));
                    e.text(e.text().replace("Л", "L"));
                    e.text(e.text().replace("ь", "'"));
                    e.text(e.text().replace("ы", "y"));
                    e.text(e.text().replace("Ы", "Y"));
                    e.text(e.text().replace("б", "b"));
                    e.text(e.text().replace("Б", "B"));
                    e.text(e.text().replace("м", "m"));
                    e.text(e.text().replace("М", "M"));
                    e.text(e.text().replace("к", "k"));
                    e.text(e.text().replace("К", "K"));
                    e.text(e.text().replace("ц", "c"));
                    e.text(e.text().replace("Ц", "C"));
                    e.text(e.text().replace("ъ", ""));
                    e.text(e.text().replace("щ", "sch"));
                    e.text(e.text().replace("Щ", "Sch"));
                }
            }

            try
            {
                Elements selects = doc.getAllElements();
                long num=0;
                for (Element e : selects) {
                    num++;
                    e.attr("s", ""+num);
                }
            }
            catch (Exception e){}

            listURL.add(iterDoc, edittext2.getText().toString());
            list.add(iterDoc, doc);
            iterDoc++;

            exec.outThread();
        }
    }

    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.button:
            if (lockedDoublePress){break;}
                str = edittext.getText().toString();
            str2 = edittext2.getText().toString();

            if(URLUtil.isValidUrl(str2))
            {
                if(!str2.contains("about:blank"))
                {
                    lockedDoublePress = true;
                    new MyTask().execute();
                }
                else
                {
                    toast = Toast.makeText(getApplicationContext(),
                            "Не иду на about:blank", Toast.LENGTH_SHORT);
                    toast.show();  ;
                }
            }
            else {
                if (URLUtil.isValidUrl("https://"+str2)){lockedDoublePress = true; str2 = "https://"+str2; new MyTask().execute();}else
                {
                    toast = Toast.makeText(getApplicationContext(),
                    "Неверный url", Toast.LENGTH_SHORT);
                    toast.show();
                }
            };

            Log.d(TAG, "onClick: "+this.toString());
            break;
            default:break;
        }
    }
}
