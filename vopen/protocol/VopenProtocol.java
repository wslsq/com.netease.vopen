package vopen.protocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import vopen.response.SyncItemInfo;
import android.content.Context;
import android.util.Log;

import com.netease.vopen.pal.Constants;
import common.framework.http.HttpRequest;
import common.multipart.FilePart;
import common.multipart.MultipartEntity;
import common.multipart.Part;
import common.pal.PalLog;
import common.util.BaseUtil;
import common.util.Util;

public class VopenProtocol {
	// 切换是否测试版本
	public static boolean TEST = false;
	// 推送是否测试版本
	public static boolean TEST_PUSH = false;

	public static boolean TEST_AD = false;

	// 服务器域名
	// private String SERVER_DOMAIN = "http://so.v.163.com";//原北京
	private static final String SERVER_DOMAIN = "http://mobile.open.163.com";// 现杭州
	// private String SERVER_DOMAIN = "http://so.open.163.com";//测试服
	private static final String TEST_SERVER_DOMAIN = "http://223.252.197.247";// 测试服务器

	private static final String URL_C_OPEN_DOMAIN = "http://c.open.163.com";
	private static final String TEST_URL_C_OPEN_DOMAIN = "http://223.252.197.246";// 测试服务器

	// 推送服务器地址
	public static final String PUSH_HOST = TEST_PUSH ? "123.58.180.233"
			: "android.push.126.net";
	public static final int PUSH_PORT = 6002;
	public static final String PUSH_KEY = TEST_PUSH ? "ff37ee2d15de4dfa9c37ddc771eb299d"
			: "70f1016e4e0647908fab6145dc1874d1";

	// 广告ID
	public static final String AD_APP_ID = "29801EC2";
	//网络监控的id
	public static final String MAM_ID = "36b36cebcb2f4bce8f14522d3b908091";

	// 其他参数
	private final static String sUserAgent = "NETS_Android";
	private final static int sTimeout = 10 * 1000;
	private final static int PUSH_TIME_OUT = 11 * 60 * 1000;// 推送协议的超时时间

	// 反馈产品ID
	private static String PRODUCT_ID = "17001";

	// 反馈类型ID
	private static String FEEDBACK_ID = "46003";

	/*************************************************
	 * API
	 */
	// 登陆
	private static final String URL_LOGIN = "https://reg.163.com/logins.jsp";
	// 注册
	private static final String URL_REGISTER = "http://c.3g.163.com/urs/reg";
	// 关于
	private static final String URL_ABOUT = "http://v.163.com/special/open_roster/";
	// 版本检查
	private static final String URL_UPDATE_CHECK = TEST ? "http://v.163.com/special/openmobile/android_update_notice_test.html"
			: "http://v.163.com/special/openmobile/android_update_notice.html";
	// 头图广告
	private static final String URL_HEAD_AD = "http://v.163.com/special/openmobile/top_advertise.html";
	// push信息
	private static final String URL_PUSH_OPENCOURSE = "http://p.3g.163.com/push/opencourse/";
	// 推荐APP
	private static final String URL_RECOMM_APP = "http://app.zs.163.com/dma/android/phone/opencourse/app.json";
	// 视频列表
	private static final String URL_VIDIO_LIST = (TEST ? TEST_SERVER_DOMAIN
			: SERVER_DOMAIN) + "/movie/2/getPlaysForAndroid.htm";

	// 添加收藏
	@Deprecated
	private static final String URL_ADDSTORE = (TEST ? TEST_SERVER_DOMAIN
			: SERVER_DOMAIN) + "/movie/store/addstore.do";

