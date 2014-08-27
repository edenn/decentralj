package com.decentralbank.decentralj.net;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

public class ThreadPool {

	  private BlockingQueue<ZMQ.Context> taskQueue = null;
	  private List<ServerThread> threads = new ArrayList<ServerThread>();
	  private boolean isStopped = false;

	  public ThreadPool(int noOfThreads, int maxNoOfTasks){
	    taskQueue = new ArrayBlockingQueue<ZMQ.Context>(maxNoOfTasks);

	    for(int i=0; i<noOfThreads; i++){
	      threads.add(new ServerThread((Context) taskQueue));
	    }
	    for(ServerThread thread : threads){
	      thread.start();
	    }
	  }

	  public void shutdown() {
		  this.isStopped = true;
		    for(ServerThread thread : threads){
		      thread.stop();
	    }

	}
	  
}
