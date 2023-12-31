/*
 * Faucet Data Plane
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.dpwallet.app.api.faucet.api;

import com.dpwallet.app.api.faucet.ApiCallback;
import com.dpwallet.app.api.faucet.ApiClient;
import com.dpwallet.app.api.faucet.ApiException;
import com.dpwallet.app.api.faucet.ApiResponse;
import com.dpwallet.app.api.faucet.Configuration;
import com.dpwallet.app.api.faucet.Pair;
import com.dpwallet.app.api.faucet.ProgressRequestBody;
import com.dpwallet.app.api.faucet.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import com.dpwallet.app.api.faucet.model.ErrorResponseModel;
import com.dpwallet.app.api.faucet.model.FaucetTransactionSummaryResponse;
import com.dpwallet.app.api.faucet.model.InlineObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaucetApi {
    private ApiClient apiClient;

    public FaucetApi() {
        this(Configuration.getDefaultApiClient());
    }

    public FaucetApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for sendFaucetTransaction
     * @param inlineObject  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call sendFaucetTransactionCall(InlineObject inlineObject, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = inlineObject;

        // create path and map variables
        String localVarPath = "/api/faucet/transactions";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if (progressListener != null) {
            apiClient.setHttpClient(apiClient.getHttpClient().newBuilder().addNetworkInterceptor(new okhttp3.Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            }).build());
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call sendFaucetTransactionValidateBeforeCall(InlineObject inlineObject, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        

        okhttp3.Call call = sendFaucetTransactionCall(inlineObject, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Send Faucet Transaction
     * 
     * @param inlineObject  (optional)
     * @return FaucetTransactionSummaryResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public FaucetTransactionSummaryResponse sendFaucetTransaction(InlineObject inlineObject) throws ApiException {
        ApiResponse<FaucetTransactionSummaryResponse> resp = sendFaucetTransactionWithHttpInfo(inlineObject);
        return resp.getData();
    }

    /**
     * Send Faucet Transaction
     * 
     * @param inlineObject  (optional)
     * @return ApiResponse&lt;FaucetTransactionSummaryResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<FaucetTransactionSummaryResponse> sendFaucetTransactionWithHttpInfo(InlineObject inlineObject) throws ApiException {
        okhttp3.Call call = sendFaucetTransactionValidateBeforeCall(inlineObject, null, null);
        Type localVarReturnType = new TypeToken<FaucetTransactionSummaryResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Send Faucet Transaction (asynchronously)
     * 
     * @param inlineObject  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call sendFaucetTransactionAsync(InlineObject inlineObject, final ApiCallback<FaucetTransactionSummaryResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        okhttp3.Call call = sendFaucetTransactionValidateBeforeCall(inlineObject, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<FaucetTransactionSummaryResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
