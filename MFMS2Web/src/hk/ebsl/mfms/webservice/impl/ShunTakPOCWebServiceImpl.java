package hk.ebsl.mfms.webservice.impl;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import hk.ebsl.mfms.webservice.ShunTakPOCWebService;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.*;

public class ShunTakPOCWebServiceImpl implements ShunTakPOCWebService {

    private Logger logger = Logger.getLogger(ShunTakPOCWebServiceImpl.class);

    private OkHttpClient client;

    private String host = "223.255.142.247";
    private Integer port = 80;
    //private String host = "localhost";
    //private Integer port = 5000;

    private static final String CLIENT_ID = "MTU5ZTVlNGEtNTdlNi00M2E3LWEwOTMtN2FjMDk2NzEzMTAz";
    private static final String CLIENT_SECRET = "YmE5NDY0MDYtY2EzYi00OWVkLWEyMjYtNzUxYjVhYzU2YWY1";
    private static final String USERNAME = "Jacky_EBSL";
    private static final String PASSWORD = "Jackytrial1011";

    private HashMap<String, String> cache = new HashMap<>();
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_TOKEN_TYPE = "token_type";

    class AuthResp {
        @SerializedName(KEY_ACCESS_TOKEN)
        String accessToken;

        @SerializedName(KEY_REFRESH_TOKEN)
        String refreshToken;

        @SerializedName("uid")
        Integer uid;

        @SerializedName(KEY_TOKEN_TYPE)
        String tokenType;
    }

