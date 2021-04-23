package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="TheOneAndOnly", group="Linear Opmode")

public class TheOneAndOnly extends LinearOpMode {


    private DcMotor backRight;
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private CRServo firstLauncher;
    private DcMotor secondLauncher;
    private DcMotor wobbler;
    private DcMotor roller;
    private Servo lifter;
    private Servo shifter;
    private Servo grabber;
    private DcMotor roller2;

    private double shooterPower = 0.45;

    @Override
    public void runOpMode() {

        waitForStart();

        backRight  = hardwareMap.get(DcMotor.class, "backRight");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");

        firstLauncher  = hardwareMap.get(CRServo.class, "firstLauncher");
        secondLauncher = hardwareMap.get(DcMotor.class, "secondLauncher");
        wobbler        = hardwareMap.get(DcMotor.class, "wobbler");
        roller         = hardwareMap.get(DcMotor.class, "roller");
        roller2        = hardwareMap.get(DcMotor.class, "roller2");

        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        roller2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        secondLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobbler.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        lifter  = hardwareMap.get(Servo.class, "lifter");
        shifter = hardwareMap.get(Servo.class, "shifter");
        grabber = hardwareMap.get(Servo.class, "grabber");


        shifter.setPosition(1);
        grabber.setPosition(0.9);

        while (opModeIsActive()) {

            double strafePower    = 1;
            double lifterPosition = lifter.getPosition();
            double rightPower     = -gamepad1.right_stick_y;
            double leftPower      = gamepad1.left_stick_y;
            double wobblerPower   = gamepad2.left_stick_y/1.5;
            double roller2Power   = -0.5;

            wobbler.setPower(wobblerPower);

            //strafe left
            if (gamepad1.dpad_left) {
                backRight.setPower(-strafePower);
                frontRight.setPower(strafePower);
                backLeft.setPower(-strafePower);
                frontLeft.setPower(strafePower);
            } else if (gamepad1.dpad_right) {
                backRight.setPower(strafePower);
                frontRight.setPower(-strafePower);
                backLeft.setPower(strafePower);
                frontLeft.setPower(-strafePower);
            } else {
                backRight.setPower(rightPower);
                backLeft.setPower(leftPower);
                frontRight.setPower(rightPower);
                frontLeft.setPower(leftPower);
            }

            if (gamepad1.dpad_up) {
                lifterPosition += 0.01;
                lifter.setPosition(lifterPosition);
                sleep(100);
            }

            if (gamepad1.dpad_down) {
                lifterPosition -= 0.01;
                lifter.setPosition(lifterPosition);
                sleep(100);
            }

            //start shooter motors
            if (gamepad2.left_trigger > 0) {

                firstLauncher.setPower(-1);
                secondLauncher.setPower(0.5);
            } else {
                firstLauncher.setPower(0);
                secondLauncher.setPower(0);
            }

            //shoot 1 ring
            if (gamepad2.a) {
                shifter.setPosition(0.57);
                sleep(500);
                shifter.setPosition(1);
            }

            //iterates shooterPower up 0.01
            if (gamepad2.dpad_up) {
                shooterPower += 0.01;
                sleep(100);
            }

            //iterates shooterPower down 0.01
            if (gamepad2.dpad_down) {
                shooterPower -= 0.01;
                sleep(100);
            }

            //smoothly moves lifter down to retrieval position
            if (gamepad2.x) {
                lifter.setPosition(0.65);
            }

            //moves lifter up to shooting position
            if (gamepad2.y) {
                lifter.setPosition(0.95);
            }
            if (gamepad2.dpad_up) {
                roller.setPower(1);
                roller2.setPower(roller2Power);
            } else {
                roller.setPower(0);
                roller2.setPower(0);
            }

            if (gamepad2.right_bumper) {
                roller2Power += 0.01;
                sleep(150);
            }
            if (gamepad2.left_bumper) {
                roller2Power -= 0.01;
                sleep(150);
            }



            telemetry.addData("shooterPower", shooterPower);
            telemetry.addData("Right Power", rightPower);
            telemetry.addData("Left Power", leftPower);
            telemetry.addData("Grabber Position", grabber.getPosition());
            telemetry.addData("Lifter Position", lifter.getPosition());
            telemetry.update();

        }
    }

    //idk why i have this. utterly useless

}



