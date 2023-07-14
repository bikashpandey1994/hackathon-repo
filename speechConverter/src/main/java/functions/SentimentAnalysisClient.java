package functions;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;


public class SentimentAnalysisClient {

	public static void postUser(String speechText, String uuid) throws IOException {
	try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
        HttpPost httpPost = new HttpPost("https://voiceprocessor-dot-hack-team-thewarroom1.uc.r.appspot.com/textSentimentAnalysis");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        Map<String, String>mp=new HashMap<String, String>();
        mp.put("text", speechText);
        mp.put("uuid", uuid);
        HttpEntity httpEntity = new StringEntity(JSONObject.toJSONString(mp), "utf-8");
        httpPost.setEntity(httpEntity);
        httpclient.execute(httpPost);
	}
	
	}
}