package com.robot.torvalds;

import com.diozero.devices.LED;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.diozero.devices.motor.CamJamKitDualMotor;
import com.diozero.util.SleepUtil;
@Controller
public class TorvaldsController {
    @RequestMapping("/home")
    public String getHome() {
        return "home";
    }

    @RequestMapping("/forward")
    public String goForward() {
    try(CamJamKitDualMotor torvalds = new CamJamKitDualMotor()) {
        torvalds.rotateRight(1);
        SleepUtil.sleepSeconds(1);
        torvalds.stop();
        SleepUtil.sleepSeconds(1);
    }
        return "home";
    }
    @RequestMapping("/backward")
    public String goBackward() {
        try(CamJamKitDualMotor torvalds = new CamJamKitDualMotor()) {
            torvalds.rotateLeft(1);
            SleepUtil.sleepSeconds(1);
            torvalds.stop();
            SleepUtil.sleepSeconds(1);
        }
        return "home";
    }
    @RequestMapping("/left")
    public String goLeft() {
        try(CamJamKitDualMotor torvalds = new CamJamKitDualMotor()) {
            torvalds.forward(1);
            SleepUtil.sleepSeconds(0.3);
            torvalds.stop();
            SleepUtil.sleepSeconds(1);
        }
        return "home";
    }
    @RequestMapping("/right")
    public String goRight() {
        try(CamJamKitDualMotor torvalds = new CamJamKitDualMotor()) {
            torvalds.backward(1);
            SleepUtil.sleepSeconds(0.3);
            torvalds.stop();
            SleepUtil.sleepSeconds(1);
        }
        return "home";
    }

    @RequestMapping("/blink")
    public String blink() {
        try(LED eye = new LED(25)) {
            eye.on();
            SleepUtil.sleepSeconds(1);
            eye.off();
            SleepUtil.sleepSeconds(1);
        }
        return "home";
    }
}
