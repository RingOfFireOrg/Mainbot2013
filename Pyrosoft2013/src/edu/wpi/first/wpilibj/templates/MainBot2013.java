/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class MainBot2013 extends SimpleRobot {
    
   //Joystick
    Joystick leftDriveStick = new Joystick(1);
    Joystick rightDriveStick = new Joystick(2);
    Joystick actionJoy = new Joystick(3);
    
    JoystickButton a1 = new JoystickButton(actionJoy,1);
    JoystickButton a2 = new JoystickButton(actionJoy,2);
    JoystickButton a3 = new JoystickButton(actionJoy,3);
    
    //PWM
    
    RobotDrive drive = new RobotDrive(1,2,3,4); 
    Jaguar shooter = new Jaguar(5);                                             //Shooter
    Jaguar shooter2 = new Jaguar(6);                                            //Shooter2
    
    Victor vic1 = new Victor(6);
    Victor vic2 = new Victor(7);
    
    //Digital IO
    DigitalInput lim1 = new DigitalInput(1);
    DigitalInput lim2 = new DigitalInput(2);
    DigitalInput lim3 = new DigitalInput(3);
    DigitalInput lim4 = new DigitalInput(4);
    DigitalInput lim5 = new DigitalInput(5);
    DigitalInput lim6 = new DigitalInput(6);
    
    
    Solenoid sol1 = new Solenoid(5);
    Solenoid sol2 = new Solenoid(6);
            
            
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        while (isAutonomous() && isEnabled()){
            
        }
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()){
            Timer.delay(0.005);
            drive.tankDrive(leftDriveStick.getY(), rightDriveStick.getY());
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}
