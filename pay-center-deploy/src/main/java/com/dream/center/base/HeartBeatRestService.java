package com.dream.center.base;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * @author: clong
 * @date: 2017-09-19
 */
public interface HeartBeatRestService {

  Response heartbeat(@QueryParam("service") String service);
}
