package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="TheOneAndOnly", group="Linear Opmode")

public class TheOneAndOnly extends LinearOpMode {


    private DcMotor backRight;
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor firstLauncher;
    private DcMotor secondLauncher;
    private DcMotor wobbler;
    private Servo lifter;
    private Servo shifter;
    private Servo grabber;

    private double shooterPower = 0.45;

    @Override
    public void runOpMode() {

        waitForStart();

        backRight  = hardwareMap.get(DcMotor.class, "backRight");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");

        firstLauncher  = hardwareMap.get(DcMotor.class, "firstLauncher");
        secondLauncher = hardwareMap.get(DcMotor.class, "secondLauncher");
        wobbler        = hardwareMap.get(DcMotor.class, "wobbler");

        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        firstLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        secondLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        lifter  = hardwareMap.get(Servo.class, "lifter");
        shifter = hardwareMap.get(Servo.class, "shifter");
        grabber = hardwareMap.get(Servo.class, "grabber");

        shifter.setPosition(1);
        grabber.setPosition(0.9);

        while (opModeIsActive()) {

            double strafePower = 1;
            double rightPower = -gamepad1.right_stick_y;
            double leftPower = gamepad1.left_stick_y;
            double wobblerPower = gamepad2.left_stick_x/1.5;

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
            
            wobbler.setPower(wobblerPower);

            //start shooter motors
            if (gamepad2.left_trigger > 0) {
                setShooterPower(shooterPower);
            } else {
                setShooterPower(0);
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
                lifter.setPosition(0.5);
            }

            //moves lifter up to shooting position
            if (gamepad2.y) {
                lifter.setPosition(0.86);
            }

            if (gamepad2.right_bumper) {
                grabber.setPosition(grabber.getPosition() + 0.01);
            }
            if (gamepad2.left_bumper) {
                grabber.setPosition(grabber.getPosition() - 0.01);
            }



            telemetry.addData("shooterPower", shooterPower);
            telemetry.addData("Right Power", rightPower);
            telemetry.addData("Left Power", leftPower);
            telemetry.addData("Grabber Position", grabber.getPosition());
            telemetry.update();

        }
    }

    //idk why i have this. utterly useless
    private void setShooterPower(double power) {
        firstLauncher.setPower(power);
        secondLauncher.setPower(power);
    }

}
