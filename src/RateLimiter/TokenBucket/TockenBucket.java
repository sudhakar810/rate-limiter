package RateLimiter.TokenBucket;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import RateLimiter.SlidingWindow.RateLimiter;

public class TockenBucket implements RateLimiter {
	private int bucketCapacity;
	private int refreshRate;
	private AtomicInteger currentCapacity;
	private AtomicLong lastUpdatedTime;
	
	
	public TockenBucket(int bucketCapacity, int refreshRate) {
		super();
		this.bucketCapacity = bucketCapacity;
		this.refreshRate = refreshRate;
		this.currentCapacity = new AtomicInteger(bucketCapacity);
		this.lastUpdatedTime = new AtomicLong(System.currentTimeMillis());
	}


	@Override
	public boolean grantAccess() {
		refreshBucket();
		if(currentCapacity.get()>0) {
			currentCapacity.decrementAndGet();
			return true;
		}
		
		return false;
	}


	private void refreshBucket() {

		long currentTime = System.currentTimeMillis();
		int additionalToken = (int) ((currentTime - lastUpdatedTime.get())/1000 *refreshRate);
		int currCapacity = Math.min(additionalToken +currentCapacity.get(),bucketCapacity);
		currentCapacity.getAndSet(currCapacity);
		lastUpdatedTime.addAndGet(System.currentTimeMillis());
		
		
	}
	
	
	

}
