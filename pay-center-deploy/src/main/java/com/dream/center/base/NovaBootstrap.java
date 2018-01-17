package com.dream.center.base;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: clong
 * @date: 2017-04-01
 */
public abstract class NovaBootstrap {

  private static final String HOOK_NAME = "hook";

  private static final Logger logger = LoggerFactory.getLogger(NovaBootstrap.class);

  private static AtomicBoolean initialized = new AtomicBoolean(false);
  private static AtomicBoolean closed = new AtomicBoolean(false);

  private NovaBootstrap() {

  }

  private static final ExtensionLoader<Container> loader =
      ExtensionLoader.getExtensionLoader(Container.class);


  public static void init() {
    if (initialized.get()) {
      return;
    }
    doInit();
  }

  private static void doInit() {
    getHookContainer().ifPresent(container -> {
      container.start();
      initialized.compareAndSet(false, true);
      logger.info("hook container is started");
    });

  }


  public static void destroy() {
    if (closed.get()) {
      return;
    }
    doDestroy();
  }

  private static void doDestroy() {
    getHookContainer().ifPresent(container -> {
      container.stop();
      closed.compareAndSet(false, true);
      logger.info("hook container is closed");
    });
  }


  private static Optional<Container> getHookContainer() {
    return Optional.ofNullable(loader.getExtension(HOOK_NAME));
  }
}
