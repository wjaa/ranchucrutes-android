package br.com.agendee.rest;

import android.util.Log;

import com.google.gson.JsonParseException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import br.com.agendee.exception.RestException;
import br.com.agendee.exception.RestRequestUnstable;
import br.com.agendee.exception.RestResponseUnsatisfiedException;
import br.com.agendee.utils.ObjectUtils;
import br.com.agendee.vo.ErrorMessageVo;

/**
 * Created by wagner on 25/07/15.
 */
public class RestUtils {

    private static final String TAG = RestUtils.class.getSimpleName();

    public static <T>T getJsonWithParamPath(Class<T> clazzReturn, String targetUrl, String ... params) throws
            RestResponseUnsatisfiedException, RestException, RestRequestUnstable {


        try {
            URL url = new URL("http://" + targetUrl + "/" + RestUtils.createParamsPath(params));
            Log.i(TAG,"Iniciando conexao para = " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");


            int statusCode = conn.getResponseCode();
            Log.d(TAG, "Response Code: " + statusCode);

            if ( statusCode >= 400 && statusCode < 500){
                throw new RestRequestUnstable("Servico está fora do ar.");
            }

            if (statusCode >= 500 && statusCode < 600){
                InputStream in = new BufferedInputStream(conn.getErrorStream());
                String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                throw new RestException(ObjectUtils.fromJson(response, ErrorMessageVo.class));
            }
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            Log.i(TAG, "Fim da conexao para " + url);
            Log.d(TAG, "m=getJsonWithParamPath Response: " + response);

            return ObjectUtils.fromJson(response, clazzReturn);

        }catch (JsonParseException e) {
            throw new RestResponseUnsatisfiedException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RestRequestUnstable(e.getMessage(), e);
        }
    }

    public static <T>T postJson(Class<T> clazzReturn, String targetUrl, String uri, String json) throws
            RestResponseUnsatisfiedException, RestException, RestRequestUnstable {

        OutputStream os = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        try {

            URL url = new URL("http://" + targetUrl + "/" + uri);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(json.getBytes());
            os.flush();


            int statusCode = conn.getResponseCode();
            Log.d(TAG,"m=getJsonWithParamPath Response: " + statusCode);

            if ( statusCode >= 400 && statusCode < 500){
                throw new RestRequestUnstable("Servico está fora do ar.");
            }

            if (statusCode >= 500 && statusCode < 600){
                in = new BufferedInputStream(conn.getErrorStream());
                String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                throw new RestException(ObjectUtils.fromJson(response, ErrorMessageVo.class));
            }
            in = new BufferedInputStream(conn.getInputStream());
            String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            System.out.println(response);


            return ObjectUtils.fromJson(response, clazzReturn);

        }catch (JsonParseException e) {
            throw new RestResponseUnsatisfiedException(e.getMessage(), e);
        } catch (IOException  e) {
            throw new RestRequestUnstable(e.getMessage(), e);
        }finally {

            try {
                if (os != null) {
                    os.close();
                }
                if (in !=null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static <T>T post(Class<T> clazzReturn, String targetUrl, String uri, Map<String,String> params) throws
            RestResponseUnsatisfiedException, RestException, RestRequestUnstable {

        OutputStream os = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        try {

            URL url = new URL("http://" + targetUrl + "/" + uri);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            os = new BufferedOutputStream(conn.getOutputStream());
            //parametros
            StringBuilder p = new StringBuilder();
            for(String key : params.keySet()){
                p.append(key + "=" + params.get(key) + "&");
            }

            os.write(p.toString().getBytes());
            os.flush();

            int statusCode = conn.getResponseCode();
            Log.d(TAG,"m=getJsonWithParamPath Response: " + statusCode);

            if ( statusCode >= 400 && statusCode < 500){
                throw new RestRequestUnstable("Servico está fora do ar.");
            }

            if (statusCode >= 500 && statusCode < 600){
                in = new BufferedInputStream(conn.getErrorStream());
                String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                throw new RestException(ObjectUtils.fromJson(response, ErrorMessageVo.class));
            }
            in = new BufferedInputStream(conn.getInputStream());
            String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

            return ObjectUtils.fromJson(response, clazzReturn);

        }catch (JsonParseException e) {
            throw new RestResponseUnsatisfiedException(e.getMessage(), e);
        } catch (IOException  e) {
            throw new RestRequestUnstable(e.getMessage(), e);
        }finally {

            try {
                if (os != null) {
                    os.close();
                }
                if (in !=null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static String createParamsPath(String[] params) {
        String result = "";
        for(String p : params){
            result += "/" + p;
        }
        return result;

    }

}
