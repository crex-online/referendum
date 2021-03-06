package com.referendum;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.referendum.tools.Tools;


public class DataSources {

	public static String APP_NAME = "referendum";
	
	public static Integer EXTERNAL_SPARK_WEB_PORT() {return (SSL) ? 443 : 80;} // Main is port 80, dev is port 4567
	
	
	public static Boolean SSL = false;
	
	
	// iptables are used to route all requests to 80 to 4567.
	public static Integer INTERNAL_SPARK_WEB_PORT = 4567;
	
	public static final String WEB_SERVICE_URL = "http://localhost:" + INTERNAL_SPARK_WEB_PORT + "/";
	
	public static String EXTERNAL_IP = Tools.httpGetString("http://api.ipify.org/").trim();
	
	public static String EXTERNAL_URL = "http://" + EXTERNAL_IP + ":" + EXTERNAL_SPARK_WEB_PORT() + "/";	
	
	public static final String DD_DOMAIN_NAME = "referendum.ml";
			
	public static final String DD_URL(){ 
		String domain = (SSL) ? "https:" : "http:";
		domain += "//" + DD_DOMAIN_NAME + "/";
		return domain;
	}
	
	public static final String DD_INTERNAL_URL = "http://" + DD_DOMAIN_NAME + ":" + INTERNAL_SPARK_WEB_PORT + "/";
	

	
	// The path to the ytm dir
	public static String HOME_DIR() {
		String userHome = System.getProperty( "user.home" ) + "/." + APP_NAME;
		return userHome;
	}
	
	public static final String KEYSTORE() { return HOME_DIR() + "/keystore.jks";}
	
	
	// This should not be used, other than for unzipping to the home dir
	public static final String CODE_DIR = System.getProperty("user.dir");
	
	public static final String SOURCE_CODE_HOME() {return HOME_DIR() + "/src";}
	
	public static final String SQL_FILE() {return SOURCE_CODE_HOME() + "/ddl_server.sql";}
	
	public static final String SQL_VIEWS_FILE() {return SOURCE_CODE_HOME() + "/views_server.sql";}
	
	public static final String SQL_FAST_TABLES_FILE() {return SOURCE_CODE_HOME() + "/fast_tables.sql";}
	
	public static final String SHADED_JAR_FILE = CODE_DIR + "/target/" + APP_NAME + ".jar";

	public static final String SHADED_JAR_FILE_2 = CODE_DIR + "/" + APP_NAME + ".jar";
	
	public static final String ZIP_FILE() {return HOME_DIR() + "/" + APP_NAME + ".zip";}
	
	public static final String TOOLS_JS() {return SOURCE_CODE_HOME() + "/web/js/tools.js";}
	
	// Web pages
	public static final String WEB_HOME() {return SOURCE_CODE_HOME() + "/web";}

	public static final String WEB_HTML() {return WEB_HOME() + "/html";}
	
//	public static final String MAIN_PAGE_URL_EN() {return WEB_HTML() + "/main_en.html";}
//	
//	public static final String MAIN_PAGE_URL_ES() {return WEB_HTML() + "/main_es.html";}
	
	public static final String MAINTENANCE_PAGE_URL() {return WEB_HTML() + "/maintenance.html";}
	
//	public static String BASE_ENDPOINT = MAIN_PAGE_URL_EN();
	
	public static String BASE_ENDPOINT = WEB_HTML() + "/home.html";
	
	public static final String PAGES(String pageName) {
		return WEB_HTML() + "/" + pageName + ".html";
	}
	
	public static final String TEMPLATES(String pageName) {
		return WEB_HTML() + "/" + pageName + ".template";
	}
	
	public static final List<String> HEAD_TEMPLATES = new ArrayList<>(Arrays.asList(
			"home",
			"tag",
			"private_poll",
			"poll",
			"trending_polls",
			"trending_tags",
			"user",
			"comment"));
	
	public static final List<String> SUB_TEMPLATES = new ArrayList<>(Arrays.asList(
			"header",
			"navbar",
			"footer",
			"modals",
			"scripts"));
	
	public static final Date APP_START_DATE = new Date();
	
	public static final String DB_PROP_FILE = HOME_DIR() + "/db.properties";

	public static final Properties DB_PROP = Tools.loadProperties(DB_PROP_FILE);

	public static final Integer EXPIRE_SECONDS = 86400 * 7; // stays logged in for 7 days
	
	
	public static final String KEYSTORE_FILE() {return HOME_DIR() + "/keystore.jks";}
	

}
