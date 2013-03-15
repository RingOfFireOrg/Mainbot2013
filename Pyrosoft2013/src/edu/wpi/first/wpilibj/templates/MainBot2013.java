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
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class MainBot2013 extends SimpleRobot {
    
//<editor-fold defaultstate="open" desc="Relays Declaration">    
    /*
     * Relays
     */
        Relay banana2 = new Relay(6);
 //</editor-fold>
        
//<editor-fold defaultstate="open" desc="Digital Inputs Declaration">      
    /*
     * Digital inputs
     */
        DigitalInput autoselect = new DigitalInput(1);
        DigitalInput tiltTop = new DigitalInput(2);
        DigitalInput tiltBot = new DigitalInput(3);
        DigitalInput rotLeft = new DigitalInput(4);
        DigitalInput rotRight = new DigitalInput(5);
//</editor-fold>
        
//<editor-fold defaultstate="open" desc="Motor Control Declaration">
    /*
     * Motor Control
     * 
     * The number at the end represents which PWM port is used on the digital sidecar
     */
        Jaguar leftdrive1 = new Jaguar(1);
        Jaguar leftdrive2 = new Jaguar(2);
        Jaguar rightdrive1 = new Jaguar(3);
        Jaguar rightdrive2 = new Jaguar(4);
        Jaguar shooter = new Jaguar(5);
        Victor tilter = new Victor(7);
        Victor rotator = new Victor(8);
        Servo leftgearbox = new Servo(10);
        Servo rightgearbox = new Servo(9);
 //</editor-fold>
  
//<editor-fold defaultstate="open" desc="Joysticks and Buttons Declaration"> 
    /*
     * Joysticks and buttons
     * 
     * Joystick: number defines which order it needs to be in on the driver station
     * JoystickButton: name at the end says which joystick, number specifies which button
     */
        Joystick leftStick = new Joystick(1);
        Joystick rightStick = new Joystick(2);
        Joystick actionJoy = new Joystick(3);
        JoystickButton trigger = new JoystickButton(actionJoy, 1);
        JoystickButton lefttrig = new JoystickButton(leftStick,1);
        JoystickButton righttrig = new JoystickButton(rightStick,1);
        JoystickButton tiltAllow = new JoystickButton(actionJoy,3);
        JoystickButton rotAllow = new JoystickButton(actionJoy,4);
        JoystickButton pyramidFront = new JoystickButton(actionJoy,11);
        JoystickButton pyramidBack = new JoystickButton(actionJoy,12); 
        JoystickButton autoAim = new JoystickButton(actionJoy,7);
        JoystickButton revTrigger = new JoystickButton(actionJoy, 8);
//</editor-fold>
        
        double diskNumberBack = 6.8, diskNumberFront = 4.9;
        // diskNumberBack: time it takes to go down for aiming from the back of the pyramid
        // diskNumberFront: time it takes to go down for aiming from the front of the pyramid

//<editor-fold defaultstate="open" desc="Autonomous Code">
    public void autonomous() {
        if (isAutonomous() && isEnabled()) {
            double tiltdown; 
            if(autoselect.get()){
                tiltdown = diskNumberFront;
            } else {
                tiltdown = diskNumberBack;
            }
            shooter.set(-1.0);
            tilter.set(1);
            Timer.delay(tiltdown);
            tilter.set(0.0);
            banana2.set(Relay.Value.kForward);
            Timer.delay(5);
            banana2.set(Relay.Value.kOff);
        }
    }
//</editor-fold>

//<editor-fold defaultstate="open" desc="RampMotor Method">    
    public double rampmotor(double req, double cur){ //init variables Requested speed and Current speed
        double error = Math.abs(req - cur); //sets variable error to Requested speed minus Current speed
        double output = 0.0;
        
        if(error >= 0.1){
            output = ((0.1)*(req-cur)+ cur);
            
        }
        else{
            output = req;
        }
        return output;
    }
 //</editor-fold>
    
//<editor-fold defaultstate="open" desc="Aimer Method">      
    /*
     * Aimer
     *   Allows controlled motion of a device where limit1 and limit2 represet the state of 
     *     limit switches at the extreme ends of allowed motion. 
     * Inputs:
     *    movement: a number between -1 and 1 which represents the speed of movement desired
     *    limit1: if true then no movement speed greater than zero is allowed
     *    limit2: if true then no movement speed less than zero allowed
     *    helpcontext: text identifying the mechanism being moved
     *    pos: text identifying the role of the limit switch at the end of *positive* travel
     *    neg: text identifying the role of the limit switch at the end of *negative* travel
     * Return:
     *   double representin speed applied to the motor
     */
    public double Aimer(double movement, boolean limit1, boolean limit2, String helpcontext, String pos, String neg){
        double output = 0;
        if (limit1 && limit2){                                          //if both are pressed, this shouldnt happen
              SmartDashboard.putString(helpcontext, "both limits pressed");
        } else if (limit1){                                             //is top limit switch pressed
              SmartDashboard.putString(helpcontext, pos);
              if (movement < 0.0){                                        //if the suggested value is down
                  output = movement;                                      //allow set of motor
              }
        } else if (limit2){                                             //is bottom limit switch pressed
              SmartDashboard.putString(helpcontext, neg);
              if (movement > 0.0){                                        //if the suggested value is up
                  output = movement;                                      //allow set of motor
              }
        } else {
              SmartDashboard.putString(helpcontext, "you're all good");
              output = movement; 
        }
        return output;
    }
 //</editor-fold>
   
/*
*      TELEOP
*/
    
    
    public void operatorControl() {
        //boolean clearedLimitSwitch;  // indicateds we've dragged free of the limit switch for this go round
        boolean fire = false, gearboxstate = false, b, a, prevalue = false;        
        //double Tiltvalue = 0.0, Rotationvalue = 0;                              
        double shooterspeed=0;
        SmartDashboard.putString("Teleop:", " Enabled");
        shooter.set(0);
        banana2.set(Relay.Value.kOff);
        int aimerState, stateMachine1 = 0;
        boolean goingDown = false, secondaryCase = false;
        double aimProcessOut, aimerDown = 0;
        long stateMachine1Time = System.currentTimeMillis();
        Relay.Value kickerAction;
        
        while (isOperatorControl() && isEnabled())                              //Runs while enagled 
        {
            Timer.delay(0.1);
            
            leftdrive1.set((leftStick.getY()*.75));
            leftdrive2.set((leftStick.getY()*.75));
            rightdrive1.set((rightStick.getY()*.75)*(-1));
            rightdrive2.set((rightStick.getY()*.75)*(-1));                                  

        //<editor-fold defaultstate="open" desc="Gearbox"> 
            /*
             * Gearbox
             * 
             */            
                    if (lefttrig.get() && righttrig.get()){
                        a = true;                                                       // b if both buttons pressed
                    } else {
                        a = false;
                    }
                    if (!prevalue && a){
                        gearboxstate=!gearboxstate;
                    }
                    if (gearboxstate){
                        SmartDashboard.putString("Gear:", " HIGH");
                        leftgearbox.set(0.23);
                        rightgearbox.set(0.23);
                    } else {
                        SmartDashboard.putString("Gear:", " LOW");
                        leftgearbox.set(0.76);
                       rightgearbox.set(0.76);
                    }
                    if (a){
                        prevalue = true;
                    } else {
                        prevalue = false;
                    }
         //</editor-fold>

        //<editor-fold defaultstate="open" desc="Shooter">             
            /*
             * Shooter
             * 
             */       
                    shooterspeed=rampmotor((((actionJoy.getThrottle()-1)/-2)*(-1)),shooterspeed);
                    shooter.set(shooterspeed);
                    SmartDashboard.putNumber("Shooter Speed: ", shooterspeed * (-100)); //happy kyle? indeed
         //</editor-fold>            

        //<editor-fold defaultstate="open" desc="Banana">          
            /*
             * Banana
             *      
             */         
                    kickerAction = Relay.Value.kOff;
                    
                    if (trigger.get()){
                        kickerAction = Relay.Value.kForward;                              //set it to on
                        SmartDashboard.putString("Kicker", "forward");
                    }
                    if (revTrigger.get()){
                        kickerAction = Relay.Value.kReverse;
                        SmartDashboard.putString("Kicker", "REVERSE");
                    }    
                    banana2.set(kickerAction);  
                    
          //</editor-fold>

        //<editor-fold defaultstate="open" desc="Auto-aimer">             
            /*
             * Aiming Systems
             *       
             * if any two or more buttons are pressed do nothing
             */



                    
                    if ((pyramidFront.get() && pyramidBack.get()) || (pyramidFront.get() && autoAim.get()) || (pyramidBack.get() && autoAim.get())){
                        rotator.set(0);
                        tilter.set(0);
                        aimerState = 0;
                    } else if (pyramidFront.get()) {
                        //if we have pressed the go down to position for front of pyramid button
                        aimerState = 1;
                        aimerDown = diskNumberFront;
                    } else if (pyramidBack.get()) {
                        //if we have pressed the go down to position for back of pyramid button.
                        aimerState = 1;
                        aimerDown = diskNumberBack;
                    } else if (autoAim.get()) {
                        //if we are going to auto aim
                        aimerState = 2;
                    } else {    
                        if (tiltAllow.get()) {
                            tilter.set(Aimer(actionJoy.getY(), tiltTop.get(), tiltBot.get(), "Tilter:", " Top", " Bottom"));
                        }
                        else{
                            tilter.set(0);
                        }
                        if (rotAllow.get()) {
                            rotator.set(Aimer(actionJoy.getX(), rotLeft.get(), rotRight.get(), "Rotation:", " Left", " Right"));
                        }
                        else{
                            rotator.set(0);
                            
                        }
                            
                        aimerState = 3;
                    }
                    
                 
                    if (aimerState == 1) {
                        if (stateMachine1 == 0) {
                            if (aimerState == 1 && !tiltTop.get()) {
                                stateMachine1 = 1;
                            } else if (aimerState == 1 && tiltTop.get()) {
                                stateMachine1 = 2;
                                stateMachine1Time = System.currentTimeMillis();
                            }
                        } else if (stateMachine1 == 1) {
                            if (tiltTop.get()) {
                                stateMachine1 = 2;
                                stateMachine1Time = System.currentTimeMillis();
                            } else {
                                tilter.set(Aimer(-1, tiltTop.get(), tiltBot.get(), "Tilter:", " Top", " Bottom"));
                                
                            }
                        } else if (stateMachine1 == 2) {
                            if ((System.currentTimeMillis() - stateMachine1Time) >= ((1000)*aimerDown)) {
                                stateMachine1 = 0;
                            } else {
                                tilter.set(Aimer(1, tiltTop.get(), tiltBot.get(), "Tilter:", " Top", " Bottom"));
                            }
                        }
                    }
        //</editor-fold>   

            }     
        }
     
    /*public void disabled() {
        SmartDashboard.putString("Teleop:", " Disabled");
        SmartDashboard.putString("Tilt limit:", "Disabled");
        SmartDashboard.putString("Turn limit:", "Disabled");
    }*/
}