	private static final String URL_ADDSTORE2 = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN)
			+ "/mob/store/addstore.do";
	// 删除收藏
	@Deprecated
	private static final String URL_DELSTORE = (TEST ? TEST_SERVER_DOMAIN
			: SERVER_DOMAIN) + "/movie/store/delstore.do";
	private static final String URL_DELSTORE2 = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN)
			+ "/mob/store/delstore.do";
	// 收藏同步
	@Deprecated
	private static final String URL_SYNCSTORE = (TEST ? TEST_SERVER_DOMAIN
			: SERVER_DOMAIN) + "/movie/store/syncstore.do";

	private static final String URL_SYNCSTORE2 = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN)
			+ "/mob/store/syncstore.do";
	// 获取课程
	private static final String URL_GET_MOVIES = (TEST ? TEST_SERVER_DOMAIN
			: SERVER_DOMAIN) + "/movie/%s/getMoviesForAndroid.htm";
	// 反馈
	private static final String URL_FEEDBACK = (TEST ? TEST_SERVER_DOMAIN
			: SERVER_DOMAIN) + "/movie/store/feedback.do";

	/* 2014-6新增 */
	// 搜索热词
	private static final String URL_HOT_SEARCH = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN) + "/opensg/hotsearch.do";
	// 个性化推荐
	private static final String URL_RECOMMEND = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN) + "/opensg/mopensg.do";
	// 用户搜索反馈
	private static final String URL_FEEDBACK_SEARCH = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN)
			+ "/ua/msearch.do";
	// 用户查看详情反馈
	private static final String URL_FEEDBACK_VIEW_COURSE = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN)
			+ "/ua/mview.do";
	// 用户下载视频反馈
	private static final String URL_FEEDBACK_DOWNLOAD = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN)
			+ "/ua/mdown.do";

	private static final String URL_GET_USER_BIND_INFO = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN)
			+ "/push/oauth.do";

	/* 2014-9新增 */
	// 获取主页的推荐信息
	private static final String URL_GET_HOME_RECOMMEND_INFO = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: URL_C_OPEN_DOMAIN)
			+ "/mobile/recommend/v1.do?mt=aphone";

	/* 2014-12新增 */
	private static final String URL_GET_COURSE_AD_INFO = (TEST ? TEST_SERVER_DOMAIN
			: SERVER_DOMAIN)
			+ "/v/moblist.htm?pid=%s";

	/* 2015-2新增 */
	// 获取用户上传urs-token的rsa公钥
