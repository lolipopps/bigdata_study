package com.bigdata.back.common.utils;

import com.bigdata.back.common.vo.Result;
import com.bigdata.back.entity.IpProxy;
import com.bigdata.back.entity.UserAgent;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * WebClient工具类
 * <p>
 * WebClient是htmlunit的东西，可模拟浏览器解析DOM、执行js、css等
 * 可以解析Html文档，例如像jq操作DOM对象一样
 */
@Slf4j//使用lombok的@Slf4j，帮我们创建Logger对象
public class WebClientUtil {
    /**
     * IP代理池
     */
    private static List<IpProxy> ipProxyPool;

    /**
     * User-Agent池
     */
    private static List<UserAgent> userAgentPool;

    /**
     * 获取一个WebClient
     */
    public static WebClient getWebClient() {
        //创建一个WebClient，并随机初始化一个浏览器模型
        BrowserVersion[] versions = {BrowserVersion.FIREFOX_60, BrowserVersion.FIREFOX_52, BrowserVersion.INTERNET_EXPLORER, BrowserVersion.CHROME, BrowserVersion.EDGE};
        WebClient webClient = new WebClient(versions[(int) (versions.length * Math.random())]);

        //几个重要配置
        webClient.getCookieManager().setCookiesEnabled(true);//启用cookie
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//抛出失败的状态码
        webClient.getOptions().setThrowExceptionOnScriptError(false);//抛出js异常
        webClient.getOptions().setUseInsecureSSL(true);//忽略ssl认证
        webClient.getOptions().setJavaScriptEnabled(true);//启用js
        webClient.getOptions().setRedirectEnabled(true);//启用重定向
        webClient.getOptions().setCssEnabled(true);//启用css
        webClient.getOptions().setTimeout(5000); //设置连接超时时间
        webClient.waitForBackgroundJavaScript(5000);//设置等待js响应时间
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//设置Ajax异步
        webClient.getOptions().setAppletEnabled(true);//启用小程序
        webClient.getOptions().setGeolocationEnabled(true);//启用定位

        return webClient;
    }

    /**
     * 更新IP代理、User Agent池
     */
    public static void updateIpProxyPoolAndUserAgentPool(List<IpProxy> ipProxyPools, List<UserAgent> userAgentPools) {
        ipProxyPool = ipProxyPools;
        log.info("WebClientUtil的IP代理池更新成功，大小：" + ipProxyPool.size());
        userAgentPool = userAgentPools;
        log.info("WebClientUtil的User Agent池更新成功，大小：" + userAgentPool.size());
    }

    /**
     * 为WebClient更新IP代理、User Agent
     */
    private static void updateIpProxyAndUserAgentForWebClient(WebClient webClient) {
        //设置IP代理，每次都取不同的
        if (ipProxyPool != null && ipProxyPool.size() > 0) {
            Integer index = randomNumber(0, ipProxyPool.size() - 1);
            IpProxy ipProxy = ipProxyPool.get(index);
            log.info("更新代理IP，当前使用：" + ipProxy.toString() + ",下标：" + index);
            ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
            proxyConfig.setProxyHost(ipProxy.getIp());//ip地址
            proxyConfig.setProxyPort(Integer.parseInt(ipProxy.getPort()));//端口
        }

        //更新User-Agent
        if (userAgentPool != null && userAgentPool.size() > 0) {
            Integer index = randomNumber(0, userAgentPool.size() - 1);
            UserAgent userAgent = userAgentPool.get(index);
            log.info("更新User Agent，当前使用：" + userAgent.getUserAgent() + ",下标：" + index);
            webClient.removeRequestHeader("User-Agent");
            webClient.addRequestHeader("User-Agent", userAgent.getUserAgent());
        }
    }

    /**
     * 根据一个url发起get请求
     */
    public static Result<HtmlPage> gather(WebClient webClient, String url, String refererUrl, List<Map<String, String>> headers) throws IOException {
        //更新代理IP、UA
        updateIpProxyAndUserAgentForWebClient(webClient);

        //Referer，默认百度 https://www.baidu.com
        webClient.removeRequestHeader("Referer");
        webClient.addRequestHeader("Referer", StringUtils.isEmpty(refererUrl) ? "https://www.baidu.com" : refererUrl);
        //是否还要其他的Header，可以直接在http请求的head里面携带cookie，或者这样设置：webClient.getCookieManager().addCookie(cookie);
        if (!StringUtils.isEmpty(headers)) {
            headers.forEach((header) -> {
                webClient.removeRequestHeader(header.get("name"));
                webClient.addRequestHeader(header.get("name"), header.get("value"));
            });
        }

        //访问url
        HtmlPage page = webClient.getPage(url);
        WebResponse response = page.getWebResponse();

        return ResultUtil.data(response.getStatusCode(), page, response.getStatusMessage());
    }

    /**
     * 返回一个最小值-最大值的随机数
     */
    public static Integer randomNumber(Integer min, Integer max) {
        //(最小值+Math.randomNumber()*(最大值-最小值+1))
        return (int) (min + Math.random() * (max - min + 1));
    }

    /**
     * 返回一个随机指定长度的字符串
     */
    public static String randomString(Integer length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * main测试
     */
    public static void main(String[] args) {
//        try {
//            ResultVo<HtmlPage> resultVo = WebClientUtil.gather(webClient, "https://book.qidian.com/info/1004608738","",null);
//            HtmlPage page = resultVo.getPage();
//
//            //模拟点击“目录”
//            page = page.getHtmlElementById("j_catalogPage").click();
//
//            //获取页面源代码
//            log.info(page.asXml());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
