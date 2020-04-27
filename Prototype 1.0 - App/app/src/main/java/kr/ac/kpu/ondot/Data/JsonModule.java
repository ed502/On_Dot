package kr.ac.kpu.ondot.Data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class JsonModule {
    private Context context;
    private RequestQueue requestQueue;


    public JsonModule(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }


    /*
    * 서버 JSON URL : http://15.165.135.160/dotJson
    *
    * */
    public void recvJsonData(){
        Log.i("jsp","데이터 받을게유~");
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://15.165.135.160/dotJson",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.i("jsp", "응답결과 : " + response);
                        // 응답 -> 파싱 -> 데이터가 잘 출력되는지?
                        Gson gson = new Gson();
                        ResDot resDot = gson.fromJson(response, ResDot.class);
                        for(DotVO dotVO : resDot.getDots()){
                            Log.i("jsp", dotVO.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("jsp", "오류 : " + error.getMessage());
                    }
                }
        );
        requestQueue.add(stringRequest);
    }
}
