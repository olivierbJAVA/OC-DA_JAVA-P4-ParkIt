package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class in charge of launching the program 
 */
public class App {
    private static final Logger logger = LogManager.getLogger("App");
    
    /**
     * Launch the program
     * 
     * @param Potential command line arguments
     * None arguments are needed
     */
    public static void main(String args[]){
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
