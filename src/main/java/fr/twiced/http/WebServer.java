package fr.twiced.http;

import java.io.File;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Example WebServer class which sets up an embedded Jetty appropriately whether
 * running in an IDE or in "production" mode in a shaded jar.
 */
public class WebServer {
	private static final String LOG_PATH = "./var/logs/access/yyyy_mm_dd.request.log";
	private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/webapp";

	public static void main(final String[] args) throws Exception {
		boolean fromCLI = true;
		if(args != null & args.length > 0){
			fromCLI = false;
		}
		WebServer server = new WebServer(8080, fromCLI);
		server.start();
		server.join();
	}
	
	public static interface WebContext {
		public File getWarPath();

		public String getContextPath();
	}

	private Server server;
	private int port;
	private String bindInterface;
	private boolean shadedMode = true;

	public WebServer(int aPort) {
		this(aPort, null, false);
	}

	public WebServer(int aPort, String aBindInterface) {
		this(aPort, aBindInterface, false);
	}

	public WebServer(int aPort, boolean shaded) {
		this(aPort, null, shaded);
	}

	public WebServer(int aPort, String aBindInterface, boolean shaded) {
		port = aPort;
		bindInterface = aBindInterface;
		shadedMode = shaded;
	}

	public void start() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		server = new Server();

		server.setThreadPool(createThreadPool());
		server.addConnector(createConnector());
		server.setHandler(createHandlers());
		server.setStopAtShutdown(true);

		server.start();
	}

	public void join() throws InterruptedException {
		server.join();
	}

	public void stop() throws Exception {
		server.stop();
	}

	private ThreadPool createThreadPool() {
		QueuedThreadPool _threadPool = new QueuedThreadPool();
		_threadPool.setMinThreads(10);
		_threadPool.setMaxThreads(100);
		return _threadPool;
	}

	private SelectChannelConnector createConnector() {
		SelectChannelConnector _connector = new SelectChannelConnector();
		_connector.setPort(port);
		_connector.setHost(bindInterface);
		return _connector;
	}

	private HandlerCollection createHandlers() {
		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/");

		if (isRunningInShadedJar()) {
			_ctx.setWar(getShadedWarUrl());
		} else {
			_ctx.setWar(PROJECT_RELATIVE_PATH_TO_WEBAPP);
		}

		List<Handler> _handlers = new ArrayList<Handler>();

		_handlers.add(_ctx);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers(_handlers.toArray(new Handler[0]));
		
		ResourceHandler _resources = new ResourceHandler();
		_resources.setDirectoriesListed(true);
		_resources.setResourceBase(getShadedWarUrl());

		RequestLogHandler _log = new RequestLogHandler();
		_log.setRequestLog(createRequestLog());

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts, _resources, _log });

		return _result;
	}

	private RequestLog createRequestLog() {
		NCSARequestLog _log = new NCSARequestLog();

		File _logPath = new File(LOG_PATH);
		_logPath.getParentFile().mkdirs();

		_log.setFilename(_logPath.getPath());
		_log.setRetainDays(90);
		_log.setExtended(false);
		_log.setAppend(true);
		_log.setLogTimeZone("GMT");
		_log.setLogLatency(true);
		return _log;
	}

	private boolean isRunningInShadedJar() {
		return shadedMode;
	}

	private String getShadedWarUrl() {
		return PROJECT_RELATIVE_PATH_TO_WEBAPP;
	}
}

