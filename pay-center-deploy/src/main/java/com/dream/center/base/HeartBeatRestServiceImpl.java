package com.dream.center.base;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: clong
 * @date: 2017-09-19
 */
@Service(protocol = {"rest"}, registry = {"zookeeper"})
@Path("/")
@Component
@Slf4j
public class HeartBeatRestServiceImpl implements HeartBeatRestService {

  private final AtomicBoolean alive = new AtomicBoolean(false);

  @Override
  @GET
  @Path("/_HB_")
  public Response heartbeat(@QueryParam("service") String service) {
    Response.Status status = Response.Status.NOT_FOUND;
    if ("online".equals(service)) {
      status = Response.Status.OK;
      alive.compareAndSet(false, true);
    } else if ("offline".equals(service)) {
      NovaBootstrap.destroy();
      alive.compareAndSet(true, false);
    } else {
      if (alive.get()) {
        status = Response.Status.OK;
      }
    }

    return Response
        .status(status)
        .entity("OK")
        .build();
  }
}
