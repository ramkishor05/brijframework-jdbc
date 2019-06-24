package org.brijframework.jdbc.context;

import org.brijframework.asm.context.AbstractModuleContext;
import org.brijframework.jdbc.container.JdbcContainer;
import org.brijframework.model.context.ModelContext;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=ModelContext.class)
public class JdbcContext extends AbstractModuleContext{

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(JdbcContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(JdbcContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startup() {
		System.err.println("JdbcContext startup processing...");
		super.startup();
		System.err.println("JdbcContext startup completed....");
	}

	@Override
	public void destory() {
		System.err.println("JdbcContext destory processing...");
		super.destory();
		System.err.println("JdbcContext destory completed....");
	}

}
