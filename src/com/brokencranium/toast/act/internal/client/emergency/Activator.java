package com.brokencranium.toast.act.internal.client.emergency;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.brokencranium.toast.act.airbag.IAirbag;
import com.brokencranium.toast.act.gps.IGps;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private IAirbag airbag;
	private IGps gps;
	
	private ServiceReference gpsRef;
	private ServiceReference airbagRef;
	
	private EmergencyMonitor monitor;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Launching...");
		monitor = new EmergencyMonitor();
		
		gpsRef = context.getServiceReference(IGps.class.getName());
		airbagRef = context.getServiceReference(IAirbag.class.getName());
		if (gpsRef ==null || airbagRef == null){
			System.err.println("Unable to acquire GPS or airbag reference");
			return;
		}
		gps = (IGps) context.getService(gpsRef);
		airbag = (IAirbag) context.getService(airbagRef);
		
		if (gps ==null || airbag == null){
			System.err.println("Unable to acquire GPS or airbag");
			return;
		}
		
		monitor.setAirbag(airbag);
		monitor.setGps(gps);
		monitor.startup();
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		monitor.shutdown();
		
	   if (gpsRef != null)
		   context.ungetService(gpsRef);
	   if(airbag != null)
		   context.ungetService(airbagRef);
	   
	   System.out.println("Shutting down...");
	  
		   
	}

}
