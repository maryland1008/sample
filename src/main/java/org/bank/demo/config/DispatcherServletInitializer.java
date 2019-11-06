package org.bank.demo.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
     @Override
     protected Class<?>[] getRootConfigClasses() {
    	 return new Class[] { RootApplicationContextConfig.class };
    }

     @Override
     protected Class<?>[] getServletConfigClasses() {
          return new Class[] { WebApplicationContextConfig.class };
     }

     @Override
     protected String[] getServletMappings() {
    	   return new String[] { "/"};
     }

     @Override     
     public void onStartup(ServletContext container) throws ServletException {
    	 super.onStartup(container); 
         container.setInitParameter("APP_TITLE", "NOAA Electronic Annual Operating System");
     } 	 	 
}
