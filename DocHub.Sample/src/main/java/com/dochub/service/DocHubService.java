package com.dochub.service;

import com.dochub.common.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class DocHubService {
    private String _accessToken;
    private String _baseUrl;
    public Result<String> AuthenticateAsync(String username, String password, String comId, String baseUrl) throws IOException {
        _baseUrl = baseUrl;

        Gson gson = new Gson();
        CloseableHttpClient client = Ultils.initSecureClient();

        HttpPost request = new HttpPost(_baseUrl + "/api/auth/password-login");
        String para = new JSONObject()
                .put("username", username)
                .put("password", password)
                .put("companyId", comId)
                .toString();
        StringEntity params = new StringEntity(para);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        CloseableHttpResponse response = client.execute(request);
        String respContent = EntityUtils.toString(response.getEntity());

        Result<String> result = gson.fromJson(respContent, Result.class);

        if (result.getSuccess()){
            _accessToken = result.getData();
        }

        return result;
    }

    public Result<BatchImportDto> CreateBatchImportAsync(CreateBatchImportDataDto dto) throws IOException {
        Gson gson = new Gson();
        CloseableHttpClient client = Ultils.initSecureClient();

        HttpPost request = new HttpPost(_baseUrl + "/api/batch-imports/create-advanced");
        request.addHeader("content-type", "application/json");

        String oauth = Ultils.BEARER + " " + _accessToken;
        request.addHeader(Ultils.AUTHORIZATION, oauth);

        String para = gson.toJson(dto);
        StringEntity params = new StringEntity(para, "UTF-8");
        request.setEntity(params);

        CloseableHttpResponse response = client.execute(request);
        String respContent = EntityUtils.toString(response.getEntity());
        Result<BatchImportDto> result = gson.fromJson(respContent, new TypeToken<Result<BatchImportDto>>() {});
        return result;
    }

    public Result<BatchImportDto> SendBatchDocumentAsync(int id) throws IOException {
        Gson gson = new Gson();
        CloseableHttpClient client = Ultils.initSecureClient();

        HttpPost request = new HttpPost(_baseUrl + "/api/batch-imports/send/" + id);
        request.addHeader("content-type", "application/json");

        String oauth = Ultils.BEARER + " " + _accessToken;
        request.addHeader(Ultils.AUTHORIZATION, oauth);

        CloseableHttpResponse response = client.execute(request);
        String respContent = EntityUtils.toString(response.getEntity());
        Result<BatchImportDto> result = gson.fromJson(respContent, new TypeToken<Result<BatchImportDto>>() {});
        return result;
    }
}