    @PostConstruct
    private void init() {
        logger.info("====== INIT ShunTakPOCWebService ======");


        HttpLoggingInterceptor bodyInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String s) {
                System.out.println("[OKHTTP] " + s);
            }
        });
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(bodyInterceptor)
                .build();
    }

    @Override
    public Response relayInventoryEnquiry(
            HttpHeaders headers,
            UriInfo uriInfo,
            Integer beId,
            Integer proId,
            Integer unitId
    ) {
        Request.Builder okRequestBuilder = new Request.Builder()
                .get();

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegments("jsf/rfws/erp/trdg/stock/viewLocLvl")
                .addPathSegment(String.valueOf(beId))
                .addPathSegment(String.valueOf(proId))
                .addPathSegment(String.valueOf(unitId));

        logger.info(String.format("relayInventoryEnquiry(beId=%d, proId=%d, unitId=%d)",
                beId,
                proId,
                unitId));
        logger.info("Headers=" + headers.toString());
        logger.info("Query Params=" + uriInfo.getQueryParameters().toString());

        // add headers
        insertHeaders(okRequestBuilder, headers.getRequestHeaders());
        // add query parameters
        insertQueryParams(urlBuilder, uriInfo.getQueryParameters());

        okRequestBuilder.url(urlBuilder.build()).build();

        return execute(okRequestBuilder);
    }

    @Override
    public Response relayInventoryUpdate(String json, HttpHeaders headers, UriInfo uriInfo, String moduleType) {
        if (!cache.containsKey(KEY_ACCESS_TOKEN)) {
            if (!requestAuth()) {
                return Response.status(401).entity(String.format(
                        "Failed to get auth token. client_id=%s,client_secret=%s,username=%s,password=%s",
                        CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD
                )).build();
            }
        }

        System.out.println("json=" + json);
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegments("jsf/rfws/entity/save")
                .addPathSegment(String.valueOf(moduleType));

        // put entity
        okRequestBuilder.put(RequestBody.create(MediaType.parse("application/json"), json));
        // put headers
        insertHeaders(okRequestBuilder, headers.getRequestHeaders());
        // put query
        insertQueryParams(urlBuilder, uriInfo.getQueryParameters());

        okRequestBuilder.url(urlBuilder.build());

        return execute(okRequestBuilder);
    }

    @Override
    public Response relayInventoryUpdateUsingGet(HttpHeaders headers, UriInfo uriInfo, String moduleType) {
        if (!cache.containsKey(KEY_ACCESS_TOKEN)) {
            if (!requestAuth()) {
                return Response.status(401).entity(String.format(
                        "Failed to get auth token. client_id=%s,client_secret=%s,username=%s,password=%s",
                        CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD
                )).build();
            }
        }

        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegments("jsf/rfws/entity/save")
                .addPathSegment(String.valueOf(moduleType));

        insertHeaders(okRequestBuilder, headers.getRequestHeaders());
        insertQueryParams(urlBuilder, uriInfo.getQueryParameters());

        okRequestBuilder.url(urlBuilder.build());

        return execute(okRequestBuilder);
    }

    private boolean requestAuth() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegments("jsf/rfws/oauth/token")
                .addQueryParameter("grant_type", "password")
                .addQueryParameter("client_id", CLIENT_ID)
                .addQueryParameter("client_secret", CLIENT_SECRET)
                .addQueryParameter("username", USERNAME)
                .addQueryParameter("password", DigestUtils.sha1Hex(PASSWORD))
                .build();

        Request okRequest = new Request.Builder().url(url).get().build();

        try {
            System.out.println("\n\n");
            okhttp3.Response okResponse = client.newCall(okRequest).execute();

            if (okResponse.code() != 200)
                return false;

            String body = okResponse.body().string();
            System.out.println("Auth resp: |" + body + "|");
            AuthResp auth = new Gson().fromJson(body, AuthResp.class);

            if (auth.accessToken != null)
                cache.put(KEY_ACCESS_TOKEN, auth.accessToken);
            else
                return false;

            if (auth.refreshToken != null)
                cache.put(KEY_REFRESH_TOKEN, auth.refreshToken);
            else
                return false;

            if (auth.tokenType != null)
                cache.put(KEY_TOKEN_TYPE, auth.tokenType);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private Response execute(Request.Builder reqBuilder) {
        Set<Integer> retryStatus = new HashSet<Integer>() {{
            add(400);
            add(401);
            add(403);
        }};
        final int retryCount = 2;

        try {
            // get token if none
            if (!cache.containsKey(KEY_ACCESS_TOKEN)) {
                if (!requestAuth()) // authorization attempt failed
                    return Response.status(401).entity(String.format(
                            "Failed to get auth token. client_id=%s,client_secret=%s,username=%s,password=%s",
                            CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD
                    )).build();
            }

            setAuthHeaders(reqBuilder);
            System.out.println("\n\n");
            okhttp3.Response okResponse = client.newCall(reqBuilder.build()).execute();

            int k = 0;
            while (k < retryCount && retryStatus.contains(okResponse.code())) {
                k += 1;

                if (!requestAuth()) // authorization attempt failed
                    return Response.status(401).entity(String.format(
                            "Failed to get auth token. client_id=%s,client_secret=%s,username=%s,password=%s",
                            CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD
                    )).build();

                setAuthHeaders(reqBuilder); // update the token and retry
                System.out.println("\n\n");
                okResponse = client.newCall(reqBuilder.build()).execute();
            }

            return buildResponse(okResponse);
        } catch (Exception e) {
            logger.error(e);
            return Response.serverError().entity("Local server error. " + e.getClass().getSimpleName() + "\n" + e.getStackTrace().toString()).build();
        }
    }

    private Response buildResponse(okhttp3.Response resp) throws IOException {
        String bodyStr = resp.body().string();
        Response.ResponseBuilder respBuilder = Response.status(resp.code()).entity(bodyStr);

        for (Map.Entry<String, List<String>> respHeader : resp.headers().toMultimap().entrySet()) {
            if (respHeader.getKey().equalsIgnoreCase("transfer-encoding"))
                continue;

            for (String val : respHeader.getValue()) {
                if (val != null)
                    respBuilder.header(respHeader.getKey(), val);
            }
        }

        return respBuilder.build();
    }

    private void setAuthHeaders(Request.Builder builder) {
        builder.header("Authorization", cache.get(KEY_TOKEN_TYPE) + " " + cache.get(KEY_ACCESS_TOKEN));
        builder.header("client_id", CLIENT_ID);
    }

    private void insertHeaders(Request.Builder builder, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> respHeader : headers.entrySet()) {
            if (respHeader.getKey().equalsIgnoreCase("host")) {
                builder.header("Host", host + ":" + port);
                continue;
            }

            for (String val : respHeader.getValue()) {
                if (val != null)
                    builder.header(respHeader.getKey(), val);
            }
        }
    }

    private void insertQueryParams(HttpUrl.Builder builder, Map<String, List<String>> params) {
        for (Map.Entry<String, List<String>> param : params.entrySet()) {
            for (String val : param.getValue()) {
                if (val != null)
                    builder.addQueryParameter(param.getKey(), val);
            }
        }
    }
}
