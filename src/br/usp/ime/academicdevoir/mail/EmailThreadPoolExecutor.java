package br.usp.ime.academicdevoir.mail;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class EmailThreadPoolExecutor {

  private int poolSize = 2;
	 
  private int maxPoolSize = 2;

  private long keepAliveTime = 10;

  private ThreadPoolExecutor threadPool = null;

  private final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5);

  public EmailThreadPoolExecutor() {
      threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
  }

  public void runTask(Runnable task) {
      threadPool.execute(task);
  }

  public void shutDown() {
      threadPool.shutdown();
  }

}