//	private static final String URL_GET_RSA_PUBLIC_KEY = (TEST ? TEST_URL_C_OPEN_DOMAIN
//			: URL_C_OPEN_DOMAIN)
//			+ "/mobile/getPublicKey.do";
	// 获取mob-token
	private static final String URL_GET_MOB_TOKEN = (TEST ? TEST_URL_C_OPEN_DOMAIN
			: "https://open-s.163.com")
			+ "/auth/mob/logon.do";

	private static VopenProtocol mInstance;

	VopenProtocol() {
	}

	public static VopenProtocol getInstance() {
		if (mInstance == null) {
			mInstance = new VopenProtocol();
		}
		return mInstance;
	}

	/**
	 * 取得视频列表
	 * 
	 * @GET
	 * @return
	 */
	public HttpRequest createGetVListRequest(String cachePath) {
		HttpRequest request = new HttpRequest(URL_VIDIO_LIST,
				HttpRequest.METHOD_GET);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		request.addHeaderField("Accept-Encoding", "gzip, deflate");
		request.setRequestTimeOut(sTimeout * 1);
		request.setCachePath(cachePath);
		
		return request;
	}

	/**
	 * 取得视频详情
	 * 
	 * @param plid
	 * @return
	 */
	public HttpRequest createGetVideoDetailRequest(String plid) {
		String url = String.format(URL_GET_MOVIES, plid);
		HttpRequest request = new HttpRequest(url);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		request.addHeaderField("Accept-Encoding", "gzip, deflate");
		request.setRequestTimeOut(sTimeout);

		return request;
	}

	/**
	 * 取得版本信息
	 * 
	 * @return
	 */
	public HttpRequest createGetVersionInfoRequest() {
		HttpRequest request = new HttpRequest(URL_UPDATE_CHECK);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		request.setRequestTimeOut(sTimeout);

		return request;
	}

	/**
	 * 登录协议
	 * 
	 * @return
	 */
	@Deprecated
	public HttpRequest createLoginRequest(String username, String pwd) {
		HttpRequest request = new HttpRequest(URL_LOGIN);
		request.setRequestMethod(HttpRequest.METHOD_POST);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", pwd));
		params.add(new BasicNameValuePair("product", "t"));
		params.add(new BasicNameValuePair("type", "1"));

		request.setRequestTimeOut(60000);// MC原版本设置60000

		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return request;
	}

	/**
	 * 注册协议
	 * 
	 * @param username
	 * @param pwd
	 * @return
	 */
	public HttpRequest createRegeisterRequest(String username, String pwd) {
		HttpRequest request = new HttpRequest(URL_REGISTER);
		request.setRequestMethod(HttpRequest.METHOD_POST);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", pwd));

		request.setRequestTimeOut(60000);// MC原版本设置60000

		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return request;
	}

	/**
	 * 同步收藏夹
	 * 
	 * @param favorites
	 * @param cookie
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Deprecated
	public HttpRequest createSyncFavoriteRequest(String userId,
			List<SyncItemInfo> favorites, String cookie)
			throws UnsupportedEncodingException {
		HttpRequest request = new HttpRequest(URL_SYNCSTORE);
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		request.setRequestMethod(HttpRequest.METHOD_POST);

		if (!Util.isStringEmpty(cookie)) {
			request.addHeaderField("Cookie", cookie);
		}

		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("ursid", userId));
		JSONArray arr = new JSONArray();
		for (SyncItemInfo item : favorites) {
			arr.put(item.toJsonObject());
		}
		pairs.add(new BasicNameValuePair("playids", arr.toString()));

		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		request.setHttpEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		// 该句没有实现
		// request.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE,false);
		return request;
	}
	
	/**
	 * 同步收藏夹
	 * 
	 * @param favorites
	 * @param cookie
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public HttpRequest createSyncFavoriteRequest2(List<SyncItemInfo> favorites, String mobToken)
			throws UnsupportedEncodingException {
		HttpRequest request = new HttpRequest(URL_SYNCSTORE2);
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		request.setRequestMethod(HttpRequest.METHOD_POST);

		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		JSONArray arr = new JSONArray();
		for (SyncItemInfo item : favorites) {
			arr.put(item.toJsonObject());
		}
		pairs.add(new BasicNameValuePair("playids", arr.toString()));
		request.setHttpEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		request.addHeaderField("mob-token", mobToken);
		return request;
	}

	/**
	 * 反馈协议
	 * 
	 * @param productType
	 *            手机型号
	 * @param osVersion
	 *            系统版本
	 * @param appVersion
	 *            程序版本
	 * @param msg
	 *            反馈信息
	 * @param email
	 *            反馈email
	 * @return
	 */
	public HttpRequest createFeedBackRequest(String productType,
			String osVersion, String appVersion, String msg, String email) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mtype", productType));
		params.add(new BasicNameValuePair("os", osVersion));
		params.add(new BasicNameValuePair("appversion", appVersion));
		params.add(new BasicNameValuePair("content", msg));
		params.add(new BasicNameValuePair("email", email));

		StringBuffer sb = new StringBuffer(URL_FEEDBACK).append("?").append(
				URLEncodedUtils.format(params, "GBK"));

		HttpRequest request = new HttpRequest(sb.toString());
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		return request;
	}

	/**
	 * 取得关于信息
	 * 
	 * @return
	 */
	public HttpRequest createGetAboutRequest() {
		HttpRequest request = new HttpRequest(URL_ABOUT);
		return request;
	}

	/**
	 * 添加收藏
	 * 
	 * @param userId
	 *            用户名
	 * @param plid
	 *            课程ID
	 * @param cookie
	 *            用户cookie
	 * @return
	 */
	@Deprecated
	public HttpRequest createAddStoreRequest(String userId, String plid,
			String cookie) {
		HttpRequest request = new HttpRequest(URL_ADDSTORE);
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String storetime = sdf.format(new Date()).toString();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ursid", userId));
		params.add(new BasicNameValuePair("playid", plid));
		params.add(new BasicNameValuePair("storetime", storetime));

		request.addHeaderField("Cookie", cookie);
		request.setRequestMethod(HttpRequest.METHOD_POST);

		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.d("createAddStoreRequest", "request:" + request);
		return request;

	}

	/**
	 * 添加一个课程到收藏
	 * @param plid
	 * @param mobToken
	 * @return
	 */
	public HttpRequest createAddStoreRequest2(String plid, String mobToken) {
		HttpRequest request = new HttpRequest(URL_ADDSTORE2, HttpRequest.METHOD_POST);
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("playid", plid));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String storetime = sdf.format(new Date()).toString();
		params.add(new BasicNameValuePair("storetime", storetime));
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		request.addHeaderField("mob-token", mobToken);
		return request;
	}

	/**
	 * 删除收藏
	 * 
	 * @param userId
	 *            用户名
	 * @param plid
	 *            课程ID
	 * @param cookie
	 *            用户cookie
	 * @return
	 */
	@Deprecated
	public HttpRequest createDelRequest(String userId, List<String> plidList,
			String cookie) {
		HttpRequest request = new HttpRequest(URL_DELSTORE);
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ursid", userId));
		StringBuilder plids = new StringBuilder();
		for (int i = 0; i < plidList.size(); i++) {
			String plid = plidList.get(i);
			plids.append(plid);
			if (i != plidList.size() - 1)
				plids.append(",");
		}

		params.add(new BasicNameValuePair("playids", plids.toString()));

		request.addHeaderField("Cookie", cookie);
		request.setRequestMethod(HttpRequest.METHOD_POST);

		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return request;
	}

	/**
	 * 删除若干个收藏
	 * @param plidList
	 * @param mobToken
	 * @return
	 */
	public HttpRequest createDelStoreRequest2(List<String> plidList,
			String mobToken) {
		HttpRequest request = new HttpRequest(URL_DELSTORE2,HttpRequest.METHOD_POST);
		if (TEST)
			request.addHeaderField("host", "so.open.163.com");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		StringBuilder plids = new StringBuilder();
		for (int i = 0; i < plidList.size(); i++) {
			String plid = plidList.get(i);
			plids.append(plid);
			if (i != plidList.size() - 1)
				plids.append(",");
		}
		params.add(new BasicNameValuePair("playids", plids.toString()));
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		request.addHeaderField("mob-token", mobToken);
		return request;
	}

	/**
	 * 获取推送课程
	 * 
	 * @param latestPushId
	 * @return
	 */
	public HttpRequest createGetPushCourseRequest(String latestPushId) {
		HttpRequest request = new HttpRequest(URL_PUSH_OPENCOURSE
				+ "?lastvideoid=" + latestPushId);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		request.setRequestTimeOut(PUSH_TIME_OUT);
		// cmwap时，需要设置代理
		// if(Tools.isCMWAPMobileNet(mContext)){
		// HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
		// httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
		// }
		return request;
	}

	public HttpRequest createGetRecommAppRequest() {
		HttpRequest request = new HttpRequest(URL_RECOMM_APP);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

	public static byte[] encodeCharacters(String from) {
		try {
			return from.getBytes(HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return from.getBytes();
	}

	/**
	 * 反馈接口:反馈log
	 * 
	 * @param
	 * @return
	 */
	public HttpRequest createPostLogReqeust() {
		HttpRequest request = new HttpRequest(
				"http://fankui.163.com/ft/file.fb?op=up",
				HttpRequest.METHOD_POST);
		request.setRequestTimeOut(sTimeout * 3);
		File upload = null;
		boolean isZip = PalLog.zipLogFile(Constants.ZIP_LOG_FILE);
		if (isZip) {
			upload = PalLog.openAbsoluteFile(Constants.ZIP_LOG_FILE);
			PalLog.i("createPostLogReqeust upload=", upload.toString());
		} else {
			PalLog.i("createPostLogReqeust()", "zip fail");
		}

		Part part1 = null;
		try {
			part1 = new FilePart("Filedata", Constants.ZIP_LOG_FILE, upload,
					"application/zip", null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		MultipartEntity entity = new MultipartEntity(new Part[] { part1 });
		request.setHttpEntity(entity);
		return request;

	}

	/**
	 * 反馈接口:反馈意见
	 * 
	 * @param
	 * @return
	 */
	public HttpRequest createFeedBackReqeust(String user, String title,
			String content, String fileId, String contact, Context context) {
		// HttpRequest request = new
		// HttpRequest("http://220.181.9.96:3102/ft/commentInner.fb?",
		// HttpRequest.METHOD_POST);
		HttpRequest request = new HttpRequest(
				"http://fankui.163.com/ft/commentInner.fb?",
				HttpRequest.METHOD_POST);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		request.addHeaderField("Content-Type",
				"application/x-www-form-urlencoded");
		request.setRequestTimeOut(sTimeout);

		String resolution = Util.getResolution(context) + ";"
				+ android.os.Build.MODEL + "/android"
				+ android.os.Build.VERSION.RELEASE + ";" + Constants.APP_NAME
				+ "/" + Util.getNumberVersion(context);

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("feedbackId", FEEDBACK_ID));
		list.add(new BasicNameValuePair("productId", PRODUCT_ID));
		list.add(new BasicNameValuePair("userName", user));
		list.add(new BasicNameValuePair("title", title));
		list.add(new BasicNameValuePair("content", content));
		list.add(new BasicNameValuePair("resolution", resolution));
		if (fileId != null) {
			list.add(new BasicNameValuePair("fileId", fileId));
			list.add(new BasicNameValuePair("fileName", Constants.ZIP_LOG_FILE));
		}
		if (contact != null) {
			list.add(new BasicNameValuePair("contact", contact));
		}

		try {
			request.setHttpEntity(new UrlEncodedFormEntity(list, "GB2312"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return request;

	}

	public HttpRequest createHotSearchRequest() {
		HttpRequest request = new HttpRequest(URL_HOT_SEARCH,
				HttpRequest.METHOD_GET);
		request.setRequestTimeOut(sTimeout);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

	public HttpRequest createRecommendRequest(String uuid, String usrId,
			int count) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("ursid", usrId));
		params.add(new BasicNameValuePair("count", String.valueOf(count)));
		StringBuffer sb = new StringBuffer(URL_RECOMMEND).append("?").append(
				URLEncodedUtils.format(params, HTTP.UTF_8));
		HttpRequest request = new HttpRequest(sb.toString(),
				HttpRequest.METHOD_GET);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

	public HttpRequest createSendSearchFeedBackRequest(String ursId,
			String uuid, String key, String loc, String sys, String deviceId,
			String ip, String mac, String version) {
		HttpRequest request = new HttpRequest(URL_FEEDBACK_SEARCH,
				HttpRequest.METHOD_POST);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ursid", BaseUtil.toString(ursId)));
		params.add(new BasicNameValuePair("uuid", BaseUtil.toString(uuid)));
		params.add(new BasicNameValuePair("key", BaseUtil.toString(key)));
		params.add(new BasicNameValuePair("loc", BaseUtil.toString(loc)));
		params.add(new BasicNameValuePair("sys", BaseUtil.toString(sys)));
		params.add(new BasicNameValuePair("did", BaseUtil.toString(deviceId)));
		params.add(new BasicNameValuePair("ip", BaseUtil.toString(ip)));
		params.add(new BasicNameValuePair("mac", BaseUtil.toString(mac)));
		params.add(new BasicNameValuePair("ver", BaseUtil.toString(version)));
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

	public HttpRequest createSendViewDetailFeedBackRequest(String ursId,
			String uuid, String pid, String mid, String loc, String sys,
			String deviceId, String ip, String mac, String version) {
		HttpRequest request = new HttpRequest(URL_FEEDBACK_VIEW_COURSE,
				HttpRequest.METHOD_POST);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ursid", BaseUtil.toString(ursId)));
		params.add(new BasicNameValuePair("uuid", BaseUtil.toString(uuid)));
		params.add(new BasicNameValuePair("pid", BaseUtil.toString(pid)));
		params.add(new BasicNameValuePair("mid", BaseUtil.toString(mid)));
		params.add(new BasicNameValuePair("loc", BaseUtil.toString(loc)));
		params.add(new BasicNameValuePair("sys", BaseUtil.toString(sys)));
		params.add(new BasicNameValuePair("did", BaseUtil.toString(deviceId)));
		params.add(new BasicNameValuePair("ip", BaseUtil.toString(ip)));
		params.add(new BasicNameValuePair("mac", BaseUtil.toString(mac)));
		params.add(new BasicNameValuePair("ver", BaseUtil.toString(version)));
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

	public HttpRequest createSendDownloadFeedBackRequest(String ursId,
			String uuid, String pid, String mid, String loc, String sys,
			String deviceId, String ip, String mac, String version) {
		HttpRequest request = new HttpRequest(URL_FEEDBACK_DOWNLOAD,
				HttpRequest.METHOD_POST);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ursid", BaseUtil.toString(ursId)));
		params.add(new BasicNameValuePair("uuid", BaseUtil.toString(uuid)));
		params.add(new BasicNameValuePair("pid", BaseUtil.toString(pid)));
		params.add(new BasicNameValuePair("mids", BaseUtil.toString(mid)));
		params.add(new BasicNameValuePair("loc", BaseUtil.toString(loc)));
		params.add(new BasicNameValuePair("sys", BaseUtil.toString(sys)));
		params.add(new BasicNameValuePair("did", BaseUtil.toString(deviceId)));
		params.add(new BasicNameValuePair("ip", BaseUtil.toString(ip)));
		params.add(new BasicNameValuePair("mac", BaseUtil.toString(mac)));
		params.add(new BasicNameValuePair("ver", BaseUtil.toString(version)));
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

	public HttpRequest createGetBindAccountInfoRequest(String ursId) {
		HttpRequest request = new HttpRequest(URL_GET_USER_BIND_INFO,
				HttpRequest.METHOD_POST);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ursid", BaseUtil.toString(ursId)));
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

	public HttpRequest createGetHeadAdInfoRequest() {
		HttpRequest request = new HttpRequest(URL_HEAD_AD,
				HttpRequest.METHOD_GET);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

	public HttpRequest createGetHomeRecommendInfoRequest() {
		HttpRequest request = new HttpRequest(URL_GET_HOME_RECOMMEND_INFO,
				HttpRequest.METHOD_GET);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		request.addHeaderField("Accept-Encoding", "gzip, deflate");
		return request;
	}

	public HttpRequest createGetCourseAdInfoRequest(String plid) {
		String url = String.format(URL_GET_COURSE_AD_INFO, plid);
		HttpRequest request = new HttpRequest(url, HttpRequest.METHOD_GET);
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

//	public HttpRequest createGetPublicKeyRequest() {
//		HttpRequest request = new HttpRequest(URL_GET_RSA_PUBLIC_KEY,
//				HttpRequest.METHOD_GET);
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("key_type", "aphone"));
//		try {
//			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
//		return request;
//	}

	public HttpRequest createGetMobTokenRequest(int logonType, String email,
			String xParam) {
		HttpRequest request = new HttpRequest(URL_GET_MOB_TOKEN,
				HttpRequest.METHOD_POST);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("logon_type", String
				.valueOf(logonType)));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("x_param", xParam));
		params.add(new BasicNameValuePair("moblie_type", "aphone"));
		try {
			request.setHttpEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.addHeaderField(HTTP.USER_AGENT, sUserAgent);
		return request;
	}

}
