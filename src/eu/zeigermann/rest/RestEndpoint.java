package eu.zeigermann.rest;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("serial")
public class RestEndpoint extends HttpServlet {
	private Logger logger = Logger.getLogger(RestEndpoint.class
			.getName());

	private GenericDataService<String, StringData<String>> service = new GenericMemcacheServiceImpl<>("stringData");
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Performing POST");
		doSave(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Performing PUT");
		doSave(req, resp);
	}

	private void doSave(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String id = parseParameter(req, 0);
		if (id == null) {
			sendResponse(req, resp, null, HttpServletResponse.SC_BAD_REQUEST);
		} else {
			String payload = Util.readerToString(req.getReader());
			StringData<String> data = new StringData<>(id, payload);
			service.save(data);
			sendResponse(req, resp, null, HttpServletResponse.SC_OK);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Performing DELETE");
		String id = parseParameter(req, 0);
		if (id == null) {
			sendResponse(req, resp, null, HttpServletResponse.SC_NOT_FOUND);
		} else {
			service.delete(id);
			sendResponse(req, resp, null, HttpServletResponse.SC_OK);
		}
	}

	@Override
	// does both REST / CORS calls as well as jsonp
	// http://devlog.info/2010/03/10/cross-domain-ajax/
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Performing GET");
		String id = parseParameter(req, 0);
		if (id == null) {
			sendResponse(req, resp, null, HttpServletResponse.SC_BAD_REQUEST);
		} else {
			StringData<String> payload = service.get(id);
			if (payload != null) {
				sendResponse(req, resp, payload);
			} else {
				sendResponse(req, resp, null, HttpServletResponse.SC_NOT_FOUND);
			}
		}
	}

	private void sendResponse(HttpServletRequest req, HttpServletResponse resp,
			Object payload) {
		sendResponse(req, resp, payload, HttpServletResponse.SC_OK);
	}

	private void sendResponse(HttpServletRequest req, HttpServletResponse resp,
			Object payload, int status) {
		String callback = req.getParameter("callback");
		passResult(resp, payload, callback, status);
		if (callback == null) {
			setCORSHeaders(resp);
		} else {
			setJsonpHeaders(resp);
		}
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String origin = req.getHeader("Origin");
		logger.info("Performing OPTIONS with Origin: " + origin);
		setCORSHeaders(resp);
	}

	// http://en.wikipedia.org/wiki/Cross-Origin_Resource_Sharing
	// https://developer.mozilla.org/en-US/docs/HTTP/Access_control_CORS?redirectlocale=en-US&redirectslug=HTTP_access_control#Access-Control-Allow-Headers
	private void setCORSHeaders(HttpServletResponse resp) {
		// allow for cross site origin
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods",
				"HEAD, POST, GET, PUT, DELETE, OPTIONS");
		resp.setHeader("Access-Control-Allow-Headers",
				"ACCEPT, ORIGIN, X-REQUESTED-WITH, CONTENT-TYPE");
	}

	private void setJsonpHeaders(HttpServletResponse resp) {
		resp.setHeader("Content-Type", "text/javascript");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setHeader("Pragma", "no-cache");
	}

	private String parseParameter(HttpServletRequest req, int position) {
		final String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.length() == 0) {
			return null;
		}
		String parameterString = pathInfo;
		if (parameterString.startsWith("/")) {
			parameterString = parameterString.substring(1);
		}
		if (parameterString.length() == 0) {
			return null;
		}
		logger.info("Parsing incoming string: " + parameterString);
		String[] split = parameterString.split("/");
		if (position >= split.length ) {
			return null;
		}
		return split[position];
	}

	private <T> void passResult(HttpServletResponse resp, Object payload,
			String callback, int status) {
		try {
			String json = mapper.writeValueAsString(payload);
			if (callback != null) {
				json = callback + "(" + json + ");";
			}
			logger.info("Result: " + json);
			resp.setHeader("Content-Type", "application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(json);
			resp.setStatus(status);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
