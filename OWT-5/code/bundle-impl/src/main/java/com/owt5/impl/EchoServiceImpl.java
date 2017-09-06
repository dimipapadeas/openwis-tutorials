package com.owt5.impl;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import com.owt5.api.EchoService;
import com.owt5.lib.DemoUtil;

/**
 * 
 * @Singleton = a single instance is reused
 * 
 * @OsgiServiceProvider = OSGi reference to interface class.
 * 
 */

@Singleton
@OsgiServiceProvider(classes = { EchoService.class })
public class EchoServiceImpl implements EchoService {

	public String echo(String text) {
		DemoUtil util = new DemoUtil();
		
		// TODO RANDOM DELAY....
		int min = 1;
		int max = 4;
		try {
			int randomDelay = ThreadLocalRandom.current().nextInt(min, max + 1);
			TimeUnit.SECONDS.sleep(randomDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return util.upperCaseIt(text);
	}
	
}